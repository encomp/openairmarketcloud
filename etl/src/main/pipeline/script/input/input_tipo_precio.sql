-- Load tipo precio from ms-sql file

DROP TABLE tipoPrecio IF EXISTS;

CREATE TABLE tipoPrecio AS SELECT * FROM CSVREAD(@path_input ||
  'tipo_precio.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS tipoPrecioIndex ON tipoPrecio(intIDTipoPrecio);
CREATE INDEX IF NOT EXISTS tipoPrecioTiendaIndex ON tipoPrecio(vcClaveTienda);