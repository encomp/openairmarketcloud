SELECT p.vcClaveTienda AS vcClaveTienda, p.vcIDProd AS vcIDProd, intIDTipoPrecio, decPrecio,
       dteFechaAsigna, pp.vcOperacion, pp.dteFechaOperacion, pp.intIDEmpleado1, pp.bitActivo
FROM dbo.PRODUCTOS AS p
INNER JOIN dbo.PRODUCTO_PRECIO AS pp
ON (p.dteFecUltVenta IS NOT NULL
    AND substring(convert(varchar, p.dteFecUltVenta, 105), 7, 4) = '2018'
    AND p.vcIDProd = pp.vcIDProd)
ORDER BY p.vcIDProd, dteFechaAsigna ASC;