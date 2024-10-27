package Client.View;

import Client.Controller.Client;
import Client.Controller.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreen extends JPanel {
    private JLabel usernameLabel;
    private JLabel scoreLabel;
    private JButton startButton;
    private JButton rankingButton;
    private Client clientSocket; // Đối tượng để giao tiếp với Server

    public HomeScreen(Main mainFrame, Client clientSocket) {
        this.clientSocket = clientSocket; // Khởi tạo clientSocket
        setLayout(null); // Sử dụng layout null để tự thiết lập vị trí

        // Phần hồ sơ
        JPanel profilePanel = new JPanel();
        profilePanel.setBounds(10, 10, 200, 100); // Vị trí và kích thước
        profilePanel.setLayout(new BorderLayout());

        // Ảnh đại diện (Giả định rằng bạn có một URL hoặc file cho ảnh đại diện)
        ImageIcon avatar = new ImageIcon("path_to_avatar_image.jpg");
        // Thay đổi thành đường dẫn đến ảnh đại diện làm sau vì chưa xong cloudinary

        JLabel avatarLabel = new JLabel(avatar);
        profilePanel.add(avatarLabel, BorderLayout.WEST);

        // Thông tin người dùng
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1));
        usernameLabel = new JLabel("Tên người dùng: ");
        scoreLabel = new JLabel("Điểm: ");
        infoPanel.add(usernameLabel);
        infoPanel.add(scoreLabel);

        profilePanel.add(infoPanel, BorderLayout.CENTER);
        add(profilePanel);

        // Nút Bắt đầu Chơi
        startButton = new JButton("Bắt đầu chơi");
        startButton.setBounds(300, 150, 200, 50);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPlayScreen(); // Chuyển đến giao diện chơi
            }
        });
        add(startButton);

        // Nút Bảng Xếp Hạng
        rankingButton = new JButton("Bảng xếp hạng");
        rankingButton.setBounds(300, 220, 200, 50);
        rankingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRankingScreen(); // Chuyển đến giao diện bảng xếp hạng
            }
        });
        add(rankingButton);
    }

    public void updateUserInfo(String username, String points) {
        usernameLabel.setText("Tên người dùng: " + username);
        scoreLabel.setText("Điểm: " + points);
    }


}
