import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * The type Menu. Responsible for the display and rendering of the menu.
 *
 * @author Aleksej
 */
public class Menu extends GameObject{
    private VBox vBox;
    private final Label label;
    private final HashMap<String, Consumer<MouseEvent>> buttonHandlers;
    private final double TITLE_SIZE;
    private final double BUTTON_SIZE;

    /**
     * Instantiates a new Menu.
     *
     * @param label              the label of the menu
     * @param buttonHandlers     the buttons for the menu
     * @param titleSizePercents  the title size percents
     * @param buttonSizePercents the button size percents
     */
    public Menu(String label, HashMap<String, Consumer<MouseEvent>> buttonHandlers,
                double titleSizePercents, double buttonSizePercents){
        this.label = new Label(label);
        this.buttonHandlers = buttonHandlers;
        this.TITLE_SIZE = titleSizePercents;
        this.BUTTON_SIZE = buttonSizePercents;

        initializeMenuContainer();
        initializeMenuItems();
    }

    private void initializeMenuContainer(){
        HBox labelBox = new HBox();

        label.getStyleClass().add("menu-label");
        labelBox.getChildren().add(label);
        labelBox.setAlignment(Pos.BOTTOM_CENTER);
        labelBox.setPrefWidth(Game.stage.getScene().getWidth());
        labelBox.setPrefHeight(Game.stage.getScene().getHeight()* TITLE_SIZE);

        this.node = (vBox = new VBox());
        root.getChildren().addAll(node, labelBox);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(50);
        vBox.setLayoutY(Game.stage.getScene().getHeight()* TITLE_SIZE);
        vBox.setPrefWidth(Game.stage.getScene().getWidth());
        vBox.setPrefHeight(Game.stage.getScene().getHeight()*(1 - TITLE_SIZE));
    }

    private void initializeMenuItems(){
        buttonHandlers.forEach((key,value)->{
            Button button = new Button(key);
            button.setPrefWidth(BUTTON_SIZE*Game.stage.getScene().getWidth());
            button.getStyleClass().add("menu-button");
            button.setFocusTraversable(false);
            button.setOnMouseClicked(value::accept);
            vBox.getChildren().add(button);
        });
    }

    /**
     * Set menu visible.
     *
     * @param option the option
     */
    public void setMenuVisible(boolean option){
        node.setVisible(option);
        label.setVisible(option);
    }
}
