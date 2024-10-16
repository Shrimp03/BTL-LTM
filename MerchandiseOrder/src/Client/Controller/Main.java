package Client.Controller;

import Client.View.HomeScreen;
import Client.View.PlayScreen;
import Client.View.QuestionScreen;
import Client.View.RankingScreen;
import Client.View.LoginScreen;
import Client.View.RegisterScreen;
import Client.Network.ClientSocket; // Nhập lớp ClientSocket
import Model.ClientData;
import Model.User; // Thêm import cho lớp User

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private ClientSocket clientSocket; // Thêm biến ClientSocket

    // Danh sách client cho bảng xếp hạng
    private List<ClientData> clientRanking;

    public Main() {
        setTitle("Multi-site Application");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Kết nối đến server
        try {
            clientSocket = new ClientSocket("localhost", 1234); // Kết nối tới server
        } catch (IOException e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tạo dữ liệu cho bảng xếp hạng client
        clientRanking = new ArrayList<>();
        clientRanking.add(new ClientData("Client.Controller.Client 1", 100));
        clientRanking.add(new ClientData("Client.Controller.Client 2", 200));
        clientRanking.add(new ClientData("Client.Controller.Client 3", 150));

        // Tạo các site từ các file bên ngoài
        HomeScreen homeScreen = new HomeScreen(this,clientSocket);  // Trang chính
        RankingScreen rankingPanel = new RankingScreen(clientRanking); // Trang bảng xếp hạng
        PlayScreen playScreen = new PlayScreen(); // Trang chơi game
        QuestionScreen questionScreen = new QuestionScreen(); // Trang câu hỏi
        RegisterScreen registerScreen = new RegisterScreen(this, clientSocket); // Thay đổi sang ClientSocket
        LoginScreen loginScreen = new LoginScreen(this, clientSocket); // Thay đổi sang ClientSocket

        // Thêm các site vào CardLayout
        mainPanel.add(loginScreen, "Login"); // Thêm trang đăng nhập
        mainPanel.add(registerScreen, "Register"); // Thêm trang đăng ký
        mainPanel.add(homeScreen, "Home"); // Trang chính
        mainPanel.add(rankingPanel, "Ranking"); // Trang bảng xếp hạng
        mainPanel.add(playScreen, "Play"); // Trang chơi game
        mainPanel.add(questionScreen, "Question"); // Trang câu hỏi

        add(mainPanel);
        setVisible(true);
    }

    // Phương thức để hiển thị lại màn hình đăng nhập
    public void showLoginScreen() {
        cardLayout.show(mainPanel, "Login"); // Chuyển đến giao diện đăng nhập
    }

    // Phương thức hiển thị giao diện chính khi đăng nhập thành công
    public void showMainScreen(String username) {
        // Cập nhật thông tin người dùng hoặc chuyển đến giao diện chính
        JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");
        cardLayout.show(mainPanel, "Home"); // Chuyển đến giao diện chính
    }

    // Phương thức để hiển thị giao diện đăng ký
    public void showRegisterScreen() {
        cardLayout.show(mainPanel, "Register"); // Chuyển đến giao diện đăng ký
    }

    // Thêm phương thức để hiển thị giao diện chơi game
    public void showPlayScreen() {
        cardLayout.show(mainPanel, "Play"); // Chuyển đến giao diện chơi
    }

    // Thêm phương thức để hiển thị giao diện bảng xếp hạng
    public void showRankingScreen() {
        cardLayout.show(mainPanel, "Ranking"); // Chuyển đến giao diện bảng xếp hạng
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
