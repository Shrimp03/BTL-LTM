package client.view;

import model.GameSession;
import model.User;

public interface GameInvitationListener {
    void onInvitationReceived(boolean isAccepted, User curUser);
}

