package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;  // Thêm nút Register

    private Image backgroundImage;
    public LoginScreen() {
        // Tải ảnh nền
        loadBackgroundImage();
        setSize(400, 300);
        setLayout(null);
        Color customColor = new Color(230, 227, 227); // Màu xám nhạt

        JTextField usernameField = new JTextField(15);
        usernameField.setBackground(customColor); // Đặt màu nền
        usernameField.setForeground(Color.BLACK); // Đặt màu chữ
        usernameField.setBorder(BorderFactory.createLineBorder(customColor));
        usernameField.setFont(new Font("Arial", Font.BOLD, 16));
        usernameField.setForeground(new Color(0, 0, 0));
        //password
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBackground(customColor); // Đặt màu nền
        passwordField.setForeground(Color.BLACK); // Đặt màu chữ
        passwordField.setBorder(BorderFactory.createLineBorder(customColor));
        passwordField.setForeground(new Color(0, 0, 0));

        JButton loginButton = new JButton("Đăng nhập");
        // Làm cho nền trong suốt
        loginButton.setContentAreaFilled(false); // Nền trong suốt
        loginButton.setFocusPainted(false); // Xóa viền tập trung
        loginButton.setOpaque(false); // Không vẽ nền
        loginButton.setFont(new Font("Arial", Font.BOLD, 26)); // Phông chữ lớn hơn

        JButton registerButton = new JButton("Đăng ký");
        // Làm cho nền trong suốt
        // Cài đặt nền và viền
        registerButton.setContentAreaFilled(true); // Đặt nền
        registerButton.setBackground(Color.BLACK); // Nền màu đen
        registerButton.setForeground(Color.WHITE); // Văn bản màu trắng
        registerButton.setFocusPainted(false); // Xóa viền tập trung
        registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));// Không vẽ nền
        // Làm cho văn bản màu trắng
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 26));

        // Set bounds for the components to position them in the center
        usernameField.setBounds(138, 224, 175, 28);

        passwordField.setBounds(138, 295, 175, 28);
        passwordField.setForeground(new Color(138, 43, 226));
        loginButton.setBounds(89, 393, 200, 30);
        registerButton.setBounds(89, 468, 200, 35);

        // Add components to the frame
        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        // Make the frame visible
        setVisible(true);




        // Thêm sự kiện khi nhấn nút "Login"
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
                    getClientFrame().setCurrentUser(user);  // Lưu thông tin người dùng
                    getClientFrame().showHomeScreen(user);  // Chuyển sang màn hình HomeScreen nếu đăng nhập thành công
                } else {
                    JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không đúng");
                }
            }
        });

        // Thêm sự kiện khi nhấn nút "Register" để chuyển sang màn hình đăng ký
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showRegisterScreen();  // Chuyển sang màn hình đăng ký
            }
        });
    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/Login.jpg");
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
