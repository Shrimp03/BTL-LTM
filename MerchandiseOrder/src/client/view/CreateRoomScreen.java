package client.view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class CreateRoomScreen extends JPanel {
    private User user;

    public CreateRoomScreen(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Tạo Phòng Chơi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Thêm các thành phần như form tạo phòng, nút tạo phòng
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));

        formPanel.add(new JLabel("Tên phòng:"));
        JTextField roomNameField = new JTextField();
        formPanel.add(roomNameField);

        formPanel.add(new JLabel("Số người tối đa:"));
        JTextField maxPlayersField = new JTextField();
        formPanel.add(maxPlayersField);

        JButton createButton = new JButton("Tạo phòng");
        createButton.addActionListener(e -> {
            // Logic để xử lý tạo phòng
            String roomName = roomNameField.getText();
            String maxPlayers = maxPlayersField.getText();
            // Xử lý tạo phòng ở đây (gửi yêu cầu tới server, v.v.)
            JOptionPane.showMessageDialog(this, "Phòng '" + roomName + "' đã được tạo!");
        });

        add(formPanel, BorderLayout.CENTER);
        add(createButton, BorderLayout.SOUTH);
    }
}
