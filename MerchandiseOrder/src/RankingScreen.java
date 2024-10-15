import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RankingScreen extends JPanel {

    public RankingScreen(List<ClientData> clientRanking) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Client Ranking");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        this.add(titleLabel);

        // Hiển thị danh sách client và điểm số
        for (ClientData client : clientRanking) {
            JLabel clientLabel = new JLabel(client.getName() + ": " + client.getScore() + " points");
            this.add(clientLabel);
        }
    }
}
