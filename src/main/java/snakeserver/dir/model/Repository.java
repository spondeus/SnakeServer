package snakeserver.dir.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Repository {


    Float result;
    static String DB_URL;
    static String USER;
    static String PASS;
    static final String TOP10 = "" +
            "    SELECT p.name, s.score\n" +
            "    FROM snake_score s\n" +
            "             INNER JOIN snake_player p ON s.player_id = p.id\n" +
            "             INNER JOIN snake_game g ON s.game_id = g.id\n" +
            "    ORDER BY s.score DESC\n" +
            "    LIMIT 10;";
    static final String TOP10InTimeLimit = "" + "SELECT p.name, s.score\n" +
            "FROM snake_score s\n" +
            "         INNER JOIN snake_player p ON s.player_id = p.id\n" +
            "         INNER JOIN snake_game g ON s.game_id = g.id\n" +
            "WHERE g.type = 'time_limit'\n" +
            "ORDER BY s.score DESC LIMIT 10;\n";
    static final String TOP10DEATHMATCH = "" + "SELECT p.name, s.score\n" +
            "FROM snake_score s\n" +
            "         INNER JOIN snake_player p ON s.player_id = p.id\n" +
            "         INNER JOIN snake_game g ON s.game_id = g.id\n" +
            "WHERE g.type = 'deathmatch'\n" +
            "ORDER BY s.score DESC LIMIT 10;";

    static final String TOP10POINTRATE = "" + "SELECT p.name, s.score / TIME_TO_SEC(TIMEDIFF(g.end_time, g.begin_time)) as point_ratio\n" +
            "FROM snake_score s\n" +
            "         INNER JOIN snake_player p ON s.player_id = p.id\n" +
            "         INNER JOIN snake_game g ON s.game_id = g.id\n" +
            "ORDER BY point_ratio DESC LIMIT 10;";
    static final String TOP10POINT_RATE_TIME_LIMIT = "" + "SELECT p.name ,s.score / TIME_TO_SEC(TIMEDIFF(g.end_time, g.begin_time)) as point\n" +
            "FROM snake_score s\n" +
            "         JOIN snake_game g ON s.game_id = g.id\n" +
            "         JOIN snake_player p ON s.player_id = p.id\n" +
            "WHERE g.type = 'true'\n" +
            "ORDER BY point DESC LIMIT 10;";
    static final String TOP10PONIT_RATE_DEATH_MATCH = "" + "SELECT p.name,s.score / TIME_TO_SEC(TIMEDIFF(g.end_time, g.begin_time)) as point\n" +
            "FROM snake_score s\n" +
            "         JOIN snake_game g ON s.game_id = g.id\n" +
            "         JOIN snake_player p ON s.player_id = p.id\n" +
            "WHERE g.type = 'false'\n" +
            "ORDER BY point DESC LIMIT 10;";

    @Autowired
    public Repository(Environment env) {
        DB_URL = env.getProperty("spring.datasource.url");
        USER = env.getProperty("spring.datasource.username");
        PASS = env.getProperty("spring.datasource.password");
    }

    public List<ResultClass> getTop10PointRateDeathMatch() {
        // Open a connection
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            List<ResultClass> resultClasses = new ArrayList<>();

            if (con != null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(TOP10PONIT_RATE_DEATH_MATCH);
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    resultClasses.add(new ResultClass(rs.getString("name"), Float.valueOf(rs.getString("score"))));
                }
            }
            return resultClasses;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ResultClass> getTop10PointRateTimeLimit() {
        // Open a connection
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            List<ResultClass> resultClasses = new ArrayList<>();

            if (con != null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(TOP10POINT_RATE_TIME_LIMIT);
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    resultClasses.add(new ResultClass(rs.getString("name"), Float.valueOf(rs.getString("score"))));
                }
            }
            return resultClasses;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ResultClass> getTop10PointRate() {
        // Open a connection
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            List<ResultClass> resultClasses = new ArrayList<>();

            if (con != null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(TOP10POINTRATE);
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    resultClasses.add(new ResultClass(rs.getString("name"), Float.valueOf(rs.getString("score"))));
                }
            }
            return resultClasses;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ResultClass> getTop10DeathMatch() {
        // Open a connection
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            List<ResultClass> resultClasses = new ArrayList<>();

            if (con != null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(TOP10DEATHMATCH);
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    resultClasses.add(new ResultClass(rs.getString("name"), Float.valueOf(rs.getString("score"))));
                }
            }
            return resultClasses;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ResultClass> getTop10TimeLimit() {
        // Open a connection
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            List<ResultClass> resultClasses = new ArrayList<>();

            if (con != null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(TOP10InTimeLimit);
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    resultClasses.add(new ResultClass(rs.getString("name"), Float.valueOf(rs.getString("score"))));
                }
            }
            return resultClasses;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ResultClass> getTop10BasedOnScore() {
        // Open a connection
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            List<ResultClass> resultClasses = new ArrayList<>();

            if (con != null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(TOP10);
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    resultClasses.add(new ResultClass(rs.getString("name"), Float.valueOf(rs.getString("score"))));
                }
            }
            return resultClasses;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
