import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventManager implements EventHandler<Event>{
    private final ConcurrentHashMap<Object, Consumer<UserEvent>> subscribers = new ConcurrentHashMap<>();

    public void handle(Event event){
        switch (event.getClass().getSimpleName()){
            case "UserEvent" -> publish((UserEvent) event);
        }
    }

    public void subscribe(Object source, Consumer<UserEvent> eventHandler) {
        subscribers.put(source, eventHandler);
    }

    public void publish(UserEvent event) {
        if (event.to != null){
            if (subscribers.get(event.to) != null) subscribers.get(event.to).accept(event);
        } else {
            for (Map.Entry<Object, Consumer<UserEvent>> item: subscribers.entrySet()) {
                item.getValue().accept(event);
            }
        }
    }
}