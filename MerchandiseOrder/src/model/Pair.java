package model;

import java.io.Serializable;
import java.util.Objects;

public class Pair<T, U> implements Serializable {
    private static final long serialVersionUID = 3L;
    private T first; // Giá trị đầu tiên
    private U second; // Giá trị thứ hai

    // Constructor
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    // Phương thức để lấy giá trị đầu tiên
    public T getFirst() {
        return first;
    }

    // Phương thức để lấy giá trị thứ hai
    public U getSecond() {
        return second;
    }

    // Phương thức để thiết lập giá trị đầu tiên
    public void setFirst(T first) {
        this.first = first;
    }

    // Phương thức để thiết lập giá trị thứ hai
    public void setSecond(U second) {
        this.second = second;
    }

    // Phương thức để in thông tin cặp
    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    // Phương thức equals để so sánh hai Pair
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return (Objects.equals(first, pair.first)) &&
                (Objects.equals(second, pair.second));
    }

    // Phương thức hashCode để sử dụng trong các cấu trúc dữ liệu như HashMap
    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}