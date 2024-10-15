package Client.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import Model.User;
import Server.Dal.DAO.UserDAOImpl;

public class RegisterScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;

    public RegisterScreen() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                User user = new User(0, username, password, email, null, null); // 0 for auto-incremented ID
                UserDAOImpl userDao = new UserDAOImpl();
                try {
                    userDao.addUser(user); // Thêm người dùng
                    System.out.println("User registered successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Registration failed!");
                }
            }
        });

        this.add(new JLabel("Username:"));
        this.add(usernameField);
        this.add(new JLabel("Password:"));
        this.add(passwordField);
        this.add(new JLabel("Email:"));
        this.add(emailField);
        this.add(registerButton);
    }
}
