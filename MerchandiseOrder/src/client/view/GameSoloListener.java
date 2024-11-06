package client.view;

import model.Pair;
import model.User;

import java.util.ArrayList;

public interface GameSoloListener {
    void onProductOrderReceived(Pair<User, ArrayList<Integer>> dataReceived);
    void onFinishGame(User winner);
}