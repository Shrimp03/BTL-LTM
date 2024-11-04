package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class GameSession implements Serializable {
    private int id;
    private Date timeStart;
    private Date timeFinish;
    private User user1;
    private User user2;
    private User winner; // Có thể là null nếu chưa có người thắng

    // Constructor đầy đủ
    public GameSession(int id, Date timeStart, Date timeFinish, User user1, User user2, User winner) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.user1 = user1;
        this.user2 = user2;
        this.winner = winner;
    }

    // Constructor khi bắt đầu trận đấu (chưa có người thắng)
    public GameSession(Date timeStart, Date timeFinish, User user1, User user2) {
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.user1 = user1;
        this.user2 = user2;
        this.winner = null; // Chưa có người thắng
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(Date timeFinish) {
        this.timeFinish = timeFinish;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }
// Getters and Setters (giữ nguyên)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSession that = (GameSession) o;
        return id == that.id; // So sánh dựa trên id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Tạo mã băm dựa trên id
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "id=" + id +
                ", timeStart=" + timeStart +
                ", timeFinish=" + timeFinish +
                ", user1=" + user1 +
                ", user2=" + user2 +
                ", winner=" + winner +
                '}';
    }
}
