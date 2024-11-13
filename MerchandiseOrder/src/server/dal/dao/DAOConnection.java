package server.dal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAOConnection {

    protected static Connection con;

    public DAOConnection(){
        if(con==null){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String jdbcUrl = "jdbc:mysql://localhost:3306/game";
                String username = "root";
                String password = "1234";
                System.out.println("Connected to database...");
                this.con =  DriverManager.getConnection(jdbcUrl, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to connect to the database.", e);
            }
        }
    }
}