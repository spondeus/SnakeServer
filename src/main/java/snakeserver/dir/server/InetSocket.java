package snakeserver.dir.server;

import lombok.val;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class InetSocket extends InetSocketAddress{
    public InetSocket(){
        super("localhost", 8082);
    }
}
