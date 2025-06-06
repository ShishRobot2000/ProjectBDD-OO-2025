package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static final String URL = "jdbc:postgresql://localhost:5432/ProgettoToDo";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection;

    private ConnessioneDatabase() {
        // Costruttore privato per evitare istanziazioni
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
