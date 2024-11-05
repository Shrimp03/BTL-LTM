package server.controller.threadManager;

import model.User;
import server.controller.ServerThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadManager {
    private static final Map<User, ServerThread> userThreads = new ConcurrentHashMap<>();

    // Thêm ServerThread vào bản đồ
    public static void addUserThread(User user, ServerThread thread) {
        userThreads.put(user, thread);
    }

    // Xóa ServerThread khỏi bản đồ
    public static void removeUserThread(User user) {
        userThreads.remove(user);
    }

    // Lấy ServerThread cho một người dùng cụ thể
    public static ServerThread getUserThread(User user) {
        return userThreads.get(user);
    }
}
