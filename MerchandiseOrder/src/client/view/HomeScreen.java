package client.view;

import client.controller.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreen extends JPanel {

    public HomeScreen() {
        JLabel lobbyLabel = new JLabel("Welcome to the Client.controller.Client Lobby");
        this.add(lobbyLabel);

        JButton question = new JButton("Question");
        question.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showQuestionScreen();
            }
        });
        this.add(question);
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
