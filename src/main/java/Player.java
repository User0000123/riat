import org.apache.log4j.Level;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

public class Player extends GameObject{
    private ClientWebSocket clientWebSocket;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Player.class);
    private static final String INSERT_USER = "INSERT INTO users VALUES (1, ?, ?)";

    private String playerName = "Default";
    private String serverName = "127.0.0.1:8080";
    private String inviteCode = "";

    public boolean isConnected = false;
    private static final int INVITE_CODE_LENGTH = 7;

    public void connectToServer(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();
            PreparedStatement statement = null;
            properties.setProperty("user", "root");
            properties.setProperty("password", "password");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/riat", properties);
            statement = con.prepareStatement(INSERT_USER);
            //statement = con.prepareStatement(ADD_USER);
            statement.setString(1, "username");
            statement.setString(2, "127.0.0.1");
            statement.executeUpdate();
            log.log(Level.INFO, "connected to db");
            System.out.println("Success");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        clientWebSocket = new ClientWebSocket(playerName, getURIToConnect());
        isConnected = clientWebSocket.getSession() != null;
    }

    @Override
    public void handle(UserEvent event) {
        switch (event.eventType){
            case APPLY_USER_NAME -> {
                playerName = ((String) event.data);
                Game.messageBox.show(String.format("Имя пользователя изменено на \"%s\"",playerName));
            }
            case APPLY_SERVER_NAME -> {
                serverName = ((String) event.data);
                Game.messageBox.show("Имя сервера изменено");
            }
            case START_GAME -> {
                String userPath = ((String) event.data);
                if (userPath.matches("ws://\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{1,5}/game/\\w+")) {
                    if (!userPath.contentEquals(getURIToConnect())){
                        StringBuilder builder = new StringBuilder(userPath);
                        builder.delete(0,5);
                        serverName = builder.substring(0,builder.indexOf("/"));
                        builder.delete(0,builder.lastIndexOf("/")+1);
                        inviteCode = builder.toString();
                    }
                    connectToServer();
                    if (isConnected) Game.gameField.setVisible(true);
                    else {
                        Game.mainMenu.setMenuVisible(true);
                        Game.messageBox.show("Нет соединения с сервером");
                    }
                } else {
                    Game.mainMenu.setMenuVisible(true);
                    Game.messageBox.show("Нет соединения с сервером");
                }
            }
            case PLAYER_WINS -> clientWebSocket.send(new WSMessage(MessageType.PLAYER_WIN, "Игрок \""+playerName+"\" выиграл!"));
            case NOBODY_WINS -> clientWebSocket.send(new WSMessage(MessageType.NOBODY_WINS, "Никто не выиграл :("));
            case SEND_MESSAGE -> clientWebSocket.send(new WSMessage(MessageType.CHAT_MESSAGE,event.data));
        }
    }

    public String createInviteCode(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder inviteCode = new StringBuilder();
        Random random = new Random();
        int alphabet_length = alphabet.length();

        for (;length>0;length--) inviteCode.append(alphabet.charAt(random.nextInt(alphabet_length)));
        return inviteCode.toString();
    }

    public void setInviteCode(){this.inviteCode = createInviteCode(INVITE_CODE_LENGTH);}

    public String getURIToConnect(){
        StringBuilder uri = new StringBuilder("ws://");
        uri.append(serverName).append("/game/").append(inviteCode);
        return uri.toString();
    }

    public void closeConnectionWithServer(){if (clientWebSocket!=null) clientWebSocket.disconnect();}
}
