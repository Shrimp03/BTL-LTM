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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class SoloScreen extends JPanel {
    private ClientSocket clientSocket;
    private User user;
    private GameSession gameSession;
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
    private int points;
    private int movesCount = 0; // Biến đếm số lần xếp
    private Timer timer; // Bộ đếm thời gian
    private int timeRemaining = 20; // Thời gian còn lại

    // Nút "Về Màn Hình Chính"
    private JButton homeButton;

    // Các JLabel để hiển thị tên hai người chơi và thời gian
    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel timerLabel; // Hiển thị thời gian

    public SoloScreen(User user, GameSession gameSession, ArrayList<Product> products) {
        this.user = user;
        this.gameSession = gameSession;
        this.products = products;
        this.correctOrder = new ArrayList<>(products); // Lưu trữ thứ tự đúng
        this.points = 500; // Điểm ban đầu
        this.clientSocket = new ClientSocket();

        // Xáo trộn sản phẩm
        Collections.shuffle(products);

        // Thiết lập màn hình chơi
        loadBackgroundImage();
        setLayout(null);

        // Tạo các thành phần giao diện
        createHomeButton();
        createPlayerLabels(); // Tạo các nhãn hiển thị tên người chơi

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

        // Bắt đầu bộ đếm thời gian
        startTimer();
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

    // Tạo nút "Về Màn Hình Chính"
    private void createHomeButton() {
        homeButton = new JButton("H"); // Hoặc thay bằng biểu tượng ngôi nhà nếu có
        homeButton.setBounds(320, 10, 50, 30); // Thu nhỏ nút và đặt ở bên phải
        homeButton.setFont(new Font("Arial", Font.BOLD, 12));
        homeButton.setBackground(new Color(255, 69, 0));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);

        homeButton.addActionListener(e -> {
            System.out.println(user.getUsername());
            getClientFrame().showHomeScreen(user);  // Quay lại màn hình chính
        });

        add(homeButton);
    }

    // Tạo các nhãn hiển thị tên người chơi và thời gian
    private void createPlayerLabels() {
        // Nhãn cho người chơi 1
        player1Label = new JLabel(gameSession.getUser1().getUsername());
        player1Label.setBounds(10, 10, 100, 30);
        player1Label.setFont(new Font("Arial", Font.BOLD, 14));
        player1Label.setForeground(Color.BLACK);
        add(player1Label);

        // Nhãn cho người chơi 2
        player2Label = new JLabel(gameSession.getUser2().getUsername());
        player2Label.setBounds(220, 10, 100, 30);
        player2Label.setFont(new Font("Arial", Font.BOLD, 14));
        player2Label.setForeground(Color.BLACK);
        add(player2Label);

        // Nhãn hiển thị thời gian
        timerLabel = new JLabel("Time: 20s");
        timerLabel.setBounds(120, 10, 100, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setForeground(Color.RED);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel);
    }

    // Bắt đầu bộ đếm thời gian
    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText("Time: " + timeRemaining + "s");
                if (timeRemaining <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(SoloScreen.this, "Hết thời gian!");
                    getClientFrame().showHomeScreen(user);
                }
            }
        });
        timer.start();
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

                    Point startPoint = SwingUtilities.convertPoint(sourcePanel, label.getLocation(), SoloScreen.this);
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
                    if (movesCount >= 2) {
                        JOptionPane.showMessageDialog(SoloScreen.this, "Bạn đã xếp tối đa 2 lần!");
                        returnItemToOriginalPosition((Product) draggedLabel.getClientProperty("product"));
                        remove(draggedLabel);
                        repaint();
                        draggedLabel = null;
                        return;
                    }

                    boolean placed = false;
                    Product draggedProduct = (Product) draggedLabel.getClientProperty("product");

                    // Kiểm tra vị trí thả sản phẩm
                    for (int i = 0; i < shelfPanels.length; i++) {
                        for (int j = 0; j < shelfSlots[i].length; j++) {
                            if (isWithinSlot(e, shelfSlots[i][j])) {
                                // Kiểm tra nếu sản phẩm đặt vào đúng thứ tự
                                if (correctOrder.get(i * 3 + j).equals(draggedProduct)) {
                                    placeItemInSlot(shelfSlots[i][j], draggedLabel, draggedProduct);
                                } else {
                                    // Nếu sai, trả sản phẩm về vị trí cũ
                                    returnItemToOriginalPosition(draggedProduct);
                                }
                                movesCount++;
                                placed = true;
                                break;
                            }
                        }
                        if (placed) break;
                    }

                    // Kiểm tra xem sản phẩm có được đặt trên sàn không
                    if (!placed) {
                        for (JLabel floorSlot : floorSlots) {
                            if (isWithinSlot(e, floorSlot)) {
                                placeItemInSlot(floorSlot, draggedLabel, draggedProduct);
                                placed = true;
                                break;
                            }
                        }
                    }

                    // Nếu không đặt vào đâu được, trả sản phẩm về vị trí cũ
                    if (!placed) {
                        returnItemToOriginalPosition(draggedProduct);
                    }

                    remove(draggedLabel);
                    repaint();
                    draggedLabel = null;
                }
            }
        });

        label.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedLabel != null) {
                    Point point = SwingUtilities.convertPoint(label, e.getPoint(), SoloScreen.this);
                    int x = point.x - initialClick.x;
                    int y = point.y - initialClick.y;
                    draggedLabel.setLocation(x, y);
                    repaint();
                }
            }
        });
    }

    // Phương thức trả sản phẩm về vị trí cũ
    private void returnItemToOriginalPosition(Product draggedProduct) {
        if (originShelfIndex == -1) {
            floorSlots[originSlotIndex].setIcon(draggedLabel.getIcon());
            floorSlots[originSlotIndex].putClientProperty("product", draggedProduct);
        } else {
            shelfSlots[originShelfIndex][originSlotIndex].setIcon(draggedLabel.getIcon());
            shelfSlots[originShelfIndex][originSlotIndex].putClientProperty("product", draggedProduct);
        }
    }

    // Đặt sản phẩm vào ô
    private void placeItemInSlot(JLabel slot, JLabel item, Product product) {
        slot.setIcon(item.getIcon());
        slot.putClientProperty("product", product);
    }

    // Kiểm tra nếu sản phẩm nằm trong ô
    private boolean isWithinSlot(MouseEvent e, JLabel slot) {
        Rectangle slotBounds = SwingUtilities.convertRectangle(slot.getParent(), slot.getBounds(), SoloScreen.this);
        Point mouseLocation = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), SoloScreen.this);
        return slotBounds.contains(mouseLocation);
    }

    // Phương thức lấy Client (JFrame) cha
    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
