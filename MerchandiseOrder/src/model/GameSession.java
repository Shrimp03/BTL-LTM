package model;

import java.io.Serializable;
import java.util.Date;

public class GameSession implements Serializable {
    private Date timeStart;
    private Date timeFinish;
    private User user1;
    private User user2;
    private User winner; // Có thể là null nếu chưa có người thắng

    // Constructor đầy đủ
    public GameSession(Date timeStart, Date timeFinish, User user1, User user2, User winner) {
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

    // Getters
    public Date getTimeStart() {
        return timeStart;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public User getWinner() {
        return winner;
    }

    // Setters
    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeFinish(Date timeFinish) {
        this.timeFinish = timeFinish;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }
}