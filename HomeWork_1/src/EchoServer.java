import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;

    private EchoServer (int port){
        this.port = port;
    }

    public static EchoServer bindToPort(int port){
        return new EchoServer(port);
    }

    public void run(){
        try (ServerSocket server = new ServerSocket(port)) {
            try(var clientSocket = server.accept()){
                System.out.println("Client connected");
                handle(clientSocket);
            }
        }catch (IOException e){
            System.out.printf("Probably the port %s is busy%n", port);
            e.printStackTrace();
        }
    }

    public void handle(Socket socket) throws IOException{
        InputStreamReader isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
        try(Scanner scanner = new Scanner(isr)){
            while (true){
                var message = scanner.nextLine().strip();
                System.out.printf("Got: %s%n", message);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                StringBuilder sb = new StringBuilder(message);
                String reversed = String.valueOf(sb.reverse());
                printWriter.write(reversed);
                printWriter.write(System.lineSeparator());
                printWriter.flush();
                if ("bye".equalsIgnoreCase(message)){
                    System.out.println("Bye");
                    return;
                }
            }
        }catch (NoSuchElementException e){
            System.out.println("Client dropped connection");
        }
    }
}
