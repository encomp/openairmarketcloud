CALL CSVWRITE(@PATH_OUTPUT ||'producto.csv',
'SELECT VCCODIGOBARRAS AS ID, VCPLU AS REFERENCEID, VCDESCPROD AS NAME, '''' AS IMAGE, ''ITEM'' AS PRODUCTTYPE, VCTIPOPRODUCTO AS productCategory, INTIDUNIDMED AS productMeasureUnit, INTIDMARCA AS productBrand, pps.DECPRECIO AS SALEPRICE, TO_CHAR((CAST(pps.DECPRECIO AS float) - CAST(ppp.DECPRECIO AS float))*100/CAST(pps.DECPRECIO AS float),''999D00'') AS PROFIT, ppp.DECPRECIO AS PURCHASEPRICE FROM PRODUCTO p INNER JOIN PRODUCTOPRECIO pps ON(p.VCIDPROD = pps.VCIDPROD AND pps.INTIDTIPOPRECIO = 1) INNER JOIN PRODUCTOPRECIO ppp ON(p.VCIDPROD = ppp.VCIDPROD AND ppp.INTIDTIPOPRECIO = 2)',
'CHARSET=UTF-8');
