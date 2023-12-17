import javafx.scene.Group;
import javafx.scene.Node;

public abstract class GameObject {
    protected Group root;
    protected Node node;

    GameObject(){
        Game.eventManager.subscribe(this,this::handle);
        this.root = ((Group) Game.stage.getScene().getRoot());
    }

    public void draw(){}
    public void handle(UserEvent event){}
}
