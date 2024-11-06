package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton; // Nút Register
    private Image backgroundImage;

    public LoginScreen() {
        // Tải ảnh nền
        loadBackgroundImage();

        setSize(400, 300);
        setLayout(null);

        // Màu xám nhạt cho các trường văn bản
        Color customColor = new Color(230, 227, 227);

        // Trường nhập tên người dùng
        usernameField = new JTextField(15);
        usernameField.setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        usernameField.setForeground(Color.BLACK);
        usernameField.setBorder(null); // Loại bỏ viền
        usernameField.setOpaque(false); // Nền trong suốt
        usernameField.setFont(new Font("Arial", Font.BOLD, 16));

        // Trường nhập mật khẩu
        passwordField = new JPasswordField(15);
        passwordField.setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(null); // Loại bỏ viền
        passwordField.setOpaque(false); // Nền trong suốt

        // Nút "Đăng nhập"
        loginButton = new JButton("Đăng nhập");
        loginButton.setContentAreaFilled(false); // Nền trong suốt
        loginButton.setFocusPainted(false); // Loại bỏ viền khi nhấn
        loginButton.setOpaque(false); // Nền trong suốt
        loginButton.setBorder(null); // Loại bỏ viền
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 26));

        // Nút "Đăng ký"
        registerButton = new JButton("Đăng ký");
        registerButton.setContentAreaFilled(false); // Nền trong suốt
        registerButton.setFocusPainted(false); // Loại bỏ viền khi nhấn
        registerButton.setOpaque(false); // Nền trong suốt
        registerButton.setBorder(null); // Loại bỏ viền
        registerButton.setForeground(Color.WHITE); // Văn bản màu trắng
        registerButton.setFont(new Font("Arial", Font.BOLD, 26));

        // Đặt vị trí cho các thành phần
        usernameField.setBounds(138, 224, 175, 28);
        passwordField.setBounds(138, 295, 175, 28);
        loginButton.setBounds(89, 393, 200, 30);
        registerButton.setBounds(89, 468, 200, 35);

        // Thêm các thành phần vào màn hình
        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        // Hiển thị màn hình
        setVisible(true);

        // Xử lý sự kiện khi nhấn nút "Đăng nhập"
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Gửi yêu cầu đăng nhập qua ClientSocket
                ClientSocket clientSocket = ClientSocket.getInstance();
                User user = clientSocket.loginUser(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                    getClientFrame().setCurrentUser(user); // Lưu thông tin người dùng
                    getClientFrame().showHomeScreen(user); // Chuyển sang màn hình HomeScreen
                } else {
                    JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không đúng");
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút "Đăng ký"
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showRegisterScreen(); // Chuyển sang màn hình đăng ký
            }
        });
    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/Login1.jpg");
            if (imgStream != null) {
                backgroundImage = ImageIO.read(imgStream);
            } else {
                System.out.println("Background image not found, proceeding without it.");
            }
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }
    }

    // Vẽ hình nền
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
