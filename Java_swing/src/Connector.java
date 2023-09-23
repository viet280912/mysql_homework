import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private static Connection connection;
    public static Connection getConnection() throws SQLException {
        try {
            String db = "jdbc:mysql://localhost/test";
            String user = "root";
            String pass = "";

            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(db,user,pass);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Cant find the database");
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return connection;
    }
}
