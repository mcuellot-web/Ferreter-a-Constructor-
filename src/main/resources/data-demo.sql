-- Datos de ejemplo para el perfil demo (H2). Se cargan al arrancar.
INSERT INTO categorias (nombre, descripcion) VALUES
  ('Tornillería', 'Tornillos, tuercas y fijaciones'),
  ('Herramientas', 'Herramientas manuales y eléctricas'),
  ('Plomería', 'Tuberías, llaves y accesorios');

INSERT INTO proveedores (nombre, nit, telefono, email) VALUES
  ('Ferreproveedores SAS', '900123456-7', '3001234567', 'ventas@ferreproveedores.com');

INSERT INTO clientes (nombre, documento, telefono, email) VALUES
  ('Constructora El Roble', '123456789', '3011112233', 'compras@elroble.com');

INSERT INTO productos (sku, nombre, descripcion, precio_costo, precio_venta, stock, stock_minimo, activo, categoria_id, proveedor_id)
SELECT 'TOR-010', 'Tornillo 1/2 x 3', 'Tornillo de acero', 100, 150, 40, 10, TRUE, c.id, p.id
FROM categorias c, proveedores p
WHERE c.nombre = 'Tornillería' AND p.nit = '900123456-7';

INSERT INTO productos (sku, nombre, descripcion, precio_costo, precio_venta, stock, stock_minimo, activo, categoria_id, proveedor_id)
SELECT 'CLA-001', 'Clavos 2 pulgadas', 'Clavos de acero', 50, 80, 2, 5, TRUE, c.id, p.id
FROM categorias c, proveedores p
WHERE c.nombre = 'Tornillería' AND p.nit = '900123456-7';

INSERT INTO productos (sku, nombre, descripcion, precio_costo, precio_venta, stock, stock_minimo, activo, categoria_id)
SELECT 'MART-001', 'Martillo de uña', 'Martillo de 16 oz', 15000, 25000, 15, 3, TRUE, c.id
FROM categorias c
WHERE c.nombre = 'Herramientas';

INSERT INTO productos (sku, nombre, descripcion, precio_costo, precio_venta, stock, stock_minimo, activo, categoria_id)
SELECT 'LLA-001', 'Llave de paso 1/2', 'Llave de bola para agua', 8000, 12000, 8, 4, TRUE, c.id
FROM categorias c
WHERE c.nombre = 'Plomería';
