package main;

import controlador.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection conexion = ConexionBD.obtenerConexion()) {

            System.out.println("================================");
            System.out.println("CONEXIÓN EXITOSA A MYSQL");
            System.out.println("Base de datos: " + conexion.getCatalog());
            System.out.println("================================");

        } catch (SQLException e) {

            System.out.println("ERROR DE CONEXIÓN");
            e.printStackTrace();
        }
    }
}