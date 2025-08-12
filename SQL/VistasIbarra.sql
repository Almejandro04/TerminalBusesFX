USE [Terminal_Ibarra]
GO

CREATE OR ALTER VIEW [dbo].[Vista_Terminal]
AS
SELECT
    cod_terminal      AS cod_terminal,
    ciudad_terminal   AS ciudad_terminal,
    nombre_terminal   AS nombre_terminal,
    direccion_terminal AS direccion_terminal
FROM dbo.TERMINAL;
GO


select * from Vista_Terminal
GO

CREATE OR ALTER VIEW [dbo].[Bus_Ibarra_Vista]
AS
SELECT
    cod_terminal,
    placa       AS placa_vehiculo,
    capacidad   AS capacidad_vehiculo
FROM dbo.Bus_2

GO

select * from Bus_Ibarra_Vista

USE [Terminal_Ibarra];
GO

CREATE OR ALTER VIEW [dbo].[Viaje_Ibarra_Vista]
AS
SELECT
    cod_terminal,
    cod_viaje,
    placa,
    cod_ruta,
    cod_conductor,
    fecha_viaje,
    hora_viaje
FROM dbo.Viaje_2
GO

USE [Terminal_Ibarra];
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


USE [Terminal_Ibarra];
GO

CREATE OR ALTER VIEW [dbo].[Ruta_Ibarra_Vista]
AS
SELECT
    cod_terminal,
    cod_ruta,
    ciudad_destino,
    precio
FROM dbo.Ruta_2
-- El CHECK ya garantiza que cod_terminal = 2, así que no es necesario filtrar
;
GO

select * from Ruta_Ibarra_Vista


USE [Terminal_Ibarra];
GO

select * from ConductorTerminal_2

CREATE OR ALTER VIEW [dbo].[Conductor_Ibarra_Vista]
AS
SELECT
    ct.cod_conductor,
    ct.cod_terminal,
    ct.cédula_conductor,
    cd.nombre_conductor,
    cd.apellido_conductor
FROM dbo.ConductorTerminal_2 AS ct
INNER JOIN [VLADIMIRJON].[Terminal_Quito].[dbo].ConductorDatos    AS cd
    ON cd.cod_conductor = ct.cod_conductor;
GO

select * from Conductor_Ibarra_Vista