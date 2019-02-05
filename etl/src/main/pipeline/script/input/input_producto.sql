-- Load productos from ms-sql file

DROP TABLE producto IF EXISTS;

CREATE TABLE producto AS SELECT * FROM CSVREAD(@path_input ||
  'producto.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS productoIndex ON producto(vcIDProd);
CREATE INDEX IF NOT EXISTS productoTiendaIndex ON producto(vcClaveTienda);