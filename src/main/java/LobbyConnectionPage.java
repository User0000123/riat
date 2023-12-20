import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The type Lobby connection page. The settings window for launching a new game,
 * connecting to the game room.
 *
 * @author Aleksej
 */
public class LobbyConnectionPage extends GameObject{
    private HBox hBox;
    private VBox vBox;
    private TextField tfLobbyPath;
    private Button startGame;
    private Button backToMenu;

    private static final double WIDTH = 0.4;
    private static final double HEIGHT = 0.4;

    /**
     * Instantiates a new Lobby connection page.
     *
     * @param isNewGame the is new game
     */
    public LobbyConnectionPage(boolean isNewGame){
        int width = ((int) Game.stage.getScene().getWidth());
        int height = ((int) Game.stage.getScene().getHeight());

        hBox = new HBox();
        hBox.setPrefWidth(width);
        hBox.setPrefHeight(height);
        hBox.setAlignment(Pos.CENTER);

        vBox = new VBox();
        hBox.getChildren().add(vBox);
        vBox.setPrefWidth(width*WIDTH);
        vBox.setPrefHeight(height*HEIGHT);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);

        tfLobbyPath = new TextField();
        tfLobbyPath.setPromptText("URI сервера");
        tfLobbyPath.setMaxWidth(width*WIDTH);
        tfLobbyPath.setEditable(!isNewGame);
        tfLobbyPath.setFocusTraversable(false);
        tfLobbyPath.getStyleClass().add("tf-user-name");

        startGame = new Button();
        startGame.setFocusTraversable(false);
        startGame.setPrefWidth(width*WIDTH);
        startGame.getStyleClass().add("menu-button");
        startGame.setText("Начать игру");
        startGame.setOnMouseClicked(e->root.fireEvent(new UserEvent(tfLobbyPath.getText(),UserEventType.START_GAME)));

        backToMenu = new Button();
        backToMenu.setFocusTraversable(false);
        backToMenu.setPrefWidth(width*WIDTH);
        backToMenu.getStyleClass().add("menu-button");
        backToMenu.setText("Назад");
        backToMenu.setOnMouseClicked(e->root.fireEvent(new UserEvent(UserEventType.SHOW_MAIN_MENU)));

        vBox.getChildren().addAll(tfLobbyPath, startGame, backToMenu);

        node = hBox;
        root.getChildren().add(node);
        this.setVisible(false);
    }

    /**
     * Set visible lobby connection page.
     *
     * @param option the option
     */
    public void setVisible(boolean option){
        node.setVisible(option);
        if (option) {
            node.toFront();
            root.fireEvent(new UserEvent(UserEventType.SHOW_ROOM_USERS));
        }
    }

    /**
     * Update lobby page.
     */
    public void update(){
        tfLobbyPath.setText(Game.player.getURIToConnect());
    }
}
