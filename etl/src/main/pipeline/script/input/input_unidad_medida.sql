-- Load measurement units from ms-sql file

DROP TABLE unidadMedida IF EXISTS;

CREATE TABLE unidadMedida AS SELECT * FROM CSVREAD(@path_input ||
  'unidad_medida.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS unidadMedidaIndex ON unidadMedida(intIDUnidMed);
CREATE INDEX IF NOT EXISTS unidadMedidaTiendaIndex ON unidadMedida(vcClaveTienda);