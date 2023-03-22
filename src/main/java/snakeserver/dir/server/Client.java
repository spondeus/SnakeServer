package snakeserver.dir.server;

import lombok.Getter;
import lombok.Setter;
import snakeserver.dir.util.Vector2;

import java.net.InetSocketAddress;

@Getter @Setter
public class Client{

    public Client(InetSocketAddress address){
        remoteAddress = address;
    }

    private InetSocketAddress remoteAddress;

    private boolean goLeft;
    private boolean goRight;

    private Vector2 position;
    private int size;
}
