CREATE PROCEDURE InsertarViaje
	@cod_terminal	int,
	@cod_viaje		int,
	@placa			varchar(10),
	@cod_ruta		int,
	@cod_conductor	int,
	@fecha_viaje	date,
	@hora_viaje		time(0)
AS 
BEGIN
	SET NOCOUNT ON
	SET XACT_ABORT ON;

	  -- Requeridos para modificar vistas particionadas
	SET NUMERIC_ROUNDABORT OFF;
	SET ANSI_PADDING ON;
	SET ANSI_WARNINGS ON;
	SET CONCAT_NULL_YIELDS_NULL ON;
	SET ARITHABORT ON;


	INSERT INTO [VLADIMIRJON].Terminal_Quito.dbo.Viaje_Vista(cod_terminal,cod_viaje,placa,cod_ruta,cod_conductor,fecha_viaje,hora_viaje)
	VALUES (@cod_terminal,@cod_viaje,@placa,@cod_ruta,@cod_conductor,@fecha_viaje,@hora_viaje)
END
GO

CREATE OR ALTER PROCEDURE EliminarViaje
  @cod_terminal INT,
  @cod_viaje    INT
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  -- Requisitos para DML en vistas particionadas
  SET NUMERIC_ROUNDABORT OFF;
  SET ANSI_PADDING ON;
  SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON;
  SET ARITHABORT ON;

  BEGIN TRY
    BEGIN TRAN;

    -- 1) Borrar boletos del viaje (necesario si NO tienes ON DELETE CASCADE)
    DELETE FROM dbo.Boleto_Vista
    WHERE cod_terminal = @cod_terminal
      AND cod_viaje    = @cod_viaje;

    -- 2) Borrar el viaje
    DELETE FROM dbo.Viaje_Vista
    WHERE cod_terminal = @cod_terminal
      AND cod_viaje    = @cod_viaje;

    IF @@ROWCOUNT = 0
      THROW 50001, 'No existe el viaje con esos identificadores.', 1;

    COMMIT TRAN;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK TRAN;
    THROW;  -- si el destino es remoto y el DTC está apagado, verás error 7391 aquí
  END CATCH
END
GO


CREATE OR ALTER PROCEDURE ActualizarViaje
  @cod_terminal  INT,
  @cod_viaje     INT,
  @placa         VARCHAR(10) = NULL,
  @cod_ruta      INT         = NULL,
  @cod_conductor INT         = NULL,
  @fecha_viaje   DATE        = NULL,
  @hora_viaje    TIME(0)     = NULL
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  -- Requisitos para DML en vistas particionadas (UNION ALL)
  SET NUMERIC_ROUNDABORT OFF;
  SET ANSI_PADDING ON;
  SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON;
  SET ARITHABORT ON;

  BEGIN TRY
    BEGIN TRAN;

    UPDATE V
       SET placa         = COALESCE(@placa,         V.placa),
           cod_ruta      = COALESCE(@cod_ruta,      V.cod_ruta),
           cod_conductor = COALESCE(@cod_conductor, V.cod_conductor),
           fecha_viaje   = COALESCE(@fecha_viaje,   V.fecha_viaje),
           hora_viaje    = COALESCE(@hora_viaje,    V.hora_viaje)
     FROM dbo.Viaje_Vista AS V
    WHERE V.cod_terminal = @cod_terminal
      AND V.cod_viaje    = @cod_viaje;

    IF @@ROWCOUNT = 0
      THROW 50002, 'No existe el viaje con esos identificadores.', 1;

    COMMIT TRAN;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK TRAN;
    THROW; -- si el destino es remoto y DTC está apagado, aquí verás el 7391
  END CATCH
END
GO

/* =======================  INSERTAR BUS  ======================= */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.InsertarBus
  @cod_terminal INT,
  @placa        VARCHAR(10),
  @capacidad    INT
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;

  -- Requisitos DML en vistas particionadas
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  IF (@capacidad <= 0) THROW 50010, 'capacidad debe ser > 0', 1;

  INSERT INTO dbo.Bus_Vista (cod_terminal, placa, capacidad)
  VALUES (@cod_terminal, @placa, @capacidad);
END
GO

/* =======================  ACTUALIZAR BUS  ======================= */
/* Nota: No cambiamos PK (cod_terminal, placa). Solo capacidad. */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.ActualizarBus
  @cod_terminal INT,
  @placa        VARCHAR(10),
  @capacidad    INT = NULL
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;

  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  IF (@capacidad IS NOT NULL AND @capacidad <= 0)
    THROW 50011, 'capacidad debe ser > 0', 1;

  BEGIN TRY
    BEGIN TRAN;

    UPDATE V
       SET capacidad = COALESCE(@capacidad, V.capacidad)
      FROM dbo.Bus_Vista AS V
     WHERE V.cod_terminal = @cod_terminal
       AND V.placa        = @placa;

    IF @@ROWCOUNT = 0
      THROW 50012, 'No existe el bus con esos identificadores.', 1;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
  END CATCH
END
GO

/* =======================  ELIMINAR BUS  ======================= */
/* Borra primero Boletos -> Viajes que usan ese bus -> Bus */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.EliminarBus
  @cod_terminal INT,
  @placa        VARCHAR(10)
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;

  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  BEGIN TRY
    BEGIN TRAN;

    -- 1) Boletos de viajes de ese bus
    DELETE B
      FROM dbo.Boleto_Vista AS B
      JOIN dbo.Viaje_Vista  AS V
        ON V.cod_terminal = B.cod_terminal
       AND V.cod_viaje    = B.cod_viaje
     WHERE V.cod_terminal = @cod_terminal
       AND V.placa        = @placa;

    -- 2) Viajes de ese bus
    DELETE FROM dbo.Viaje_Vista
     WHERE cod_terminal = @cod_terminal
       AND placa        = @placa;

    -- 3) Bus
    DELETE FROM dbo.Bus_Vista
     WHERE cod_terminal = @cod_terminal
       AND placa        = @placa;

    IF @@ROWCOUNT = 0
      THROW 50013, 'No existe el bus con esos identificadores.', 1;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;  -- Si es remoto y DTC está apagado, verás 7391 aquí
  END CATCH
END
GO


/* =======================  INSERTAR RUTA  ======================= */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.InsertarRuta
  @cod_terminal   INT,
  @cod_ruta       INT,
  @ciudad_destino VARCHAR(80),
  @precio         DECIMAL(8,2)
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  -- Requisitos DML en vistas particionadas
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  IF (@precio <= 0) THROW 50020, 'precio debe ser > 0', 1;

  INSERT INTO dbo.Ruta_Vista (cod_terminal, cod_ruta, ciudad_destino, precio)
  VALUES (@cod_terminal, @cod_ruta, @ciudad_destino, @precio);
END
GO

/* =======================  ACTUALIZAR RUTA  ======================= */
/* Nota: no cambiamos PK (cod_terminal, cod_ruta). Solo destino/precio. */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.ActualizarRuta
  @cod_terminal   INT,
  @cod_ruta       INT,
  @ciudad_destino VARCHAR(80) = NULL,
  @precio         DECIMAL(8,2) = NULL
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  IF (@precio IS NOT NULL AND @precio <= 0)
    THROW 50021, 'precio debe ser > 0', 1;

  BEGIN TRY
    BEGIN TRAN;

    UPDATE R
       SET ciudad_destino = COALESCE(@ciudad_destino, R.ciudad_destino),
           precio         = COALESCE(@precio,         R.precio)
      FROM dbo.Ruta_Vista AS R
     WHERE R.cod_terminal = @cod_terminal
       AND R.cod_ruta     = @cod_ruta;

    IF @@ROWCOUNT = 0
      THROW 50022, 'No existe la ruta con esos identificadores.', 1;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
  END CATCH
END
GO

/* =======================  ELIMINAR RUTA  ======================= */
/* Borra primero Boletos -> Viajes de esa ruta -> Ruta */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.EliminarRuta
  @cod_terminal INT,
  @cod_ruta     INT
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  BEGIN TRY
    BEGIN TRAN;

    -- 1) Boletos de los viajes de la ruta
    DELETE B
      FROM dbo.Boleto_Vista AS B
      JOIN dbo.Viaje_Vista  AS V
        ON V.cod_terminal = B.cod_terminal
       AND V.cod_viaje    = B.cod_viaje
     WHERE V.cod_terminal  = @cod_terminal
       AND V.cod_ruta      = @cod_ruta;

    -- 2) Viajes de la ruta
    DELETE FROM dbo.Viaje_Vista
     WHERE cod_terminal = @cod_terminal
       AND cod_ruta     = @cod_ruta;

    -- 3) La ruta
    DELETE FROM dbo.Ruta_Vista
     WHERE cod_terminal = @cod_terminal
       AND cod_ruta     = @cod_ruta;

    IF @@ROWCOUNT = 0
      THROW 50023, 'No existe la ruta con esos identificadores.', 1;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;  -- si el destino es remoto y DTC está apagado, aquí verás el 7391
  END CATCH
END
GO

/* =======================  INSERTAR BOLETO  ======================= */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.InsertarBoleto
  @cod_terminal     INT,
  @cod_viaje        INT,
  @cedula_pasajero  VARCHAR(10),
  @num_asiento      INT
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;

  -- Requisitos DML en vistas particionadas
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  IF (@num_asiento <= 0) THROW 50030, 'num_asiento debe ser > 0', 1;

  INSERT INTO dbo.Boleto_Vista (cod_terminal, cod_viaje, cedula_pasajero, num_asiento)
  VALUES (@cod_terminal, @cod_viaje, @cedula_pasajero, @num_asiento);
END
GO

/* =======================  ACTUALIZAR BOLETO  ======================= */
/* Nota: no cambiamos la PK (cod_terminal, cod_viaje, cedula_pasajero).
         Si debes cambiar pasajero o viaje, haz DELETE + INSERT. */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.ActualizarBoleto
  @cod_terminal     INT,
  @cod_viaje        INT,
  @cedula_pasajero  VARCHAR(10),
  @num_asiento      INT = NULL
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;

  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  IF (@num_asiento IS NOT NULL AND @num_asiento <= 0)
    THROW 50031, 'num_asiento debe ser > 0', 1;

  BEGIN TRY
    BEGIN TRAN;

    UPDATE B
       SET num_asiento = COALESCE(@num_asiento, B.num_asiento)
      FROM dbo.Boleto_Vista AS B
     WHERE B.cod_terminal    = @cod_terminal
       AND B.cod_viaje       = @cod_viaje
       AND B.cedula_pasajero = @cedula_pasajero;

    IF @@ROWCOUNT = 0
      THROW 50032, 'No existe el boleto con esos identificadores.', 1;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
  END CATCH
END
GO

/* =======================  ELIMINAR BOLETO  ======================= */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.EliminarBoleto
  @cod_terminal     INT,
  @cod_viaje        INT,
  @cedula_pasajero  VARCHAR(10)
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;

  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  DELETE FROM dbo.Boleto_Vista
   WHERE cod_terminal    = @cod_terminal
     AND cod_viaje       = @cod_viaje
     AND cedula_pasajero = @cedula_pasajero;

  IF @@ROWCOUNT = 0
    THROW 50033, 'No existe el boleto con esos identificadores.', 1;
END
GO


/* =============  INSERTAR PASAJERO  ============= */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.InsertarPasajero
  @cedula_pasajero VARCHAR(10),
  @nombre_pasajero VARCHAR(100),
  @apellido_pasajero VARCHAR(100),
  @telefono_pasajero VARCHAR(20) = NULL,
  @correo_pasajero  VARCHAR(100) = NULL,
  @rowguid UNIQUEIDENTIFIER = NULL
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  -- (No es vista particionada; estos SET son opcionales)
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  IF LEN(@cedula_pasajero) = 0 OR @cedula_pasajero IS NULL
     THROW 50040, 'cedula_pasajero es requerida', 1;

  INSERT INTO dbo.Pasajero_Vista
    (cedula_pasajero, nombre_pasajero, apellido_pasajero,
     telefono_pasajero, correo_pasajero, rowguid)
  VALUES
    (@cedula_pasajero, @nombre_pasajero, @apellido_pasajero,
     @telefono_pasajero, @correo_pasajero, COALESCE(@rowguid, NEWID()));
END
GO

/* =============  ACTUALIZAR PASAJERO  ============= */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.ActualizarPasajero
  @cedula_pasajero VARCHAR(10),
  @nombre_pasajero   VARCHAR(100) = NULL,
  @apellido_pasajero VARCHAR(100) = NULL,
  @telefono_pasajero VARCHAR(20)  = NULL,
  @correo_pasajero   VARCHAR(100) = NULL
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  BEGIN TRY
    BEGIN TRAN;

    UPDATE P
       SET nombre_pasajero   = COALESCE(@nombre_pasajero,   P.nombre_pasajero),
           apellido_pasajero = COALESCE(@apellido_pasajero, P.apellido_pasajero),
           telefono_pasajero = COALESCE(@telefono_pasajero, P.telefono_pasajero),
           correo_pasajero   = COALESCE(@correo_pasajero,   P.correo_pasajero)
      FROM dbo.Pasajero_Vista AS P
     WHERE P.cedula_pasajero = @cedula_pasajero;

    IF @@ROWCOUNT = 0
      THROW 50041, 'No existe el pasajero con esa cédula.', 1;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
  END CATCH
END
GO

/* =============  ELIMINAR PASAJERO  ============= */
/* Si hay FKs desde Boleto -> (cod_viaje, cedula_pasajero),
   primero limpia boletos del pasajero (en cualquier terminal). */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.EliminarPasajero
  @cedula_pasajero VARCHAR(10)
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  BEGIN TRY
    BEGIN TRAN;

    -- Eliminar boletos del pasajero en ambos terminales (por la vista global si la usas)
    DELETE FROM dbo.Boleto_Vista
     WHERE cedula_pasajero = @cedula_pasajero;

    -- Eliminar pasajero
    DELETE FROM dbo.Pasajero_Vista
     WHERE cedula_pasajero = @cedula_pasajero;

    IF @@ROWCOUNT = 0
      THROW 50042, 'No existe el pasajero con esa cédula.', 1;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
  END CATCH
END
GO


/* ===============  INSERTAR CONDUCTOR  =============== */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.InsertarConductor
  @cod_conductor    INT,
  @nombre_conductor VARCHAR(80),
  @apellido_conductor VARCHAR(80),
  @cod_terminal     INT,          -- 1 Quito, 2 Ibarra
  @cedula_conductor VARCHAR(15)
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  -- Requisitos para DML en vistas particionadas
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  BEGIN TRY
    BEGIN TRAN;

    -- 1) Datos (local en Quito)
    INSERT INTO dbo.ConductorDatos (cod_conductor, nombre_conductor, apellido_conductor)
    VALUES (@cod_conductor, @nombre_conductor, @apellido_conductor);

    -- 2) Asignación a terminal (vía vista particionada → puede usar DTC)
    INSERT INTO dbo.ConductorHorizontal_Vista (cod_conductor, cod_terminal, cedula_conductor)
    VALUES (@cod_conductor, @cod_terminal, @cedula_conductor);

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
  END CATCH
END
GO

/* ===============  ACTUALIZAR CONDUCTOR  =============== */
/* Permite cambiar nombre/apellido y cedula. 
   Si cambias de terminal, hace "move": DELETE del fragmento actual + INSERT en el nuevo. */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.ActualizarConductor
  @cod_conductor      INT,
  @nombre_conductor   VARCHAR(80) = NULL,
  @apellido_conductor VARCHAR(80) = NULL,
  @cod_terminal_nuevo INT         = NULL,  -- si NULL no cambia
  @cedula_conductor   VARCHAR(15) = NULL   -- si NULL conserva actual
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  DECLARE @cod_terminal_actual INT, @cedula_actual VARCHAR(15);

  BEGIN TRY
    BEGIN TRAN;

    /* 1) Actualiza los datos personales (Quito) */
    IF @nombre_conductor IS NOT NULL OR @apellido_conductor IS NOT NULL
    BEGIN
      UPDATE D
         SET nombre_conductor   = COALESCE(@nombre_conductor,   D.nombre_conductor),
             apellido_conductor = COALESCE(@apellido_conductor, D.apellido_conductor)
        FROM dbo.ConductorDatos AS D
       WHERE D.cod_conductor = @cod_conductor;

      IF @@ROWCOUNT = 0
        THROW 50050, 'No existe ConductorDatos para ese cod_conductor.', 1;
    END

    /* 2) Lee asignación actual en vista horizontal */
    SELECT @cod_terminal_actual = H.cod_terminal,
           @cedula_actual       = H.cedula_conductor
      FROM dbo.ConductorHorizontal_Vista AS H
     WHERE H.cod_conductor = @cod_conductor;

    IF @cod_terminal_actual IS NULL
      THROW 50051, 'No existe asignación de terminal para ese conductor.', 1;

    /* 3) Decide operación horizontal */
    SET @cedula_conductor = COALESCE(@cedula_conductor, @cedula_actual);

    IF @cod_terminal_nuevo IS NULL OR @cod_terminal_nuevo = @cod_terminal_actual
    BEGIN
      -- Solo edición dentro del mismo fragmento
      UPDATE H
         SET cedula_conductor = @cedula_conductor
        FROM dbo.ConductorHorizontal_Vista AS H
       WHERE H.cod_conductor = @cod_conductor
         AND H.cod_terminal  = @cod_terminal_actual;

      IF @@ROWCOUNT = 0
        THROW 50052, 'No se pudo actualizar la cédula en el fragmento actual.', 1;
    END
    ELSE
    BEGIN
      -- "Move" entre particiones: DELETE + INSERT (no se puede cambiar la columna de partición con UPDATE)
      DELETE FROM dbo.ConductorHorizontal_Vista
       WHERE cod_conductor = @cod_conductor
         AND cod_terminal  = @cod_terminal_actual;

      INSERT INTO dbo.ConductorHorizontal_Vista (cod_conductor, cod_terminal, cedula_conductor)
      VALUES (@cod_conductor, @cod_terminal_nuevo, @cedula_conductor);
    END

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;  -- si el destino es remoto y DTC está apagado, fallará aquí
  END CATCH
END
GO

/* ===============  ELIMINAR CONDUCTOR  =============== */
/* Elimina boletos y viajes del conductor, luego su fila horizontal y por último ConductorDatos. */
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO
CREATE OR ALTER PROCEDURE dbo.EliminarConductor
  @cod_conductor INT
AS
BEGIN
  SET NOCOUNT ON; SET XACT_ABORT ON;
  SET NUMERIC_ROUNDABORT OFF; SET ANSI_PADDING ON; SET ANSI_WARNINGS ON;
  SET CONCAT_NULL_YIELDS_NULL ON; SET ARITHABORT ON;

  DECLARE @t INT;

  BEGIN TRY
    BEGIN TRAN;

    -- Detecta en qué terminal está (para borrar en el nodo correcto)
    SELECT @t = H.cod_terminal
      FROM dbo.ConductorHorizontal_Vista AS H
     WHERE H.cod_conductor = @cod_conductor;

    IF @t IS NULL
      THROW 50053, 'El conductor no tiene asignación de terminal.', 1;

    -- 1) Boletos de viajes de ese conductor
    DELETE B
      FROM dbo.Boleto_Vista AS B
      JOIN dbo.Viaje_Vista  AS V
        ON V.cod_terminal = B.cod_terminal
       AND V.cod_viaje    = B.cod_viaje
     WHERE V.cod_conductor = @cod_conductor;

    -- 2) Viajes del conductor
    DELETE FROM dbo.Viaje_Vista
     WHERE cod_conductor = @cod_conductor;

    -- 3) Asignación horizontal
    DELETE FROM dbo.ConductorHorizontal_Vista
     WHERE cod_conductor = @cod_conductor;

    -- 4) Datos personales
    DELETE FROM dbo.ConductorDatos
     WHERE cod_conductor = @cod_conductor;

    COMMIT;
  END TRY
  BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
  END CATCH
END
GO
