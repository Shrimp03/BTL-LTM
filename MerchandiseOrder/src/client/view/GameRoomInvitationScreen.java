package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameRoomInvitationScreen extends JPanel implements GamePlayListener {
    private JLabel currentUserAvatar;
    private JLabel currentUserName;
    private JLabel invitedUserAvatar;
    private JLabel invitedUserName;
    private JButton inviteButton;
    private JButton homeButton;
    private JButton playButton;
    private boolean isUserInvited = false;
    private User user;
    private Timer inviteTimer;
    private User onlineUser;
    private Boolean isOnlineUser ;
    private User inviteUser;
    private Image backgroundImage;

    public GameRoomInvitationScreen(User user, Boolean isOnlineUser, User inviteUser) {
        this.isOnlineUser = isOnlineUser;
        this.user = user;
        this.inviteUser = inviteUser;
        setSize(400, 600);
        setLayout(new BorderLayout());

        ClientSocket.getInstance().addGamePlayListener(this, user);
        loadBackgroundImage();
        initializeUI();
        setVisible(true);
    }


    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false);

//        mainPanel.setBackground(new Color(173, 216, 230)); // Light blue background

        // Home button
        homeButton = new JButton();
        homeButton.setBounds(10, 20, 50, 30);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorder(null);
        homeButton.setOpaque(false);
        mainPanel.add(homeButton);

        homeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showHomeScreen(user);
            }
        });

        // Current user's avatar and name
        currentUserAvatar = new JLabel(loadImageFromURL("https://th.bing.com/th/id/OIP.xyVi_Y3F3YwEIKzQm_j_jQHaHa?rs=1&pid=ImgDetMain"));
        currentUserAvatar.setBounds(155, 180, 60, 60);
        mainPanel.add(currentUserAvatar);

        currentUserName = new JLabel(user.getUsername());
        currentUserName.setHorizontalAlignment(SwingConstants.CENTER);
        currentUserName.setBounds(150, 240, 70, 20);
        mainPanel.add(currentUserName);


        // Avatar và tên của người dùng được mời (ban đầu ẩn)
        invitedUserAvatar = new JLabel();
        invitedUserAvatar.setBounds(165, 340, 60, 60);
        invitedUserAvatar.setVisible(true);
        mainPanel.add(invitedUserAvatar);

        invitedUserName = new JLabel();
        invitedUserName.setHorizontalAlignment(SwingConstants.CENTER);
        invitedUserName.setBounds(155, 400, 70, 20);
        invitedUserName.setVisible(true); // Ban đầu ẩn
        mainPanel.add(invitedUserName);


        // Nút Invite User (ban đầu hiện)
        inviteButton = new JButton("+");
        inviteButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        inviteButton.setBorder(null);
        inviteButton.setContentAreaFilled(false);
        inviteButton.setOpaque(false);
        inviteButton.setBounds(150, 350, 50, 50);
        inviteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInvitationPopup();
            }
        });
        mainPanel.add(inviteButton);

        // Nút Play ở dưới
        playButton = new JButton();
        playButton.setBounds(140, 580, 100, 40);
        playButton.setContentAreaFilled(false);
        playButton.setBorder(null);
        playButton.setEnabled(false); // Chỉ khả dụng khi có người dùng được mời
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUserInvited) {
                    List<User> twoUser = new ArrayList<User>();
                    twoUser.add(getOnlineUser());
                    twoUser.add(user);
                    ClientSocket.getInstance().sendPlay("PLAY", twoUser);
                    JOptionPane.showMessageDialog(null, "Game Started!");
                }
            }
        });
        mainPanel.add(playButton);

        if(isOnlineUser){
            invitedUserAvatar.setIcon(loadImageFromURL("https://th.bing.com/th/id/OIP.xyVi_Y3F3YwEIKzQm_j_jQHaHa?rs=1&pid=ImgDetMain"));// Ban đầu ẩn
            invitedUserName.setText(inviteUser.getUsername());
            inviteButton.setVisible(false);
        }

        add(mainPanel);
    }

    private void showInvitationPopup() {
        JDialog popup = new JDialog(JOptionPane.getFrameForComponent(this), "Invite User", true);
        popup.setSize(300, 400);
        popup.setLocationRelativeTo(this);

        // Panel chứa danh sách người dùng với chức năng cuộn
        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));

        List<User> onlineUsers = ClientSocket.getInstance().getUsersByStatus(user.getUsername(), String.valueOf(UserStatus.ONLINE));
        if( onlineUsers == null || onlineUsers.size() == 0 ) {
            JOptionPane.showMessageDialog(null, "Hiện không có người chơi nào trực tuyến!");
            return;
        }
        for (User onlineUser : onlineUsers) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BorderLayout());
            userPanel.setPreferredSize(new Dimension(280, 50)); // Kích thước cố định cho mỗi hàng

            JLabel userAvatar = new JLabel(loadImageFromURL("https://th.bing.com/th/id/OIP.xyVi_Y3F3YwEIKzQm_j_jQHaHa?rs=1&pid=ImgDetMain"));
            userAvatar.setPreferredSize(new Dimension(50, 50)); // Kích thước ảnh avatar
            JLabel userName = new JLabel(onlineUser.getUsername());
            userName.setHorizontalAlignment(SwingConstants.LEFT);

            JButton inviteUserButton = new JButton("Mời");
            inviteUserButton.setPreferredSize(new Dimension(80, 30)); // Kích thước nút Invite
            inviteUserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    invitedUserName.setText(onlineUser.getUsername());
                    invitedUserAvatar.setIcon(loadImageFromURL("https://th.bing.com/th/id/OIP.xyVi_Y3F3YwEIKzQm_j_jQHaHa?rs=1&pid=ImgDetMain"));
                    ClientSocket.getInstance().sendInvite(onlineUser, user);
                    playButton.setEnabled(true);
                    inviteTimer = new Timer();
                    inviteTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Boolean accepted = ClientSocket.getInstance().getAccepted();
                                    if (accepted) {
                                        popup.dispose();
                                        invitedUserName.setVisible(true);
                                        invitedUserAvatar.setVisible(true);
                                        inviteButton.setVisible(false);
                                        setOnlineUser(onlineUser);
                                        isUserInvited = true;
                                    } else {
                                        invitedUserName.setVisible(false);
                                        invitedUserAvatar.setVisible(false);
                                        inviteButton.setVisible(true);
                                        isUserInvited = false;
                                        JOptionPane.showMessageDialog(null, "Người chơi không chấp nhận lời mời!");
                                    }
                                }
                            });
                        }
                    }, 5000); // Dela
                }
            });

            userPanel.add(userAvatar, BorderLayout.WEST);
            userPanel.add(userName, BorderLayout.CENTER);
            userPanel.add(inviteUserButton, BorderLayout.EAST);
            popupPanel.add(userPanel);
        }

        JScrollPane scrollPane = new JScrollPane(popupPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        popup.add(scrollPane);
        popup.setVisible(true);
    }

    // Phương thức để tải ảnh từ URL và chỉnh sửa kích thước
    private ImageIcon loadImageFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            ImageIcon imageIcon = new ImageIcon(url);
            Image image = imageIcon.getImage(); // Lấy Image từ ImageIcon
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Thay đổi kích thước ảnh
            return new ImageIcon(scaledImage); // Trả về ImageIcon đã chỉnh sửa kích thước
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức tải hình nền
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/static/pvp.png");
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

    private void setOnlineUser(User onlineUser){
        this.onlineUser = onlineUser;
    }

    public User getOnlineUser() {
        return onlineUser;
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }

    @Override
    public void onPlay(GameSession gameSession, User startingPlayer, Product[] products) {

        Pair<Integer, String> pair = new Pair<>(user.getId(), String.valueOf(UserStatus.PLAYING));
        Boolean updateStatusUser = ClientSocket.getInstance().updateStatusUser("UpdateStatusUser", pair);
        System.out.println(updateStatusUser);
        getClientFrame().showQuestionScreenSolo(user, gameSession, startingPlayer, products);
    }
}
