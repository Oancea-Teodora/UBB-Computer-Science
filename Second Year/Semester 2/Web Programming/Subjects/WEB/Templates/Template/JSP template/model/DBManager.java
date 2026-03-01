package template.model;

import template.domain.Game;
import template.domain.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBManager {
    private Statement stmt;

    private Connection conn;

    public DBManager() {
        connect();
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wddb", "root", "");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/wddb", "root", "");

            stmt = con.createStatement();
        } catch(Exception ex) {
            System.out.println("eroare la connect:"+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public User authenticate(String username, String password) {
        ResultSet rs;
        User u = null;
        System.out.println("AUTHENTIFICARE IN PROGRES");

        System.out.println(username+" "+password);
        try {
            rs = stmt.executeQuery("select * from users where userName='"+username+"' and userPass='"+password+"'");
            if (rs.next()) {
                u = new User(rs.getInt("userID"), rs.getString("userName"), rs.getString("userPass"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public ArrayList<Game> getGames() {
        ArrayList<Game> games = new ArrayList<>();
        ResultSet rs;
        System.out.println("SE IAU JOCURILE");

        try {
            rs = stmt.executeQuery("select * from games");
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("Id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("release_date"),
                        rs.getString("platform")

                        ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(games.toString());
        return games;
    }

    public boolean createGame(String title, String genre, LocalDate releaseDate, String platform) {
        int result = 0;
        try {
            String query = "INSERT INTO games (title, genre, release_date,platform) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setDate(3, Date.valueOf(releaseDate));
            pstmt.setString(4,platform);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result > 0;
    }

}
