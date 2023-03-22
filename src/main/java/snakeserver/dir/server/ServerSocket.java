package snakeserver.dir.server;

import lombok.val;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServerSocket extends WebSocketServer{

    private final List<Client> clients = new ArrayList<>();

    public ServerSocket(InetSocket address){
        super(address);
        this.start();
        //this.run();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake){
        if(clients.size() >= 5){
            webSocket.closeConnection(0, "server full");
            return;
        }

        val clientAddress = webSocket.getRemoteSocketAddress();
        System.out.println("Client connected: "+ clientAddress);
        val newClient = new Client(clientAddress);
        clients.add(newClient);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b){
        val clientAddress = webSocket.getRemoteSocketAddress();
        clients.removeIf(x -> x.getRemoteAddress() == clientAddress);

        System.out.println("Client disconnected: "+ clientAddress);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s){
        System.out.println(webSocket.getRemoteSocketAddress() + ": " + s);
        webSocket.send("ANYADAT LBGDX");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e){

    }

    @Override
    public void onStart(){
        System.out.println(
                        """
                            ////////////////////////////////
                                    Server Started
                            ////////////////////////////////
                            """);
    }
}
