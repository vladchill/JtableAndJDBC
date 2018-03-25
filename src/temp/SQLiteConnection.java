package temp;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLiteConnection {
    
    private static Connection con;
    
    public static Connection getConnection(){
        try {
            // динамическая регистрация драйвера SQLite
              Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();

              // создание подключение к базе данных по пути, указанному в урле
              String url = "jdbc:sqlite:c:/DB/CarShop_current/CarShop.db";
              
              if (con==null) con = DriverManager.getConnection(url);
              
              return con;
              
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}
