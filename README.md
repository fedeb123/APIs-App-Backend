# APIs-App-Backend
Desarrollo del Backend para la materia Aplicaciones Interactivas (APIs) en UADE

# Ejecutar el Proyecto
Para correr el proyecto ejecutar dentro de la carpeta `petshop` el comando: `mvn clean spring-boot:run`

# Variables de Entorno
Es necesario tener un archivo .env con las siguientes clave valor como el .env.example en la raiz del proyecto

```.env

MYSQL_VERSION=8.4
MYSQL_DATABASE=appdb
MYSQL_USER=app
MYSQL_PASSWORD=app123
MYSQL_ROOT_PASSWORD=root123
MYSQL_PORT=3306
```
# Docker Compose MySQL
Se adjunta un archivo tipo docker-compose para poder levantar la base de datos MySQL rapidamente con el siguiente comando
`docker compose up -d`

# Swagger
Se utiliza Swagger para la documentacion de Endpoints.

Ruta de Swagger: 
```Ruta de UI Swagger
/swagger-ui/index.html
```

Recordar solicitar el access token a las siguientes rutas

```POST Auth
api/v1/auth/register
api/v1/auth/authenticate
```

# Pedido - DetallePedido - Factura (Manejo y Control)
Se opto por un modelo en el cual primero se crea el pedido. Luego se adhieren los productos mediante DetallePedido para la adicion al pedido creado.

PedidoController recibe un POST del tipo: 

``` POST Pedido Body
{
    clienteId: INT //que refiere a algun cliente cargado en la BD
}

```

El controller devolvera el id del pedido creado, sino consultarlo mediante el mismo.

```POST DetallePedido Body
{
    "cantidad": INT, //cantidad a llevar del producto (se valida el stock y si se confirma, se resta del stock del producto)
    "productoId": INT, //que refiere a algun producto cargado en la BD
    "pedidoId": INT //que refiere a algun pedido cargado en la BD
}
```

Para crear una factura de un Pedido. Hacerlo mediante FacturaController que recibe un POST del tipo:

```POST Factura Body
{
    "pedidoId": INT, //que refiere a algun pedido cargado en la BD
    "metodoDePago": "TARJETA_CREDITO" //que refiere al metodo de pago segun MetodoDePagoEnum `(package com.uade.tpo.petshop.entity.enums)`
}
```