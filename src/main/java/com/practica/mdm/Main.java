package com.practica.mdm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
public static void main(String[] args) {
    System.out.println("Iniciando la conexión...");
    try {
    Class.forName("org.postgresql.Driver"); // PostgreSQL
    String url = "jdbc:postgresql://localhost:5432/mdm";
    String user = "postgres";
    String password = "admin";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        System.out.println("✅ Conexión establecida correctamente.");

            String sqlMkt = "SELECT nombre, correo, tel FROM marketing ";
            String sqlSoporte = "SELECT cliente, mail, telefono_contacto FROM soporte ";
            String sqlVentas = "SELECT nombre_cliente, email, telefono FROM ventas";
            try (PreparedStatement stmt = conn.prepareStatement(sqlMkt);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String nombreMkt = rs.getString("nombre");
                    String correoMkt = rs.getString("correo");
                    String telMkt = rs.getString("tel");


                    String nombreTransformado = nombreMkt.toUpperCase();
                    String correoTransformado = correoMkt.toUpperCase();
                    String telTransformado = telMkt.toUpperCase();


                    String sqlInsert = "INSERT INTO cliente_maestro (nombreMkt, correoMkt, telMkt) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                        insertStmt.setString(1, nombreTransformado);
                        insertStmt.setString(2, correoTransformado);
                        insertStmt.setString(2, telTransformado);
                        insertStmt.executeUpdate();
                        System.out.println("✔ Registro insertado: " + nombreTransformado + " - " + correoTransformado + " + " + telTransformado);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en la conexión o consulta:");
            e.printStackTrace();
        }
    } catch (Exception e) {
            System.err.println("Error");
            e.printStackTrace();
        }
}
}