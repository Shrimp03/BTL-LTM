package Client.View;

import Model.Product;
import Model.User;

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

    private BufferedImage backgroundImage;
    private JLabel draggedLabel = null;
    private Point initialClick;
    private final JPanel[] shelfPanels;
    private final JLabel[] floorSlots;
    private final JLabel[][] shelfSlots;
    private int originSlotIndex;
    private int originShelfIndex;
    private final ArrayList<Product> products;

    public PlayScreen(User user, ArrayList<Product> products) {
        this.products = products;
        ArrayList<Product> shuffledProducts = new ArrayList<>(products);

        Collections.shuffle(shuffledProducts);

        loadBackgroundImage();
        setLayout(null);

        // Tạo và sắp xếp các kệ
        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][3];
        int[] xPositions = {2, 205, 2, 205};
        int[] yPositions = {230, 230, 305, 305};

        for (int i = 0; i < shelfPanels.length; i++) {
            shelfPanels[i] = createShelfPanel(xPositions[i], yPositions[i], i);
            add(shelfPanels[i]);
        }

        // Tạo và sắp xếp sàn
        floorSlots = new JLabel[12];
        JPanel floorPanel = createFloorPanel(shuffledProducts);
        add(floorPanel);
    }

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

    private JPanel createFloorPanel(ArrayList<Product> shuffledProducts) {
        JPanel floorPanel = new JPanel();
        floorPanel.setBounds(50, 500, 270, 100);
        floorPanel.setLayout(new GridLayout(2, 6, 5, 5));
        floorPanel.setOpaque(false);

        for (int i = 0; i < floorSlots.length; i++) {
            floorSlots[i] = createSlotLabel();
            floorPanel.add(floorSlots[i]);
            if (i < shuffledProducts.size()) {
                ImageIcon icon = createIconFromText(shuffledProducts.get(i).getName());
                if (icon != null) {
                    floorSlots[i].setIcon(icon);
                    floorSlots[i].putClientProperty("product", shuffledProducts.get(i)); // Gắn product vào label
                    addDragAndDropFunctionality(floorSlots[i], floorPanel, i, -1);
                } else {
                    System.out.println("Failed to load image for product at index: " + i);
                }
            }
        }
        return floorPanel;
    }

    private static ImageIcon createIconFromText(String text) {
        int width = 50;
        int height = 50;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setColor(Color.BLACK);

        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);

        g2d.dispose();

        return new ImageIcon(image);
    }

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

    private void addDragAndDropFunctionality(JLabel label, JPanel sourcePanel, int slotIndex, int shelfIndex) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (label.getIcon() != null) {
//                    System.out.println(label.getClientProperty("product"));
                    draggedLabel = createSlotLabel();
                    draggedLabel.setIcon(label.getIcon());
                    draggedLabel.putClientProperty("product", label.getClientProperty("product")); // Gắn product vào draggedLabel

                    initialClick = e.getPoint();

                    label.setIcon(null);
                    label.putClientProperty("product", null); // Xóa product khỏi label gốc

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

            if (shelf.equals(this.products)) {
                System.out.println("All shelves filled");
            }

        }
    }

    private boolean isWithinSlot(MouseEvent e, JLabel slot) {
        Rectangle slotBounds = SwingUtilities.convertRectangle(slot.getParent(), slot.getBounds(), PlayScreen.this);
        Point mouseLocation = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), PlayScreen.this);
        return slotBounds.contains(mouseLocation);
    }

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

    private JLabel createSlotLabel() {
        JLabel slotLabel = new JLabel();
        slotLabel.setHorizontalAlignment(JLabel.CENTER);
        slotLabel.setVerticalAlignment(JLabel.CENTER);
        slotLabel.setPreferredSize(new Dimension(50, 50));
        return slotLabel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        User user = new User(1, "testUser", "password", "test@example.com", "10 20 30", "C:/full/path/to/your/image/avatar.png");

        ArrayList<Product> products = new ArrayList<>();
        String[] urls = {
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/anh-do-an-doc-dao-dep-nhat_095145309.jpg",
                "https://th.bing.com/th/id/OIP.r0X-CYZvvYM71ZdNU4LXQQHaE8?pid=ImgDet&w=474&h=316&rs=1",
                "https://th.bing.com/th/id/OIP.2dpZ2lPu39T_WCBOZEOZFgHaI7?pid=ImgDet&w=474&h=571&rs=1",
                "https://phunugioi.com/wp-content/uploads/2022/02/Anh-Do-An-Cute-2.jpg",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-cuc-de-thuong_095148496.png",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-picnic-de-thuong_095150617.jpg",
                "https://img6.thuthuatphanmem.vn/uploads/2022/11/17/anh-chibi-cute_014001732.png",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-cute-de-thuong_095148049.jpg",
                "https://img.lovepik.com/free-png/20210924/lovepik-cute-jane-food-png-image_401296684_wh1200.png",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-ngo-nghinh-dep-nhat_095150416.jpg",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-cute_095148385.jpg",
                "https://img.lovepik.com/free-png/20210927/lovepik-school-supplies-school-pencil-ruler-png-image_401534722_wh1200.png"
        };

        for (int i = 0; i < urls.length; ++i) {
            products.add(new Product(products.size(), "" + (i + 1), urls[i]));
        }

        JFrame frame = new JFrame("PlayScreen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 685);
        frame.setLocationRelativeTo(null);

        PlayScreen playScreen = new PlayScreen(user, products);
        frame.add(playScreen);
        frame.setVisible(true);
    }
}
