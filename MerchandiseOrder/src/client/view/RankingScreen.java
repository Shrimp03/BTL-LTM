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
    private JTable yourRankingTable;
    private JScrollPane leaderboardScrollPane;
    private JButton backButton;
    private User currentUser;
    private ClientSocket clientSocket;
    public RankingScreen(User currentUser ) {
        this.currentUser = currentUser;
        clientSocket = ClientSocket.getInstance();
        this.userList = new ArrayList<>(clientSocket.getAllUsers());

        // Tải ảnh nền
        loadBackgroundImage();
        if (backgroundImage != null) {
            System.out.println("Background image loaded successfully.");
        } else {
            System.out.println("Failed to load background image.");
        }


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
        Object[][] leaderboardData = new Object[userList.size()][4];
        for (int i = 0; i < numberOfPlayersToShow; i++) {
            User user = userList.get(i);
            leaderboardData[i][0] = (i + 1); // Số thứ tự
            leaderboardData[i][1] = user.getUsername(); // Tên người chơi
            leaderboardData[i][2] = user.getTotalPoints(); // Tổng điểm
            leaderboardData[i][3] = user.getHighScore(); // Điểm cao nhất
        }

        // Cài đặt giao diện
        setLayout(null);



        // Tạo bảng xếp hạng
        String[] columnNames = {"Rank", "Name", "Total Score", "High Score"};
        leaderboardTable = new JTable(leaderboardData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa ô
            }

            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
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

        leaderboardTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // Cột "Rank"
        leaderboardTable.getColumnModel().getColumn(1).setPreferredWidth(170); // Cột "Name"
        leaderboardTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Cột "Total Score"
        leaderboardTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Cột "High Score"

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
        leaderboardScrollPane.setBounds(18, 165, 340, 500);
        // Cài đặt không hiển thị thanh cuộn cho leaderboardScrollPane
        leaderboardScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        leaderboardScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        add(leaderboardScrollPane);


        int currentUserRank = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(currentUser.getUsername())) {
                currentUserRank = i + 1; // Cộng 1 để thành số thứ tự (1-based)
                break;
            }
        }

        // Thông tin người chơi hiện tại
        Object[][] yourRankingData = {
                {currentUserRank, currentUser.getUsername(), currentUser.getTotalPoints(), currentUser.getHighScore()}
        };

        // Tạo bảng hiển thị thông tin người chơi hiện tại
        String[] yourRankingColumnNames = {"#", "Name", "Total Score", "High Score"};
        JTable yourRankingTable = new JTable(yourRankingData, yourRankingColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa ô
            }

            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(false); // Làm cho ô trong suốt
                }
                return c;
            }
        };

        yourRankingTable.setRowHeight(38); // Chiều cao của mỗi dòng
        yourRankingTable.setFont(new Font("Arial", Font.PLAIN, 14));
        yourRankingTable.setShowGrid(false); // Ẩn lưới của bảng
        yourRankingTable.setOpaque(false); // Bảng trong suốt
        yourRankingTable.setTableHeader(null); // Loại bỏ tiêu đề của bảng

        // Căn giữa nội dung các ô và đặt màu chữ trắng
        for (int i = 0; i < yourRankingTable.getColumnCount(); i++) {
            yourRankingTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Tạo JScrollPane cho bảng
        JScrollPane yourRankingScrollPane = new JScrollPane(yourRankingTable);
        yourRankingScrollPane.setOpaque(false);
        yourRankingScrollPane.getViewport().setOpaque(false);
        yourRankingScrollPane.setBorder(BorderFactory.createEmptyBorder());
        yourRankingScrollPane.setBounds(18, 595, 340, 50); // Đặt vị trí bảng hiển thị thông tin người chơi
        add(yourRankingScrollPane);

        yourRankingTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // Cột "Rank"
        yourRankingTable.getColumnModel().getColumn(1).setPreferredWidth(170); // Cột "Name"
        yourRankingTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Cột "Total Score"
        yourRankingTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Cột "High Score"

        // Tạo nút quay lại
        backButton = new JButton("Trang chủ");
        backButton.setBounds(0, 0, 150, 40);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(100, 149, 237));
        backButton.setForeground(Color.WHITE);

        setComponentZOrder(backButton, 0);
        add(backButton);
        backButton.addActionListener(e -> {
            // Quay lại màn hình chính
            getClientFrame().showHomeScreen(currentUser);
        });

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
