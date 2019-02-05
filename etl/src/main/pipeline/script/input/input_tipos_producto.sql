-- Load tipo prodcto from ms-sql file

DROP TABLE tipoProducto IF EXISTS;

CREATE TABLE tipoProducto AS SELECT * FROM CSVREAD(@path_input ||
  'tipo_producto.csv', NULL,'charset=UTF-8 fieldSeparator=;');

CREATE INDEX IF NOT EXISTS tipoProductoIndex ON tipoProducto(vcTipoProducto);
CREATE INDEX IF NOT EXISTS tipoProductoTiendaIndex ON tipoProducto(vcClaveTienda);