package client.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;
import utils.CloudinaryConfig;

public class UpdateUser extends JPanel {
    private JTextField txtUsername, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnUpdate, btnChangeAvatar, btnBack;
    private User currentUser;
    private String imgUrl;
    private Image backgroundImage;
    private ClientSocket clientSocket;

    public UpdateUser(User currentUser) {
        this.currentUser = currentUser;
        setLayout(null);
        // Tải ảnh nền
        loadBackgroundImage();
        if (backgroundImage != null) {
            System.out.println("Background image loaded successfully.");
        } else {
            System.out.println("Failed to load background image.");
        }



        txtUsername = new JTextField(currentUser.getUsername());
        styleTextField(txtUsername,160,198,175,50);
        add(txtUsername);


        txtEmail = new JTextField(currentUser.getEmail());
        styleTextField(txtEmail,160,275,175,50);
        add(txtEmail);


        txtPassword = new JPasswordField(currentUser.getPassword());
        txtPassword.setBounds(160, 355, 175, 50);
        styleTextField(txtPassword,160,355,175,50);
        add(txtPassword);

        // Avatar
        btnChangeAvatar = new JButton("Change Avatar");
        styleButton(btnChangeAvatar,157,430,175,50);
        btnChangeAvatar.addActionListener(e -> uploadImage());
        add(btnChangeAvatar);



        // Update Button
        btnUpdate = new JButton("Update");
        styleButton(btnUpdate,50,555,100,30);
        btnUpdate.addActionListener(e -> updateUserProfile());
        add(btnUpdate);

        // Back Button
        btnBack = new JButton("Back");
        styleButton(btnBack,220,555,100,30);
        btnBack.addActionListener(e -> goBack());
        add(btnBack);
    }
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            CloudinaryConfig cloudinaryHelper = new CloudinaryConfig();
            String imageUrl = cloudinaryHelper.uploadImage(selectedFile.getAbsolutePath());

            if (imageUrl != null) {
                this.imgUrl = imageUrl;
                System.out.println(imageUrl);
            } else {
                JOptionPane.showMessageDialog(this, "Upload thất bại!");
            }
        }
    }


    // Method to update user profile
    private void updateUserProfile() {
        if(txtUsername.getText() != "" && txtEmail.getText() != "" && new String(txtPassword.getPassword()) != "" && imgUrl != "" ){
            currentUser.setUsername(txtUsername.getText());
            currentUser.setEmail(txtEmail.getText());
            currentUser.setPassword(new String(txtPassword.getPassword()));
            currentUser.setAvatar(this.imgUrl);
            System.out.println(currentUser);
            ClientSocket.getInstance().updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin cá nhân thành công!");
        }
        else {
            JOptionPane.showMessageDialog(this, "Không được để trống các trường!");
        }
    }
    private void styleTextField(JTextField textField, int x, int y, int width, int height) {
        textField.setBounds(x, y, width, height);
        textField.setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        textField.setForeground(Color.BLACK);
        textField.setBorder(null); // Loại bỏ viền
        textField.setOpaque(false); // Nền trong suốt
        textField.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void styleButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setFocusPainted(false); // Loại bỏ viền khi nhấn
        button.setContentAreaFilled(false); // Loại bỏ màu nền
        button.setOpaque(false); // Không vẽ nền
        button.setBorderPainted(false); // Loại bỏ viền của nút
        button.setForeground(Color.WHITE); // Đặt màu chữ
        button.setFont(new Font("Arial", Font.BOLD, 18));
    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/userprofile.png");
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

    // Go back to previous screen
    private void goBack() {
        getClientFrame().showHomeScreen(currentUser);
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
