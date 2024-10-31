package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    // Mã hóa mật khẩu sử dụng SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Xác minh mật khẩu (so sánh với mật khẩu đã mã hóa)
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }

    // Kiểm tra mật khẩu có tuân thủ các quy tắc không
    public static boolean isValidPassword(String password) {
        if (password.length() < 8) return false; // Phải có ít nhất 8 ký tự
        if (!password.matches(".*[A-Z].*")) return false; // Phải có ít nhất một ký tự viết hoa
        if (!password.matches(".*\\d.*")) return false; // Phải có ít nhất một chữ số
        return true;
    }
}
