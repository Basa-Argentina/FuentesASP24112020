-- Controlar Elementos en Consulta de legajos, deberia estar antes de asignar HdR y no luego de la HdR como esta actualmente.

UPDATE basa.dbo.tipos_operacion SET basa.dbo.tipos_operacion.tipoOperacionSiguiente_id = 15 WHERE basa.dbo.tipos_operacion.id = 23; -- Luego de Crear Remito ejecutar Asignar Hdr
UPDATE basa.dbo.tipos_operacion SET basa.dbo.tipos_operacion.tipoOperacionSiguiente_id = 55 WHERE basa.dbo.tipos_operacion.id = 15; -- Luego de Asignar Hdr ejecutar Controlar Elementos
UPDATE basa.dbo.tipos_operacion SET basa.dbo.tipos_operacion.tipoOperacionSiguiente_id = 16 WHERE basa.dbo.tipos_operacion.id = 55; -- Luego de Controlar Elementos ejecutar Leer Hdr y Remitos