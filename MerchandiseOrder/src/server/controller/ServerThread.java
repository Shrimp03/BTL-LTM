package server.controller;

import model.DataTransferObject;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            while (true) {
                try {
                    DataTransferObject<?> request = (DataTransferObject<?>) ois.readObject();
                    System.out.println("Received request from client: " + request);

                    // TODO: Kiểm tra nếu client yêu cầu ngắt kết nối
                    if ("DISCONNECT".equals(request.getType())) {
                        System.out.println("Client requested to disconnect.");
                        break;
                    }

                    DataTransferObject<?> response = RequestDispatcher.dispatch(request);
                    oos.writeObject(response);
                    oos.flush();
                    System.out.println("Response sent to client");
                } catch (EOFException e) {
                    System.out.println("Client has closed the connection unexpectedly.");
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during communication with client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Server socket closed");
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
