package snakeserver.dir;

import com.esotericsoftware.kryonet.Server;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import snakeserver.dir.server.ServerController;

import java.security.SecureRandom;

@SpringBootApplication
public class RunServer implements ApplicationRunner {

    @Autowired
    private Server kryonetServer;

    public static void main(String[] args) {
        SpringApplication.run(RunServer.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @PreDestroy
    public void stop() {
        kryonetServer.stop();
    }
}
