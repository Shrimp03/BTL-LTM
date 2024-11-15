package client.view;

import client.controller.ClientSocket;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PopupInvite {

    private static GameInvitationListener gameInvitationListener;

    public void setGameInvitationListener(GameInvitationListener listener) {
        this.gameInvitationListener = listener;
    }

    public PopupInvite() {
    }

    public static void showInvitationDialog(User currentUser, User inviter) {
        final JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Game Invitation");

        // Set preferred size for the dialog
        dialog.setPreferredSize(new Dimension(400, 250));
        dialog.setMinimumSize(new Dimension(400, 250));  // Ensure minimum size

        // Create a panel with a dark background color
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 231, 202)); // Dark background for contrast
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Invitation message
        JTextArea messageArea = new JTextArea(inviter.getUsername() + " đã mời bạn tham gia trò chơi. Bạn có muốn tham gia?");
        messageArea.setFont(new Font("Arial", Font.BOLD, 14));
        messageArea.setForeground(Color.BLACK); // Darker text for readability
        messageArea.setBackground(new Color(245, 231, 202)); // Match panel background color
        messageArea.setLineWrap(true);  // Enable line wrapping
        messageArea.setWrapStyleWord(true);  // Wrap at word boundaries
        messageArea.setEditable(false);  // Make it non-editable
        messageArea.setFocusable(false);  // Remove focus for better UI experience
        messageArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        panel.add(messageArea);

        // Countdown label
        JLabel countdownLabel = new JLabel("Thời gian: 5 s");
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 18));
        countdownLabel.setForeground(new Color(184, 134, 11)); // Gold color for visibility
        countdownLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        countdownLabel.setPreferredSize(new Dimension(360, 50));
        panel.add(countdownLabel);

        // Accept and Decline buttons with themed colors
        JButton acceptButton = new JButton("Chấp nhận");
        JButton declineButton = new JButton("Từ chối");

        // Set button colors to match the game theme
        acceptButton.setBackground(new Color(34, 139, 34)); // Green for "Accept"
        acceptButton.setForeground(Color.WHITE); // White text for contrast
        acceptButton.setFont(new Font("Arial", Font.BOLD, 14));
        declineButton.setBackground(new Color(178, 34, 34)); // Red for "Decline"
        declineButton.setForeground(Color.WHITE);
        declineButton.setFont(new Font("Arial", Font.BOLD, 14));

        final boolean[] hasResponded = {false};
        Timer countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {
            int secondsLeft = 5;

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    countdownLabel.setText("Thời gian: " + secondsLeft + " s");
                    secondsLeft--;
                } else {
                    if (!hasResponded[0]) {
                        hasResponded[0] = true;
                        List<User> twoUsers = new ArrayList<>();
                        twoUsers.add(currentUser);
                        twoUsers.add(inviter);
                        ClientSocket.getInstance().sendAcceptInvite("DECLINE", twoUsers);
                        dialog.dispose();
                    }
                    countdownTimer.cancel();
                }
            }
        }, 0, 1000);

        acceptButton.addActionListener(e -> {
            hasResponded[0] = true;
            countdownTimer.cancel();
            dialog.dispose();
            List<User> twoUsers = new ArrayList<>();
            twoUsers.add(currentUser);
            twoUsers.add(inviter);

            if (gameInvitationListener != null) {
                gameInvitationListener.onInvitationReceived(true, currentUser, inviter);
            }
            ClientSocket.getInstance().sendAcceptInvite("ACCEPT", twoUsers);
        });

        declineButton.addActionListener(e -> {
            hasResponded[0] = true;
            countdownTimer.cancel();
            dialog.dispose();
            List<User> twoUsers = new ArrayList<>();
            twoUsers.add(currentUser);
            twoUsers.add(inviter);

            ClientSocket.getInstance().setAccepted(false);
            ClientSocket.getInstance().sendAcceptInvite("DECLINE", twoUsers);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 231, 202)); // Match background color
        buttonPanel.add(acceptButton);
        buttonPanel.add(declineButton);

        panel.add(buttonPanel);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
