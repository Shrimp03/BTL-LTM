package model;

import java.io.Serializable;

public class DataTransferObject<T> implements Serializable {
    private String type; // Kiểu dữ liệu để xác định loại thông điệp
    private T data;      // Dữ liệu dạng generic

    public DataTransferObject(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public DataTransferObject(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
