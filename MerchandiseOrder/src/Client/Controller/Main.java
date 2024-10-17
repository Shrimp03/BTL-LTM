package Client.Controller;

import Client.View.HomeScreen;
import Client.View.PlayScreen;
import Client.View.QuestionScreen;
import Client.View.RankingScreen;
import Model.ClientData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Danh sách client cho bảng xếp hạng
    private List<ClientData> clientRanking;

    public Main() {
        setTitle("Multi-site Application");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tạo dữ liệu cho bảng xếp hạng client
        clientRanking = new ArrayList<>();
        clientRanking.add(new ClientData("Client.Controller.Client 1", 100));
        clientRanking.add(new ClientData("Client.Controller.Client 2", 200));
        clientRanking.add(new ClientData("Client.Controller.Client 3", 150));

        // Tạo các site từ các file bên ngoài
        HomeScreen lobbyPanel = new HomeScreen();  // Site Sảnh
        RankingScreen rankingPanel = new RankingScreen(clientRanking);
//        PlayScreen playScreen = new PlayScreen();
        QuestionScreen questionScreen = new QuestionScreen();
        // Site Bảng xếp hạng

        // Thêm các site vào CardLayout
        mainPanel.add(lobbyPanel, "Lobby");
        mainPanel.add(rankingPanel, "Ranking");
//        mainPanel.add(playScreen, "Play");
        mainPanel.add(questionScreen, "Question");

        // Tạo thanh điều hướng
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Sites");
        JMenuItem goToLobbyItem = new JMenuItem("Go to Lobby");
        JMenuItem goToRankingItem = new JMenuItem("Go to Client.Controller.Client Ranking");
        JMenuItem goToPlayItem = new JMenuItem("Go to Play");
        JMenuItem goToQuestionItem = new JMenuItem("Go to Question");

        // Gắn hành động chuyển đổi site
        goToLobbyItem.addActionListener(e -> cardLayout.show(mainPanel, "Lobby"));
        goToRankingItem.addActionListener(e -> cardLayout.show(mainPanel, "Ranking"));
        goToPlayItem.addActionListener(e -> cardLayout.show(mainPanel, "Play"));
        goToQuestionItem.addActionListener(e -> cardLayout.show(mainPanel, "Question"));


        menu.add(goToLobbyItem);
        menu.add(goToRankingItem);
        menu.add(goToPlayItem);
        menu.add(goToQuestionItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    // Lớp Model.ClientData để lưu thông tin client và điểm số

}
