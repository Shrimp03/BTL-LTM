package Client.View;

import Client.Controller.Main;
import Server.Dal.DAO.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO; // Đối tượng để giao tiếp với server

    public LoginScreen(Main mainFrame, UserDAO userDAO) {
        this.userDAO = userDAO; // Khởi tạo userDAO
        setLayout(new GridBagLayout()); // Sử dụng GridBagLayout để dễ dàng bố trí
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần

        // Tạo và thiết lập các thành phần giao diện
        createUIComponents(gbc);

        // Action listener cho nút đăng nhập
        addLoginButtonActionListener(mainFrame);

        // Action listener cho nút đăng ký
        addRegisterButtonActionListener(mainFrame);
    }

    private void createUIComponents(GridBagConstraints gbc) {
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        add(registerButton, gbc);
    }

    private void addLoginButtonActionListener(Main mainFrame) {
        JButton loginButton = (JButton) getComponent(4); // Lấy nút đăng nhập
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Kiểm tra xem người dùng có nhập tên người dùng và mật khẩu không
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty.");
                    return; // Ngừng thực hiện nếu không hợp lệ
                }

                // Xác thực người dùng thông qua server
                if (authenticateUser(username, password)) {
                    try {
                        mainFrame.showMainScreen(username); // Hiển thị giao diện chính với tên người dùng
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username or password is incorrect, please try again.");
                }
            }
        });
    }

    private void addRegisterButtonActionListener(Main mainFrame) {
        JButton registerButton = (JButton) getComponent(5); // Lấy nút đăng ký
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRegisterScreen(); // Chuyển đến giao diện đăng ký
            }
        });
    }

    private boolean authenticateUser(String username, String password) {
        return userDAO.login(username, password); // Gọi đến phương thức login trong UserDAO
    }
}
