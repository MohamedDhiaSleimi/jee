package metier;
import java.sql.Connection;
import java.sql.DriverManager;
public class SingletonConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_j2ee", "user", "pass");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
