# ERP-Lite · Auditoría de Discrepancias para Lavandería Industrial

Aplicación web para **registrar, organizar y resolver las discrepancias de prendas** (faltantes entre lo despachado y lo recibido) de los hoteles y clientes de una lavandería industrial: cada discrepancia se **devuelve (reposición)** o se **paga (nota de crédito)**.

## ¿Por qué existe este proyecto?

En mi antiguo trabajo, las devoluciones y discrepancias de prendas se coordinaban a través de **un grupo de WhatsApp**. Con el volumen de hoteles y órdenes, el grupo se saturaba: los reportes se perdían entre cientos de mensajes, nadie sabía con certeza qué estaba pendiente, qué ya se había repuesto y qué se había cobrado.

ERP-Lite reemplaza ese chat por un tablero centralizado donde cada faltante queda registrado, con su cliente, su prenda, sus cantidades y su estado.

## Funcionalidades

- Registro de discrepancias por orden de servicio y prenda (cantidad despachada vs. recibida).
- Cálculo automático del faltante.
- Resolución en un clic: **Parchar** (reposición encontrada) o **Cobrar** (nota de crédito).
- Filtros por estado: Todos, Pendientes, Reposiciones, Nota de Crédito.
- **Ordenamiento tipo explorador de archivos** ("Ordenar por"), con dirección adaptada al dato:
  - Cantidades y peso: menor a mayor / mayor a menor
  - Nombres (hotel, prenda): A → Z / Z → A
  - Fecha: más antiguo / más reciente primero
- API REST para clientes, prendas y órdenes (`/api/clientes`, `/api/prendas`, `/api/ordenes`).
- Datos de ejemplo cargados automáticamente al primer arranque.

## Tecnologías

- Java 17
- Spring Boot (Web MVC, Data JPA, Thymeleaf)
- PostgreSQL
- Lombok
- Maven

## Requisitos

- JDK 17 o superior
- PostgreSQL instalado y en ejecución

## Cómo ejecutarlo

1. Clona el repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/erplite.git
   cd erplite
   ```
2. Crea la base de datos en PostgreSQL:
   ```sql
   CREATE DATABASE erplite_db;
   ```
3. Crea tu configuración local copiando el ejemplo:
   ```bash
   cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
   ```
   y edita en ese archivo la URL, usuario y contraseña de tu PostgreSQL. **Este archivo no se sube a Git.**
4. Ejecuta:
   ```bash
   ./mvnw spring-boot:run        # Linux / macOS
   mvnw.cmd spring-boot:run      # Windows
   ```
5. Se abrirá el navegador en <http://localhost:8080/ordenes>.

Las tablas se crean solas (`ddl-auto=update`) y se insertan datos de ejemplo la primera vez.

## Estructura del proyecto

```
src/main/java/com/lavanderia/erplite/
├── config/        Datos de ejemplo y apertura automática del navegador
├── controller/    Vista web (Thymeleaf) y API REST
├── model/         Entidades: Cliente, OrdenServicio, Prenda, Discrepancia
├── repository/    Acceso a datos (Spring Data JPA)
└── service/       Lógica de negocio
src/main/resources/
├── templates/     ordenes-listado.html (dashboard)
└── static/css/    Estilos
```

## Modelo de datos

`Cliente` 1—N `OrdenServicio` 1—N `Discrepancia` N—1 `Prenda`

Estados de una discrepancia: `PENDIENTE` → `REPOSICION_ENCONTRADA` o `NOTA_CREDITO`.

## Próximos pasos

- [ ] Login y roles (Spring Security)
- [ ] Pantallas para crear clientes, prendas y órdenes
- [ ] Ordenamiento en las demás secciones
- [ ] Reportes y exportación a Excel/PDF
- [ ] Notificaciones al hotel por correo o WhatsApp
- [ ] Pruebas automatizadas

## Autor

Hecho por **TU NOMBRE** — [GitHub](https://github.com/TU_USUARIO)
