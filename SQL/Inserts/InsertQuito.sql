use Terminal_Quito

select * from TERMINAL
select * from PASAJERO
select * from ConductorDatos
select * from ConductorTerminal_1
select * from Ruta_1
select * from Bus_1
select * from Viaje_1
select * from Boleto_1


------------------ TABLAS REPLICADAS-----------------------

INSERT INTO dbo.TERMINAL (cod_terminal, ciudad_terminal, nombre_terminal, direccion_terminal)
VALUES (1,'Quito','Terminal Terrestre Quito','Av. Mariscal Sucre'),
       (2,'Ibarra','Terminal Terrestre Ibarra','Av. Mariano Acosta');

INSERT INTO dbo.PASAJERO
(cedula_pasajero, nombre_pasajero, apellido_pasajero, telefono_pasajero, correo_pasajero)
VALUES
('1712345678','Ana','Torres','0998765432','ana.torres@gmail.com'),
('0923456781','Luis','Garcia','0981122334','luis.garcia@gmail.com'),
('1109876543','Maria','Lopez','0965566778','maria.lopez@gmail.com'),
('1754321098','Carlos','Perez','0993344556','carlos.perez@gmail.com'),
('0956781234','Sofia','Sanchez','0978899001','sofia.sanchez@gmail.com'),
('1720183456','Diego','Herrera','0987654321','diego.herrera@gmail.com'),
('0932105678','Valeria','Molina','0954433221','valeria.molina@gmail.com'),
('1204567890','Jorge','Rojas','0961122334','jorge.rojas@gmail.com'),
('1711122233','Daniela','Chavez','0992211334','daniela.chavez@gmail.com'),
('0945678901','Miguel','Ortiz','0956677889','miguel.ortiz@gmail.com');

INSERT INTO dbo.ConductorDatos (cod_conductor, nombre_conductor, apellido_conductor) VALUES
-- Quito (cod 104–108)
(104,'Lucia','Salazar'),
(105,'David','Narvaez'),
(106,'Paola','Benitez'),
(107,'Andres','Gomez'),
(108,'Santiago','Mora'),
-- Ibarra (cod 204–208)
(204,'Rene','Zambrano'),
(205,'Estefania','Almeida'),
(206,'Felipe','Vargas'),
(207,'Monica','Flores'),
(208,'Javier','Cedeno');

INSERT INTO dbo.ConductorTerminal_1 (cod_conductor, cod_terminal, cedula_conductor) VALUES
(104,1,'1715045678'),
(105,1,'1715056789'),
(106,1,'1715067890'),
(107,1,'1715078901'),
(108,1,'1715089012');

INSERT INTO dbo.Ruta_1 (cod_terminal, cod_ruta, ciudad_destino, precio) VALUES
(1, 10, 'Ibarra',      5.00),
(1, 20, 'Ambato',      8.50),
(1, 30, 'Cuenca',     15.00),
(1, 40, 'Guayaquil',  18.50);

INSERT INTO dbo.Bus_1 (cod_terminal, placa, capacidad) VALUES
(1, 'PQT-1001', 40),
(1, 'PQT-1002', 44),
(1, 'PQT-1003', 36),
(1, 'PQT-1004', 32);

INSERT INTO dbo.Viaje_1
(cod_terminal, cod_viaje, placa, cod_ruta, cod_conductor, fecha_viaje, hora_viaje)
VALUES
(1, 100, 'PQT-1001', 10, 104, '2025-08-15', '08:00:00'),
(1, 101, 'PQT-1002', 20, 105, '2025-08-15', '09:30:00'),
(1, 102, 'PQT-1003', 30, 106, '2025-08-16', '07:15:00'),
(1, 103, 'PQT-1004', 40, 107, '2025-08-16', '14:45:00'),
(1, 104, 'PQT-1001', 20, 108, '2025-08-17', '12:00:00');


INSERT INTO dbo.Boleto_1 (cod_terminal, cod_viaje, cedula_pasajero, num_asiento) VALUES
(1,100,'1712345678',1),
(1,100,'0923456781',2),
(1,101,'1109876543',5),
(1,101,'1754321098',6),
(1,102,'0956781234',3),
(1,102,'1720183456',4),
(1,103,'0932105678',10),
(1,103,'1711122233',11),
(1,104,'1204567890',7),
(1,104,'0945678901',8);
