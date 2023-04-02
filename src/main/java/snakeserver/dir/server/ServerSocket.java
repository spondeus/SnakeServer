package snakeserver.dir.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.val;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import snakeserver.dir.server.message.*;
import com.badlogic.gdx.graphics.Color;
import snakeserver.dir.server.message.pickups.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ServerSocket extends WebSocketServer {

    public int lobbySize = 1;   // LOBBY SIZE

    public static ServerSocket socket;

    @Getter
    private final List<Client> clients = new ArrayList<>();

    private Queue<SnakeConstruct> snakeConstructs2 = new ArrayDeque<>();

    private Set<Integer> ids = new HashSet<>();

    private Gson gson = new Gson();

    private boolean started = false;

    private ServerPickup pickupsClass = new ServerPickup();
    private final int minPickup = 3;

    public List<Pickup> pickups() {
        return pickupsClass.getPickups();
    }

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(50);

    public ServerSocket(InetSocket address) {
        super(address);
        this.start();
        //this.run();
        System.out.println(address.ip);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        if (clients.size() >= lobbySize) {
            webSocket.closeConnection(0, "server full");
            return;
        }

        val clientAddress = webSocket.getRemoteSocketAddress();
        System.out.println("Client connected: " + clientAddress);
        val newClient = new Client(clientAddress, webSocket);

//        while (true) {
//            Integer random = new Random().nextInt(1, 10);
//            if (!ids.contains(random)) {
//                ids.add(random);
//                newClient.setId(random);
//                break;
//            }
//        }
        newClient.setId(ids.size());
        ids.add(ids.size());


//        webSocket.send("id#"+newClient.getId());
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(newClient.getId()));
        jsonObject.add("type", new JsonPrimitive("id"));
        String msg = gson.toJson(jsonObject);
        webSocket.send(msg);

        clients.add(newClient);

        for (var x : clients) {
            if (webSocket != x.getWebSocket()) {
                x.getWebSocket().send("new player joined");
            }
        }

        webSocket.send("you're the: " + clients.size() + ". player in the lobby");

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        val clientAddress = webSocket.getRemoteSocketAddress();

        for (var x : clients)
            if (x.getWebSocket() == webSocket)
                ids.remove(x.getId());

        clients.removeIf(x -> x.getRemoteAddress() == clientAddress);

        System.out.println("Client disconnected: " + clientAddress);

        if(clients.size() == 0){
            started = false;
            pickupsClass.reset();
            snakeConstructs2.clear();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        readMsg(s, webSocket);

        if(!started && clients.size() == lobbySize){
            if(snakeConstructs2.size() == lobbySize){
                for (var msg : snakeConstructs2) {
                    for (var x : clients) {     // CONSTRUCT BROADCAST

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.add("id", new JsonPrimitive(x.getId()));
                        String type = "snakeConstruct";
                        jsonObject.add("type", new JsonPrimitive(type));
                        String color = gson.toJson(Color.RED);
                        String innerJson = gson.toJson(msg);
                        jsonObject.add("data", new JsonPrimitive(innerJson));
                        String send = gson.toJson(jsonObject);

                        x.getWebSocket().send(send);
                    }
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.add("id", new JsonPrimitive(-1));
                jsonObject.add("type", new JsonPrimitive("id"));
                String msg = gson.toJson(jsonObject);
                for(var c: clients){
                    c.getWebSocket().send(msg);
                }


                pickupsClass = new ServerPickup(10);
                for(var p: pickups())
                    writeMsg(p.getPickUpId(), p);

                started = true;
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println(
                """
                    
                    ================================
                            Server Started
                    ================================
                    
                    """);
    }


    public void writeMsg(int id, Message msgObj) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(id));
        String type;
        if (msgObj instanceof SnakeMove) type = "snakeMove";
        else if (msgObj instanceof SnakeConstruct) type = "snakeConstruct";
        else if (msgObj instanceof Pickup) type = "pickupConst";
        else if(msgObj instanceof PickupRemove) type="pickupRemove";
        else if(msgObj instanceof SnakeSpeedChange) type = "speedChange";
        else if(msgObj instanceof SnakePointChange) type = "pointChange";
        else type = "id";
        jsonObject.add("type", new JsonPrimitive(type));
        String color = gson.toJson(Color.RED);
        String innerJson = gson.toJson(msgObj);
        jsonObject.add("data", new JsonPrimitive(innerJson));
        String msg = gson.toJson(jsonObject);
        for (var x : clients) {
            x.getWebSocket().send(msg);
        }
        System.out.println("sent:" + jsonObject);
    }

    public void readMsg(String s, WebSocket webSocket) {
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
        // process header
        JsonElement cId = jsonObject.get("id");
        int clientId = cId.getAsInt();
        JsonElement msgType = jsonObject.get("type");
        String type = msgType.getAsString();

        if(!type.equals("snakeMove")){
            System.out.println(webSocket.getRemoteSocketAddress() + ": " + s);
        }

        if (type.equals("snakeMove")) {     // only forwarding
            for (var x : clients) {
                x.getWebSocket().send(s);
            }
            return;
        }
        // process body
        JsonObject innerJson;
        if (type.startsWith("snake")) {
            if (type.equals("snakeColorChange")) {
                String data = jsonObject.get("data").getAsString();
                SnakeColorChange snakeColorChange = gson.fromJson(data, SnakeColorChange.class);
                if (snakeColorChange.getFirst() == -1) { // starter color
                    val xCord = new Random().nextInt(100, 800);
                    val yCord = new Random().nextInt(100, 800);
                    snakeConstructs2.add(new SnakeConstruct(xCord, yCord, 20, snakeColorChange.getNewColor()));
                }
            }
        } else if(type.startsWith("pickup")) {
            if(type.equals("pickupRemove")){
                String data = jsonObject.get("data").getAsString();
                PickupRemove pickupRemove = gson.fromJson(data, PickupRemove.class);
                for (var p: pickups())
                    if(p.getPickUpId() == pickupRemove.getPickupId())
                        if(p.getType() == Type.GHOST) {
                            writeMsg(clientId, new TimedPickup(true, true));
                            executorService.schedule(() ->{
                                writeMsg(clientId, new TimedPickup(true, false));
                            }, 10, TimeUnit.SECONDS);
                        }
                        else if(p.getType() == Type.ICE) {
                            writeMsg(clientId, new TimedPickup(false, true));
                            executorService.schedule(() ->{
                                writeMsg(clientId, new TimedPickup(false, false));
                            }, 5, TimeUnit.SECONDS);
                        }

                pickupsClass.removePickupById(pickupRemove.getPickupId());
                if (pickups().size() < minPickup) {
                    for (int i = 0; i < 10-pickups().size(); i++){
                        val newPickup = pickupsClass.newPickup();
                        writeMsg(0, newPickup);
                        pickupsClass.addPickup(newPickup);
                    }
                    writeMsg(clientId, pickupRemove);
                }
            }
        }
    }
}

//        else if (type.startsWith("pickup")) ;
//        else if (type.startsWith("wall")) ;
//        else {
//            switch (type) {
//                case "id":
//                    id = clientId;
//                    break;
//                case "die":
//                    break;
//                default:
//                    System.err.println("Unknown message type!");
//
//            }
//        }

