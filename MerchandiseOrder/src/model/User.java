package model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String email;
    private String points;
    private String avatar;

    public User(int id, String username, String password, String email, String points, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        if(points == null){
            this.points = "0";
        }
        else this.points = points;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPoints() {
        return points;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighScore() {
        String[] points = this.points.split(" ");
        int result = 0;
        for(String point : points) {
            if(result < Integer.parseInt(point))
            {
                result = Integer.parseInt(point);
            }
        }
        return result;
    }

    public int getTotalPoints() {
        String[] points = this.points.split(" ");
        int result = 0;
        for(String point : points) {
            result += Integer.parseInt(point);
        }
        return result;
    }



}
