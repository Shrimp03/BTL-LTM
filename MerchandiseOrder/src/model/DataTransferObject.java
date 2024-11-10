package model;

import java.io.Serializable;
import java.util.List;

public class DataTransferObject<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type; // Kiểu dữ liệu để xác định loại thông điệp
    private T data;  // Dữ liệu dạng generic
    private Product[] products;

    public DataTransferObject(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public DataTransferObject(String type, T data, Product[] product) {
        this.type = type;
        this.data = data;
        this.products = product;
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

    public Product[] getProducts() {
        return products;
    }
}
