package client.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

public class RankingScreen extends JPanel {
    private Image backgroundImage;
    private ArrayList<User> userList;
    private JLabel titleLabel;
    private JTable leaderboardTable;
    private JScrollPane leaderboardScrollPane;
    private JButton backButton;
    private User user;
    private ClientSocket clientSocket;
    public RankingScreen() {
//        this.user = user;
        this.clientSocket = new ClientSocket();
        this.userList = new ArrayList<>(clientSocket.getAllUsers());
        System.out.println(clientSocket.getAllUsers().get(0).getPoints());
        for(User user : this.userList) {
            System.out.println(user.getPoints());
        }
        // Tải ảnh nền
        loadBackgroundImage();


        // Sắp xếp danh sách người dùng theo điểm số (HighScore giảm dần, sau đó TotalPoints giảm dần)
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                int highScoreComparison = Integer.compare(u2.getHighScore(), u1.getHighScore());
                if (highScoreComparison != 0) {
                    return highScoreComparison;
                }
                return Integer.compare(u2.getTotalPoints(), u1.getTotalPoints());
            }
        });

        // Chỉ hiển thị 10 người chơi xếp hạng đầu tiên
        int numberOfPlayersToShow = Math.min(10, userList.size());
        Object[][] leaderboardData = new Object[numberOfPlayersToShow][4];
        for (int i = 0; i < numberOfPlayersToShow; i++) {
            User user = userList.get(i);
            leaderboardData[i][0] = (i + 1); // Số thứ tự
            leaderboardData[i][1] = user.getUsername(); // Tên người chơi
            leaderboardData[i][2] = user.getTotalPoints(); // Tổng điểm
            leaderboardData[i][3] = user.getHighScore(); // Điểm cao nhất
        }

        // Cài đặt giao diện
        setLayout(null);
        setPreferredSize(new Dimension(385, 685));

        // Tạo nhãn tiêu đề
        titleLabel = new JLabel("Bảng xếp hạng", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(10, 10, 365, 40);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        // Tạo bảng xếp hạng
        String[] columnNames = {"Rank", "Name", "Total Score", "High Score"};
        leaderboardTable = new JTable(leaderboardData, columnNames) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(false); // Làm cho ô trong suốt
                }
                return c;
            }
        };

        leaderboardTable.setRowHeight(38); // Chiều cao của mỗi dòng
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 14));
        leaderboardTable.setShowGrid(false); // Ẩn lưới của bảng
        leaderboardTable.setOpaque(false); // Bảng trong suốt
        leaderboardTable.setTableHeader(null); // Loại bỏ tiêu đề của bảng

        // Căn giữa nội dung các ô và đặt màu chữ trắng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setForeground(Color.WHITE);

        for (int i = 0; i < leaderboardTable.getColumnCount(); i++) {
            leaderboardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Tạo JScrollPane cho bảng
        leaderboardScrollPane = new JScrollPane(leaderboardTable);
        leaderboardScrollPane.setOpaque(false);
        leaderboardScrollPane.getViewport().setOpaque(false);
        leaderboardScrollPane.setBorder(BorderFactory.createEmptyBorder());
        leaderboardScrollPane.setBounds(10, 60, 365, 500);
        add(leaderboardScrollPane);

        // Tạo nút quay lại
        backButton = new JButton("Về màn hình chính");
        backButton.setBounds(125, 580, 150, 40);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(100, 149, 237));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> {
            // Quay lại màn hình chính
            getClientFrame().showHomeScreen(user);
        });

        add(backButton);
    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/1.jpg");
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

    // Phương thức lấy Client (JFrame) cha
    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
