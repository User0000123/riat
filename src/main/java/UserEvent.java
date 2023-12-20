import javafx.event.Event;

/**
 * The type User event. Custom event.
 */
public class UserEvent extends Event{
    /**
     * The Event type.
     */
    UserEventType eventType;
    Object data;
    Object from;
    Object to;

    /**
     * Instantiates a new User event.
     *
     * @param eventType the event type
     */
    public UserEvent(UserEventType eventType){
        super(Event.ANY);
        this.eventType = eventType;
    }

    /**
     * Instantiates a new User event.
     *
     * @param data      the data
     * @param eventType the event type
     * @param from      from listener
     * @param to        to listener
     */
    public UserEvent(Object data, UserEventType eventType, Object from, Object to) {
        super(Event.ANY);
        this.data = data;
        this.eventType = eventType;
        this.from = from;
        this.to = to;
    }

    /**
     * Instantiates a new User event.
     *
     * @param data      the data
     * @param eventType the event type
     */
    public UserEvent(Object data, UserEventType eventType) {
        super(Event.ANY);
        this.data = data;
        this.eventType = eventType;
    }

    /**
     * Instantiates a new User event.
     *
     * @param eventType the event type
     * @param from      from listener
     * @param to        to listener
     */
    public UserEvent(UserEventType eventType, Object from, Object to) {
        super(Event.ANY);
        this.eventType = eventType;
        this.from = from;
        this.to = to;
    }
}