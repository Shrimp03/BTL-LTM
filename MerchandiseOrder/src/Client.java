import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            // Gửi dữ liệu tới server
            writer.println("Hello from Client!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
