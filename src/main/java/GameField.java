public class GameField extends GameObject{
    private StatusBar statusBar;
    private CardsField cardsField;
    private ChatField chatField;
    private RoomUsersField roomUsersField;

    public GameField(){
        statusBar = new StatusBar();
        cardsField = new CardsField();
        roomUsersField = new RoomUsersField();
        chatField = new ChatField();
    }

    public void setVisible(boolean option){
        statusBar.setVisible(option);
        cardsField.node.setVisible(option);
        cardsField.node.toFront();
        roomUsersField.setVisible(option);
        chatField.setVisible(option);
    }

    public RoomUsersField getRoomUsersField(){return this.roomUsersField;}
}
