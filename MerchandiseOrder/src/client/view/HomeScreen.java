package client.view;

import client.controller.Client;
import model.Product;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HomeScreen extends JPanel {
    private User user;
    private ArrayList<Product> products;

    public HomeScreen() {
        this.user = new User(5, "user5", "password123", "user5@example.com", "10 20 30 40", "C:/full/path/to/your/image/avatar.png");
        JLabel lobbyLabel = new JLabel("Welcome to the Client.controller.Client Lobby");
        this.add(lobbyLabel);

        JButton question = new JButton("Question");
        question.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getClientFrame().showQuestionScreen(user);
            }
        });
        this.add(question);
    }

    private Client getClientFrame() {
        return (Client) SwingUtilities.getWindowAncestor(this);
    }
}
