import org.apache.log4j.Logger;
import org.glassfish.tyrus.server.Server;

import org.apache.log4j.Level;

public class GameServer {
    private final static Logger log = Logger.getLogger(GameServer.class);
    public static void main(String[] args) {
        Server server = new Server("127.0.0.1",8080,"/",null, ServerWebSocket.class);
        try{
            server.start();
            log.log(Level.INFO, "server started");
            Thread.currentThread().join();
        } catch (Exception e){
            log.log(Level.INFO, "server failed to start");
            e.printStackTrace();
        }
    }
}
