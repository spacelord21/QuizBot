import com.mysql.jdbc.Driver;

import java.sql.*;

public class BaseContent {
    private static final String url = "jdbc:mysql://localhost/store";
    private static final String user = "root";
    private static final String password = "2100";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    BaseContent() {
        try {
            con = DriverManager.getConnection(url,user,password);
            stmt = con.createStatement();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String query = "select points from users where name='spacelord21';";
        try {
            con = DriverManager.getConnection(url,user,password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                int points = rs.getInt(1);
                System.out.println(points);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
