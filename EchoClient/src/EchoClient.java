import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port){
        String localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run(){
        System.out.printf("send 'bye' to disconnect%n%n%n");
        try (Socket socket = new Socket(host, port)) {
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
                while (true){
                    System.out.println("Enter message");
                    String message = scanner.nextLine();
                    printWriter.write(message);
                    printWriter.write(System.lineSeparator());
                    printWriter.flush();
                    InputStreamReader isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
                    Scanner scanner1 = new Scanner(isr);
                    var message1 = scanner1.nextLine().strip();
                    System.out.println(message1);
                    if ("bye".equalsIgnoreCase(message)){
                        return;
                    }
                }
            }
        }catch (NoSuchElementException ex){
            System.out.printf("Connection dropped%n");
        }catch (IOException e){
            System.out.printf("Can not connect to %s:%s !%n", host,port);
            e.printStackTrace();
        }
    }
}
