package client.view;

import client.controller.ClientSocket;
import model.User;
import model.UserStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
public class GameRoomInvitationScreen extends JPanel {
    private JLabel currentUserAvatar;
    private JLabel currentUserName;
    private JLabel invitedUserAvatar;
    private JLabel invitedUserName;
    private JButton inviteButton;
    private JButton homeButton;
    private JButton playButton;
    private JButton removeUserButton;
    private boolean isUserInvited = false;
    private User user;
    private ClientSocket clientSocket;

    public GameRoomInvitationScreen(User user) {
        this.user = user;
        this.clientSocket = ClientSocket.getInstance();
        setSize(400, 600);
        setLayout(new BorderLayout());

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(173, 216, 230)); // Light blue background

        // Home button
        homeButton = new JButton("Home");
        homeButton.setBounds(300, 20, 80, 30);
        mainPanel.add(homeButton);

        // Current user's avatar and name
        currentUserAvatar = new JLabel(loadImageFromURL("https://th.bing.com/th/id/OIP.xyVi_Y3F3YwEIKzQm_j_jQHaHa?rs=1&pid=ImgDetMain"));
        currentUserAvatar.setBounds(90, 150, 50, 50);
        mainPanel.add(currentUserAvatar);

        currentUserName = new JLabel(user.getUsername());
        currentUserName.setHorizontalAlignment(SwingConstants.CENTER);
        currentUserName.setBounds(80, 210, 70, 20);
        mainPanel.add(currentUserName);

        // "VS" ở giữa
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        vsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        vsLabel.setBounds(175, 175, 50, 20);
        mainPanel.add(vsLabel);

        // Avatar và tên của người dùng được mời (ban đầu ẩn)
        invitedUserAvatar = new JLabel();
        invitedUserAvatar.setBounds(260, 150, 50, 50);
        invitedUserAvatar.setVisible(false); // Ban đầu ẩn
        mainPanel.add(invitedUserAvatar);

        invitedUserName = new JLabel();
        invitedUserName.setHorizontalAlignment(SwingConstants.CENTER);
        invitedUserName.setBounds(250, 210, 70, 20);
        invitedUserName.setVisible(false); // Ban đầu ẩn
        mainPanel.add(invitedUserName);

        // Nút "X" để xóa người dùng được mời (ban đầu ẩn)
        removeUserButton = new JButton("x");
        removeUserButton.setBounds(290, 130, 40, 20);
        removeUserButton.setVisible(false); // Ban đầu ẩn
        removeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invitedUserName.setVisible(false);
                invitedUserAvatar.setVisible(false);
                inviteButton.setVisible(true); // Hiện nút +
                isUserInvited = false;
                playButton.setEnabled(false);
                removeUserButton.setVisible(false);
            }
        });
        mainPanel.add(removeUserButton);

        // Nút Invite User (ban đầu hiện)
        inviteButton = new JButton("+");
        inviteButton.setBounds(270, 175, 50, 30);
        inviteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInvitationPopup();
            }
        });
        mainPanel.add(inviteButton);

        // Nút Play ở dưới
        playButton = new JButton("Play");
        playButton.setBounds(150, 400, 100, 40);
        playButton.setEnabled(false); // Chỉ khả dụng khi có người dùng được mời
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUserInvited) {
                    JOptionPane.showMessageDialog(null, "Game Started!");
                }
            }
        });
        mainPanel.add(playButton);

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
        for (User onlineUser : onlineUsers) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BorderLayout());
            userPanel.setPreferredSize(new Dimension(280, 50)); // Kích thước cố định cho mỗi hàng

            JLabel userAvatar = new JLabel(loadImageFromURL("https://th.bing.com/th/id/OIP.xyVi_Y3F3YwEIKzQm_j_jQHaHa?rs=1&pid=ImgDetMain"));
            userAvatar.setPreferredSize(new Dimension(50, 50)); // Kích thước ảnh avatar
            JLabel userName = new JLabel(onlineUser.getUsername());
            userName.setHorizontalAlignment(SwingConstants.LEFT);

            JButton inviteUserButton = new JButton("Invite");
            inviteUserButton.setPreferredSize(new Dimension(80, 30)); // Kích thước nút Invite
            inviteUserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    invitedUserName.setText(onlineUser.getUsername());
                    ClientSocket.getInstance().sendInvite(onlineUser, user);
                    invitedUserAvatar.setIcon(loadImageFromURL("https://th.bing.com/th/id/OIP.xyVi_Y3F3YwEIKzQm_j_jQHaHa?rs=1&pid=ImgDetMain"));
                    invitedUserName.setVisible(true); // Hiện tên người dùng được mời
                    invitedUserAvatar.setVisible(true); // Hiện avatar người dùng được mời
                    inviteButton.setVisible(false); // Ẩn nút +
                    isUserInvited = true;
                    playButton.setEnabled(true);
                    removeUserButton.setVisible(true); // Hiện nút x
                    popup.dispose();
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
}
