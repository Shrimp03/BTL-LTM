package model;

import java.io.Serializable;

public class UserStatusDto implements Serializable {
    private String userName;
    private String status;

    public UserStatusDto(String userName, String status) {
        this.userName = userName;
        this.status = status;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
