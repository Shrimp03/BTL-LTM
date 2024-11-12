package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.Pair;
import model.Product;
import model.User;
import model.UserStatus;
import utils.RoundedBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;


public class QuestionScreen extends JPanel{
    private User user;
    private ClientSocket clientSocket;
    private ArrayList<Product> products;
    private JLabel imgGoToPlay;
    private JButton homeButton;
    private int animatedBarWidth = 10; // Initial width of the white bar
    private Timer animationTimer;
    private JLabel[][] shelfSlots;
    private JPanel[] shelfPanels;
    private JPanel shelfMechandise;
    private int countdownSeconds = 5; // Set the countdown time to 15 seconds
    private JLabel countdownLabel;
    private Timer countdownTimer;

    public QuestionScreen(User user) {
        this.user = user;

        clientSocket = ClientSocket.getInstance();
        ArrayList<Product> products = new ArrayList<>();

        Optional<Product[]> optionalProducts = clientSocket.getProduct();
        if(optionalProducts.isPresent()) {
            Product[] productsArray = optionalProducts.get();
            for(Product product : productsArray) {
                products.add(product);
            }
        }

        this.products = products;

        this.setLayout(null);


        homeButton = new JButton("Trang chủ");
        styleButton(homeButton);
        homeButton.setBounds(0, 10, 100, 40);
        homeButton.addActionListener(e -> getClientFrame().showHomeScreen(user));

        this.add(homeButton);

        imgGoToPlay = new JLabel(showImage("/static/door.png", 100, 100));
        imgGoToPlay.setBounds(135, 250, 100, 100);// Đường dẫn tới hình ảnh
        imgGoToPlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thay đổi con trỏ khi di chuột qua ảnh
        imgGoToPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Pair<Integer, String> pair = new Pair<>(user.getId(), String.valueOf(UserStatus.PLAYING));
                Boolean updateStatusUser = ClientSocket.getInstance().updateStatusUser("UpdateStatusUser", pair);
                System.out.println(updateStatusUser);
                startBarAnimation();
            }
        });

        this.add(imgGoToPlay);
    }

    private void startBarAnimation() {
        animatedBarWidth = 15;

        animationTimer = new Timer(30,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animatedBarWidth += 10;
                if (animatedBarWidth >= getWidth()) {
                    animatedBarWidth = getWidth();
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

        ArrayList<Product> products = this.products;

        // Sử dụng layout null để có thể tùy chỉnh vị trí của các thành phần
        this.setLayout(null);

        // Tạo panel chứa các kệ hàng (shelf) với GridLayout (2 hàng và 2 cột)
        shelfMechandise = new JPanel();
        shelfMechandise.setLayout(new GridLayout(2, 2, 10, 10));
        shelfMechandise.setOpaque(false);

        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][3];

        // Thêm từng kệ hàng vào panel shelfMechandise
        for (int i = 0; i < shelfPanels.length; i++) {
            shelfPanels[i] = createShelfPanel(0, 0, i, products);
            shelfMechandise.add(shelfPanels[i]);
        }

        shelfMechandise.setBounds(0, 225, getWidth(), 150); // X: 0, Y: 200, chiều cao 200px (tùy theo bạn cần)

        add(shelfMechandise);

        countdownLabel = new JLabel("Time left: " + countdownSeconds + "s");
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 24));
        countdownLabel.setForeground(Color.RED);
        countdownLabel.setBounds(10, 10, 200, 50);  // Vị trí của label đếm ngược
        add(countdownLabel);

        startCountdownTimer();

        revalidate();
        repaint();
    }
    private void startCountdownTimer() {
        countdownSeconds = 5; // Set lại thời gian đếm ngược

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownSeconds--;
                countdownLabel.setText("Time left: " + countdownSeconds + "s");

                if (countdownSeconds <= 0) {
                    countdownTimer.stop();

                    getClientFrame().showPlayScreen(user, products); // Chuyển đến màn hình PlayScreen sau 15 giây
                }
            }
        });
        countdownTimer.start();
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

            g.fillRect(barX - animatedBarWidth, barY, barWidth, barHeight);
            g.fillRect(barX + animatedBarWidth, barY, barWidth, barHeight);

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
        shelfPanel.setBounds(x, y, 160, 50);
        shelfPanel.setLayout(new GridLayout(1, 3, 5, 5));
        shelfPanel.setOpaque(false);

        int img_index = shelfIndex*3;

        for (int i = 0; i < shelfSlots[shelfIndex].length; i++) {
            shelfSlots[shelfIndex][i] = createSlotLabel();
            String imagePath = "/static/item/" + products.get(img_index + i).getImageUrl();
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
            Image image = ImageIO.read(QuestionScreen.class.getResource(imagePath.startsWith("/") ? imagePath : "/" + imagePath));
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
        button.setFont(new Font("Arial", Font.BOLD, 13)); // Kiểu chữ

        // Tạo đường viền bằng cách kết hợp với EmptyBorder
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createLineBorder(Color.WHITE, 1)
//                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        button.setBorderPainted(false);
        button.setFocusPainted(false); // Không hiển thị viền khi chọn
        button.setPreferredSize(new Dimension(130, 30)); // Kích thước nút
    }



    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }

}