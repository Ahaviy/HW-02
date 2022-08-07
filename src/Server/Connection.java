package Server;

import Interfaces.ChatServer;
import Interfaces.ClientObserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Connection implements Runnable, ClientObserver {

    private final ChatServer server;
    private final Socket socket;
    private String name;
    private PrintStream out;

    public Connection(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            Scanner in = new Scanner(is);
            out = new PrintStream(os);
            out.println("What's your name?");
            name = in.nextLine();
            server.registrClient(this);
            server.pushMessage(name + " enter in the chat");
            String text = in.nextLine();
            while (!text.equalsIgnoreCase("bye")) {
                server.pushMessage(name + ": " + text);
                text = in.nextLine();
            }
        } catch (NoSuchElementException e){ //не разобрался почему после ввода "bye" выскакивает этот эксепшен
            // в переменной in (Scanner), причем вызов из 40 строки, хотя по логике мы уже вышли из цикла while
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.removeClient(this);
            server.pushMessage(name + " leave chat");
        }
    }

    @Override
    public void update() {
        out.println(server.getLastMessage());
    }
}
