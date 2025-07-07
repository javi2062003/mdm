CREATE TABLE VENTAS (
    id_venta SERIAL INT primary key,
    nombre_cliente VARCHAR (255),
    direccion VARCHAR (255),
    email VARCHAR(255),
    telefono VARCHAR(255)
);

CREATE TABLE SOPORTE (
    id_ticket VARCHAR(255) primary key,
    cliente VARCHAR (255),
    mail VARCHAR (255),
    telefono_contacto VARCHAR(255),
    producto VARCHAR(255)
);

CREATE TABLE MARKETING (
    id_lead VARCHAR(255) primary key,
    nombre VARCHAR (255),
    correo VARCHAR (255),
    tel VARCHAR(255),
    empresa VARCHAR(255)
);

CREATE TABLE cliente_maestro (
    id_cliente SERIAL INT PRIMARY KEY,
    nombre_cliente_completo VARCHAR(255),
    email_confirmado VARCHAR(255),
    telefono_contacto VARCHAR(255),
    fecha_creacion TIMESTAMP,
    ultima_actualizacion TIMESTAMP
)



