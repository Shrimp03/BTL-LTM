package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
public class HomeScreen extends JPanel {
    private JLabel nameLabel;
    private JLabel pointsLabel;
    private JButton playButton;
    private JButton rankingButton;
    private User user;

    public HomeScreen(User user) {
        this.user = user;
        // Thiết lập layout cho toàn bộ màn hình
        setLayout(new BorderLayout(20, 20)); // Thêm khoảng cách giữa các thành phần

        // Panel hiển thị thông tin người dùng (tên, điểm số)
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout(10, 10)); // Giữ khoảng cách giữa các thành phần
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Tạo khoảng cách bao quanh

        // Lấy thông tin người dùng từ đối tượng client

        // Hiển thị tên người dùng và điểm số
        JPanel userInfoTextPanel = new JPanel(new GridLayout(2, 1));
        nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Phông chữ lớn hơn cho tên người dùng
        pointsLabel = new JLabel(user.getPoints() + " điểm");
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Phông chữ nhỏ hơn cho điểm số

        userInfoTextPanel.add(nameLabel);
        userInfoTextPanel.add(pointsLabel);

        // Thêm panel chứa thông tin người dùng vào HomeScreen
        userInfoPanel.add(userInfoTextPanel, BorderLayout.WEST); // Đặt thông tin người dùng ở bên trái
        add(userInfoPanel, BorderLayout.NORTH); // Đặt panel thông tin người dùng ở trên cùng

        // Panel chứa các nút "Chơi trò chơi" và "Bảng xếp hạng"
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Layout dọc cho các nút

        // Nút "Bắt đầu chơi"
        playButton = new JButton("Bắt đầu chơi");
        playButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Tăng kích thước chữ cho nút
        playButton.setPreferredSize(new Dimension(200, 50)); // Đặt kích thước nút đồng nhất
        buttonPanel.add(playButton);

        // Nút "Bảng xếp hạng"
        rankingButton = new JButton("Bảng xếp hạng");
        rankingButton.setFont(new Font("Arial", Font.PLAIN, 18));
        rankingButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(rankingButton);

        // Thêm panel chứa nút vào HomeScreen
        add(buttonPanel, BorderLayout.CENTER); // Đặt các nút vào giữa

        // Thêm sự kiện cho nút "Bắt đầu chơi"
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showQuestionScreen(user);
            }
        });

        // Thêm sự kiện cho nút "Bảng xếp hạng"
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showRankingScreen(user);  // Chuyển sang trang "Bảng xếp hạng"
            }
        });
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}