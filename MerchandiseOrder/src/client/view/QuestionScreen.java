package client.view;

import client.controller.Client;
import model.Product;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;



public class QuestionScreen extends JPanel{
    private User user;
    private ArrayList<Product> products;
    private JButton btnGoToPlay;
    private int animatedBarWidth = 15; // Initial width of the white bar
    private Timer animationTimer;
    private JLabel[] floorSlots;
    private int originSlotIndex;
    private int originShelfIndex;
    private JLabel[][] shelfSlots;
    private JPanel[] shelfPanels;

    public QuestionScreen() {
        this.user = new User(5, "user5", "password123", "user5@example.com", "10 20 30 40", "C:/full/path/to/your/image/avatar.png");

        ArrayList<Product> products = new ArrayList<>();
        String[] urls = {
                "chuoi.png",
                "coca.png",
                "hamber.png",
                "le.png",
                "nctangluc.png",
                "ngucocxanh.png",
                "nuocgiatcam.png",
                "nuocgiattrang.png",
                "quadao.png",
                "sua.png",
                "tomato.png",
                "xanh.png"
        };
        for (int i = 0; i < urls.length; ++i) {
            products.add(new Product(products.size(), "" + (i + 1), urls[i]));
        }
        this.products = products;

        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][3];
        int[] xPositions = {2, 207, 2, 207};
        int[] yPositions = {230, 230, 305, 305};

        System.out.println(shelfPanels.length);

        for (int i = 0; i < shelfPanels.length; i++) {
            shelfPanels[i] = createShelfPanel(xPositions[i], yPositions[i], i, products);
            add(shelfPanels[i]);
        }

        btnGoToPlay = new JButton("Go to Play");
        btnGoToPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBarAnimation();
            }
        });


        this.add(btnGoToPlay);
    }

    private void startBarAnimation() {
        int delay = 10; // Speed of animation (lower is faster)
        animatedBarWidth = 15; // Reset bar width at the start

        animationTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animatedBarWidth += 5; // Increase the width gradually
                if (animatedBarWidth >= getWidth()) {
                    animatedBarWidth = getWidth(); // Ensure it covers the panel
                    animationTimer.stop();
                    addShelfScreen();

                }
                repaint(); // Repaint panel to update the bar width
            }
        });

        animationTimer.start();
    }

    private void addShelfScreen() {
        // Xóa nội dung hiện tại của QuestionScreen để thêm màn mới
        this.removeAll();
        this.revalidate();
        this.repaint();

        // Tạo và thêm kệ sản phẩm vào panel
        ArrayList<Product> products = this.products; // Sử dụng danh sách sản phẩm đã có
        JPanel shelfPanel = createShelfPanel(200, 100, 0, products); // Điều chỉnh tọa độ theo mong muốn
        this.add(shelfPanel);
    }

    // Vẽ hình nền
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Load the background image and scale it according to the panel size
        ImageIcon backgroundImage = showImage("/static/inGameBackground.png", getWidth(), getHeight());

        if (backgroundImage != null) {
            // Draw the scaled background image
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        g.setColor(Color.WHITE);

        int barWidth = 15;
        int barHeight = getHeight();
        int barX = (getWidth() - barWidth) / 2;
        int barY = 0;


        if (animatedBarWidth < getWidth()) {

            g.fillRect(barX-10, barY, barWidth, barHeight);
            g.fillRect(barX+10, barY, barWidth, barHeight);

            Color overlayColor = new Color(255, 255, 255, 128); // Semi-transparent white
            g.setColor(overlayColor);
            // Left overlay
            g.fillRect(0, 0, barX-animatedBarWidth + 15, barHeight);

            // Right overlay
            g.fillRect(barX + animatedBarWidth + 10, 0, getWidth() - (barX + animatedBarWidth), barHeight);
        }
    }


    private JPanel createShelfPanel(int x, int y, int shelfIndex, ArrayList<Product> products) {
        JPanel shelfPanel = new JPanel();
        shelfPanel.setBounds(x, y, 160, 60);
        shelfPanel.setLayout(new GridLayout(1, 3, 5, 5));
        shelfPanel.setOpaque(false);


        System.out.println(shelfIndex);

        for (int i = 0; i < shelfSlots[shelfIndex].length; i++) {
            shelfSlots[shelfIndex][i] = createSlotLabel();
            // Nếu sản phẩm tồn tại, đặt icon và gán sản phẩm cho slot
                String imagePath = "/static/item/" + products.get(i).getImageUrl();
                ImageIcon icon = showImage(imagePath, 50, 50);
                if (icon != null) {
                    shelfSlots[shelfIndex][i].setIcon(icon);
                    shelfSlots[shelfIndex][i].putClientProperty("product", products.get(i));
                }
            shelfPanel.add(shelfSlots[shelfIndex][i]);
        }

        return shelfPanel;
    }

    private JLabel createSlotLabel() {
        JLabel slotLabel = new JLabel();
        slotLabel.setHorizontalAlignment(JLabel.CENTER);
        slotLabel.setVerticalAlignment(JLabel.CENTER);
        slotLabel.setPreferredSize(new Dimension(50, 50));
        return slotLabel;
    }



    private static ImageIcon showImage(String imagePath, Integer width, Integer height) {
        try {
            Image image = ImageIO.read(QuestionScreen.class.getResource(imagePath));
            if (height == null) {
                double aspectRatio = (double) image.getHeight(null) / image.getWidth(null);
                height = (int) (width * aspectRatio);
            }
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(58, 164, 246)); // Màu nền của nút #3AA4F6
        button.setForeground(Color.WHITE); // Màu chữ
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Kiểu chữ

        // Tạo đường viền bằng cách kết hợp với EmptyBorder
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3), // Đường viền trắng 3px
                BorderFactory.createEmptyBorder(15, 15, 15, 15) // Đệm bên trong
        ));

        button.setFocusPainted(false); // Không hiển thị viền khi chọn
        button.setPreferredSize(new Dimension(100, 50)); // Kích thước nút
    }


}
