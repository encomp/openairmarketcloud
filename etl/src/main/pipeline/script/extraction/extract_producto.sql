SELECT vcClaveTienda, vcIDProd, vcCodigoBarras, vcPLU, vcTipoProducto, intIdUnidMed,intIDMarca,
       vcDescProd, vcNombCorto, dteFecUltCompra, dteFecUltVenta, dteFechAlta, vcOperacion,
       dteFechaOperacion
FROM dbo.PRODUCTOS
WHERE dteFecUltVenta IS NOT NULL
AND substring(convert(varchar, dteFecUltVenta, 105), 7, 4) = '2018';