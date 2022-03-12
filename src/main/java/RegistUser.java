import java.sql.*;

public class RegistUser {
    private static final String url = "jdbc:mysql://localhost/store";
    private static final String user = "root";
    private static final String password = "2100";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public String registerUser(String userName, long chatId) {
        String query = "insert into users(chatId,name,level,points) values("
                + chatId + ",'" + userName + "'," + "1,0);";
        if(checkChatId(chatId)) {
            if(checkUser(userName)) {
                try {
                    con = DriverManager.getConnection(url, user, password);
                    stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    return "Добро пожаловать, " + userName + "!";

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return "Error";
            }
            else {
                return "Данное имя пользователя уже зарегестрировано. Пожалуйста, выбери другое.";
            }
        }
        else {
            query = "select name,level,points from users where chatId=" + chatId + ";";
            try{
                rs = stmt.executeQuery(query);
                String name = null;
                int level = 0;
                int points = 0;
                while(rs.next()) {
                    name = rs.getString(1);
                    level = rs.getInt(2);
                    points = rs.getInt(3);
                }
                return "Ты уже существуешь в моей базе данных. Твое имя: " + name + ", твой уровень: " + level+
                        ", у тебя " + points + " очков!";
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return "";
    }

    private boolean checkUser(String userName) {
        int count = 0;
        String query = "select name from users;";
        String queryCount = "select count(name) from users;";
        try {
            con = DriverManager.getConnection(url,user,password);
            stmt = con.createStatement();

            ResultSet countRes  = stmt.executeQuery(queryCount);
            while(countRes.next()) {
                count = countRes.getInt(1);
            }
            try{
                countRes.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                 {
                    String name = rs.getString(1);
                    if(userName.equals(name)) {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean checkChatId(long chatId) {
        String query = "select chatId from users where chatId=" + chatId + ";";
        try {
            con = DriverManager.getConnection(url,user,password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                int checkId = rs.getInt(1);
                if(checkId==chatId) {
                    return false;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
