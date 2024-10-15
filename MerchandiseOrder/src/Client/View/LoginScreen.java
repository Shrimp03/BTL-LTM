package Client.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.User;
import Server.Dal.DAO.UserDAOImpl;

public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                UserDAOImpl userDao = new UserDAOImpl();
                User user = userDao.getUser(username, password);
                if (user != null) {
                    // Đăng nhập thành công
                    System.out.println("Login successful!");
                } else {
                    // Đăng nhập thất bại
                    System.out.println("Invalid username or password.");
                }
            }
        });

        this.add(new JLabel("Username:"));
        this.add(usernameField);
        this.add(new JLabel("Password:"));
        this.add(passwordField);
        this.add(loginButton);
    }
}
