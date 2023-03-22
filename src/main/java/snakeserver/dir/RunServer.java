package snakeserver.dir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import snakeserver.dir.server.ServerSocket;

@SpringBootApplication
public class RunServer {

    public static void main(String[] args) {
        SpringApplication.run(RunServer.class, args);

    }
}


