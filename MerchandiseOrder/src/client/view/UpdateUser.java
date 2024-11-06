package client.view;

import javax.swing.*;
import java.awt.*;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

public class UpdateUser extends JPanel {
    private JLabel lblTitle, lblUsername, lblEmail, lblPassword, lblAvatar;
    private JTextField txtUsername, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnUpdate, btnChangeAvatar, btnBack;
    private User currentUser;
    private ClientSocket clientSocket;

    public UpdateUser(User currentUser) {
        this.currentUser = currentUser;
        setLayout(null);

        // Title
        lblTitle = new JLabel("Thông tin người dùng", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(0, 10, 400, 30);  // Căn giữa với chiều rộng 400
        add(lblTitle);

        // Username
        lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 60, 80, 25);
        add(lblUsername);

        txtUsername = new JTextField(currentUser.getUsername());
        txtUsername.setBounds(140, 60, 200, 25);
        add(txtUsername);

        // Email
        lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 110, 80, 25);
        add(lblEmail);

        txtEmail = new JTextField(currentUser.getEmail());
        txtEmail.setBounds(140, 110, 200, 25);
        add(txtEmail);

        // Password
        lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 160, 80, 25);
        add(lblPassword);

        txtPassword = new JPasswordField(currentUser.getPassword());
        txtPassword.setBounds(140, 160, 200, 25);
        add(txtPassword);

        // Avatar
        lblAvatar = new JLabel("Avatar:");
        lblAvatar.setBounds(50, 210, 80, 25);
        add(lblAvatar);

        btnChangeAvatar = new JButton("Change Avatar");
        btnChangeAvatar.setBounds(140, 210, 200, 25);
        add(btnChangeAvatar);

        // Update Button
        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(100, 300, 100, 30);
        btnUpdate.setBackground(new Color(252, 218, 134));
        btnUpdate.setForeground(Color.BLACK);
        btnUpdate.addActionListener(e -> updateUserProfile());
        add(btnUpdate);

        // Back Button
        btnBack = new JButton("Back");
        btnBack.setBounds(220, 300, 100, 30);
        btnBack.addActionListener(e -> goBack());
        add(btnBack);
    }

    // Method to update user profile
    private void updateUserProfile() {
        currentUser.setUsername(txtUsername.getText());
        currentUser.setEmail(txtEmail.getText());
        currentUser.setPassword(new String(txtPassword.getPassword()));
        ClientSocket.getInstance().updateUser(currentUser);
        JOptionPane.showMessageDialog(this, "Profile updated successfully!");
    }

    // Go back to previous screen
    private void goBack() {
        getClientFrame().showHomeScreen(currentUser);
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
