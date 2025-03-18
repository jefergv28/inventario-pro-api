# Inventario Pro API

Este es un proyecto de API REST desarrollado con **Spring Boot**, que forma parte de un ejercicio basado en el módulo de productos del proyecto **Inventario Pro**. Además, incluye un pequeño **frontend** en JavaScript para probar la API de forma sencilla.

## Tecnologías utilizadas

- **Spring Boot** (Backend)
- **Spring Data JPA** (Persistencia)
- **MySQL** (Base de datos)
- **JavaScript** (Frontend para pruebas)
- **Bootstrap** (Estilos en el frontend)

## Instalación y ejecución

### 1. Clonar el repositorio
```sh
 git clone https://github.com/jefergv28/inventario-pro-api.git
 cd inventario-pro-api
```

### 2. Configurar la base de datos
Se debe crear una base de datos en MySQL con el nombre **inventariopro** y configurar el archivo `application.properties` con las credenciales adecuadas:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventariopro
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```

### 3. Ejecutar el backend
Desde el directorio del proyecto, ejecutar:
```sh
mvn spring-boot:run
```
Por defecto, la API se ejecutará en **http://localhost:8000**

## Endpoints disponibles

| Método | Endpoint              | Descripción                   |
|--------|-----------------------|-------------------------------|
| GET    | `/productos`          | Obtiene todos los productos  |
| GET    | `/productos/{id}`     | Obtiene un producto por ID   |
| POST   | `/productos`          | Agrega un nuevo producto     |
| PUT    | `/productos/{id}`     | Actualiza un producto por ID |
| DELETE | `/productos/{id}`     | Elimina un producto por ID   |

## Frontend de prueba

Este proyecto incluye una pequeña interfaz en **JavaScript + Bootstrap** que permite interactuar con la API de manera visual.

### 1. Abrir el archivo HTML
Abre `index.html` en un navegador para acceder a la interfaz de prueba.

### 2. Funcionalidades del frontend
- Listar productos desde la API.
- Agregar productos mediante un formulario.
- Eliminar productos con un botón de acción.

## Contribuciones
Si deseas contribuir al proyecto, puedes hacer un **fork** y enviar un **pull request** con mejoras o nuevas funcionalidades.

## Autor
**Jeferson galvis** - Proyecto para evidencia del SENA

