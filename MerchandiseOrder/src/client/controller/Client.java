package client.controller;

import client.view.*;
import model.DataTransferObject;
import model.GameSession;
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
    private ClientSocket clientSocket;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private QuestionScreen questionScreen; // TODO: sau sửa lại

    private User currentUser;
    public Client() {
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);

        LoginScreen loginScreen = new LoginScreen();
        RegisterScreen registerScreen = new RegisterScreen();
        cardPanel.add(loginScreen, "LoginScreen");
        cardPanel.add(registerScreen, "RegisterScreen");

        this.add(cardPanel);

        setTitle("Merchandise Order");
        setSize(385, 685);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        showLoginScreen();
    }

    public void startListening() {
        clientSocket = ClientSocket.getInstance();
        clientSocket.listening(); // Gọi phương thức lắng nghe broadcast
    }

    // Thêm phương thức điều hướng giữa các màn hình
    public void showLoginScreen() {
        cardLayout.show(cardPanel, "LoginScreen");
    }

    public void showRegisterScreen() {
        cardLayout.show(cardPanel, "RegisterScreen");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void showHomeScreen(User user) {
        HomeScreen homeScreen = new HomeScreen(user);  // Truyền đối tượng Client để lấy thông tin người dùng
        cardPanel.add(homeScreen, "HomeScreen");
        cardLayout.show(cardPanel, "HomeScreen");
    }


    // Chuyển sang màn hình "Bảng xếp hạng"
    public void showRankingScreen(User user) {
        RankingScreen rankingScreen = new RankingScreen(user);
        cardPanel.add(rankingScreen, "RuleScreen");
        cardLayout.show(cardPanel, "RuleScreen");
    }

    //Chuyển sang màn hình "Luật chơi"

    public void showRuleScreen(User user) {
        RuleScreen ruleScreen = new RuleScreen(user);
        cardPanel.add(ruleScreen, "RuleScreen");
        cardLayout.show(cardPanel, "RuleScreen");
    }

    //chuyen man hinh " profile"
    public void showProfileScreen(User user) {
        UpdateUser updateUser = new UpdateUser(user);
        cardPanel.add(updateUser, "UpdateUser");
        cardLayout.show(cardPanel, "UpdateUser");
    }

    // Chuyển sang màn hình "Phòng ra đề"


    public void showPlayScreen(User user, ArrayList<Product> products) {
        PlayScreen playScreen = new PlayScreen(user, products);
        cardPanel.add(playScreen, "PlayScreen");
        cardLayout.show(cardPanel, "PlayScreen");
    }

    public void showQuestionScreen(User user) { // TODO: Sau sửa: thêm tham số user
        QuestionScreen questionScreen = new QuestionScreen(user);
        cardPanel.add(questionScreen, "QuestionScreen");
        cardLayout.show(cardPanel, "QuestionScreen");
    }

    public void showSoloScreen(User user, GameSession gameSession, ArrayList<Product> products) {
        SoloScreen soloScreen = new SoloScreen(user, gameSession, products);
        cardPanel.add(soloScreen, "SoloScreen");
        cardLayout.show(cardPanel, "SoloScreen");
    }

    public void showInvitaionScreen(User user) {
        GameRoomInvitationScreen gameRoomInvitationScreen = new GameRoomInvitationScreen(user);
        cardPanel.add(gameRoomInvitationScreen, "GameRoomInvitationScreen");
        cardLayout.show(cardPanel, "GameRoomInvitationScreen");
    }



    public void showCreateRoomScreen(User user) {
        CreateRoomScreen createRoomScreen = new CreateRoomScreen(user); // Tạo màn hình tạo phòng
        cardPanel.add(createRoomScreen, "CreateRoomScreen"); // Thêm vào card panel
        cardLayout.show(cardPanel, "CreateRoomScreen"); // Hiển thị màn hình tạo phòng
    }

//    public void showSuggestScreen(User user) {
//        SuggestScreen suggestScreen = new SuggestScreen(user); // Tạo màn hình gợi ý
//        cardPanel.add(suggestScreen, "SuggestScreen"); // Thêm vào card panel
//        cardLayout.show(cardPanel, "SuggestScreen"); // Hiển thị màn hình gợi ý
//    }


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
            client.startListening();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(Client::closeConnection));
    }
}