package snakeserver.dir.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Component;

@Component
public class ServerTest extends WebSocketServer{

    public ServerTest(InetSocket address){
        super(address);
        this.start();
        //this.run();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake){
        System.out.println("Client connected");

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b){

    }

    @Override
    public void onMessage(WebSocket webSocket, String s){
        System.out.println("MESSAGE MEGERKEZETT");
        webSocket.send("ANYADAT LBGDX");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e){

    }

    @Override
    public void onStart(){
        System.out.println("Server started");
    }
}
