package model;

import java.io.Serializable;
import java.util.Date;

public class GameSession implements Serializable {
    private int id;
    private Date timeStart;
    private Date timeFinish;
    private int user1Id;
    private int user2Id;
    private Integer winnerId;

    // Constructor đầy đủ
    public GameSession(int id, Date timeStart, Date timeFinish, int user1Id, int user2Id, Integer winnerId) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.winnerId = winnerId;
    }

    public GameSession(Date timeStart, Date timeFinish, int user1Id, int user2Id) {
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeFinish(Date timeFinish) {
        this.timeFinish = timeFinish;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }
}