package Server;

import Interfaces.ChatServer;
import Interfaces.ClientObserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NetChat implements ChatServer {

    private ArrayList<ClientObserver> chatClients;
    private String lastMessage;

    public NetChat() {
        chatClients = new ArrayList<>();
    }

    public static void main(String[] args) {
        NetChat netChat = new NetChat();
        try {
            netChat.StartServer();
        } catch (IOException e) {
            System.out.println("Возникла сетавая ошибка:");
            e.printStackTrace();
        }
    }

    public void StartServer() throws IOException {
        ServerSocket server = new ServerSocket(1234);
        System.out.println("Waiting...");
        while (true) {
            Socket s = server.accept();
            new Thread(new Connection(s, this)).start();
        }
    }

    @Override
    public void registrClient(ClientObserver clientObserver) {
        if(!chatClients.contains(clientObserver)) chatClients.add(clientObserver);
    }

    @Override
    public void removeClient(ClientObserver clientObserver) {
        chatClients.remove(clientObserver);
    }

    @Override
    public synchronized void pushMessage(String message) {
        lastMessage = message;
        for(ClientObserver client : chatClients){
            client.update();
        }
    }

    @Override
    public String getLastMessage() {
        return lastMessage;
    }


}
