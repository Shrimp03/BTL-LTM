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
    private JButton backButton;  // Nút quay lại màn hình đăng nhập
    private Image backgroundImage;

    public RegisterScreen() {
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
        usernameField.setFont(new Font("Arial", Font.BOLD, 17));

        // Trường nhập mật khẩu
        passwordField = new JPasswordField(15);
        passwordField.setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(null); // Loại bỏ viền
        passwordField.setOpaque(false); // Nền trong suốt
        passwordField.setFont(new Font("Arial", Font.BOLD, 17));
        // Trường nhập email
        emailField = new JTextField(15);
        emailField.setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        emailField.setForeground(Color.BLACK);
        emailField.setBorder(null); // Loại bỏ viền
        emailField.setOpaque(false); // Nền trong suốt
        emailField.setFont(new Font("Arial", Font.BOLD, 17));

        // Nút "Đăng kí"
        registerButton = new JButton("Đăng kí");
        registerButton.setContentAreaFilled(false); // Nền trong suốt
        registerButton.setFocusPainted(false); // Loại bỏ viền khi nhấn
        registerButton.setOpaque(false); // Nền trong suốt
        registerButton.setBorder(null); // Loại bỏ viền hoàn toàn
        registerButton.setFont(new Font("Arial", Font.BOLD, 26));
        registerButton.setForeground(Color.WHITE); // Đặt màu chữ

        // Nút "Quay lại trang đăng nhập"
        backButton = new JButton("Quay lại");
        backButton.setContentAreaFilled(false); // Nền trong suốt
        backButton.setFocusPainted(false); // Loại bỏ viền khi nhấn
        backButton.setOpaque(false); // Nền trong suốt
        backButton.setBorder(null); // Loại bỏ viền
        backButton.setForeground(Color.WHITE); // Văn bản màu trắng
        backButton.setFont(new Font("Arial", Font.BOLD, 26));

        // Đặt vị trí cho các thành phần
        usernameField.setBounds(152, 188, 160, 30);
        passwordField.setBounds(152, 272, 160, 30);
        emailField.setBounds(152, 358, 160, 30);
        registerButton.setBounds(80, 450, 220, 33);
        backButton.setBounds(80, 555, 220, 33);

        // Thêm các thành phần vào giao diện
        add(usernameField);
        add(passwordField);
        add(emailField);
        add(registerButton);
        add(backButton);

        // Thêm sự kiện khi nhấn nút "Register"
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();

                // Gửi yêu cầu đăng ký qua ClientSocket
                Boolean success = ClientSocket.getInstance().registerUser(new User(username, password, email));
                if (success) {
                    JOptionPane.showMessageDialog(null, "Đăng kí thành công!");
                    getClientFrame().showLoginScreen();  // Chuyển về màn hình đăng nhập nếu đăng ký thành công
                } else {
                    JOptionPane.showMessageDialog(null, "Đăng kí thất bại!");
                }
            }
        });

        // Thêm sự kiện khi nhấn nút "Trang đăng nhập" để quay lại màn hình đăng nhập
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
            InputStream imgStream = getClass().getResourceAsStream("/static/dangky.png");
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
