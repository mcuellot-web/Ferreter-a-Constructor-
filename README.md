# Ferretería API

API REST de inventario y ventas para una ferretería. Java 17, Spring Boot 3, Spring Data JPA, PostgreSQL (Supabase), Docker, JUnit 5 + Mockito.

## Requisitos
- Java 17 y Maven 3.9+ (o solo Docker)
- Proyecto en Supabase

## Configuración
1. Copia `.env.example` a `.env` y completa los datos de Supabase (Project Settings > Database > Connection string > **Session pooler**, puerto 5432).
2. Las tablas se crean solas al arrancar (`ddl-auto=update`). Referencia en `docs/schema.sql`.

## Ejecutar
```bash
# Local
export $(grep -v '^#' .env | xargs)
mvn spring-boot:run

# Docker
docker compose up --build
```
Swagger UI: http://localhost:8080/swagger-ui.html

## Pruebas unitarias
```bash
mvn test
```

## Endpoints
| Recurso | Método y ruta |
|---|---|
| Categorías | `POST/GET /api/categorias`, `GET/PUT/DELETE /api/categorias/{id}` |
| Proveedores | `POST/GET /api/proveedores`, `GET/PUT/DELETE /api/proveedores/{id}` |
| Clientes | `POST/GET /api/clientes`, `GET/PUT/DELETE /api/clientes/{id}` |
| Productos | `POST/GET /api/productos`, `GET /api/productos/stock-bajo`, `GET/PUT/DELETE /api/productos/{id}` |
| Ventas | `POST/GET /api/ventas`, `GET /api/ventas/{id}` |
| Inventario | `POST /api/inventario/entradas`, `GET /api/inventario/productos/{id}/entradas` |

Colección de Postman: `docs/ferreteria-api.postman_collection.json` (importar y usar `baseUrl`).

## Reglas de negocio
- RN1: SKU de producto, NIT de proveedor, documento de cliente y nombre de categoría son únicos.
- RN2: el precio de venta no puede ser menor al precio de costo.
- RN3: no se puede vender más unidades que el stock disponible.
- RN4: cada venta descuenta stock; cada entrada de inventario lo aumenta.
- RN5: el precio unitario de la venta se toma del producto al momento de vender; el total es la suma de subtotales.
- RN6: un producto inactivo no se puede vender; eliminar un producto es una baja lógica.
- RN7: `GET /api/productos/stock-bajo` lista productos con stock menor o igual al mínimo.

## Arquitectura y SOLID
Capas: `controller` → `service` (interfaces + implementaciones) → `repository` → `model`.
- **S**: cada clase tiene una sola responsabilidad (controlador HTTP, servicio de negocio, repositorio, DTO).
- **O**: nuevos servicios se agregan implementando `CrudService` sin modificar los existentes.
- **L**: toda implementación de `CrudService` es intercambiable.
- **I**: interfaces pequeñas (`VentaService`, `InventarioService`, `ProductoService`).
- **D**: controladores y servicios dependen de abstracciones; inyección por constructor.
