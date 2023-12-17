import javafx.event.Event;

public class UserEvent extends Event{
    UserEventType eventType;
    Object data;
    Object from;
    Object to;

    public UserEvent(UserEventType eventType){
        super(Event.ANY);
        this.eventType = eventType;
    }

    public UserEvent(Object data, UserEventType eventType, Object from, Object to) {
        super(Event.ANY);
        this.data = data;
        this.eventType = eventType;
        this.from = from;
        this.to = to;
    }
    public UserEvent(Object data, UserEventType eventType) {
        super(Event.ANY);
        this.data = data;
        this.eventType = eventType;
    }

    public UserEvent(UserEventType eventType, Object from, Object to) {
        super(Event.ANY);
        this.eventType = eventType;
        this.from = from;
        this.to = to;
    }
}