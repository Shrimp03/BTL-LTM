package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.GameSession;
import model.Product;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HomeScreen extends JPanel {
    private JLabel nameLabel;
    private JLabel pointsLabel;
    private JButton playButton;
    private JButton rankingButton;
    private JButton soloButton; // Nút Solo
    private User user;
    private ClientSocket clientSocket;

    public HomeScreen(User user) {
        this.user = user;
        clientSocket = ClientSocket.getInstance();
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

        // Panel chứa các nút "Chơi trò chơi", "Bảng xếp hạng" và "Solo"
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

        // Nút "Solo"
        soloButton = new JButton("Solo");
        soloButton.setFont(new Font("Arial", Font.PLAIN, 18));
        soloButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(soloButton);

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

        // Thêm sự kiện cho nút "Solo"
        soloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tạo một GameSession mới cho trò chơi solo
                GameSession gameSession = clientSocket.requestSolo(user);

                // Tạo ArrayList<Product> và thêm 12 sản phẩm giống như trong cơ sở dữ liệu
                ArrayList<Product> products = new ArrayList<>();
                products.add(new Product(21, "Chuối", "chuoi.png"));
                products.add(new Product(22, "Coca", "coca.png"));
                products.add(new Product(23, "Hamber", "hamber.png"));
                products.add(new Product(24, "Lê", "le.png"));
                products.add(new Product(25, "Nước tăng lực", "nctangluc.png"));
                products.add(new Product(26, "Ngũ cốc xanh", "ngucocxanh.png"));
                products.add(new Product(27, "Nước giặt cam", "nuocgiatcam.png"));
                products.add(new Product(28, "Nước giặt trắng", "nuocgiattrang.png"));
                products.add(new Product(29, "Nước giặt xanh", "nuocgiatxanh.png"));
                products.add(new Product(30, "Trà đào", "quadao.png"));
                products.add(new Product(31, "Sữa", "sua.png"));
                products.add(new Product(32, "Cà chua", "tomato.png"));

                // Hiển thị màn hình solo với danh sách sản phẩm
                getClientFrame().showSoloScreen(user, gameSession, products);
            }
        });

    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}