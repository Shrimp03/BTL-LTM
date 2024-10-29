package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
public class HomeScreen extends JPanel {
    private JLabel nameLabel;
    private JLabel pointsLabel;
    private JLabel avatarLabel;
    private JButton playButton;
    private JButton rankingButton;
    private JButton uploadAvatarButton;

    private Client client;

    public HomeScreen(Client client) {
        this.client = client;

        // Thiết lập layout cho toàn bộ màn hình
        setLayout(new BorderLayout(20, 20)); // Thêm khoảng cách giữa các thành phần

        // Panel hiển thị thông tin người dùng (tên, điểm số và avatar)
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout(10, 10)); // Giữ khoảng cách giữa các thành phần
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Tạo khoảng cách bao quanh

        // Panel chứa avatar và thông tin người dùng
        JPanel avatarInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        avatarInfoPanel.setPreferredSize(new Dimension(150, 150)); // Đảm bảo kích thước đồng nhất

        // Lấy thông tin người dùng từ đối tượng client
        User user = client.getCurrentUser();

        // Hiển thị avatar (100x100)
        avatarLabel = new JLabel();
        if (user.getAvatar() != null) {
            ImageIcon avatarIcon = new ImageIcon(user.getAvatar());
            avatarLabel.setIcon(new ImageIcon(avatarIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        } else {
            avatarLabel.setText("Avatar");
        }
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Tùy chọn: viền xung quanh avatar
        avatarInfoPanel.add(avatarLabel);

        // Hiển thị tên người dùng và điểm số
        JPanel userInfoTextPanel = new JPanel(new GridLayout(2, 1));
        nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Phông chữ lớn hơn cho tên người dùng
        pointsLabel = new JLabel(user.getPoints() + " điểm");
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Phông chữ nhỏ hơn cho điểm số

        userInfoTextPanel.add(nameLabel);
        userInfoTextPanel.add(pointsLabel);
        avatarInfoPanel.add(userInfoTextPanel);

        // Thêm nút "Upload Avatar"
        uploadAvatarButton = new JButton("Upload Avatar");
        uploadAvatarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(HomeScreen.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    user.setAvatar(selectedFile.getAbsolutePath()); // Lưu đường dẫn ảnh mới vào User

                    // Cập nhật avatar mới lên giao diện
                    ImageIcon avatarIcon = new ImageIcon(user.getAvatar());
                    avatarLabel.setIcon(new ImageIcon(avatarIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

                    // Hiển thị thông báo
                    JOptionPane.showMessageDialog(null, "Cập nhật avatar thành công!");
                }
            }
        });

        // Thêm nút upload vào panel avatar
        avatarInfoPanel.add(uploadAvatarButton);

        // Thêm panel chứa thông tin người dùng vào HomeScreen
        userInfoPanel.add(avatarInfoPanel, BorderLayout.WEST); // Đặt thông tin avatar ở bên trái
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
    }
}