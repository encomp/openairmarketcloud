CALL CSVWRITE(@PATH_OUTPUT ||'marca.csv',
'SELECT INTIDMARCA AS REFERENCEID, VCDESCMARCA AS NAME, INTIDFABRICANTE AS IDFABRICANTE FROM MARCA',
'CHARSET=UTF-8');