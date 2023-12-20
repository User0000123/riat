import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * The type Status bar. It contains elements located on the top panel of the playing field.
 */
public class StatusBar extends GameObject{
    private TrayNumbers trayNumbers;

    private Button openMenu;
    private HBox openMenuHBox;
    private HBox hBox;
    private static final double HEIGHT = 0.2;
    private static final double WIDTH = 0.2;

    /**
     * Instantiates a new Status bar.
     */
    public StatusBar(){
        int height = ((int) (Game.stage.getScene().getHeight() * HEIGHT));
        int width = ((int) Game.stage.getScene().getWidth());
        trayNumbers = new TrayNumbers();

        openMenu = new Button("Открыть меню");
        openMenu.getStyleClass().add("openMenu-button");
        openMenu.setOnMouseClicked(e->root.fireEvent(new UserEvent(UserEventType.GAME_MENU_OPEN)));

        openMenuHBox = new HBox(openMenu);
        openMenuHBox.setPrefHeight(height);
        openMenuHBox.setPrefWidth(width*WIDTH);
        openMenuHBox.setAlignment(Pos.CENTER);

        hBox = new HBox(trayNumbers.node);
        hBox.setPrefHeight(Game.stage.getScene().getHeight()*HEIGHT);
        hBox.setPrefWidth(Game.stage.getScene().getWidth());
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-background-color: orange;");

        node = hBox;
        root.getChildren().addAll(node,openMenuHBox);
    }

    /**
     * Set visible.
     *
     * @param option the option
     */
    public void setVisible(boolean option){
        node.setVisible(option);
        openMenuHBox.setVisible(option);
        openMenu.setVisible(option);
        if (option) openMenuHBox.toFront();
    }
}
