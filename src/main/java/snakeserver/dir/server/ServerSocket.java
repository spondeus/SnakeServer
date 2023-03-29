package snakeserver.dir.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.val;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Component;
import snakeserver.dir.server.message.Message;
import com.badlogic.gdx.graphics.Color;
import snakeserver.dir.server.message.SnakeConstruct;
import snakeserver.dir.server.message.SnakeMove;

import java.awt.*;
import java.util.*;
import java.util.List;

@Component
public class ServerSocket extends WebSocketServer {

    public int lobbySize = 1;   // LOBBY SIZE

    public static ServerSocket socket;

    private final List<Client> clients = new ArrayList<>();

    private Map<WebSocket, String> snakeConstructs = new HashMap<>();

    private Set<Integer> ids = new HashSet<>();

    private Gson gson = new Gson();

    public ServerSocket(InetSocket address) {
        super(address);
        this.start();
        //this.run();
        System.out.println(address.ip);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        if (clients.size() >= 5) {
            webSocket.closeConnection(0, "server full");
            return;
        }

        val clientAddress = webSocket.getRemoteSocketAddress();
        System.out.println("Client connected: " + clientAddress);
        val newClient = new Client(clientAddress, webSocket);

        while (true) {
            Integer random = new Random().nextInt(1, 10);
            if (!ids.contains(random)) {
                ids.add(random);
                newClient.setId(random);
                break;
            }
        }

//        webSocket.send("id#"+newClient.getId());
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(1));
        jsonObject.add("type", new JsonPrimitive("id"));
        String msg = gson.toJson(jsonObject);
        webSocket.send(msg);

        clients.add(newClient);

        for (var x : clients) {
            if (webSocket != x.getWebSocket())
                x.getWebSocket().send("new player joined");
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

        for (var x : snakeConstructs.keySet()) {
            if (x == webSocket)
                snakeConstructs.remove(x);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(webSocket.getRemoteSocketAddress() + ": " + s);
        readMsg(s);
//        writeMsg(1,new SnakeConstruct(10,10,10, Color.BLUE));



//        val builder = new StringBuilder();


//
//        if(s.startsWith("input")){       // SNAKE INPUT HANDLER
//            val msg = s.substring(5);
//            val split = msg.split(",");
//            builder.append("input#");
//            for(var x: split){
//                builder.append(x).append("#");
//            }
//            for(var client: clients){
//                client.getWebSocket().send(builder.toString());
//            }
//        }
//
//
//        if(s.startsWith("cons")){       // SNAKE CONSTRUCT MESSAGE HANDLER
//            val xCord = new Random().nextInt(100,500);
//            val yCord = new Random().nextInt(100,500);
//            val msg = s.substring(4);
//            val split = msg.split(",");
//            for(var x: split){
//                if(x.equals("?1")){
//                    builder.append(xCord).append(",");
//                } else if (x.equals("?2")){
//                    builder.append(yCord).append(",");
//                } else{
//                    builder.append(x).append(",");
//                }
//            }
//            snakeConstructs.put(webSocket, builder.substring(0, builder.length()-1));
//            System.out.println(builder.substring(0, builder.length()));
//        }
//
//        if(clients.size() == lobbySize){        // LOBBY SIZE
//            val string = new StringBuilder();
//            string.append("cons#");
//
//            for(var x: snakeConstructs.values()){
//                string.append(x).append("#");
//            }
//            for(var x: clients){
//                x.getWebSocket().send(string.toString());
//            }
//        }

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
        else type = "id";
        jsonObject.add("type", new JsonPrimitive(type));
        String color=gson.toJson(Color.RED);
        String innerJson = gson.toJson(msgObj);
        jsonObject.add("data", new JsonPrimitive(innerJson));
        String msg = gson.toJson(jsonObject);
        for (var x : clients) {
            x.getWebSocket().send(msg);
        }
        System.out.println("sent:" + jsonObject);
    }

    public void readMsg(String s) {
        System.out.println(" got:" + s);
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
        JsonElement cId = jsonObject.get("id");
        int clientId = cId.getAsInt();
        JsonElement msgType = jsonObject.get("type");
        String type = msgType.getAsString();
        if(type.equals("snakeMove")) {
            for (var x : clients) {
                x.getWebSocket().send(s);
            }
            return;
        }
        JsonObject innerJson;
        if (type.startsWith("snake")) {
            if (type.equals("snakeMove")) {
                String dataStr= jsonObject.get("data").getAsString();
                SnakeMove snakeMove = gson.fromJson(dataStr, SnakeMove.class);
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
    }
}
