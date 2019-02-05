SELECT vcClaveTienda, intIDEmpleado, vcNombreEmpleado, vcPaternoEmpleado, vcMaternoEmpleado,
       vcUsuario, vcPassword, intIDRol, dteFecIngresoEmpleado, dteFecTerminacionEmpleado,
       vcOperacion, dteFechaOperacion, intIDEmpleado1, bitActivo, bitModificable
FROM dbo.EMPLEADOS
ORDER BY 1;