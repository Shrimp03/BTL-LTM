package client.controller;

import client.view.*;
import model.Product;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JFrame {
    protected static final String serverHost = "localhost";
    protected static final int serverPort = 12345;
    protected static Socket socket;
    protected static ObjectInputStream ois;
    protected static ObjectOutputStream oos;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private QuestionScreen questionScreen; // TODO: sau sửa lại

    public Client() {
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);
        this.questionScreen = new QuestionScreen();

        cardPanel.add(questionScreen, "QuestionScreen");
        this.add(cardPanel);

        setTitle("Merchandise Order");
        setSize(385, 685);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showPlayScreen(User user, ArrayList<Product> products) {
        PlayScreen playScreen = new PlayScreen(user, products);
        cardPanel.add(playScreen, "PlayScreen");
        cardLayout.show(cardPanel, "PlayScreen");
    }

    public void showQuestionScreen() { // TODO: Sau sửa: thêm tham số user
        QuestionScreen questionScreen = new QuestionScreen();
        cardPanel.add(questionScreen, "QuestionScreen");
        cardLayout.show(cardPanel, "QuestionScreen");
    }

    public void showHomeScreen() { // TODO: Sau sửa: thêm tham số user
        HomeScreen homeScreen = new HomeScreen();
        cardPanel.add(homeScreen, "HomeScreen");
        cardLayout.show(cardPanel, "HomeScreen");
    }

    public static void connectToServer() {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(serverHost, serverPort);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("Connected to server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (ois != null) ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (oos != null) oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (socket != null) socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            connectToServer();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(Client::closeConnection));
    }
}