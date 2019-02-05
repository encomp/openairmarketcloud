-- Load all the prices for a product from ms-sql file

DROP TABLE productoPrecio IF EXISTS;

CREATE TABLE productoPrecio AS SELECT * FROM CSVREAD(@path_input ||
  'producto_precio.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS productoPrecioIndex ON productoPrecio(vcIDProd);
CREATE INDEX IF NOT EXISTS productoPrecioTiendaIndex ON productoPrecio(vcClaveTienda);