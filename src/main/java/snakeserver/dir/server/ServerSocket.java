package snakeserver.dir.server;

import lombok.val;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import snakeserver.dir.util.Snake;

import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerSocket extends WebSocketServer{

    public static ServerSocket socket;

    private final List<Client> clients = new ArrayList<>();



    public ServerSocket(InetSocket address){
        super(address);
        this.start();
        //this.run();
        System.out.println(address.ip);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake){
        if(clients.size() >= 5){
            webSocket.closeConnection(0, "server full");
            return;
        }

        val clientAddress = webSocket.getRemoteSocketAddress();
        System.out.println("Client connected: "+ clientAddress);
        val newClient = new Client(clientAddress, webSocket);
        clients.add(newClient);

        webSocket.send(new Snake(1,100,100,20,Color.RED).toString());


        for(var x: clients){
            if(webSocket != x.getWebSocket())
                x.getWebSocket().send("new player joined");
        }

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
//        Client sender=getClient(webSocket);


    }

    private Client getClient(WebSocket webSocket) {
        for (Client client:clients) {
            if(client.getWebSocket().equals(webSocket)) return client;
        }
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Exception e){
        e.printStackTrace();
    }

    @Override
    public void onStart(){
        System.out.println(
                        """
                            ////////////////////////////////
                                    Server Started
                            ////////////////////////////////
                            """);
        System.out.println(new Snake(1,100,100,20, Color.BLUE));
    }
}
