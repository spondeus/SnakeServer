package snakeserver.dir.server;

import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.val;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import snakeserver.dir.save.SaveService;
import snakeserver.dir.server.message.*;
import snakeserver.dir.controller.PlayerController;
import snakeserver.dir.server.message.Message;
import com.badlogic.gdx.graphics.Color;
import snakeserver.dir.server.message.pickups.Pickup;
import snakeserver.dir.server.message.pickups.PickupRemove;
import snakeserver.dir.server.message.pickups.ServerPickup;

import java.util.*;
import java.util.List;

@Component
public class ServerSocket extends WebSocketServer {

    public int lobbySize = 2;   // LOBBY SIZE

    public static ServerSocket socket;

    @Getter
    private final List<Client> clients = new ArrayList<>();

    private Queue<SnakeConstruct> snakeConstructs2 = new ArrayDeque<>();

    private Set<Integer> ids = new HashSet<>();

    private Gson gson = new Gson();

    private boolean started = false;

    private ServerPickup pickupsClass = new ServerPickup();
    private final int minPickup = 5;

    @Autowired
    private PlayerController playerController;
    @Autowired
    private SaveService saveService;
    private Map<Integer,Long> clientIdPlayerIdMap = new HashMap<>();

    private Long[] points = new Long[lobbySize];

    private boolean[] diedSnakes = new boolean[lobbySize];

    public List<Pickup> pickups() {
        return pickupsClass.getPickups();
    }

    private final int gameEndCode=999;

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
        newClient.setId(ids.size());
        clientIdPlayerIdMap.put(ids.size(),playerController.playerIpPlayerIdMap.get(clientAddress.getAddress().toString()));
        ids.add(ids.size());

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

        if (clients.size() == 0) {
            started = false;
            pickupsClass.reset();
            snakeConstructs2.clear();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        readMsg(s, webSocket);
        if (!started && clients.size() == lobbySize) {
            if (snakeConstructs2.size() == lobbySize) {
                for (var msg : snakeConstructs2) {
                    for (var x : clients) {     // CONSTRUCT BROADCAST

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.add("id", new JsonPrimitive(x.getId()));
                        String type = "snakeConstruct";
                        jsonObject.add("type", new JsonPrimitive(type));
                        String innerJson = gson.toJson(msg);
                        jsonObject.add("data", new JsonPrimitive(innerJson));
                        String send = gson.toJson(jsonObject);

                        x.getWebSocket().send(send);
                        System.out.println("sent:" + send);
                    }
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.add("id", new JsonPrimitive(-1));
                jsonObject.add("type", new JsonPrimitive("id"));
                String msg = gson.toJson(jsonObject);
                for (var c : clients) {
                    c.getWebSocket().send(msg);

                }

                WallMessage wallMessage=new WallMessage(Wall.spawnWalls());
                for (var x:clients
                     ) {
                    writeMsg(x.getId(),wallMessage);
                }

                pickupsClass = new ServerPickup(10);
                for (var p : pickups())
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
        else if (msgObj instanceof PickupRemove) type = "pickupRemove";
        else if(msgObj instanceof WallMessage) type="wall";
        else if(msgObj instanceof Death) type="death";
        else type = "id";
        jsonObject.add("type", new JsonPrimitive(type));
        if (type.equals("id")) msgObj.setId(id - 100);
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

        if (!type.equals("snakeMove")) {
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
        if (type.startsWith("snake")) snakeMsgHandler(jsonObject, clientId, type);
        else if (type.startsWith("pickup")) pickupMsgHandler(jsonObject, type);
        else if (type.equals("death")) dieMsgHandler(jsonObject, (long) clientId);
        else if (type.equals("score")) scoreMsgHandler(jsonObject, clientId);
        else System.out.println("unknown message type");
    }

    private void scoreMsgHandler(JsonObject jsonObject, int clientId) {
        String data = jsonObject.get("data").getAsString();
        ScoreMessage scoreMessage = gson.fromJson(data, ScoreMessage.class);
        points[clientId] = (long)scoreMessage.getScore();
    }

    private void dieMsgHandler(JsonObject jsonObject, Long clientId) {
        Death dieMessage = gson.fromJson(jsonObject, Death.class);
        writeMsg((int) (long)clientId, dieMessage);
        diedSnakes[(int) (long)clientId] = true;
        int deadSnakes = 0;
        for (boolean dead : diedSnakes) {
            if (dead) deadSnakes++;
        }
        if (lobbySize - deadSnakes == 1) {
            Message msg = new Message();
            msg.setId(gameEndCode);
            writeMsg(gameEndCode, msg);    // game end message
            TimerTask removeLastSnake=new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.setId(gameEndCode+1);
                    writeMsg(gameEndCode+1, msg);    // return to main menu message
                }
            };
            Timer timer=new Timer();
            long delay=5000;
            timer.schedule(removeLastSnake,delay);
        } else if (lobbySize == deadSnakes) winner(clientId);
    }

    private void winner(Long clientId) {
        int intClientId = (int)(long)clientId;
        System.out.println("The winner SNAKE is #" + clientId);
        System.out.println("Points: " + points[intClientId]);
        saveService.savePlayerScore(clientId,points[intClientId]);
    }

    private void pickupMsgHandler(JsonObject jsonObject, String type) {
        if (type.equals("pickupRemove")) {
            int id = jsonObject.getAsJsonPrimitive("id").getAsInt();
            String data = jsonObject.get("data").getAsString();
            PickupRemove pickupRemove = gson.fromJson(data, PickupRemove.class);

            pickupsClass.removePickupById(pickupRemove.getPickupId());
            if (pickups().size() < minPickup) {
                for (int i = 0; i < 10 - pickups().size(); i++) {
                    val newPickup = pickupsClass.newPickup();
                    writeMsg(0, newPickup);
                    pickupsClass.addPickup(newPickup);
                }
            }
            writeMsg(id, pickupRemove);
        }
    }

    private void snakeMsgHandler(JsonObject jsonObject, int clientId, String type) {
        if (type.equals("snakeColorChange")) {
            String data = jsonObject.get("data").getAsString();
            SnakeColorChange snakeColorChange = gson.fromJson(data, SnakeColorChange.class);
            int xCord, yCord;
            if (snakeColorChange.getFirst() == -1) { // starter color
                switch (clientId) {  // 1200*800
                    case 0:
                        xCord = yCord = 25;
                        break;
                    case 1:
                        xCord = 25;
                        yCord = 755;
                        break;
                    case 2:
                        xCord = 25;
                        yCord = 725;
                        break;
                    case 3:
                        xCord = 1175;
                        yCord = 25;
                        break;
                    default:
                        xCord = 25;
                        yCord = 400;
                        break;
                }
                snakeConstructs2.add(new SnakeConstruct(xCord, yCord, 20, snakeColorChange.getNewColor()));
            }
        }
    }
}
