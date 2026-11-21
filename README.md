# Ferretería API

API REST de inventario y ventas para la Ferretería El Constructor. Está construida con **Java 17, Spring Boot 3, Spring Data JPA y PostgreSQL (Supabase)**, y se puede ejecutar con Maven o con Docker. Incluye pruebas unitarias con JUnit 5 + Mockito y una colección de Postman.

> **Esta aplicación es un backend, no una página web.** No tiene pantallas de login ni un panel de administración: expone endpoints JSON que consumen Postman, Swagger UI, una futura aplicación web o una app móvil. La única interfaz web incluida es **Swagger UI**, que sirve para documentar y probar los endpoints.

## Requisitos

- Java 17 y Maven 3.9+ (para ejecución local), o
- Docker + Docker Compose (para ejecución en contenedor)
- Una base de datos PostgreSQL. Para producción se usa Supabase; para una demostración rápida no se necesita nada, gracias al perfil `demo` con H2.

## Opción 1: probar sin base de datos (modo demo)

Ideal para ver la API funcionando sin crear un proyecto en Supabase. Usa una base H2 en memoria y carga datos de ejemplo.

```bash
mvn -Pdemo spring-boot:run -Dspring-boot.run.profiles=demo
```

Luego abre:

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- Consola H2: <http://localhost:8080/h2-console> (JDBC URL: `jdbc:h2:mem:ferreteria`, usuario `sa`, sin contraseña)

Datos de ejemplo: categorías, un proveedor, un cliente y cuatro productos (uno de ellos con stock bajo).

> El perfil `demo` es solo para pruebas. La aplicación entregada sigue usando PostgreSQL/Supabase por defecto.

## Opción 2: conectar con Supabase (PostgreSQL)

1. Crea un proyecto en [Supabase](https://supabase.com).
2. En **Project Settings → Database → Connection string**, copia la cadena del **Session pooler** (puerto 5432).
3. Copia `.env.example` a `.env` y completa las variables:

```bash
cp .env.example .env
```

```env
DB_URL=jdbc:postgresql://aws-0-REGION.pooler.supabase.com:5432/postgres?sslmode=require
DB_USER=postgres.TU_PROJECT_REF
DB_PASSWORD=TU_PASSWORD
```

4. Exporta las variables y ejecuta:

```bash
set -a
source .env
set +a
mvn spring-boot:run
```

Las tablas se crean automáticamente al arrancar (`spring.jpa.hibernate.ddl-auto=update`). El archivo `docs/schema.sql` contiene el esquema de referencia para ejecutarlo manualmente en el SQL Editor de Supabase.

## Opción 3: Docker

```bash
cp .env.example .env    # completar con los datos de Supabase
docker compose up --build
```

La API queda en <http://localhost:8080> y Swagger UI en <http://localhost:8080/swagger-ui.html>.

El Dockerfile usa dos etapas: compila con Maven/Java 17 y ejecuta el JAR en una imagen ligera de Java 17. Las credenciales se pasan por variables de entorno y nunca se incluyen en la imagen.

## Despliegue en Vercel + Supabase

Vercel puede ejecutar esta API mediante su **Container Runtime** (Beta), usando el archivo `Dockerfile.vercel` que está en la raíz. No es una web estática: Vercel construye la imagen, la ejecuta y le asigna una URL pública.

### 1. Base de datos en Supabase

1. Crea un proyecto en [Supabase](https://supabase.com).
2. Ve a **Project Settings → Database → Connection string → Session pooler** (puerto 5432).
3. Guarda estos tres datos:
   - `DB_URL`: `jdbc:postgresql://aws-0-REGION.pooler.supabase.com:5432/postgres?sslmode=require`
   - `DB_USER`: `postgres.TU_PROJECT_REF`
   - `DB_PASSWORD`: la contraseña del proyecto.
4. No hace falta crear las tablas a mano: al arrancar, Hibernate las crea o actualiza (`ddl-auto=update`). Si prefieres hacerlo manualmente, ejecuta `docs/schema.sql` en el SQL Editor.

### 2. Proyecto en Vercel

1. En Vercel, elige **Add New → Project** e importa el repositorio de GitHub.
2. Vercel detecta `Dockerfile.vercel` y selecciona el preset **Container**. Si no lo detecta, en **Framework Preset** elige **Container**.
3. En **Environment Variables** agrega:

   | Variable | Valor |
   |---|---|
   | `DB_URL` | La cadena JDBC de Supabase |
   | `DB_USER` | `postgres.TU_PROJECT_REF` |
   | `DB_PASSWORD` | La contraseña de Supabase |

4. Pulsa **Deploy**. Al terminar tendrás una URL como `https://ferreteria-api.vercel.app`.

Comprueba:

- `https://TU-PROYECTO.vercel.app/` → JSON con el estado de la API.
- `https://TU-PROYECTO.vercel.app/swagger-ui.html` → Swagger UI.
- `https://TU-PROYECTO.vercel.app/api/productos` → lista de productos en JSON.

> El Container Runtime de Vercel es Beta y escala a cero tras unos minutos sin tráfico; la primera petición puede tardar unos segundos (cold start). Para producción estable también sirven Render, Railway o Fly.io con el `Dockerfile` normal.

### 3. Importar el repositorio

Si el repositorio original aún no tiene el código, usa el fork `https://github.com/zxvsho/Ferreter-a-Constructor-` o fusiona el Pull Request #1 en el repositorio original. Luego impórtalo en Vercel.

## Pruebas unitarias

```bash
mvn test
```

Son 13 pruebas con JUnit 5 y Mockito. No requieren base de datos porque los repositorios se simulan.

## Endpoints

| Recurso | Métodos y rutas |
|---|---|
| Categorías | `POST/GET /api/categorias`, `GET/PUT/DELETE /api/categorias/{id}` |
| Proveedores | `POST/GET /api/proveedores`, `GET/PUT/DELETE /api/proveedores/{id}` |
| Clientes | `POST/GET /api/clientes`, `GET/PUT/DELETE /api/clientes/{id}` |
| Productos | `POST/GET /api/productos`, `GET /api/productos/stock-bajo`, `GET/PUT/DELETE /api/productos/{id}` |
| Ventas | `POST/GET /api/ventas`, `GET /api/ventas/{id}` |
| Inventario | `POST /api/inventario/entradas`, `GET /api/inventario/productos/{id}/entradas` |

En total son 26 endpoints. La colección `docs/ferreteria-api.postman_collection.json` incluye solicitudes de ejemplo; se importa en Postman o Insomnia y se ajusta la variable `baseUrl` (por defecto `http://localhost:8080`).

### Ejemplo rápido con curl

```bash
# Listar productos (con los datos del perfil demo)
curl http://localhost:8080/api/productos

# Registrar una venta
curl -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"items":[{"productoId":1,"cantidad":2}]}'

# Consultar productos con stock bajo
curl http://localhost:8080/api/productos/stock-bajo
```

## Reglas de negocio

- **RN1:** SKU de producto, NIT de proveedor, documento de cliente y nombre de categoría son únicos.
- **RN2:** el precio de venta no puede ser menor al precio de costo.
- **RN3:** no se puede vender más unidades que el stock disponible.
- **RN4:** cada venta descuenta stock; cada entrada de inventario lo aumenta.
- **RN5:** el precio unitario se toma del producto al momento de vender; el total es la suma de subtotales.
- **RN6:** un producto inactivo no se puede vender; eliminar un producto es una baja lógica.
- **RN7:** `GET /api/productos/stock-bajo` lista productos con stock menor o igual al mínimo.

## Arquitectura y SOLID

Capas: `controller` → `service` (interfaces + implementaciones) → `repository` → `model`.

- **S:** cada clase tiene una sola responsabilidad (controlador HTTP, servicio de negocio, repositorio, DTO).
- **O:** nuevos servicios se agregan implementando `CrudService` sin modificar los existentes.
- **L:** toda implementación de `CrudService` es intercambiable.
- **I:** interfaces pequeñas (`VentaService`, `InventarioService`, `ProductoService`).
- **D:** controladores y servicios dependen de abstracciones; inyección por constructor.

## Estructura del proyecto

```
src/main/java/com/ferreteria/api/
├── controller/     Endpoints REST
├── service/        Lógica de negocio e interfaces
├── repository/     Acceso a datos (Spring Data JPA)
├── model/          Entidades JPA
├── dto/            Objetos de entrada y salida
└── exception/      Manejo global de errores

src/main/resources/
├── application.properties        Configuración por variables de entorno
├── application-demo.properties   Perfil H2 para pruebas locales
└── data-demo.sql                 Datos de ejemplo del perfil demo

docs/
├── ferreteria-api.postman_collection.json
└── schema.sql
```

## Estado del proyecto

- Compilación y pruebas verificadas con Java 17 y Maven 3.9.9.
- 13/13 pruebas unitarias en verde.
- Endpoints probados de extremo a extremo (creación, ventas, stock, validaciones y errores).
- Despliegue con Docker preparado; requiere Docker Engine y un `.env` con credenciales de Supabase.
