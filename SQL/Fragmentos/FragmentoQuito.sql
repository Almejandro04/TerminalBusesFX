create database Terminal_Quito COLLATE Modern_Spanish_CI_AS;

use Terminal_Quito

--------------- TABLAS A REPLICAR ------------------
-- TERMINAL
CREATE TABLE TERMINAL (
    cod_terminal INT PRIMARY KEY,
    ciudad_terminal VARCHAR(100),
    nombre_terminal VARCHAR(100),
    direccion_terminal VARCHAR(150)
);
GO

-- PASAJEROS
-- Crear tabla PASAJEROS
CREATE TABLE PASAJERO (
    cedula_pasajero VARCHAR(10) PRIMARY KEY,
    nombre_pasajero VARCHAR(100),
    apellido_pasajero VARCHAR(100),
    telefono_pasajero VARCHAR(20),
    correo_pasajero VARCHAR(100),
);
GO
----------------------------------------------------


-- 1) Fragmentacion vertical de CONDUCTOR
CREATE TABLE dbo.ConductorDatos (
  cod_conductor INT PRIMARY KEY,
  nombre_conductor VARCHAR(80) NOT NULL,
  apellido_conductor VARCHAR(80) NOT NULL
);

CREATE TABLE dbo.ConductorTerminal_1 (
  cod_terminal       INT          NOT NULL,
  cod_conductor      INT          NOT NULL,
  cedula_conductor   VARCHAR(15)  NOT NULL,
  CONSTRAINT CK_CT1_cod_terminal CHECK (cod_terminal = 1),
  CONSTRAINT PK_CT1 PRIMARY KEY (cod_terminal, cod_conductor),
  CONSTRAINT FK_CT1_Terminal FOREIGN KEY (cod_terminal)
    REFERENCES dbo.TERMINAL(cod_terminal)
);
GO

-- 2) Fragmentacion horizontal primaria de RUTA y BUS (Quito)

CREATE TABLE dbo.Ruta_1 (
  cod_terminal INT NOT NULL,
  cod_ruta INT NOT NULL,  
  ciudad_destino VARCHAR(80) NOT NULL,
  precio DECIMAL(8,2) NOT NULL,
  CONSTRAINT PK_Ruta_1 PRIMARY KEY (cod_terminal, cod_ruta),
  CONSTRAINT CK_Ruta_1 CHECK (cod_terminal = 1),
  CONSTRAINT FK_Ruta1_Terminal FOREIGN KEY (cod_terminal) REFERENCES dbo.TERMINAL(cod_terminal)
);


CREATE TABLE dbo.Bus_1 (
  cod_terminal INT NOT NULL,
  placa VARCHAR(10) NOT NULL,  
  capacidad INT NOT NULL,
  CONSTRAINT PK_Bus_1 PRIMARY KEY (cod_terminal, placa),
  CONSTRAINT CK_Bus_1 CHECK (cod_terminal = 1 AND capacidad > 0),
  CONSTRAINT FK_Bus1_Terminal FOREIGN KEY (cod_terminal) REFERENCES dbo.TERMINAL(cod_terminal)
);

-- 3) Fragmentacion horizontal derivada: VIAJE_1 respecto a RUTA_1

CREATE TABLE dbo.Viaje_1 ( 
  cod_terminal  INT        NOT NULL,
  cod_viaje     INT        NOT NULL,  
  placa         VARCHAR(10) NOT NULL,
  cod_ruta      INT        NOT NULL,
  cod_conductor INT        NOT NULL,
  fecha_viaje   DATE       NOT NULL,
  hora_viaje    TIME(0)    NOT NULL,

  CONSTRAINT PK_Viaje_1 PRIMARY KEY (cod_terminal, cod_viaje),
  CONSTRAINT CK_Viaje_1 CHECK (cod_terminal = 1),

  CONSTRAINT FK_V1_Bus
    FOREIGN KEY (cod_terminal, placa)
    REFERENCES dbo.Bus_1 (cod_terminal, placa),

  CONSTRAINT FK_V1_Ruta
    FOREIGN KEY (cod_terminal, cod_ruta)
    REFERENCES dbo.Ruta_1 (cod_terminal, cod_ruta),

  -- Referencia compuesta a la PK (cod_terminal, cod_conductor)
  CONSTRAINT FK_V1_Cond
    FOREIGN KEY (cod_terminal, cod_conductor)
    REFERENCES dbo.ConductorTerminal_1 (cod_terminal, cod_conductor)
);
GO

-- 4) Fragmentacion horizontal derivada: BOLETO_1 respecto a VIAJE_1

CREATE TABLE dbo.Boleto_1 (
  cod_terminal    INT         NOT NULL,
  cod_viaje       INT         NOT NULL,
  cedula_pasajero VARCHAR(10) NOT NULL,
  num_asiento     INT         NOT NULL,

  CONSTRAINT PK_Boleto_1 PRIMARY KEY (cod_terminal, cod_viaje, cedula_pasajero),
  CONSTRAINT CK_Boleto_1 CHECK (cod_terminal = 1),

  CONSTRAINT FK_B1_Viaje FOREIGN KEY (cod_terminal, cod_viaje)
    REFERENCES dbo.Viaje_1 (cod_terminal, cod_viaje),

  CONSTRAINT FK_B1_Pasaj FOREIGN KEY (cedula_pasajero)
    REFERENCES dbo.PASAJERO(cedula_pasajero),
);