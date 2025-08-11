use Terminal_Ibarra

select * from TERMINAL
select * from PASAJERO
select * from ConductorTerminal_2
select * from Ruta_2
select * from Bus_2
select * from Viaje_2
select * from Boleto_2

INSERT INTO dbo.ConductorTerminal_2 (cod_conductor, cod_terminal, cédula_conductor) VALUES
(204,2,'1004045678'),
(205,2,'1004056789'),
(206,2,'1004067890'),
(207,2,'1004078901'),
(208,2,'1004089012');

INSERT INTO dbo.Ruta_2 (cod_terminal, cod_ruta, ciudad_destino, precio) VALUES
(2, 50, 'Quito',       5.00),
(2, 60, 'Otavalo',     2.00),
(2, 70, 'Tulcan',      7.50),
(2, 80, 'Esmeraldas', 14.00);

INSERT INTO dbo.Bus_2 (cod_terminal, placa, capacidad) VALUES
(2, 'PIB-2001', 40),
(2, 'PIB-2002', 44),
(2, 'PIB-2003', 36),
(2, 'PIB-2004', 32);

INSERT INTO dbo.Viaje_2
(cod_terminal, cod_viaje, placa, cod_ruta, cod_conductor, fecha_viaje, hora_viaje)
VALUES
(2, 200, 'PIB-2001', 50, 204, '2025-08-15', '08:30:00'),
(2, 201, 'PIB-2002', 60, 205, '2025-08-15', '10:00:00'),
(2, 202, 'PIB-2003', 70, 206, '2025-08-16', '06:45:00'),
(2, 203, 'PIB-2004', 80, 207, '2025-08-16', '15:20:00'),
(2, 204, 'PIB-2001', 60, 208, '2025-08-17', '11:10:00');

INSERT INTO dbo.Boleto_2 (cod_terminal, cod_viaje, cedula_pasajero, num_asiento) VALUES
(2,200,'1712345678',1),
(2,200,'0923456781',2),
(2,201,'1109876543',5),
(2,201,'1754321098',6),
(2,202,'0956781234',3),
(2,202,'1720183456',4),
(2,203,'0932105678',10),
(2,203,'1711122233',11),
(2,204,'1204567890',7),
(2,204,'0945678901',8);
