package client.view;

import client.controller.ClientSocket;
import model.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PopupInvite {

    public static void showInvitationDialog(User inviter, User currentUser) {
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

        if ("ACCEPT".equals(responseType)) {
            List<User> twoUsers = new ArrayList<>();
            twoUsers.add(inviter);
            twoUsers.add(currentUser);
            ClientSocket.getInstance().sendAcceptInvite(responseType, twoUsers);
        } else {
            ClientSocket.getInstance().sendAcceptInvite(responseType, null);
        }
    }


}
