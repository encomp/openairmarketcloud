-- Load product provider from ms-sql file

DROP TABLE fabricante IF EXISTS;

CREATE TABLE fabricante AS SELECT * FROM CSVREAD(@path_input ||
  'fabricante.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS fabricanteIndex ON fabricante(intIDFabricante);
CREATE INDEX IF NOT EXISTS fabricanteTiendaIndex ON fabricante(vcClaveTienda);