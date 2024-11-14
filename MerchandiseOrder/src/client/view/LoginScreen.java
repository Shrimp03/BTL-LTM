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



    public static class CustomDialog extends JDialog {

        private Image backgroundImage;

        public CustomDialog(JFrame parent, String message, boolean success) {
            super(parent, "Thông báo", true);

            // Tải ảnh nền
            loadBackgroundImage();

            // Đặt layout cho nội dung của hộp thoại
            JPanel contentPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Vẽ ảnh nền
                    if (backgroundImage != null) {
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            };
            contentPanel.setLayout(new BorderLayout());
            contentPanel.setOpaque(false); // Nền trong suốt cho panel

            // Tạo nhãn với nội dung và màu chữ
            JLabel label = new JLabel(message);
            label.setForeground(Color.BLACK);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            label.setOpaque(false); // Nền trong suốt cho nhãn

            // Tạo nút đóng
            JButton closeButton = new JButton("Đóng");
            closeButton.setBackground(new Color(0, 0, 0, 100)); // Nền xám trong suốt cho nút
            closeButton.setFont(new Font("Arial", Font.BOLD, 16));
            closeButton.setForeground(Color.WHITE);
            closeButton.setOpaque(false);
            closeButton.setContentAreaFilled(false);
            closeButton.setBorderPainted(false);
            closeButton.setFocusPainted(false);
            closeButton.addActionListener(e -> dispose());

            // Thêm các thành phần vào contentPanel
            contentPanel.add(label, BorderLayout.CENTER);
            contentPanel.add(closeButton, BorderLayout.SOUTH);

            // Đặt contentPanel làm nội dung của hộp thoại
            setContentPane(contentPanel);

            setSize(300, 150);
            setLocationRelativeTo(parent);
        }

        // Phương thức tải hình nền
        private void loadBackgroundImage() {
            try {
                InputStream imgStream = getClass().getResourceAsStream("/static/popup1.png"); // Đảm bảo đường dẫn ảnh đúng
                if (imgStream != null) {
                    backgroundImage = ImageIO.read(imgStream);
                } else {
                    System.out.println("Background image not found, proceeding without it.");
                }
            } catch (IOException e) {
                System.out.println("Error loading background image: " + e.getMessage());
            }
        }

        public static void showDialog(JFrame parent, String message, boolean success) {
            new CustomDialog(parent, message, success).setVisible(true);
        }
    }


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
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 26));

        // Nút "Đăng ký"
        registerButton = new JButton("Đăng ký");
        registerButton.setContentAreaFilled(false); // Nền trong suốt
        registerButton.setFocusPainted(false); // Loại bỏ viền khi nhấn
        registerButton.setOpaque(false); // Nền trong suốt
        registerButton.setBorder(null); // Loại bỏ viền
        registerButton.setForeground(Color.BLACK); // Văn bản màu trắng
        registerButton.setFont(new Font("Arial", Font.BOLD, 26));

        // Đặt vị trí cho các thành phần
        usernameField.setBounds(160, 230, 175, 50);
        passwordField.setBounds(160, 315, 175, 50);
        loginButton.setBounds(89, 450, 200, 30);
        registerButton.setBounds(89, 550, 200, 35);

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
                    // Sử dụng CustomDialog để hiển thị thông báo thành công
                    CustomDialog.showDialog(getClientFrame(), "Đăng nhập thành công!", true);
                    getClientFrame().setCurrentUser(user); // Lưu thông tin người dùng
                    getClientFrame().showHomeScreen(user); // Chuyển sang màn hình HomeScreen
                } else {
                    // Sử dụng CustomDialog để hiển thị thông báo thất bại
                    CustomDialog.showDialog(getClientFrame(), "Tên đăng nhập hoặc mật khẩu không đúng", false);
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
            InputStream imgStream = getClass().getResourceAsStream("/static/dangnhap.png");
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
