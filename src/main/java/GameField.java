/**
 * The type Game field. Responsible for drawing the entire playing field and managing the objects on it.
 * @author Aleksej
 */
public class GameField extends GameObject{
    private StatusBar statusBar;
    private CardsField cardsField;
    private ChatField chatField;
    private RoomUsersField roomUsersField;

    /**
     * Instantiates a new Game field.
     */
    public GameField(){
        statusBar = new StatusBar();
        cardsField = new CardsField();
        roomUsersField = new RoomUsersField();
        chatField = new ChatField();
    }

    /**
     * Set visible all game field objects.
     *
     * @param option the option
     */
    public void setVisible(boolean option){
        statusBar.setVisible(option);
        cardsField.node.setVisible(option);
        cardsField.node.toFront();
        roomUsersField.setVisible(option);
        chatField.setVisible(option);
    }

    /**
     * Get room users field.
     *
     * @return the room users field
     */
    public RoomUsersField getRoomUsersField(){return this.roomUsersField;}
}
