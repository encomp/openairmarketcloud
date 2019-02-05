-- Load employee from ms-sql file

DROP TABLE empleado IF EXISTS;

CREATE TABLE empleado AS SELECT * FROM CSVREAD(@path_input ||
  'empleado.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS empleadoIndex ON empleado(intIDEmpleado);
CREATE INDEX IF NOT EXISTS empleadoTiendaIndex ON empleado(vcClaveTienda);