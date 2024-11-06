package client.view;

import client.controller.Client;
import model.User;

import javax.swing.*;
import java.awt.*;

public class SuggestScreen extends JPanel {
    private User user;

    public SuggestScreen(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        // Tiêu đề cho màn hình gợi ý
        JLabel titleLabel = new JLabel("Gợi ý cho bạn", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Khu vực hiển thị gợi ý
        JTextArea suggestionArea = new JTextArea();
        suggestionArea.setText("Đây là một số gợi ý cho bạn:\n- Gợi ý 1\n- Gợi ý 2\n- Gợi ý 3");
        suggestionArea.setEditable(false);
        add(new JScrollPane(suggestionArea), BorderLayout.CENTER); // Thêm JScrollPane nếu nội dung nhiều

        // Nút quay lại
        JButton backButton = new JButton("Quay lại");
        backButton.addActionListener(e -> getClientFrame().showHomeScreen(user)); // Sử dụng getClientFrame()
        add(backButton, BorderLayout.SOUTH);
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this); // Lấy đối tượng Client
    }
}
