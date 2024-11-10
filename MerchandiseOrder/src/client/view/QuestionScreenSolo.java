package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.GameSession;
import model.Product;
import model.User;
import utils.RoundedBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class QuestionScreenSolo extends JPanel {
    private User user;
    private ClientSocket clientSocket;
    private ArrayList<Product> products;
    private int animatedBarWidth = 10; // Initial width of the white bar
    private Timer animationTimer;
    private JLabel[][] shelfSlots;
    private JPanel[] shelfPanels;
    private JPanel shelfMechandise;
    private int countdownSeconds = 5; // Set the countdown time to 15 seconds
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private GameSession gameSession;
    private User startingPlayer;

    public QuestionScreenSolo(User user, GameSession gameSession, User startingPlayer, Product[] products) {
        this.user = user;
        this.gameSession = gameSession;
        this.startingPlayer = startingPlayer;
        this.products = new ArrayList<>(Arrays.asList(products));
        clientSocket = ClientSocket.getInstance();
        addShelfScreen();
    }



    private void addShelfScreen() {

        // Sử dụng layout null để có thể tùy chỉnh vị trí của các thành phần
        this.setLayout(null);

        // Tạo panel chứa các kệ hàng (shelf) với GridLayout (2 hàng và 2 cột)
        shelfMechandise = new JPanel();
        shelfMechandise.setLayout(new GridLayout(2, 2, 10, 10));
        shelfMechandise.setOpaque(false);

        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][3];

        // Thêm từng kệ hàng vào panel shelfMechandise
        System.out.println("Products");
        System.out.println(products.get(0).getImageUrl());
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

//        revalidate();
//        repaint();
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
                    getClientFrame().showSoloScreen(user, gameSession, products, startingPlayer);
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
            // Kiểm tra đường dẫn ảnh
            System.out.println("Loading image from path: " + imagePath);

            // Đảm bảo đường dẫn đúng chuẩn cho resource
            imagePath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            Image image = ImageIO.read(QuestionScreenSolo.class.getResource(imagePath));

            if (image == null) {
                System.err.println("Image not found at path: " + imagePath);
                return null;
            }

            // Điều chỉnh kích thước ảnh
            if (height == null) {
                double aspectRatio = (double) image.getHeight(null) / image.getWidth(null);
                height = (int) (width * aspectRatio);
            }
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        } catch (IOException e) {
            System.err.println("Error loading image from path: " + imagePath);
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Resource not found: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }



    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
