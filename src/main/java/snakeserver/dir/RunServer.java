package snakeserver.dir;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

    private void restartServer(){
        val scn = new Scanner(System.in);
        while(true) {
            String command = scn.nextLine();

            switch (command) {
                case "restart_server"->{
                    try{
                        socket.stop();
                        socket.start();
                        System.out.println("[Console]: Server restarted");
                    } catch (IOException | InterruptedException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}


