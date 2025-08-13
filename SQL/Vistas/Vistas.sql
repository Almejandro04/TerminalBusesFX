----------------------------------------------------------
/*CREACION DE VISTAS*/
select * from Ruta_Vista
select * from Bus_Vista
select * from Viaje_Vista
select * from Boleto_Vista
select * from Pasajero_Vista
select * from Conductor_Vista
select * from Terminal_Vista

SET XACT_ABORT ON 
-- RUTA
Create view Ruta_Vista AS
SELECT * from [VLADIMIRJON].Terminal_Quito.dbo.Ruta_1
UNION ALL
SELECT * FROM [ASUS-8KR2UI2].Terminal_Ibarra.dbo.Ruta_2
GO

-- BUS
Create view Bus_Vista AS
SELECT * from [VLADIMIRJON].Terminal_Quito.dbo.Bus_1
UNION ALL
SELECT * FROM [ASUS-8KR2UI2].Terminal_Ibarra.dbo.Bus_2
GO

-- VIAJE
Create view Viaje_Vista AS
SELECT * from [VLADIMIRJON].Terminal_Quito.dbo.Viaje_1
UNION ALL
SELECT * FROM [ASUS-8KR2UI2].Terminal_Ibarra.dbo.Viaje_2
GO

-- BOLETO
Create view Boleto_Vista AS
SELECT * from [VLADIMIRJON].Terminal_Quito.dbo.Boleto_1
UNION ALL
SELECT * FROM [ASUS-8KR2UI2].Terminal_Ibarra.dbo.Boleto_2
GO

-- PASAJERO
Create view Pasajero_Vista AS
SELECT * from [VLADIMIRJON].Terminal_Quito.dbo.Pasajero
UNION 
SELECT * FROM [ASUS-8KR2UI2].Terminal_Ibarra.dbo.Pasajero
GO

-- CONDUCTOR
-- PRIMERO LA HORIZONTAL
Create view ConductorHorizontal_Vista AS
SELECT * from [VLADIMIRJON].Terminal_Quito.dbo.ConductorTerminal_1
UNION ALL
SELECT * FROM [ASUS-8KR2UI2].Terminal_Ibarra.dbo.ConductorTerminal_2
GO

select * from ConductorHorizontal_Vista
select * from ConductorDatos
-- UNION VISTA HORIZONTAL CON EL FRAGMENTO VERTICAL


CREATE OR ALTER VIEW dbo.Conductor_Vista AS
SELECT
  d.cod_conductor,
  d.nombre_conductor,
  d.apellido_conductor,
  h.cod_terminal,
  h.cedula_conductor
FROM [VLADIMIRJON].[Terminal_Quito].dbo.ConductorDatos          AS d
JOIN [VLADIMIRJON].[Terminal_Quito].dbo.ConductorHorizontal_Vista AS h
  ON h.cod_conductor = d.cod_conductor;
GO

-- TERMINAL
CREATE OR ALTER VIEW dbo.Terminal_Vista AS
SELECT
    cod_terminal,
    ciudad_terminal,
    nombre_terminal,
    direccion_terminal
FROM dbo.TERMINAL;
GO
