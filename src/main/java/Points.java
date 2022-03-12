import java.sql.*;

public class Points {
    private static final String url = "jdbc:mysql://localhost/store";
    private static final String user = "root";
    private static final String password = "2100";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private int points = 0;

    public String givePoint(long chatId) {
        String startQuery = "select points from users where chatId=" + chatId + ";";

        String levelUp = "";
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(startQuery);
            while (rs.next()) {
                points = rs.getInt(1);
            }
            points += 1;
            levelUp = changeUserLevel(chatId);
            String resultQuery = "update users set points=" + points + " where chatId=" + chatId + ";";
            stmt.executeUpdate(resultQuery);
            rs = stmt.executeQuery(startQuery);
            while (rs.next()) {
                points = rs.getInt(1);
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
        return "+1 очко в копилку. Всего у тебя очков - " + points + "!" + levelUp;
    }

    public boolean giveLevel(int points) {
        return points == 5 || points == 100 || points == 150 || points == 200 || points == 300;
    }

    public String changeUserLevel(long chatId) {
        int level = 0;
        String query = "select level from users where chatId=" + chatId + ";";
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                level = rs.getInt(1);
            }
            if(giveLevel(points)) {
                level+=1;
                String finishQuery = "update users set level=" + level + " where chatId=" + chatId + ";";
                stmt.executeUpdate(finishQuery);
                return "Поздравляю! Твой уровень увеличился! Текущий уровень: " + level;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getLevel(long chatId) {
        String query = "select points from users where chatId=" + chatId + ";";
        int level = 1;
        try {
            con = DriverManager.getConnection(url,user,password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                level = rs.getInt(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return level;
    }

}
