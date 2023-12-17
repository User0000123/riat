import org.glassfish.tyrus.server.Server;

public class GameServer {
    public static void main(String[] args) {
        Server server = new Server("127.0.0.1",8080,"/",null, ServerWebSocket.class);
        try{
            server.start();
            Thread.currentThread().join();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
