package client.view;

import client.controller.Client;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import model.User;
import utils.CloudinaryConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public class HomeScreen extends JPanel {
    private JLabel nameLabel;
    private JLabel pointsLabel;
    private JButton playButton;
    private JButton rankingButton;
    private JButton uploadButton; // Nút upload ảnh
    private User user;

    public HomeScreen(User user) {
        this.user = user;
        // Thiết lập layout cho toàn bộ màn hình
        setLayout(new BorderLayout(20, 20)); // Thêm khoảng cách giữa các thành phần

        // Panel hiển thị thông tin người dùng (tên, điểm số)
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout(10, 10)); // Giữ khoảng cách giữa các thành phần
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Tạo khoảng cách bao quanh

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
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Layout dọc cho các nút

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

        // Nút "Upload Ảnh"
        uploadButton = new JButton("Upload Ảnh");
        uploadButton.setFont(new Font("Arial", Font.PLAIN, 18));
        uploadButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(uploadButton);

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

        // Thêm sự kiện cho nút "Upload Ảnh"
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadImage();
            }
        });
    }

    // Hàm để upload ảnh
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            CloudinaryConfig cloudinaryHelper = new CloudinaryConfig();
            String imageUrl = cloudinaryHelper.uploadImage(selectedFile.getAbsolutePath());

            if (imageUrl != null) {
                JOptionPane.showMessageDialog(this, "Upload thành công! URL: " + imageUrl);
            } else {
                JOptionPane.showMessageDialog(this, "Upload thất bại!");
            }
        }
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
