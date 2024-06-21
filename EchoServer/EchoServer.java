package month6.Echo.EchoServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
  private final int port;

  private EchoServer(int port) {
    this.port = port;
  }

  public static EchoServer bindToPort(int port) {
    return new EchoServer(port);
  }

  public void run() {
    try (var server = new ServerSocket(port)) {
      try (var clientSocket = server.accept()) {
        handle(clientSocket);
      }
    } catch (IOException e) {
      var formatMsg = "Вероятнее всего порт %s занят.%n";
      System.out.printf(formatMsg, port);
      e.printStackTrace();
    }
  }

  private void handle(Socket socket) throws IOException {
    var input = socket.getInputStream();
    var isr = new InputStreamReader(input, "UTF-8");
    try (var scanner = new Scanner(isr)) {
      while (true) {
        var message = scanner.nextLine().strip();
        var msgReversed = reverseMessage(message);
        System.out.printf("Got: %s%n", msgReversed);
        if (message.equalsIgnoreCase("bye")) {
          System.out.println("Bye bye");
          return;
        }
      }
    } catch (NoSuchElementException ex) {
      System.out.println("Client dropped connection");
    }
  }

  private String reverseMessage(String str){
    StringBuilder reversed = new StringBuilder(str);
    return reversed.reverse().toString();
  }
}
