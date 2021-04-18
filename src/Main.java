import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        String index = "";
        for(int i = 0;i < args.length;i++){
            switch (args[i]){
                case "-port":
                    port = Integer.parseInt(args[i + 1]);
                    break;
                case "-help":
                    System.out.println("Usage:port is port =P");
                    System.exit(0);
                    break;
                case "-index":
                    index = args[i + 1];
                    break;
                case "":
                    System.out.println("run java -jar MyCMS.jar -help");
                    System.exit(0);
                    break;
            }
        }
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            Handle handle = new Handle(socket,port,index);
            handle.start();
        }
    }
}
