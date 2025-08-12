-- Quito
USE [Terminal_Quito];
GO

CREATE OR ALTER VIEW [dbo].[Vista_Terminal]
AS
SELECT
    cod_terminal,
    ciudad_terminal,
    nombre_terminal,
    direccion_terminal
FROM dbo.TERMINAL;
GO

USE [Terminal_Quito];
GO


CREATE OR ALTER VIEW [dbo].[Bus_Quito_Vista]
AS
SELECT
    cod_terminal,
    placa       AS placa_vehiculo,
    capacidad   AS capacidad_vehiculo
FROM dbo.Bus_1

GO

select * from Bus_Quito_Vista

USE [Terminal_Ibarra];
GO

CREATE OR ALTER VIEW [dbo].[Viaje_Quito_Vista]
AS
SELECT
    cod_terminal,
    cod_viaje,
    placa,
    cod_ruta,
    cod_conductor,
    fecha_viaje,
    hora_viaje
FROM dbo.Viaje_1
-- El CHECK ya asegura que cod_terminal = 2, así que no es obligatorio:
-- WHERE cod_terminal = 2
;
GO

CREATE OR ALTER VIEW [dbo].[Pasajero_Vista]
AS
SELECT
    cedula_pasajero,
    nombre_pasajero,
    apellido_pasajero,
    telefono_pasajero,
    correo_pasajero
FROM dbo.PASAJERO;
GO

select * from Pasajero_Vista

CREATE OR ALTER VIEW [dbo].[Ruta_Quito_Vista]
AS
SELECT
    cod_terminal,
    cod_ruta,
    ciudad_destino,
    precio
FROM dbo.Ruta_1
-- El CHECK ya garantiza que cod_terminal = 2, así que no es necesario filtrar
;
GO

select * from Ruta_Quito_Vista

USE [Terminal_Quito];
GO

select * from ConductorTerminal_1

CREATE OR ALTER VIEW [dbo].[Conductor_Quito_Vista]
AS
SELECT
    ct.cod_conductor,
    ct.cod_terminal,
    ct.cedula_conductor,
    cd.nombre_conductor,
    cd.apellido_conductor
FROM dbo.ConductorTerminal_1 AS ct
INNER JOIN dbo.ConductorDatos    AS cd
    ON cd.cod_conductor = ct.cod_conductor;
GO

select * from Conductor_Quito_Vista
