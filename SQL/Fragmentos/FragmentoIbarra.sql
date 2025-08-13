create database Terminal_Ibarra COLLATE Modern_Spanish_CI_AS;

use Terminal_Ibarra


-- Vertical (solo este fragmento en Ibarra)
CREATE TABLE dbo.ConductorTerminal_2 (
  cod_conductor INT PRIMARY KEY,
  cod_terminal  INT NOT NULL,
  cedula_conductor VARCHAR(15) NOT NULL,
  CONSTRAINT FK_CT2_Terminal FOREIGN KEY (cod_terminal) REFERENCES dbo.TERMINAL(cod_terminal)
);

-- Horizontal primaria (PK incluye cod_terminal)
CREATE TABLE dbo.Ruta_2 (
  cod_terminal    INT         NOT NULL,
  cod_ruta        INT         NOT NULL,
  ciudad_destino  VARCHAR(80) NOT NULL,
  precio          DECIMAL(8,2) NOT NULL,
  CONSTRAINT PK_Ruta_2 PRIMARY KEY (cod_terminal, cod_ruta),
  CONSTRAINT CK_Ruta_2 CHECK (cod_terminal = 2),
  CONSTRAINT FK_Ruta2_Terminal FOREIGN KEY (cod_terminal)
    REFERENCES dbo.TERMINAL(cod_terminal)
);


CREATE TABLE dbo.Bus_2 (
  cod_terminal  INT          NOT NULL,
  placa         VARCHAR(10)  NOT NULL,
  capacidad     INT          NOT NULL,
  CONSTRAINT PK_Bus_2 PRIMARY KEY (cod_terminal, placa),
  CONSTRAINT CK_Bus_2 CHECK (cod_terminal = 2 AND capacidad > 0),
  CONSTRAINT FK_Bus2_Terminal FOREIGN KEY (cod_terminal)
    REFERENCES dbo.TERMINAL(cod_terminal)
);


-- Horizontal derivada (a�adir cod_terminal y ponerlo en la PK)
CREATE TABLE dbo.Viaje_2 (
  cod_terminal   INT          NOT NULL,
  cod_viaje      INT          NOT NULL,
  placa          VARCHAR(10)  NOT NULL,
  cod_ruta       INT          NOT NULL,
  cod_conductor  INT          NOT NULL,
  fecha_viaje    DATE         NOT NULL,
  hora_viaje     TIME(0)      NOT NULL,
  CONSTRAINT PK_Viaje_2 PRIMARY KEY (cod_terminal, cod_viaje),
  CONSTRAINT CK_Viaje_2 CHECK (cod_terminal = 2),
  CONSTRAINT FK_V2_Bus  FOREIGN KEY (cod_terminal, placa)
    REFERENCES dbo.Bus_2 (cod_terminal, placa),
  CONSTRAINT FK_V2_Ruta FOREIGN KEY (cod_terminal, cod_ruta)
    REFERENCES dbo.Ruta_2 (cod_terminal, cod_ruta),
  CONSTRAINT FK_V2_Cond FOREIGN KEY (cod_conductor)
    REFERENCES dbo.ConductorTerminal_2 (cod_conductor)
);


CREATE TABLE dbo.Boleto_2 (
  cod_terminal     INT          NOT NULL,
  cod_viaje        INT          NOT NULL,
  cedula_pasajero  VARCHAR(10)  NOT NULL,
  num_asiento      INT          NOT NULL,
  CONSTRAINT PK_Boleto_2 PRIMARY KEY (cod_terminal, cod_viaje, cedula_pasajero),
  CONSTRAINT CK_Boleto_2 CHECK (cod_terminal = 2),
  CONSTRAINT FK_B2_Viaje FOREIGN KEY (cod_terminal, cod_viaje)
    REFERENCES dbo.Viaje_2 (cod_terminal, cod_viaje),
  CONSTRAINT FK_B2_Pasaj FOREIGN KEY (cedula_pasajero)
    REFERENCES dbo.PASAJERO(cedula_pasajero)
  -- Opcional: UNIQUE (cod_terminal, cod_viaje, num_asiento)
);