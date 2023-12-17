public class WSMessage {
    public MessageType messageType;
    public Object data;

    public WSMessage(MessageType messageType, Object data){
        this.messageType = messageType;
        this.data = data;
    }
}
