package snakeserver.dir.server;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.*;

@Component public class InetSocket extends InetSocketAddress{

    public static String ip; static{
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e){
            throw new RuntimeException(e);
        }
    }

    public InetSocket(){
        super(ip, 8080);
    }
}
