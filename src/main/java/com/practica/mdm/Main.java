package com.practica.mdm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Statement;

public class Main {
public static void main(String[] args) {
    System.out.println("Iniciando la conexión...");
    try {
    Class.forName("org.postgresql.Driver"); 
    String url = "jdbc:postgresql://localhost:5432/mdm";
    String user = "postgres";
    String password = "admin";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        System.out.println("✅ Conexión establecida correctamente.");
        List<clienteOrigen> todosLosClientes = new ArrayList<>();

            String sqlMkt = "SELECT nombre, correo, tel FROM marketing ";
            String sqlSoporte = "SELECT cliente, mail, telefono_contacto FROM soporte ";
            String sqlVentas = "SELECT nombre_cliente, email, telefono FROM ventas";
            try (PreparedStatement stmt = conn.prepareStatement(sqlMkt);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    System.out.println("Leyendo datos de MARKETING...");
                    String nombreMkt = rs.getString("nombre");
                    String correoMkt = rs.getString("correo");
                    String telMkt = rs.getString("tel");                    
                    clienteOrigen clienteMkt = new clienteOrigen(nombreMkt, correoMkt, telMkt);

                            todosLosClientes.add(clienteMkt);
                        }
                    }
            try (PreparedStatement stmt = conn.prepareStatement(sqlSoporte);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    System.out.println("Leyendo datos de SOPORTE...");
                    String nombreSoporte = rs.getString("cliente");
                    String correoSoporte = rs.getString("mail");
                    String telSoporte = rs.getString("telefono_contacto");                    
                    clienteOrigen clienteSoporte = new clienteOrigen(nombreSoporte, correoSoporte, telSoporte);

                            todosLosClientes.add(clienteSoporte);
                        }
                    }
            try (PreparedStatement stmt = conn.prepareStatement(sqlVentas);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    System.out.println("Leyendo datos de VENTAS...");
                    String nombreVentas = rs.getString("nombre_cliente");
                    String correoVentas = rs.getString("email");
                    String telVentas = rs.getString("telefono");                    
                    clienteOrigen clienteVentas = new clienteOrigen(nombreVentas, correoVentas, telVentas);

                            todosLosClientes.add(clienteVentas);
                        }
                    }

            System.out.println("Total de registros leídos: " + todosLosClientes.size());
            Map<String, List<clienteOrigen>> clientesAgrupados = new HashMap<>();
            for (clienteOrigen cliente : todosLosClientes) {
                String email = cliente.getEmail();
                String emailLimpio = limpiaremail(email);

                if (emailLimpio == null) { 
                    continue; 
                }
                if (clientesAgrupados.containsKey(emailLimpio)){
                    List<clienteOrigen> listaExistente = clientesAgrupados.get(emailLimpio);
                    listaExistente.add(cliente);
                } else {
                     List<clienteOrigen> nuevaLista = new ArrayList<>();
                     nuevaLista.add(cliente);
                     clientesAgrupados.put(emailLimpio, nuevaLista);
                }
            }

            List<clienteMaestro> goldenRecords = new ArrayList<>();
            for (List<clienteOrigen> grupoDeDuplicados : clientesAgrupados.values()) {
                String emailMaestro = limpiaremail(grupoDeDuplicados.get(0).getEmail());
                String nombreMaestro = "";
                String telefonoMaestro = "";
                for (clienteOrigen cliente : grupoDeDuplicados) {
                    String nombreclientelimpio = limpiarnombre(cliente.getNombre());
                    if (nombreMaestro.length() < nombreclientelimpio.length()){
                        nombreMaestro = nombreclientelimpio;
                    }
                    String telefonoclientelimpio = limpiartelefono(cliente.getTelefono());
                    if (telefonoMaestro == ""){
                        telefonoMaestro = telefonoclientelimpio;
                    }
                }
                clienteMaestro goldenRecord = new clienteMaestro(nombreMaestro, emailMaestro, telefonoMaestro);
                goldenRecords.add(goldenRecord);
            }
            System.out.println("FASE TRANSFORM COMPLETADA. Total de Golden Records creados: " + goldenRecords.size());
            System.out.println("FASE LOAD: Iniciando carga en la tabla cliente_maestro...");

            System.out.println(" -> Vaciando la tabla cliente_maestro...");
            try (Statement stmt = conn.createStatement()) { // Solo declaramos el recurso que se debe cerrar
                
                // La ejecución de la sentencia va DENTRO del bloque try
                stmt.executeUpdate("TRUNCATE TABLE cliente_maestro RESTART IDENTITY;");
                
                System.out.println(" -> Tabla vaciada con éxito.");
            }

            String sqlInsert = "INSERT INTO cliente_maestro (nombre_completo, email_confirmado, telefono_contacto, fecha_creacion, ultima_actualizacion) VALUES (?, ?, ?, NOW(), NOW())";

            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                
                for (clienteMaestro record : goldenRecords) {
                    pstmt.setString(1, record.getNombreCompleto());
                    pstmt.setString(2, record.getEmailPrincipal());
                    pstmt.setString(3, record.getTelefonoPrincipal());
                    
                    pstmt.addBatch();
                }

                int[] resultados = pstmt.executeBatch();
                
                System.out.println(" -> Se han insertado " + resultados.length + " registros en la base de datos.");
            }

            System.out.println("\n==============================================");
            System.out.println("✅ ¡PROCESO MDM COMPLETADO CON ÉXITO!");
            System.out.println("==============================================");

        } catch (SQLException e) {
            System.err.println("❌ Error en la conexión o consulta:");
            e.printStackTrace();
        }
    } catch (Exception e) {
            System.err.println("Error");
            e.printStackTrace();
        }
}

private static String limpiaremail (String emailsucio) {
    if (emailsucio == null || emailsucio.trim().isEmpty()) {
        return null;
    } else {
    String emaillimpio = emailsucio.trim();
    emaillimpio = emaillimpio.toLowerCase();
    return emaillimpio;
    }
}
private static String limpiarnombre (String nombresucio) {
    if (nombresucio == null || nombresucio.trim().isEmpty()) {
        return null;
    } else {
    String nombrelimpio = nombresucio.trim();
    nombrelimpio = nombrelimpio.toLowerCase();
    return nombrelimpio;
    }
}
private static String limpiartelefono (String telefonosucio) {
    if (telefonosucio == null || telefonosucio.trim().isEmpty()) {
        return null;
    } else {
    String telefonolimpio = telefonosucio.trim();
    return telefonolimpio;
    }
}

}