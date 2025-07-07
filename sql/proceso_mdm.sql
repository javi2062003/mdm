
TRUNCATE TABLE cliente_maestro RESTART IDENTITY;

INSERT INTO cliente_maestro (
    nombre_completo,
    email_confirmado,
    telefono_contacto,
    fecha_creacion,
    ultima_actualizacion
)
WITH
clientes_unificados AS (
    SELECT 'Ventas' AS fuente, nombre_cliente AS nombre, email, telefono FROM ventas
    UNION ALL
    SELECT 'Marketing' AS fuente, nombre, correo AS email, tel AS telefono FROM marketing
    UNION ALL
    SELECT 'Soporte' AS fuente, cliente AS nombre, mail AS email, telefono_contacto AS telefono FROM soporte
),
clientes_limpios AS (
    SELECT
        fuente,
        TRIM(UPPER(nombre)) AS nombre,
        TRIM(LOWER(email)) AS email,
        REGEXP_REPLACE(REGEXP_REPLACE(telefono, '[^0-9]', '', 'g'), '^34', '') AS telefono
    FROM clientes_unificados
    WHERE email IS NOT NULL AND email != '' AND email NOT LIKE '%test%'
),
golden_records AS (
    SELECT
        email,
        MAX(nombre) AS nombre_maestro,
        MAX(telefono) FILTER (WHERE telefono IS NOT NULL AND telefono != '') AS telefono_maestro
    FROM clientes_limpios
    GROUP BY email
)
SELECT
    gr.nombre_maestro AS nombre_completo,
    gr.email AS email_confirmado,
    gr.telefono_maestro AS telefono_contacto,
    NOW() AS fecha_creacion,
    NOW() AS ultima_actualizacion
FROM
    golden_records gr;

SELECT * FROM cliente_maestro ORDER BY id_cliente;