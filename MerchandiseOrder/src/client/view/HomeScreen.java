package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import model.User;
import utils.CloudinaryConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class HomeScreen extends JPanel {
    private JLabel nameLabel;
    private JLabel pointsLabel;
    private JButton playButton;
    private JButton rankingButton;
    private JButton uploadButton; // Nút upload ảnh
    private User user;
    private Image backgroundImage;
    private JButton hintButton;
    private JLabel imageLabel;
    public HomeScreen(User user) {
        this.user = user;
        setSize(400, 600);
        setLayout(null);

        // Tải ảnh nền
        loadBackgroundImage();

        // Khởi tạo JLabel cho ảnh
        imageLabel = new JLabel();
        imageLabel.setBounds(7, 7, 94, 87); // Đặt vị trí và kích thước cho JLabel
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.white, 5)); // Viền màu đen, độ dày 2 pixel
        add(imageLabel);

//        // Hiển thị thông tin người dùng (Tên và Điểm)
//        nameLabel = new JLabel(user.getUsername());
//        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        nameLabel.setBounds(20, 20, 200, 30); // Vị trí cho nhãn tên
//        nameLabel.setForeground(Color.WHITE); // Đặt màu chữ để dễ nhìn
//        add(nameLabel);
//
//        pointsLabel = new JLabel(user.getPoints() + " điểm");
//        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        pointsLabel.setBounds(20, 50, 200, 30); // Vị trí cho nhãn điểm
//        pointsLabel.setForeground(Color.WHITE); // Đặt màu chữ để dễ nhìn
//        add(pointsLabel);

        // Nút "Bắt đầu chơi"
        playButton = new JButton("Bắt đầu chơi");
        styleButton(playButton, 98, 313, 170, 60); // Đặt vị trí và kích thước
        playButton.addActionListener(e -> getClientFrame().showQuestionScreen(user));
        add(playButton);

        // Nút "Bảng xếp hạng"
        rankingButton = new JButton("Bảng xếp hạng");
        styleButton(rankingButton, 88, 392, 200, 60); // Đặt vị trí và kích thước
        rankingButton.addActionListener(e -> getClientFrame().showRankingScreen(user));
        add(rankingButton);

        // Nút "Upload Ảnh"
        uploadButton = new JButton("Upload Ảnh");
        styleButton(uploadButton, 20, 21, 60, 50); // Đặt vị trí và kích thước
        uploadButton.addActionListener(e -> uploadImage());
        add(uploadButton);

        // Nút "Logout"
        JButton logoutButton = new JButton("Đăng Xuất");
        styleButton(logoutButton, 88, 476, 200, 60); // Đặt vị trí và kích thước
        logoutButton.addActionListener(e -> {
            ClientSocket clientSocket = new ClientSocket();
            clientSocket.logoutUser(user);
        });
        add(logoutButton);

        // Nút hình tròn cho gợi ý
        hintButton = new JButton("?") {
            @Override
            protected void paintComponent(Graphics g) {
                // Vẽ hình tròn
                if (getModel().isArmed()) {
                    g.setColor(Color.LIGHT_GRAY); // Màu khi nhấn
                } else {
                    g.setColor(new Color(0, 0, 0, 0)); // Màu nền trong suốt
                }
                g.fillOval(0, 0, getWidth(), getHeight()); // Vẽ hình tròn

                // Vẽ ký tự "?"
                super.paintComponent(g);
            }
            @Override
            public Dimension getPreferredSize() {
                // Đặt kích thước hình tròn
                return new Dimension(60, 60);
            }
        };
        hintButton.setBounds(295, 23, 60, 60); // Đặt vị trí và kích thước
        hintButton.setFocusPainted(false);
        hintButton.setContentAreaFilled(false); // Nền trong suốt
        hintButton.setOpaque(false);
        hintButton.setFont(new Font("Arial", Font.BOLD, 18));
        hintButton.setForeground(Color.WHITE); // Màu chữ
        hintButton.setBorderPainted(false); // Loại bỏ viền nút
        hintButton.addActionListener(e -> showHint()); // Sự kiện click
        add(hintButton);
    }

    // Phương thức để hiển thị gợi ý
    private void showHint() {
        JOptionPane.showMessageDialog(this, "Đây là gợi ý!");
    }
    // Phương thức để định dạng các nút, làm cho nền trong suốt
    private void styleButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setFocusPainted(false); // Loại bỏ viền khi nhấn
        button.setContentAreaFilled(false); // Loại bỏ màu nền
        button.setOpaque(false); // Không vẽ nền
        button.setBorderPainted(false); // Loại bỏ viền của nút
        button.setForeground(Color.WHITE); // Đặt màu chữ
        button.setFont(new Font("Arial", Font.BOLD, 22));
    }
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            CloudinaryConfig cloudinaryHelper = new CloudinaryConfig();
            String imageUrl = cloudinaryHelper.uploadImage(selectedFile.getAbsolutePath());

            if (imageUrl != null) {
                JOptionPane.showMessageDialog(this, "Upload thành công! URL: " + imageUrl);

                try {
                    // Tải ảnh từ URL và hiển thị trong JLabel
                    ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                    Image image = imageIcon.getImage().getScaledInstance(94, 87, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi tải ảnh từ URL.");
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Upload thất bại!");
            }
        }
    }



//    // Phương thức để upload ảnh
//    private void uploadImage() {
//        JFileChooser fileChooser = new JFileChooser();
//        int result = fileChooser.showOpenDialog(this);
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            CloudinaryConfig cloudinaryHelper = new CloudinaryConfig();
//            String imageUrl = cloudinaryHelper.uploadImage(selectedFile.getAbsolutePath());
//
//            if (imageUrl != null) {
//                JOptionPane.showMessageDialog(this, "Upload thành công! URL: " + imageUrl);
//            } else {
//                JOptionPane.showMessageDialog(this, "Upload thất bại!");
//            }
//        }
//    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/home1.jpg");
            if (imgStream != null) {
                backgroundImage = ImageIO.read(imgStream);
            } else {
                System.out.println("Không tìm thấy hình nền, tiếp tục không có ảnh nền.");
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi tải hình nền: " + e.getMessage());
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

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
