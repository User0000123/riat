import javafx.scene.Group;
import javafx.scene.Node;

/**
 * The type Game object. An abstract class for all game objects.
 * @author Aleksej
 */
public abstract class GameObject {
    /**
     * The Root element of the scene.
     */
    protected Group root;
    /**
     * The current game object node.
     */
    protected Node node;

    /**
     * Instantiates a new Game object.
     */
    GameObject(){
        Game.eventManager.subscribe(this,this::handle);
        this.root = ((Group) Game.stage.getScene().getRoot());
    }

    /**
     * Draw game object.
     */
    public void draw(){}

    /**
     * Handle the event from the event manager.
     *
     * @param event the event
     */
    public void handle(UserEvent event){}
}
