package snakeserver.dir.server;

import com.esotericsoftware.kryonet.Server;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ServerController {

    @Bean(destroyMethod = "stop")
    public Server kryoNetServer() throws IOException {
        val server = new Server();
        server.start();
        server.bind(54555, 54777);

        server.addListener(new KryoListener());

        return server;
    }

}
