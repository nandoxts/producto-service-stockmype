# producto-service-stockmype

Microservicio de **inventario**: CRUD de productos en MySQL (`stockmype_productos`). Cada petición usa HTTP Basic y se valida llamando a **auth-service por Feign**.

- Puerto: **8082** (se accede por el gateway: `http://localhost:8080/api/productos`)

| Método | Ruta | Respuestas |
|---|---|---|
| GET | `/api/productos` | 200, 401 |
| GET | `/api/productos/{id}` | 200, 404 |
| POST | `/api/productos` | 201, 400, 409 |
| PUT | `/api/productos/{id}` | 200, 400, 404, 409 |
| DELETE | `/api/productos/{id}` | 204, 404 |

```bash
./mvnw spring-boot:run
./mvnw test     # pruebas de la capa de datos: insertar, actualizar, eliminar y listar
```
