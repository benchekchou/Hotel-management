package hotelmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String URL = "jdbc:sqlite:hotelmanagement.db";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        initializeDatabase(connection);
        return connection;
    }

    private static void initializeDatabase(Connection connection) {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";

        String createClientsTable = "CREATE TABLE IF NOT EXISTS clients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "contact_info TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "phone_number TEXT NOT NULL)";

        String createRoomsTable = "CREATE TABLE IF NOT EXISTS rooms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "available BOOLEAN NOT NULL)";

        String createReservationsTable = "CREATE TABLE IF NOT EXISTS reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "room_id INTEGER NOT NULL, " +
                "client_id INTEGER NOT NULL, " +
                "start_date date NOT NULL, " +
                "end_date date NOT NULL, " +
                "confirmed BOOLEAN NOT NULL, " +
                "statut BOOLEAN NOT NULL, " +

                "FOREIGN KEY (room_id) REFERENCES rooms(id), " +
                "FOREIGN KEY (client_id) REFERENCES clients(id))";

        String createPaymentsTable = "CREATE TABLE IF NOT EXISTS payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "client_id INTEGER NOT NULL, " +
                "amount REAL NOT NULL, " +
                "date TEXT NOT NULL, " +
                "FOREIGN KEY (client_id) REFERENCES clients(id))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createUsersTable);
            statement.execute(createClientsTable);
            statement.execute(createRoomsTable);
            statement.execute(createReservationsTable);
            statement.execute(createPaymentsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}