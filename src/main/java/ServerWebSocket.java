import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

@ServerEndpoint("/game/{room_name}")
public class ServerWebSocket {
    private static final String gameMessageQueue = "gameMessageQueue";
    private static final Proxy proxy = new Proxy();
    private static final HashMap<String, ArrayList<Session>> usersByRooms = new HashMap<>();
    private static final RandomGenerator generator = new RandomGenerator();
    private static Timer timer;
    private static Channel channel;

    @OnOpen
    public void open(Session session, EndpointConfig c, @PathParam("room_name") String roomName) throws IOException, TimeoutException {
        ConnectionFactory cf = new ConnectionFactory();
        Connection connection = cf.newConnection();

        try
        {
            channel = connection.createChannel();
            channel.queueDeclare(gameMessageQueue, false, false, false, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Session> roomUsers = usersByRooms.get(roomName);
        if (roomUsers == null) {
            roomUsers = new ArrayList<>();
            usersByRooms.put(roomName, roomUsers);
        }

        roomUsers.add(session);
        session.getUserProperties().put("room_name",roomName);
        if (timer == null && usersByRooms.get(roomName).size() >= 2){
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendGlobalMessage(session, generator.getNextBarrel(), MessageType.NEXT_BARREL, roomName);
                }
            }, 5000, 2000);
        }
    }

    @OnMessage
    public void message(Session session, String msg, @PathParam("room_name") String roomName){
        WSMessage message = ((WSMessage) proxy.deserializeFromJSON(msg));
        switch (message.messageType){
            case REGISTER_USER -> {
                session.getUserProperties().put("name", message.data);
                sendRoomUsers(roomName);
            }
            case CHAT_MESSAGE -> {
                String chatMsg = String.format("%s: %s\n", ((String) session.getUserProperties().get("name")),message.data);
                sendGlobalMessage(session, chatMsg, MessageType.CHAT_MESSAGE,roomName);
            }
            case PLAYER_WIN -> {
                timer.cancel();
                sendGlobalMessage(session, message.data, MessageType.PLAYER_WIN, roomName);
            }
            case NOBODY_WINS -> {
                timer.cancel();
                sendGlobalMessage(session, message.data, MessageType.NOBODY_WINS, roomName);
            }
        }
    }

    @OnClose
    public void close(Session session, CloseReason reason, @PathParam("room_name") String roomName) throws IOException {
        usersByRooms.get(roomName).remove(session);
        sendRoomUsers(roomName);
//        channel.queueDelete(gameMessageQueue);
    }

    private ArrayList<String> generateRoomUsersArray(ArrayList<Session> roomUsersSessions, String roomName){
        ArrayList<String> roomUsers = new ArrayList<>();

        if (roomUsersSessions != null){
            for (Session session:roomUsersSessions) if (session.isOpen()) roomUsers.add((String) session.getUserProperties().get("name"));
        }

        return roomUsers;
    }

    private void sendMessage(Session session, Object msg) {
        try {
            channel.basicPublish("", gameMessageQueue, null, proxy.serializeToJSON(msg).getBytes());
//            session.getBasicRemote().sendText(proxy.serializeToJSON(msg));
        } catch (IOException e) {
            System.out.println("IO");
        }
    }

    private void sendRoomUsers(String roomName){
        ArrayList<Session> roomUsersSessions = usersByRooms.get(roomName);
        ArrayList<String> users = generateRoomUsersArray(roomUsersSessions, roomName);

        for (Session session:roomUsersSessions) {
            if (session.isOpen())
                try {
                    session.getBasicRemote().sendText(proxy.serializeToJSON(new WSMessage(MessageType.ROOM_USERS,users)));
                } catch (IOException e) {
                    System.out.println("IO exception when sending message to users");
                }
        }
    }

    private void sendGlobalMessage(Session current, Object message, MessageType messageType, String roomName){
        for (Session session:current.getOpenSessions()) {
            if (session.isOpen() && ((String) session.getUserProperties().get("room_name")).contentEquals(roomName)) {
                sendMessage(session, new WSMessage(messageType, message));
            }
        }
    }
}
