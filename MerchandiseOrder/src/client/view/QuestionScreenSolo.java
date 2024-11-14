package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.GameSession;
import model.Product;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class QuestionScreenSolo extends JPanel {
    private User user;
    private ClientSocket clientSocket;
    private ArrayList<Product> products;
    private Timer countdownTimer;
    private JLabel[][] shelfSlots;
    private JPanel[] shelfPanels;
    private JPanel shelfMerchandise;
    private int countdownSeconds = 5;
    private JLabel countdownLabel;
    private GameSession gameSession;
    private User startingPlayer;
    private ImageIcon backgroundImage;
    private JButton buttonPlay;

    public QuestionScreenSolo(User user, GameSession gameSession, User startingPlayer, Product[] products) {
        this.user = user;
        this.gameSession = gameSession;
        this.startingPlayer = startingPlayer;
        this.products = new ArrayList<>(Arrays.asList(products));
        this.clientSocket = ClientSocket.getInstance();

        // Gọi trực tiếp addShelfScreen để hiển thị sản phẩm ngay lập tức
        SwingUtilities.invokeLater(this::addShelfScreen);
    }


    private void addShelfScreen() {
        this.setLayout(null);

        // Tạo panel chứa các kệ hàng với GridLayout (2 hàng và 2 cột)
        shelfMerchandise = new JPanel();
        shelfMerchandise.setLayout(new GridLayout(2, 2, 10, 10));
        shelfMerchandise.setOpaque(false);

        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][3];

        for (int i = 0; i < shelfPanels.length; i++) {
            shelfPanels[i] = createShelfPanel(i, products);
            shelfMerchandise.add(shelfPanels[i]);
        }

        // Cập nhật kích thước và vị trí của shelfMerchandise sau khi thêm các thành phần
        SwingUtilities.invokeLater(() -> {
            shelfMerchandise.setBounds(0, 225, getWidth(), 150);
            add(shelfMerchandise);
            revalidate();
            repaint();
        });

        countdownLabel = new JLabel("Thời gian chờ: " + countdownSeconds + "s");
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 16));
        countdownLabel.setForeground(Color.RED);
        countdownLabel.setBounds(10, 10, 200, 50);
        add(countdownLabel);

        startCountdownTimer();
    }

    private void startCountdownTimer() {
        countdownSeconds = 5;  // Start from 5 seconds

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownSeconds--;
                countdownLabel.setText("Thời gian chờ: " + countdownSeconds + "s");

                if (countdownSeconds <= 0) {
                    countdownTimer.stop();
                    getClientFrame().showSoloScreen(user, gameSession, products, startingPlayer);
                }
            }
        });
        countdownTimer.start();  // Start the timer immediately
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon backgroundImage = loadImage("/static/inGameBackground.png", getWidth(), getHeight());

        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JPanel createShelfPanel(int shelfIndex, ArrayList<Product> products) {
        JPanel shelfPanel = new JPanel();
        shelfPanel.setLayout(new GridLayout(1, 3, 5, 5));
        shelfPanel.setOpaque(false);

        int imgIndex = shelfIndex * 3;

        for (int i = 0; i < shelfSlots[shelfIndex].length; i++) {
            shelfSlots[shelfIndex][i] = createSlotLabel();
            if (imgIndex + i < products.size()) { // Đảm bảo không vượt quá số lượng sản phẩm
                String imagePath = "/static/item/" + products.get(imgIndex + i).getImageUrl();
                System.out.println(imagePath);
                ImageIcon icon = loadImage(imagePath, 50, 50);
                if (icon != null) {
                    shelfSlots[shelfIndex][i].setIcon(icon);
                    shelfSlots[shelfIndex][i].putClientProperty("product", products.get(imgIndex + i));
                }
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

    private static ImageIcon loadImage(String imagePath, Integer width, Integer height) {
        try {
            System.out.println("Loading image from path: " + imagePath);

            imagePath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;

            InputStream input = QuestionScreenSolo.class.getResourceAsStream(imagePath);
            if (input == null) {
                System.err.println("Image not found at path: " + imagePath);
                return null;
            }

            Image image = ImageIO.read(input);
            if (image == null) {
                System.err.println("Image is corrupted or unsupported: " + imagePath);
                return null;
            }

            if (width == 0 || height == 0) {
                System.err.println("Invalid width or height for image scaling.");
                return new ImageIcon(image); // Trả về ảnh gốc nếu width hoặc height không hợp lệ
            }

            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            System.out.println(scaledImage);
            return new ImageIcon(scaledImage);

        } catch (IOException e) {
            System.err.println("Error loading image from path: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
