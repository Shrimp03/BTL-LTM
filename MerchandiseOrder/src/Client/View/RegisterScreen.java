package Client.View;

import Client.Controller.Main;
import Server.Dal.DAO.UserDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private UserDAO userDAO; // Đối tượng để giao tiếp với server

    public RegisterScreen(Main mainFrame, UserDAO userDAO) {
        this.userDAO = userDAO; // Khởi tạo userDAO
        setLayout(null); // Để có thể thiết lập vị trí

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 30, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 150, 25);
        add(passwordField);

        // Thêm trường nhập email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 110, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 110, 150, 25);
        add(emailField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(30, 150, 100, 30);
        add(registerButton);

        // Action listener for register
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText(); // Lấy email từ trường nhập
                // Gọi đến server để đăng ký
                if (registerUser(username, password, email)) {
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    mainFrame.showLoginScreen(); // Chuyển về giao diện đăng nhập
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed, please try again.");
                }
            }
        });
    }

    private boolean registerUser(String username, String password, String email) {
        return userDAO.register(username, password, email); // Gọi đến phương thức đăng ký trong UserDAO
    }
}
