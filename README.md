# Carrito API – Spring Boot

## Descripción

Este proyecto implementa una **API REST para la gestión de carritos de compra** utilizando **Spring Boot, JPA y una base de datos relacional**.
La API permite crear carritos, añadir artículos al carrito, consultar el carrito y eliminarlo.

La persistencia se gestiona mediante **Spring Data JPA**, que genera automáticamente las tablas de la base de datos a partir de las entidades del proyecto.

---

# Arquitectura

El proyecto sigue una arquitectura en capas:

**Controller**

* Expone los endpoints REST
* Recibe las peticiones HTTP

**Service**

* Contiene la lógica de negocio
* Gestiona validaciones y transacciones

**Repository**

* Acceso a base de datos
* Usa Spring Data JPA

**Entity**

* Representación de las tablas de la base de datos

---

# Entidades

## Carrito

Representa un carrito de compra.

Campos:

| Campo         | Tipo   | Descripción                     |
| ------------- | ------ | ------------------------------- |
| id            | Long   | Clave primaria generada         |
| idCarrito     | String | Identificador único del carrito |
| idUsuario     | String | Identificador del usuario       |
| correoUsuario | String | Email del usuario               |
| totalPrecio   | double | Precio total del carrito        |

---

## LineaCarrito

Representa un artículo dentro de un carrito.

Campos:

| Campo          | Tipo    | Descripción                       |
| -------------- | ------- | --------------------------------- |
| id             | Long    | Clave primaria                    |
| carrito        | Carrito | Relación ManyToOne con el carrito |
| idArticulo     | String  | Identificador del artículo        |
| precioUnitario | double  | Precio por unidad                 |
| numeroUnidades | int     | Cantidad del artículo             |
| costeLinea     | double  | Coste total de la línea           |

---

# Endpoints

## Crear carrito

POST /api/carritos

Body ejemplo:

```json
{
  "idCarrito": "C-001",
  "idUsuario": "U-001",
  "correoUsuario": "usuario@email.com"
}
```

Respuesta:

```
201 CREATED
```

---

## Obtener carrito

GET /api/carritos/{idCarrito}

Ejemplo:

```
GET /api/carritos/C-001
```

---

## Añadir artículo al carrito

POST /api/carritos/{idCarrito}/lineas

Body:

```json
{
  "idArticulo": "A-100",
  "precioUnitario": 25.0,
  "numeroUnidades": 2
}
```

Respuesta:

```
201 CREATED
```

---

## Eliminar carrito

DELETE /api/carritos/{idCarrito}

Respuesta:

```
204 NO CONTENT
```

---

# Persistencia

La persistencia se gestiona mediante **Spring Data JPA**.
Las tablas se generan automáticamente a partir de las entidades del proyecto.

Relación principal:

```
Carrito 1 ---- * LineaCarrito
```

Un carrito puede contener múltiples líneas de artículos.

---

# Tecnologías utilizadas

* Java
* Spring Boot
* Spring Data JPA
* H2 / Base de datos relacional
* Maven
* REST API

---

# Ejecución del proyecto

1. Clonar el repositorio
2. Ejecutar la aplicación Spring Boot
3. Acceder a la API mediante herramientas como:

* Postman
* curl
* navegador

La API estará disponible en:

```
http://localhost:8080
```

---

# Autor

Proyecto desarrollado como parte de la práctica de **Programación de Aplicaciones Telemáticas (PAT)**.
