package client.view;

import client.controller.Client;
import model.Product;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuestionScreen extends JPanel{
    private User user;
    private ArrayList<Product> products;
    private JButton btnGoToPlay;

    public QuestionScreen() {
        this.user = new User(5, "user5", "password123", "user5@example.com", "10 20 30 40", "C:/full/path/to/your/image/avatar.png");

        ArrayList<Product> products = new ArrayList<>();
        String[] urls = {
                "chuoi.png",
                "coca.png",
                "hamber.png",
                "le.png",
                "nctangluc.png",
                "ngucocxanh.png",
                "nuocgiatcam.png",
                "nuocgiattrang.png",
                "quadao.png",
                "sua.png",
                "tomato.png",
                "xanh.png"
        };
        for (int i = 0; i < urls.length; ++i) {
            products.add(new Product(products.size(), "" + (i + 1), urls[i]));
        }
        this.products = products;

        btnGoToPlay = new JButton("Go to Play");
        btnGoToPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client frame = (Client) SwingUtilities.getWindowAncestor(QuestionScreen.this);
                frame.showPlayScreen(user, products);
            }
        });

        this.add(btnGoToPlay);
    }
}
