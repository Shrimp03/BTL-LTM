package client.view;

import model.User;

public interface GameInvitationListener {
    void onInvitationReceived(boolean isAccepted, User curUser);
}

