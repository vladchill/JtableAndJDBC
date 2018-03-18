import java.sql.*;

public class Main {

    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            Driver d = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
//            DriverManager.registerDriver(d);

            String url = "jdbc:sqlite:C:/Users/Tania/IdeaProjects/JtableAndJDBC/db/CarShop.db";
            con = DriverManager.getConnection(url);

            String sql = "SELECT * FROM spr_Model";
            stmt = con.createStatement();
            res = stmt.executeQuery(sql);

            while (res.next()){
                System.out.println(res.getString("name_en") + " - " + res.getObject("name_ru"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
