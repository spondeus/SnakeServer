package snakeserver.dir.server;

import lombok.val;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Component;
import snakeserver.dir.server.message.pickups.Pickup;
import snakeserver.dir.server.message.pickups.ServerPickup;

import java.util.*;

@Component
public class ServerSocket extends WebSocketServer{

    public int lobbySize = 1;   // LOBBY SIZE

    public static ServerSocket socket;

    private final List<Client> clients = new ArrayList<>();

    private Map<WebSocket,String> snakeConstructs = new HashMap<>();

    private Set<Integer> ids = new HashSet<>();

    private ServerPickup pickups;
    public List<Pickup> getPickups(){
        return pickups.getPickups();
    }

    private final int minPickup = 5;


    public ServerSocket(InetSocket address){
        super(address);
        this.start();
        //this.run();
        System.out.println(address.ip);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake){
        if(clients.size() >= lobbySize){
            webSocket.closeConnection(0, "server full");
            return;
        }

        val clientAddress = webSocket.getRemoteSocketAddress();
        System.out.println("Client connected: "+ clientAddress);
        val newClient = new Client(clientAddress, webSocket);

        while(true){
            Integer random = new Random().nextInt(1,10);
            if(!ids.contains(random)){
                ids.add(random);
                newClient.setId(random);
                break;
            }
        }

        webSocket.send("id#"+newClient.getId());

        clients.add(newClient);

        for(var x: clients){
            if(webSocket != x.getWebSocket())
                x.getWebSocket().send("new player joined");
        }

        webSocket.send("you're the: "+clients.size()+". player in the lobby");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b){
        val clientAddress = webSocket.getRemoteSocketAddress();

        for(var x:clients)
            if (x.getWebSocket() == webSocket)
                ids.remove(x.getId());

        clients.removeIf(x -> x.getRemoteAddress() == clientAddress);

        System.out.println("Client disconnected: "+ clientAddress);

        for(var x: snakeConstructs.keySet()){
            if(x == webSocket)
                snakeConstructs.remove(x);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String s){

        if(s.startsWith("pickup")){
            val msg = s.substring(6);
            val split = msg.split(",");

            pickups.removePickupById(Integer.parseInt(split[1]));
            for(var x: clients)
                x.getWebSocket().send("pickupRem#"+split[1]);

            if(getPickups().size() < 5){
                pickups.addPickup(10-getPickups().size());
            }
            for(var p: getPickups()){
                for(var c: clients){
                    c.getWebSocket().send(p.toString());
                }
            }
        }

        if(!s.startsWith("input"))
            System.out.println(webSocket.getRemoteSocketAddress() + ": " + s);
        val builder = new StringBuilder();

        if(s.startsWith("input")){       // SNAKE INPUT HANDLER
            val msg = s.substring(5);
            val split = msg.split(",");
            builder.append("input#");
            for(var x: split){
                builder.append(x).append("#");
            }
            for(var client: clients){
                client.getWebSocket().send(builder.toString());
            }
        }


        if(s.startsWith("cons")){       // SNAKE CONSTRUCT MESSAGE HANDLER
            val xCord = new Random().nextInt(100,500);
            val yCord = new Random().nextInt(100,500);
            val msg = s.substring(4);
            val split = msg.split(",");
            for(var x: split){
                if(x.equals("?1")){
                    builder.append(xCord).append(",");
                } else if (x.equals("?2")){
                    builder.append(yCord).append(",");
                } else{
                    builder.append(x).append(",");
                }
            }
            snakeConstructs.put(webSocket, builder.substring(0, builder.length()-1));
            System.out.println(builder.substring(0, builder.length()));
        }

        if(clients.size() == lobbySize){        // LOBBY SIZE
            pickups = new ServerPickup(10);
            System.out.println("pickups " + getPickups());

            for(var c: clients){
                for(var p: getPickups()){
                    c.getWebSocket().send(p.toString());
                }
            }

            val string = new StringBuilder();
            string.append("cons#");

            for(var x: snakeConstructs.values()){
                string.append(x).append("#");
            }
            for(var x: clients){
                x.getWebSocket().send(string.toString());
            }
        }

    }

    @Override
    public void onError(WebSocket webSocket, Exception e){
        e.printStackTrace();
    }

    @Override
    public void onStart(){
        System.out.println(
                """
                    ================================
                            Server Started
                    ================================
                    """);
    }
}
