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

public class RegisterScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;  // Thêm nút quay lại màn hình đăng nhập
    private ClientSocket clientSocket;
    private Image backgroundImage;

    public RegisterScreen() {
        loadBackgroundImage();
        setSize(400, 300);
        setLayout(null);  // Kích thước khung giống PlayScreen
        Color customColor = new Color(230, 227, 227); // Màu xám nhạt
        //Login

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

        JTextField emailField  = new JTextField(15);
        emailField.setBackground(customColor); // Đặt màu nền
        emailField.setForeground(Color.BLACK); // Đặt màu chữ
        emailField.setBorder(BorderFactory.createLineBorder(customColor));
        emailField.setFont(new Font("Arial", Font.BOLD, 16));
        emailField.setForeground(new Color(0, 0, 0));



        registerButton = new JButton("Đăng kí");
        // Làm cho nền trong suốt
        registerButton.setContentAreaFilled(false); // Nền trong suốt
        registerButton.setFocusPainted(false); // Xóa viền tập trung
        registerButton.setOpaque(false); // Không vẽ nền
        registerButton.setFont(new Font("Arial", Font.BOLD, 26)); // Đặt font chữ
        // Cài đặt màu viền
        Color customPurple = new Color(126, 38, 197); // Màu tím cho viền
        registerButton.setBorder(BorderFactory.createLineBorder(customPurple, 2)); // Viền màu tím dày 2px


        backButton = new JButton("Trang đăng nhập");  // Thêm nút quay lại màn hình đăng nhập
        backButton.setContentAreaFilled(true); // Đặt nền
        backButton.setBackground(Color.BLACK); // Nền màu đen
        backButton.setForeground(Color.WHITE); // Văn bản màu trắng
        backButton.setFocusPainted(false); // Xóa viền tập trung
        backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));// Không vẽ nền
        // Làm cho văn bản màu trắng
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 26));

        // Set bounds for the components to position them in the center
        usernameField.setBounds(149, 184, 160, 29);
        passwordField.setBounds(149, 246, 160, 28);
        passwordField.setForeground(new Color(138, 43, 226));
        emailField.setBounds(149, 308, 160, 28);
        registerButton.setBounds(90, 411, 220, 33);
        backButton.setBounds(90, 480, 220, 33);

        add(usernameField);
        add(passwordField);
        add(emailField);
        add(registerButton);
        add(backButton);  // Thêm nút Back to Login vào giao diện

        // Thêm sự kiện khi nhấn nút "Register"
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();

                // Gửi yêu cầu đăng ký qua ClientSocket
                ClientSocket clientSocket = new ClientSocket();
                Boolean success = clientSocket.registerUser(new User(username, password, email));
                if (success) {
                    JOptionPane.showMessageDialog(null, "Đăng kí thành công!");
                    getClientFrame().showLoginScreen();  // Chuyển về màn hình đăng nhập nếu đăng ký thành công
                } else {
                    JOptionPane.showMessageDialog(null, "Đăng kí thất bại!");
                }
            }
        });

        // Thêm sự kiện khi nhấn nút "Đăng kí thành công" để quay lại màn hình đăng nhập
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showLoginScreen();  // Quay lại màn hình đăng nhập
            }
        });
    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/Register.jpg");
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
