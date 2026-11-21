-- Referencia del modelo (Hibernate lo crea con ddl-auto=update). Ejecutable en Supabase SQL Editor.
CREATE TABLE categorias (
  id BIGSERIAL PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL UNIQUE,
  descripcion VARCHAR(255)
);
CREATE TABLE proveedores (
  id BIGSERIAL PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  nit VARCHAR(30) NOT NULL UNIQUE,
  telefono VARCHAR(30),
  email VARCHAR(120)
);
CREATE TABLE clientes (
  id BIGSERIAL PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  documento VARCHAR(30) NOT NULL UNIQUE,
  telefono VARCHAR(30),
  email VARCHAR(120)
);
CREATE TABLE productos (
  id BIGSERIAL PRIMARY KEY,
  sku VARCHAR(40) NOT NULL UNIQUE,
  nombre VARCHAR(150) NOT NULL,
  descripcion VARCHAR(255),
  precio_costo NUMERIC(12,2) NOT NULL,
  precio_venta NUMERIC(12,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
  stock_minimo INT NOT NULL DEFAULT 0,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  categoria_id BIGINT NOT NULL REFERENCES categorias(id),
  proveedor_id BIGINT REFERENCES proveedores(id)
);
CREATE TABLE ventas (
  id BIGSERIAL PRIMARY KEY,
  fecha TIMESTAMP NOT NULL,
  cliente_id BIGINT REFERENCES clientes(id),
  total NUMERIC(14,2) NOT NULL
);
CREATE TABLE detalle_ventas (
  id BIGSERIAL PRIMARY KEY,
  venta_id BIGINT NOT NULL REFERENCES ventas(id) ON DELETE CASCADE,
  producto_id BIGINT NOT NULL REFERENCES productos(id),
  cantidad INT NOT NULL CHECK (cantidad > 0),
  precio_unitario NUMERIC(12,2) NOT NULL,
  subtotal NUMERIC(14,2) NOT NULL
);
CREATE TABLE movimientos_inventario (
  id BIGSERIAL PRIMARY KEY,
  producto_id BIGINT NOT NULL REFERENCES productos(id),
  proveedor_id BIGINT REFERENCES proveedores(id),
  cantidad INT NOT NULL CHECK (cantidad > 0),
  fecha TIMESTAMP NOT NULL,
  observacion VARCHAR(255)
);
