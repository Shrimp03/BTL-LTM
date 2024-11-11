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
        // Biến lưu trạng thái lựa chọn của người dùng
        final boolean[] hasResponded = {false};

        // Tạo một Timer để đếm ngược 5 giây
        Timer declineTimer = new Timer();
        declineTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!hasResponded[0]) {
                    // Người dùng không phản hồi sau 5 giây, tự động gửi "DECLINE"
                    ClientSocket.getInstance().sendAcceptInvite("DECLINE", null);
                }
                declineTimer.cancel();
            }
        }, 5000); // Thời gian chờ 5 giây

        int option = JOptionPane.showOptionDialog(
                null,
                inviter.getUsername() + " đã mời bạn tham gia game. Bạn có muốn tham gia?",
                "Game Invitation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Chấp nhận", "Từ chối"},
                "Chấp nhận"
        );

        // Khi người dùng chọn, hủy timer và xử lý phản hồi
        declineTimer.cancel();
        hasResponded[0] = true;

        String responseType = (option == JOptionPane.YES_OPTION) ? "ACCEPT" : "DECLINE";
        System.out.println(responseType);

        List<User> twoUsers = new ArrayList<>();
        twoUsers.add(currentUser);
        twoUsers.add(inviter);

        if ("ACCEPT".equals(responseType)) {
//            ClientSocket.getInstance().setAccepted(true);
            // Nếu chọn chấp nhận, thông báo cho Client và gửi mời
            if (gameInvitationListener != null) {
                gameInvitationListener.onInvitationReceived(true, currentUser);
            }
            ClientSocket.getInstance().sendAcceptInvite(responseType, twoUsers);
        } else {
            // Nếu chọn từ chối, thông báo từ chối và gửi yêu cầu
            ClientSocket.getInstance().setAccepted(false);
            ClientSocket.getInstance().sendAcceptInvite(responseType, twoUsers);
        }
    }
}
