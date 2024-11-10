package client.view;

import model.GameSession;
import model.Product;
import model.User;

import java.util.ArrayList;

public interface GamePlayListener {
    void onPlay(User curUser, GameSession gameSession, ArrayList<Product> products, User startingPlayer);
}
