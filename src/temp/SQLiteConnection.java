package temp;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLiteConnection {

    private static Connection con;

    public static Connection getConnection() {
        try {
            // динамическая регистрация драйвера SQLite
            Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();

            // создание подключение к базе данных по пути, указанному в урле
            String url = "jdbc:sqlite:"+ new File("db/CarShop.db").getAbsolutePath();

            if (con == null || con.isValid(500)) con = DriverManager.getConnection(url);
            return con;

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
