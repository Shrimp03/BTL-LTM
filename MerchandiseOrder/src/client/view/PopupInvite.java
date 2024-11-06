package client.view;

import client.controller.ClientSocket;
import model.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PopupInvite {

    public static void showInvitationDialog(User inviter, User currentUser) {
        int option = JOptionPane.showOptionDialog(
                null, // Chỉ định null để popup ở giữa màn hình
                inviter.getUsername() + " has invited you to join a game. Do you accept?",
                "Game Invitation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Accept", "Decline"},
                "Decline"
        );

        System.out.println(inviter.getUsername());
        System.out.println( currentUser.getUsername());
        // Xử lý lựa chọn của người dùng
        String responseType = (option == JOptionPane.YES_OPTION) ? "ACCEPT" : "DECLINE";

        ClientSocket clientSocket = ClientSocket.getInstance();

        if (responseType.equals("ACCEPT")) {
            List<User> twoUsers = new ArrayList<>();
            twoUsers.add(inviter);
            twoUsers.add(currentUser);
//            clientSocket.sendResponse(responseType, twoUsers);
        } else {
//            clientSocket.sendResponse(responseType, null);
        }
    }


}
