package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConectaDB {

    private static final String URL = "jdbc:mysql://localhost:3306/loja";
    private static final String USUARIO = "root";
    private static final String SENHA = "@1@senac2021";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (Exception e) {
            System.out.println("Erro na conexão");
            e.printStackTrace();
            return null;
        }
    }
}