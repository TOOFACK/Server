import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    String login = "TOOFACk";
    String password = "pavel2002";

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private final int PORT = 8080;

    private boolean isRunning;

    @Override
    public void run() {
        super.run();
        runServer();
    }

    public void runServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("Сервер успешно запущен\nОжидание новых подключений...");
            clientSocket = serverSocket.accept();
            System.out.println("Зарегистрировано новое входящее подключение: " + clientSocket.getInetAddress());
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            while (isRunning) {
                String message;
                message = bufferedReader.readLine();
                String[] data = message.split(":");
                String passwordFromAndroid = data[0];
                String loginFromAndroid = data[1];

                if(loginFromAndroid.equalsIgnoreCase(login)&&(passwordFromAndroid.equalsIgnoreCase(password))){
                    System.out.println("OK");
                    sendMessage("OK");
                }else{
                    System.out.println("GO HOME");
                    sendMessage("GO HOME");
                }
//                System.out.println("Получено сообщение от клиента: " + message);
//               sendMessage("Echo " + message);
            }
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера " + e);
        }
    }

    public void stopServer() {
        isRunning = false;
        if(bufferedReader != null){
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(printWriter != null){
            printWriter.close();
        }

        try {
            clientSocket.close();
            serverSocket.close();
            System.out.println("Сервер завершил свою работу.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        printWriter.println(message);
        printWriter.flush();
    }
}
