package client.view;

import client.controller.Client;
import model.User;
import utils.CloudinaryConfig;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UpdateUserScreen extends JPanel {
    private User user;
    private JLabel avatarLabel;
    private JButton uploadButton;
    private JButton saveButton;

    public UpdateUserScreen(User user) {
        this.user = user;
        setLayout(null);

        // Hiển thị avatar hiện tại
        avatarLabel = new JLabel();
        avatarLabel.setBounds(100, 50, 100, 100);
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        loadAvatar();
        add(avatarLabel);

        // Nút "Upload Ảnh"
        uploadButton = new JButton("Upload Ảnh");
        uploadButton.setBounds(100, 200, 150, 30);
        uploadButton.addActionListener(e -> uploadImage());
        add(uploadButton);

        // Nút "Lưu" để lưu cập nhật và quay lại màn hình Home
        saveButton = new JButton("Lưu");
        saveButton.setBounds(100, 250, 150, 30);
        saveButton.addActionListener(e -> {
            getClientFrame().showHomeScreen(user);
        });
        add(saveButton);
    }

    private void loadAvatar() {
        try {
            ImageIcon imageIcon = new ImageIcon(new URL(user.getAvatar()));
            Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            avatarLabel.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            CloudinaryConfig cloudinaryHelper = new CloudinaryConfig();
            String imageUrl = cloudinaryHelper.uploadImage(selectedFile.getAbsolutePath());

            if (imageUrl != null) {
                user.setAvatar(imageUrl);
                loadAvatar();
                JOptionPane.showMessageDialog(this, "Upload thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Upload thất bại!");
            }
        }
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
