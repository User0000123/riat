/**
 * The type Ws message. The message that the client and server use to communicate.
 */
public class WSMessage {
    /**
     * The Message type.
     */
    public MessageType messageType;
    /**
     * The Data of the message.
     */
    public Object data;

    /**
     * Instantiates a new Ws message.
     *
     * @param messageType the message type
     * @param data        the data
     */
    public WSMessage(MessageType messageType, Object data){
        this.messageType = messageType;
        this.data = data;
    }
}
