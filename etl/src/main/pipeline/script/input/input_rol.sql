-- Load role for all employees from ms-sql file

DROP TABLE rol IF EXISTS;

CREATE TABLE rol AS SELECT * FROM CSVREAD(@path_input ||
  'rol.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS rolIndex ON rol(intIDRol);
CREATE INDEX IF NOT EXISTS rolTiendaIndex ON rol(vcClaveTienda);