package Server.Dal.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAOConnection {

    protected static Connection con;

    public DAOConnection() {
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
//                String jdbcUrl = "jdbc:mysql://localhost:3306/game";
                String jdbcUrl = "jdbc:mysql://localhost:4306/game";

                String username = "root";
                String password = "";
                System.out.println("Connected to database...");
                this.con = DriverManager.getConnection(jdbcUrl, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to connect to the database.", e);
            }
        }
    }
    public Connection getConnection() {
        return con;
    }

    // Đảm bảo rằng kết nối không bị đóng trước khi trả về
    public static boolean isConnectionOpen() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


