package client.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

public class RuleScreen extends JPanel {
    private Image backgroundImage1;
    private Image backgroundImage;
    private JButton backButton;
    private User currentUser;
    private ClientSocket clientSocket;
    private JButton PvP;
    private JButton singlePlayer;

    public RuleScreen(User currentUser ) {
        this.currentUser = currentUser;




        // Tải ảnh nền
        loadBackgroundImage();
        if (backgroundImage != null) {
            System.out.println("Background image loaded successfully.");
        } else {
            System.out.println("Failed to load background image.");
        }


        // Cài đặt giao diện
        setLayout(null);


        // Tạo nút quay lại
        backButton = new JButton("Trang chủ");
        backButton.setBounds(0, 0, 102, 32);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(252, 218, 134));
        backButton.setForeground(Color.BLACK);

        setComponentZOrder(backButton, 0);
        add(backButton);
        backButton.addActionListener(e -> {
            // Quay lại màn hình chính
            getClientFrame().showHomeScreen(currentUser);
        });

        //Tạo nút "PvP"
        PvP = new JButton("PvP");
        PvP.setBounds(269, 0, 102, 32);
        PvP.setFont(new Font("Arial", Font.BOLD, 14));
        PvP.setBackground(new Color(252, 218, 134));
        PvP.setForeground(Color.BLACK);
        add(PvP);
        PvP.addActionListener(e -> {
            PvP.setBackground(new Color(252,14,28));
            singlePlayer.setBackground(new Color(252, 218, 134));
            loadBackgroundImage1();
            repaint();

        });

        //Tạo nút "singlePlayer"
        singlePlayer = new JButton("1 Player");
        singlePlayer.setBounds(135, 0, 102, 32);
        singlePlayer.setFont(new Font("Arial", Font.BOLD, 14));
        singlePlayer.setBackground(new Color(252,14,28));
        singlePlayer.setForeground(Color.BLACK);
        add(singlePlayer);
        singlePlayer.addActionListener(e -> {
            singlePlayer.setBackground(new Color(252,14,28));
            PvP.setBackground(new Color(252, 218, 134));
            loadBackgroundImage();
            repaint();
        });

    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/2.jpg");
            if (imgStream != null) {
                backgroundImage = ImageIO.read(imgStream);
            } else {
                System.out.println("Background image not found, proceeding without it.");
            }
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }
    }


    private void loadBackgroundImage1() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/3.jpg");
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

    // Phương thức lấy Client (JFrame) cha
    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
