package model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 5L;
    private int id;
    private String username;
    private String password;
    private String email;
    private String points;
    private String avatar;
    private UserStatus status;

    public User(int id, String username, String password, String email, String points, String avatar, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.points = points;
        this.avatar = avatar;
        this.status = UserStatus.valueOf(status.toUpperCase());
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

    public User() {
        this.username = username;
        this.points = points;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", points='" + points + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status=" + status +
                '}';
    }

    public int getTotalPoints() {
        String[] points = this.points.split(" ");
        int result = 0;
        for(String point : points) {
            result += Integer.parseInt(point);
        }
        return result;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id; // So sánh theo ID hoặc thuộc tính đặc biệt xác định người dùng
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Sử dụng ID để tạo mã băm
    }
}
