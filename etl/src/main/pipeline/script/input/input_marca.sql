-- Load product brands from ms-sql file

DROP TABLE marca IF EXISTS;

CREATE TABLE marca AS SELECT * FROM CSVREAD(@path_input ||
  'marca.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS marcaIndex ON marca(intIDMarca);
CREATE INDEX IF NOT EXISTS marcaTiendaIndex ON marca(vcClaveTienda);