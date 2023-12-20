import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyTest {

    @Test
    void serializeToJSON() {
        Proxy proxy = new Proxy();
        WSMessage message = new WSMessage(MessageType.NOBODY_WINS, null);
        WSMessage serialized = (WSMessage) proxy.deserializeFromJSON(proxy.serializeToJSON(message));

        assertEquals(message.data, serialized.data);
        assertEquals(message.messageType, serialized.messageType);
    }
}