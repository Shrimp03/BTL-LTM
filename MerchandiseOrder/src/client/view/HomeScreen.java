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
        System.out.println("new home screen, user is: " + user);
        this.user = user;
        setSize(385, 685); // Đặt kích thước lớn
        setLayout(null);
        // Tải ảnh nền
        loadBackgroundImage();

        // Khởi tạo JLabel cho ảnh
        imageLabel = new JLabel();
        imageLabel.setBounds(5, 5, 95, 95);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.white, 5));
        add(imageLabel);

        // Tải ảnh avatar từ URL
        loadAvatar();

        // Sự kiện khi nhấn vào avatar để chuyển đến trang cập nhật người dùng
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                getClientFrame().showProfileScreen(user);
            }
        });


        // Nút "Bắt đầu chơi"
        playButton = new JButton("1 PHAYER");
        styleButton(playButton, 98, 300, 170, 60); // Đặt vị trí và kích thước
        playButton.addActionListener(e -> getClientFrame().showQuestionScreen(user));
        add(playButton);

        // Nút "Bảng xếp hạng"
        rankingButton = new JButton("Bảng xếp hạng");
        styleButton(rankingButton, 88, 490, 200, 60); // Đặt vị trí và kích thước
        rankingButton.addActionListener(e -> getClientFrame().showRankingScreen(user));
        add(rankingButton);


        // Nút "Tạo phòng chơi"
        JButton createRoomButton = new JButton("PvP");
        styleButton(createRoomButton, 88, 392, 200, 60);
        createRoomButton.setFocusPainted(false); // Loại bỏ viền khi focus
        createRoomButton.setContentAreaFilled(false); // Loại bỏ màu nền
        createRoomButton.setOpaque(false); // Không vẽ nền
        createRoomButton.setBorderPainted(false); // Loại bỏ viền của nút

        createRoomButton.addActionListener(e -> getClientFrame().showInvitaionScreen(user, false, null)); // Chuyển đến màn hình tạo phòng
        add(createRoomButton);

        // Nút "Đăng Xuất" hình tròn
        JButton logoutButton = new JButton("") {
            @Override
            protected void paintComponent(Graphics g) {
                // Vẽ hình tròn trong suốt
                if (getModel().isArmed()) {
                    g.setColor(Color.LIGHT_GRAY); // Màu khi nhấn
                } else {
                    g.setColor(new Color(0, 0, 0, 0)); // Màu nền trong suốt
                }
                g.fillOval(0, 0, getWidth(), getHeight()); // Vẽ hình tròn

                // Vẽ ký tự "X" (hoặc bất kỳ ký tự nào bạn muốn cho nút "Đăng Xuất")
                g.setColor(Color.WHITE); // Màu chữ
                g.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);

                super.paintComponent(g);
            }

            @Override
            public Dimension getPreferredSize() {
                // Đặt kích thước hình tròn
                return new Dimension(60, 60);
            }
        };
        logoutButton.setBounds(315, 600, 58, 58); // Đặt vị trí và kích thước
        logoutButton.setFocusPainted(false); // Loại bỏ viền khi focus
        logoutButton.setContentAreaFilled(false); // Loại bỏ màu nền
        logoutButton.setOpaque(false); // Không vẽ nền
        logoutButton.setBorderPainted(false); // Loại bỏ viền của nút

        // Thêm sự kiện click
        logoutButton.addActionListener(e -> {
            // Hiển thị hộp thoại xác nhận
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có muốn đăng xuất không?",
                    "Xác nhận đăng xuất",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Kiểm tra phản hồi của người dùng
            if (response == JOptionPane.YES_OPTION) {
                boolean logoutSuccess = ClientSocket.getInstance().logoutUser(user); // Gửi yêu cầu logout tới server

                if (logoutSuccess) {
                    // Chuyển về màn hình đăng nhập
                    getClientFrame().showLoginScreen();
                    JOptionPane.showMessageDialog(this, "Bạn đã đăng xuất thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Đăng xuất thất bại. Vui lòng thử lại.");
                }
            }
        });
        add(logoutButton);


        hintButton = new JButton() {

            @Override
            public Dimension getPreferredSize() {
                // Đặt kích thước hình tròn
                return new Dimension(60, 60);
            }
        };
        hintButton.setBounds(295, 23, 60, 60); // Đặt vị trí và kích thước
        hintButton.setFocusPainted(false); // Loại bỏ viền khi focus
        hintButton.setContentAreaFilled(false); // Loại bỏ màu nền
        hintButton.setOpaque(false); // Không vẽ nền
        hintButton.setFont(new Font("Arial", Font.BOLD, 18));
        hintButton.setForeground(Color.WHITE); // Màu chữ
        hintButton.setBorderPainted(false); // Loại bỏ viền của nút
        hintButton.addActionListener(e -> getClientFrame().showRuleScreen(user)); // Chuyển đến màn hình gợi ý
        // Sự kiện click
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

    private void loadAvatar() {
        try {
            // Kiểm tra xem URL avatar có hợp lệ hay không
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(new URL(user.getAvatar()));
                Image image = imageIcon.getImage().getScaledInstance(94, 87, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } else {
                // Nếu không có URL, hiển thị ảnh mặc định
                imageLabel.setIcon(null); // Hoặc bạn có thể đặt một ảnh mặc định
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/home.png");
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