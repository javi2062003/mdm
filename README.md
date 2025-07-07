
## 🛠️ Tecnologías y Herramientas

-   **Base de Datos**: PostgreSQL
-   **Lenguaje de Scripting**: SQL (específicamente, dialecto de PostgreSQL)
-   **Entorno de Desarrollo**: Visual Studio Code con la extensión SQLTools.

## 🚀 Cómo Empezar

Para replicar este proyecto y ejecutar el proceso, sigue estos pasos:

### Prerrequisitos

1.  Tener **PostgreSQL** instalado y un servidor en ejecución.
2.  Haber creado una base de datos vacía (ej. `practica_mdm`).
3.  Tener **Visual Studio Code** con la extensión **SQLTools** y el driver de **PostgreSQL** correspondiente.

### Pasos de Ejecución

1.  **Clonar el Repositorio (Opcional)**
    ```bash
    git clone [URL-de-tu-repositorio-github]
    cd PRACTICA-MDM
    ```

2.  **Configurar la Conexión en VS Code**
    -   Abre el proyecto en VS Code.
    -   Usa la extensión SQLTools para crear una nueva conexión a tu base de datos `practica_mdm`.

3.  **Ejecutar los Scripts en Orden**

    Abre cada archivo `.sql` en VS Code y ejecútalo en tu conexión activa en el siguiente orden:

    a. **`sql/crear_tablas.sql`**: Este script creará las tres tablas de origen (`ventas`, `marketing`, `soporte`) y la tabla de destino final (`cliente_maestro`).

    b. **`sql/cargar_datos.sql`**: **Importante:** Antes de ejecutar, debes modificar las rutas dentro de este archivo para que apunten a la ubicación **absoluta** de los archivos `.csv` en tu máquina local. Este script poblará las tablas de origen con los datos de ejemplo.

    c. **`sql/proceso_mdm.sql`**: Este es el corazón del proyecto. El script ejecutará la lógica de consolidación y cargará los "Golden Records" resultantes en la tabla `cliente_maestro`.

4.  **Verificar el Resultado**
    -   Al final de la ejecución de `proceso_mdm.sql`, se incluye una consulta `SELECT * FROM cliente_maestro;` para que puedas ver el resultado final: una tabla de clientes limpia, unificada y sin duplicados.

## 🧠 Lógica del Proceso MDM

El script `proceso_mdm.sql` sigue una estrategia de consolidación basada en las siguientes reglas de negocio:

1.  **Unificación**: Se utiliza `UNION ALL` para combinar los registros de las tres tablas de origen en una vista temporal.
2.  **Clave de Negocio**: El **email** se utiliza como el identificador principal para detectar registros pertenecientes al mismo cliente.
3.  **Limpieza y Estandarización**:
    -   Los **nombres** se convierten a mayúsculas y se eliminan espacios extra.
    -   Los **emails** se convierten a minúsculas.
    -   Los **teléfonos** se normalizan a un formato numérico único, eliminando caracteres especiales y prefijos nacionales (`34`).
4.  **Deduplicación y Fusión (Golden Record)**:
    -   Se agrupan todos los registros por `email`.
    -   Para cada grupo, se selecciona la "mejor" información disponible:
        -   **Nombre**: Se escoge el nombre más largo, asumiendo que es el más completo.
        -   **Teléfono**: Se escoge el primer número de teléfono válido encontrado dentro del grupo.

## Próximos Pasos
- [ ] Replicar la lógica del script `proceso_mdm.sql` en una aplicación de consola Java usando JDBC.
- [ ] Desarrollar una API REST con Spring Boot para exponer los datos de la tabla `cliente_maestro`.