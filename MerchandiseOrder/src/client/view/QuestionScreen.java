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
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/anh-do-an-doc-dao-dep-nhat_095145309.jpg",
                "https://th.bing.com/th/id/OIP.r0X-CYZvvYM71ZdNU4LXQQHaE8?pid=ImgDet&w=474&h=316&rs=1",
                "https://th.bing.com/th/id/OIP.2dpZ2lPu39T_WCBOZEOZFgHaI7?pid=ImgDet&w=474&h=571&rs=1",
                "https://phunugioi.com/wp-content/uploads/2022/02/Anh-Do-An-Cute-2.jpg",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-cuc-de-thuong_095148496.png",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-picnic-de-thuong_095150617.jpg",
                "https://img6.thuthuatphanmem.vn/uploads/2022/11/17/anh-chibi-cute_014001732.png",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-cute-de-thuong_095148049.jpg",
                "https://img.lovepik.com/free-png/20210924/lovepik-cute-jane-food-png-image_401296684_wh1200.png",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-ngo-nghinh-dep-nhat_095150416.jpg",
                "https://img5.thuthuatphanmem.vn/uploads/2021/11/09/hinh-anh-do-an-cute_095148385.jpg",
                "https://img.lovepik.com/free-png/20210927/lovepik-school-supplies-school-pencil-ruler-png-image_401534722_wh1200.png"
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
