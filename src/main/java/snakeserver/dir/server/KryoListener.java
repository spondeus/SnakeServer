package snakeserver.dir.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KryoListener extends com.esotericsoftware.kryonet.Listener {

    @Autowired
    private Server kryonetServer;

    @Override
    public void received(Connection connection, Object message) {

    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Client connected: " + connection.getRemoteAddressTCP().getHostString());
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Client disconnected: " + connection.getRemoteAddressTCP().getHostString());
    }
}
