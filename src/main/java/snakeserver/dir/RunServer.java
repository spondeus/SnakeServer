package snakeserver.dir;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import snakeserver.dir.server.ServerSocket;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class RunServer {


    @Autowired
    private ServerSocket socket;
    public static boolean restart;

    public static void main(String[] args) {
        SpringApplication.run(RunServer.class, args);

    }


}


