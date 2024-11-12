package client.view;

import client.controller.ClientSocket;
import model.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PopupInvite {

    private static GameInvitationListener gameInvitationListener;

    // Phương thức đăng ký listener
    public void setGameInvitationListener(GameInvitationListener listener) {
        this.gameInvitationListener = listener;
    }

    // Constructor
    public PopupInvite() {
        // Đảm bảo listener được set khi khởi tạo
//        this.setGameInvitationListener(Client.getInstance()); // Đăng ký Client là listener
    }

    public static void showInvitationDialog(User currentUser, User inviter) {
        // Create a dialog to contain the invitation message and countdown
        final JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Mời người chơi.");

        // Create a panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Label to display the invitation message
        JLabel messageLabel = new JLabel(inviter.getUsername() + " đã mời bạn tham gia. Bạn có muốn tham gia chơi?");
        messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(messageLabel);

        // Label to display countdown
        JLabel countdownLabel = new JLabel("Thời gian chờ: 5 s");
        countdownLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(countdownLabel);

        // Buttons for accepting or declining the invitation
        JButton acceptButton = new JButton("Chấp nhận");
        JButton declineButton = new JButton("Từ chối");

        // Track if the user has responded
        final boolean[] hasResponded = {false};

        // Timer for the countdown
        Timer countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {
            int secondsLeft = 5; // Countdown from 5 seconds

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    countdownLabel.setText("Thời gian chờ " + secondsLeft + " s");
                    secondsLeft--;
                } else {
                    // Time ran out, auto-decline and close dialog
                    if (!hasResponded[0]) {
                        hasResponded[0] = true;
                        ClientSocket.getInstance().sendAcceptInvite("DECLINE", null);
                        dialog.dispose();
                    }
                    countdownTimer.cancel();
                }
            }
        }, 0, 1000);

        // Action listeners for the buttons
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

        // Add buttons to a button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(declineButton);

        // Add button panel to the main panel
        panel.add(buttonPanel);

        // Add panel to dialog
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null); // Center the dialog on the screen
        dialog.setVisible(true);
    }


}
