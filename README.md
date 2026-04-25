# Práctica 5 – Web de Boda + API REST

## Descripción

En esta práctica se ha desarrollado una aplicación completa que conecta un **frontend en HTML, CSS y JavaScript** con un **backend en Spring Boot**, utilizando una API REST.

La web simula la página de una boda donde los usuarios pueden ver información del evento, una línea temporal con fotos y una lista de regalos con carrito de compra.

El objetivo principal es aprender a consumir endpoints REST desde el frontend mediante `fetch` y manipular el DOM dinámicamente.

---

## Tecnologías utilizadas

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)
- Fetch API

### Backend
- Java
- Spring Boot
- JPA / Hibernate
- Base de datos H2

---

## Funcionalidades

### Página principal
- Información de la boda
- Cuenta atrás en tiempo real
- Línea temporal con imágenes (slider)

### Lista de regalos
- Productos con imagen, precio y descripción
- Botón para añadir al carrito

### Carrito
- Visualización de productos desde el backend
- Cálculo automático del total
- Eliminación de productos

### Checkout
- Formulario con nombre y correo
- Creación de pedido en backend
- Vaciado del carrito

### Aportación libre
- Permite añadir dinero para la luna de miel

### Vista Admin
- Endpoint protegido para ver pedidos
- Uso de cabecera:
  X-Admin-Key: clave-admin-2027


---

## API REST

### Carrito

POST /api/carritos  
GET /api/carritos/{idCarrito}  
POST /api/carritos/{idCarrito}/lineas  
GET /api/carritos/{idCarrito}/lineas  
DELETE /api/carritos/{idCarrito}/lineas/{idArticulo}  
DELETE /api/carritos/{idCarrito}

### Pedidos

POST /api/pedidos/{idCarrito}  
GET /api/admin/pedidos

---

## Estructura

Practica-5/
├── Frontend/
│   ├── index.html
│   ├── gifts.html
│   ├── cart.html
│   ├── checkout.html
│   ├── admin.html
│   ├── script.js
│   ├── styles.css
│   └── assets/
│
├── src/
│   ├── controlador/
│   ├── servicio/
│   ├── repositorio/
│   └── entity/
│
├── pom.xml
└── application.properties

---

## Cómo ejecutar

### Backend
mvn spring-boot:run


Acceso:
http://localhost:8080

H2:
http://localhost:8080/h2-console

---

### Frontend
Abrir con Live Server:
http://127.0.0.1:3000/index.html

---

## Notas

- El carrito se gestiona completamente desde el backend
- El frontend usa `fetch` para comunicarse con la API
- GitHub Pages solo sirve el frontend

---

## Autor

Ana Recarte Pacheco