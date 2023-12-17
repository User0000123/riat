import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import javafx.application.Platform;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

@ClientEndpoint
public class ClientWebSocket {
    private static final String gameMessageQueue = "gameMessageQueue";
    private Session session;
    private Channel channel;
    private final Proxy proxy = new Proxy();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException, TimeoutException {
        ConnectionFactory cf = new ConnectionFactory();
        Connection connection = cf.newConnection();

        try
        {
            channel = connection.createChannel();
            channel.queueDeclare(gameMessageQueue, false, false, false, null);
            channel.basicConsume(gameMessageQueue, true, (consumerTag, message) ->
                {
                    String m = new String(message.getBody(), StandardCharsets.UTF_8);
                    message(null, m);
                }, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.session = session;
    }

    @OnMessage
    public void message(Session session, String msg) {
        WSMessage message = ((WSMessage) proxy.deserializeFromJSON(msg));
        switch (message.messageType){
            case CHAT_MESSAGE -> Platform.runLater(()-> Game.stage.fireEvent(new UserEvent(message.data, UserEventType.APPEND_INCOMING_MESSAGE)));
            case NEXT_BARREL -> Platform.runLater(()->Game.stage.fireEvent(new UserEvent(message.data, UserEventType.NEW_BARREL)));
            case ROOM_USERS -> Platform.runLater(()->Game.gameField.getRoomUsersField().update(preprocessData(message.data.toString())));
            case PLAYER_WIN, NOBODY_WINS -> Platform.runLater(()->Game.messageBox.show(message.data.toString()));
        }
    }

    private ArrayList<String> preprocessData(String message){
        StringBuilder stringBuilder = new StringBuilder(message);
        ArrayList<String> result = new ArrayList<>();

        stringBuilder.deleteCharAt(message.length()-1);
        stringBuilder.deleteCharAt(0);

        if (stringBuilder.indexOf(",")!=0)
            for (String string:stringBuilder.toString().split(",")){
                result.add(string.trim());
            }
        else if (stringBuilder.length() != 0) result.add(stringBuilder.toString());

        return result;
    }

    public ClientWebSocket(String userName, String uriToConnect){
        URI uri = URI.create(uriToConnect);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, uri);
            send(new WSMessage(MessageType.REGISTER_USER, userName));
            System.out.println();
        } catch (DeploymentException | IOException e) {
            System.out.println("Exception at the process of connection to server " + e);
        }
    }

    public void send(WSMessage message){
        try {
            session.getBasicRemote().sendText(proxy.serializeToJSON(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Session getSession(){return this.session;}

    public void disconnect() {
        if (session != null){
            try {
                session.close();
            } catch (IOException e) {
                System.out.println("IO exception when closing connection");
            }
        }
    }
}
