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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import snakeserver.dir.model.Repository;
import snakeserver.dir.save.SaveService;
import snakeserver.dir.server.message.*;
import snakeserver.dir.controller.PlayerController;
import snakeserver.dir.server.message.Message;
import snakeserver.dir.server.message.pickups.Pickup;
import snakeserver.dir.server.message.pickups.PickupRemove;
import snakeserver.dir.server.message.pickups.ServerPickup;
import snakeserver.dir.server.message.pickups.*;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ServerSocket extends WebSocketServer {

    private boolean isWalls = true;
    @EventListener(ApplicationReadyEvent.class)
    public void CONSOLE() throws IOException, InterruptedException
    {
        val scn = new Scanner(System.in);
        while (true){
            val com = scn.nextLine();
            switch (com){
                case "wall_disable" -> {
                    isWalls = false;
                    System.out.println("Disable walls");
                }
                case "wall_enable" -> {
                    isWalls = true;
                    System.out.println("Enabled walls");
                }
            }
        }
    }

    public int lobbySize = 2;   // LOBBY SIZE

    public static ServerSocket socket;

    @Getter
    private final List<Client> clients = new ArrayList<>();

    private Queue<SnakeConstruct> snakeConstructs2 = new ArrayDeque<>();

    private Set<Integer> ids = new HashSet<>();

    private Gson gson = new Gson();

    private boolean started = false;

    private ServerPickup pickupsClass = new ServerPickup();
    private final int minPickup = 4;

    @Autowired
    private PlayerController playerController;
    @Autowired
    private SaveService saveService;
    private final Map<Integer, Long> clientIdPlayerIdMap = new HashMap<>();

    private Long[] points = new Long[lobbySize];

    private boolean[] diedSnakes;

    public List<Pickup> pickups() {
        return pickupsClass.getItems();
    }

    private final int gameEndCode = 999;

    WallMessage wallMessage;
    @Autowired
    Repository pointRepository;

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
        newClient.setId(ids.size());
        if (playerController.playerIpPlayerIdMap.isEmpty()){
            clientIdPlayerIdMap.put(ids.size(), 1L);
        }else{
            clientIdPlayerIdMap.put(ids.size(), playerController.playerIpPlayerIdMap.get(clientAddress.getAddress().toString()));
        }
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

        for(var c: clients){
            c.getWebSocket().close();
        }
//        for (var x : clients){
//            snakeConstructs2.removeIf(sc -> sc.getId() == x.getId());
//            if (x.getWebSocket() == webSocket)
//                ids.remove(x.getId());
//        }

        webSocket.close();

        clients.removeIf(x -> x.getRemoteAddress() == clientAddress);

        System.out.println("Client disconnected: " + clientAddress);

        if (clients.size() == 0) {
            started = false;
            pickupsClass.reset();
            snakeConstructs2.clear();
            ids.clear();
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

                wallMessage = new WallMessage(Wall.spawnWalls());
                if(isWalls){
                    for (var x : clients
                    ) {
                        writeMsg(x.getId(), wallMessage);
                    }
                }

                pickupsClass = new ServerPickup();
                for (int i = 0; i < 10; i++) {
                    pickupsClass.addPickup(pickupsClass.newPickup(wallMessage.getWallList()));
                }
                for (var p : pickups())
                    writeMsg(p.getPickUpId(), p);


                started = true;
                diedSnakes = new boolean[lobbySize];
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
        else if (msgObj instanceof WallMessage) type = "wall";
        else if (msgObj instanceof Death) type = "death";
        else if (msgObj instanceof Top10) type = "top10";
        else type = "id";
        jsonObject.add("type", new JsonPrimitive(type));
        if (type.equals("id")) msgObj.setId(id - 100);
        String innerJson = gson.toJson(msgObj);
        jsonObject.add("data", new JsonPrimitive(innerJson));
        String msg = gson.toJson(jsonObject);
        if (type.equals("top10")) clients.get(id).getWebSocket().send(msg);
        else
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
        else if (type.startsWith("pickup")) pickupMsgHandler(jsonObject, type, clientId);
        else if (type.equals("death")) dieMsgHandler(jsonObject, (long) clientId);
        else if (type.equals("score")) scoreMsgHandler(jsonObject, clientId);
        else if (type.equals("points")) pointsMsgHandler(clientId);
        else System.out.println("unknown message type");
    }

    private void pointsMsgHandler(int clientId) {
        writeMsg(clientId, new Top10(pointRepository.getTop10BasedOnScore()));
    }

    private void scoreMsgHandler(JsonObject jsonObject, int clientId) {
        String data = jsonObject.get("data").getAsString();
        ScoreMessage scoreMessage = gson.fromJson(data, ScoreMessage.class);
        points[clientId] = (long) scoreMessage.getScore();
    }

    private void dieMsgHandler(JsonObject jsonObject, Long clientId) {
        Death dieMessage = gson.fromJson(jsonObject, Death.class);
        writeMsg((int) (long) clientId, dieMessage);
        diedSnakes[(int) (long) clientId] = true;
        int deadSnakes = 0;
        for (boolean dead : diedSnakes) {
            if (dead) deadSnakes++;
            writeMsg((int) (long) clientId, dieMessage);
            diedSnakes[(int) (long) clientId] = true;
            int alive = lobbySize;
            for (boolean living : diedSnakes) {
                if (living) alive--;
            }
            if (alive == 1) {
                Message msg = new Message();
                msg.setId(gameEndCode);
                writeMsg(gameEndCode, msg);    // game end message
                TimerTask removeLastSnake = new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.setId(gameEndCode + 1);
                        writeMsg(gameEndCode + 1, msg);     // return to main menu message
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        for (var c : clients){
                            c.getWebSocket().close();
                        }
                    }
                };
                Timer timer = new Timer();
                long delay = 5000;
                timer.schedule(removeLastSnake, delay);
            } else if (alive == 0) winner(clientId, clientIdPlayerIdMap.get((int) (long) clientId));
        }
    }

    private void winner(Long clientId, Long playerId) {
        saveService.saveGame();
        int intClientId = (int) (long) clientId;
        System.out.println("The winner SNAKE is #" + clientId);
        System.out.println("Points: " + points[intClientId]);
        saveService.savePlayerScore(playerId, points[intClientId]);
    }

    private void pickupMsgHandler(JsonObject jsonObject, String type, int clientId) {

        if (type.equals("pickupRemove")) {
            int id = jsonObject.getAsJsonPrimitive("id").getAsInt();
            String data = jsonObject.get("data").getAsString();
            PickupRemove pickupRemove = gson.fromJson(data, PickupRemove.class);
            for (var p : pickups())
                if (p.getPickUpId() == pickupRemove.getPickupId())
                    if (p.getType() == Type.GHOST) {
                        writeMsg(clientId, new TimedPickup(true, true));
                        executorService.schedule(() -> {
                            writeMsg(clientId, new TimedPickup(true, false));
                        }, 10, TimeUnit.SECONDS);
                    } else if (p.getType() == Type.ICE) {
                        writeMsg(clientId, new TimedPickup(false, true));
                        executorService.schedule(() -> {
                            writeMsg(clientId, new TimedPickup(false, false));
                        }, 5, TimeUnit.SECONDS);
                    }
            pickupsClass.removePickupById(pickupRemove.getPickupId());
            System.out.println(pickups().size());
            if (pickups().size() < minPickup) {
                for (int i = 0; i < 10 - pickups().size(); i++) {
                    val newPickup = pickupsClass.newPickup(wallMessage.getWallList());
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
            int xCord = 0, yCord = 0;
            int xPush = 195, yPush = 25;
            int w = 1200, h = 720;
            if (snakeColorChange.getFirst() == -1) { // starter color
                switch (clientId) {  // 1200*800
                    case 0:
                        xCord = xPush;
                        yCord = yPush;
                        break;
                    case 1:
                        xCord = w - xPush;
                        yCord = h - yPush;
                        break;
                    case 2:
                        xCord = yPush;
                        yCord = h - xPush;
                        break;
                    case 3:
                        xCord = w - yPush;
                        yCord = xPush;
                        break;
                    default:
                        xCord = w/2;
                        yCord = h/2;
                        break;
                }
                snakeConstructs2.add(new SnakeConstruct(xCord, yCord, 20, snakeColorChange.getNewColor()));
            }
        }
    }

}