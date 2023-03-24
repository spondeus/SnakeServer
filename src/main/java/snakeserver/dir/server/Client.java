package snakeserver.dir.server;

import lombok.Getter;
import lombok.Setter;
import org.java_websocket.WebSocket;
import snakeserver.dir.util.Vector2;

import java.net.InetSocketAddress;

@Getter @Setter
public class Client{

    public Client(InetSocketAddress address, WebSocket webSocket){
        remoteAddress = address;
        this.webSocket = webSocket;
    }

    private InetSocketAddress remoteAddress;
    private int id;

    private boolean goLeft;
    private boolean goRight;

    private Vector2 position;
    private int size;

    private WebSocket webSocket;
}
