package Interfaces;

public interface ChatServer {
    void registrClient(ClientObserver clientObserver);
    void removeClient(ClientObserver clientObserver);
    void pushMessage(String message);
    String getLastMessage();
}
