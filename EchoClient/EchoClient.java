package month6.Echo.EchoClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
  private final int port;
  private final String host;

  private EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public static EchoClient connectTo(int port) {
    var localhost = "127.0.0.1";
    return new EchoClient(localhost, port);
  }

  public void run() {
    System.out.printf("напиши 'bye' чтобы выйти%n%n%n");
    try (var socket = new Socket(host, port)) {
      var scanner = new Scanner(System.in, "UTF-8");
      var output = socket.getOutputStream();
      var input = socket.getInputStream();
      var writer = new PrintWriter(output);
      var isr = new InputStreamReader(input, "UTF-8");
      var clientScanner = new Scanner(isr);
      try (scanner; clientScanner; writer) {
        while (true) {
          String message = scanner.nextLine();
          writer.write(message);
          writer.write(System.lineSeparator());
          writer.flush();
          if ("bye".equalsIgnoreCase(message)) {
            return;
          }
          var serverMessage = clientScanner.nextLine().strip();
          System.out.printf("Got %s%n", serverMessage);
        }
      }
    } catch (NoSuchElementException ex) {
      System.out.printf("Connection dropped!%n");
    } catch (IOException e) {
      var msg = "Can't connect to %s:%s!%n";
      System.out.printf(msg, host, port);
      e.printStackTrace();
    }
  }

  // прочие методы
}
