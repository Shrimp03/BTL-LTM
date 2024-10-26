package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.Product;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class PlayScreen extends JPanel {
    private ClientSocket clientSocket;
    private User user;
    private ArrayList<Product> products;
    private ArrayList<Product> correctOrder;
    private BufferedImage backgroundImage;
    private JLabel draggedLabel = null;
    private Point initialClick;
    private JPanel[] shelfPanels;
    private JLabel[] floorSlots;
    private JLabel[][] shelfSlots;
    private int originSlotIndex;
    private int originShelfIndex;
    private JLabel timerLabel;
    private int remainingTime;
    private int penaltyTime;
    private Timer gameTimer;
    private int points;

    // Nút "Xem Gợi Ý" và "Về Màn Hình Chính"
    private JButton hintButton;
    private JButton homeButton;

    public PlayScreen(User user, ArrayList<Product> products) {
        this.user = user;
        this.products = products;
        this.correctOrder = new ArrayList<>(products); // Lưu trữ thứ tự đúng
        this.points = 500; // Điểm ban đầu
        this.remainingTime = 60; // Thời gian đếm ngược
        this.penaltyTime = 0; // Bộ đếm thời gian phạt
        this.clientSocket = new ClientSocket();

        // Xáo trộn sản phẩm
        Collections.shuffle(products);

        // Thiết lập màn hình chơi
        loadBackgroundImage();
        setLayout(null);

        // Tạo các thành phần giao diện
        createTimer();
        createHintButton();
        createHomeButton();

        // Tạo các kệ và sản phẩm
        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][3];
        int[] xPositions = {2, 207, 2, 207};
        int[] yPositions = {230, 230, 305, 305};

        for (int i = 0; i < shelfPanels.length; i++) {
            shelfPanels[i] = createShelfPanel(xPositions[i], yPositions[i], i);
            add(shelfPanels[i]);
        }

        // Tạo và sắp xếp các sản phẩm trên sàn
        floorSlots = new JLabel[12];
        JPanel floorPanel = createFloorPanel(products);
        add(floorPanel);
    }

    // Tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/inGameBackground.png");
            if (imgStream != null) {
                backgroundImage = ImageIO.read(imgStream);
            } else {
                System.out.println("Background image not found, proceeding without it.");
            }
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
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

    // Tạo các kệ và ô chứa sản phẩm
    private JPanel createShelfPanel(int x, int y, int shelfIndex) {
        JPanel shelfPanel = new JPanel();
        shelfPanel.setBounds(x, y, 160, 60);
        shelfPanel.setLayout(new GridLayout(1, 3, 5, 5));
        shelfPanel.setOpaque(false);

        for (int i = 0; i < shelfSlots[shelfIndex].length; i++) {
            shelfSlots[shelfIndex][i] = createSlotLabel();
            addDragAndDropFunctionality(shelfSlots[shelfIndex][i], shelfPanel, i, shelfIndex);
            shelfPanel.add(shelfSlots[shelfIndex][i]);
        }

        return shelfPanel;
    }

    // Tạo sản phẩm trên sàn
    private JPanel createFloorPanel(ArrayList<Product> shuffledProducts) {
        JPanel floorPanel = new JPanel();
        floorPanel.setBounds(20, 500, 320, 100);
        floorPanel.setLayout(new GridLayout(2, 6, 5, 5));
        floorPanel.setOpaque(false);

        for (int i = 0; i < floorSlots.length; i++) {
            floorSlots[i] = createSlotLabel();
            floorPanel.add(floorSlots[i]);
            if (i < shuffledProducts.size()) {
                String imagePath = "/static/item/" + shuffledProducts.get(i).getImageUrl();
                ImageIcon icon = createIconFromResource(imagePath);

                if (icon != null) {
                    floorSlots[i].setIcon(icon);
                    floorSlots[i].putClientProperty("product", shuffledProducts.get(i));
                    addDragAndDropFunctionality(floorSlots[i], floorPanel, i, -1);
                }
            }
        }
        return floorPanel;
    }

    // Tạo nhãn chứa sản phẩm
    private JLabel createSlotLabel() {
        JLabel slotLabel = new JLabel();
        slotLabel.setHorizontalAlignment(JLabel.CENTER);
        slotLabel.setVerticalAlignment(JLabel.CENTER);
        slotLabel.setPreferredSize(new Dimension(50, 50));
        return slotLabel;
    }

    // Tạo biểu tượng từ tài nguyên
    private ImageIcon createIconFromResource(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            try {
                BufferedImage img = ImageIO.read(imgURL);
                Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            } catch (IOException e) {
                System.err.println("Couldn't load image: " + path);
                return null;
            }
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    // Tạo nút "Xem Gợi Ý"
    private void createHintButton() {
        hintButton = new JButton("Gợi Ý");
        hintButton.setBounds(10, 10, 120, 30); // Vị trí trên cùng bên trái
        hintButton.setFont(new Font("Arial", Font.BOLD, 12));
        hintButton.setBackground(new Color(100, 149, 237));
        hintButton.setForeground(Color.WHITE);
        hintButton.setFocusPainted(false);

        hintButton.addActionListener(e -> {
            showCorrectOrderDialog();  // Hiển thị gợi ý
        });

        add(hintButton);
    }

    // Tạo nút "Về Màn Hình Chính"
    private void createHomeButton() {
        homeButton = new JButton("Trang chủ");
        homeButton.setBounds(255, 10, 150, 30); // Vị trí trên cùng bên phải
        homeButton.setFont(new Font("Arial", Font.BOLD, 12));
        homeButton.setBackground(new Color(255, 69, 0));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);

        homeButton.addActionListener(e -> {
            gameTimer.stop();  // Dừng bộ đếm thời gian
            getClientFrame().showHomeScreen();  // Quay lại màn hình chính
        });

        add(homeButton);
    }

    // Kéo thả sản phẩm
    private void addDragAndDropFunctionality(JLabel label, JPanel sourcePanel, int slotIndex, int shelfIndex) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (label.getIcon() != null) {
                    draggedLabel = createSlotLabel();
                    draggedLabel.setIcon(label.getIcon());
                    draggedLabel.putClientProperty("product", label.getClientProperty("product"));

                    initialClick = e.getPoint();

                    label.setIcon(null);
                    label.putClientProperty("product", null);

                    Point startPoint = SwingUtilities.convertPoint(sourcePanel, label.getLocation(), PlayScreen.this);
                    add(draggedLabel);
                    draggedLabel.setLocation(startPoint);
                    draggedLabel.setSize(draggedLabel.getPreferredSize());
                    repaint();

                    originSlotIndex = slotIndex;
                    originShelfIndex = shelfIndex;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedLabel != null) {
                    boolean placed = false;
                    Product draggedProduct = (Product) draggedLabel.getClientProperty("product");

                    // Kiểm tra vị trí thả sản phẩm
                    for (int i = 0; i < shelfPanels.length; i++) {
                        for (int j = 0; j < shelfSlots[i].length; j++) {
                            if (isWithinSlot(e, shelfSlots[i][j])) {
                                placeItemInSlot(shelfSlots[i][j], draggedLabel, draggedProduct);
                                placed = true;
                                break;
                            }
                        }
                        if (placed) break;
                    }

                    if (!placed) {
                        for (JLabel floorSlot : floorSlots) {
                            if (isWithinSlot(e, floorSlot)) {
                                placeItemInSlot(floorSlot, draggedLabel, draggedProduct);
                                placed = true;
                                break;
                            }
                        }
                    }

                    if (!placed) {
                        if (originShelfIndex == -1) {
                            floorSlots[originSlotIndex].setIcon(draggedLabel.getIcon());
                            floorSlots[originSlotIndex].putClientProperty("product", draggedProduct);
                        } else {
                            shelfSlots[originShelfIndex][originSlotIndex].setIcon(draggedLabel.getIcon());
                            shelfSlots[originShelfIndex][originSlotIndex].putClientProperty("product", draggedProduct);
                        }
                    }

                    remove(draggedLabel);
                    repaint();
                    draggedLabel = null;

                    checkIfAllShelvesFilled();
                }
            }
        });

        mountMotion(label);
    }

    // Kiểm tra sản phẩm có nằm trong ô không
    private boolean isWithinSlot(MouseEvent e, JLabel slot) {
        Rectangle slotBounds = SwingUtilities.convertRectangle(slot.getParent(), slot.getBounds(), PlayScreen.this);
        Point mouseLocation = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), PlayScreen.this);
        return slotBounds.contains(mouseLocation);
    }

    // Đặt sản phẩm vào ô
    private void placeItemInSlot(JLabel slot, JLabel item, Product product) {
        Icon currentIcon = slot.getIcon();
        Product currentProduct = (Product) slot.getClientProperty("product");

        slot.setIcon(item.getIcon());
        slot.putClientProperty("product", product);

        if (currentIcon != null && currentProduct != null) {
            if (originShelfIndex == -1) {
                floorSlots[originSlotIndex].setIcon(currentIcon);
                floorSlots[originSlotIndex].putClientProperty("product", currentProduct);
            } else {
                shelfSlots[originShelfIndex][originSlotIndex].setIcon(currentIcon);
                shelfSlots[originShelfIndex][originSlotIndex].putClientProperty("product", currentProduct);
            }
        }
    }

    // Phương thức kéo thả sản phẩm
    private void mountMotion(JLabel label) {
        label.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedLabel != null) {
                    Point point = SwingUtilities.convertPoint(label, e.getPoint(), PlayScreen.this);
                    int x = point.x - initialClick.x;
                    int y = point.y - initialClick.y;
                    draggedLabel.setLocation(x, y);
                    repaint();
                }
            }
        });
    }

    // Tạo bộ đếm thời gian
    private void createTimer() {
        timerLabel = new JLabel("00:60");
        timerLabel.setBounds(140, 10, 100, 30);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.BLACK);
        add(timerLabel);

        gameTimer = new Timer(1000, e -> updateTimer());
        gameTimer.start();
    }

    // Cập nhật bộ đếm thời gian
    private void updateTimer() {
        if (remainingTime > 0) {
            remainingTime--;
            int minutes = remainingTime / 60;
            int seconds = remainingTime % 60;
            String timeString = String.format("%02d:%02d", minutes, seconds);
            timerLabel.setText(timeString);

            ++penaltyTime;
            if (penaltyTime == 5) {
                penaltyTime = 0;
                points -= 30;
            }
        } else {
            gameTimer.stop();
            timerLabel.setText("Time's up!");
            handleTimeUp();
            points = 0;
            updateUser();
        }
    }

    // Kiểm tra khi tất cả các kệ đã đầy sản phẩm
    private void checkIfAllShelvesFilled() {
        boolean allFilled = true;

        for (JLabel[] slot : shelfSlots) {
            for (JLabel jLabel : slot) {
                if (jLabel.getIcon() == null) {
                    allFilled = false;
                    break;
                }
            }
            if (!allFilled) break;
        }

        if (allFilled) {
            ArrayList<Product> shelf = new ArrayList<>();
            for (JLabel[] shelfSlot : shelfSlots) {
                for (JLabel jLabel : shelfSlot) {
                    shelf.add((Product) jLabel.getClientProperty("product"));
                }
            }

            if (shelf.equals(correctOrder)) {
                gameTimer.stop();
                updateUser();
            } else {
                System.out.println("Sai thứ tự!");
                showCorrectOrderDialog();
            }
        }
    }

    // Hiển thị gợi ý sắp xếp đúng
    private void showCorrectOrderDialog() {
        points -= 50;
        JDialog dialog = new JDialog(getClientFrame(), "Gợi ý sắp xếp đúng", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());

        JPanel correctPanel = new JPanel();
        correctPanel.setLayout(new GridLayout(2, correctOrder.size() / 2));

        for (Product product : correctOrder) {
            String imagePath = "/static/item/" + product.getImageUrl();
            ImageIcon icon = createIconFromResource(imagePath);

            JLabel label = new JLabel();
            label.setIcon(icon);
            label.setHorizontalAlignment(JLabel.CENTER);
            correctPanel.add(label);
        }

        dialog.add(correctPanel, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(this);

        SwingUtilities.invokeLater(() -> {
            dialog.setVisible(true);
        });

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // Cập nhật người dùng
    private void updateUser() {
        this.user.setPoints(this.user.getPoints() + " " + Math.max(points, 0));
        boolean update = clientSocket.updateUser(this.user);
        if (update) {
            createSuccessDialog();
        } else {
            createErrorDialog();
        }
    }

    // Xử lý hết thời gian
    private void handleTimeUp() {
        JDialog dialog = new JDialog(getClientFrame(), "Thời gian đã hết!", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setUndecorated(true);

        JLabel titleLabel = new JLabel("Time's Up!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.RED);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 223, 186));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel("Bạn đã hết thời gian.", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.setForeground(Color.WHITE);
        okButton.setBackground(new Color(100, 149, 237));
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);

        dialog.add(titleLabel, BorderLayout.NORTH);
        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Tạo dialog thông báo thành công
    private void createSuccessDialog() {
        JDialog dialog = new JDialog(getClientFrame(), "Tổng kết", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new BorderLayout());
        dialog.setUndecorated(true);

        JLabel titleLabel = new JLabel("Tổng kết", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(220, 255, 220));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel scoreLabel = new JLabel("Điểm của bạn: " + points, JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton replayButton = new JButton("Chơi lại");
        replayButton.setFont(new Font("Arial", Font.BOLD, 16));
        replayButton.setForeground(Color.WHITE);
        replayButton.setBackground(new Color(100, 149, 237));
        replayButton.setFocusPainted(false);
        replayButton.addActionListener(e -> {
            dialog.dispose();
            getClientFrame().showQuestionScreen();  // Gọi màn hình câu hỏi để chơi lại
        });

        JButton mainMenuButton = new JButton("Màn hình chính");
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 16));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setBackground(new Color(100, 149, 237));
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.addActionListener(e -> {
            dialog.dispose();
            getClientFrame().showHomeScreen();  // Quay lại màn hình chính
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(replayButton);
        buttonPanel.add(mainMenuButton);

        dialog.add(titleLabel, BorderLayout.NORTH);
        dialog.add(scoreLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Tạo dialog thông báo lỗi
    private void createErrorDialog() {
        JDialog dialog = new JDialog(getClientFrame(), "Lỗi", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setUndecorated(true);

        JLabel titleLabel = new JLabel("Đã xảy ra lỗi khi cập nhật!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.RED);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 220, 220));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.setForeground(Color.WHITE);
        okButton.setBackground(new Color(255, 69, 0));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> {
            dialog.dispose();
            getClientFrame().showHomeScreen();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        dialog.add(titleLabel, BorderLayout.NORTH);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Phương thức lấy Client (JFrame) cha
    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}