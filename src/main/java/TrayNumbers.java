import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.LinkedList;

/**
 * The type Tray numbers. Responsible for storing and displaying the current active barrels available to the user.
 */
public class TrayNumbers extends GameObject{
    private HBox trayBox;
    private HBox hBoxNumbers;
    private StackPane toDeletePane;
    private LinkedList<Integer> numbersInTray;

    private static final double WIDTH = 0.27;
    private static final double HEIGHT = 0.1;
    private static final double CELL_RADIUS = HEIGHT*0.8/2;

    /**
     * Instantiates a new Tray numbers.
     */
    public TrayNumbers(){
        int width = ((int) (Game.stage.getScene().getWidth() * WIDTH));
        int height = ((int) (Game.stage.getScene().getHeight() * HEIGHT));
        StackPane tray;

        numbersInTray = new LinkedList<>();

        Rectangle rectangle = new Rectangle(width,height);
        rectangle.setFill(Color.BLANCHEDALMOND);
        rectangle.getStyleClass().add("tf-user-name");

        tray = new StackPane();
        tray.getChildren().add(rectangle);
        tray.setPrefWidth(width);
        tray.setPrefHeight(height);

        hBoxNumbers = new HBox();
        tray.getChildren().add(hBoxNumbers);
        hBoxNumbers.setPrefWidth(width);
        hBoxNumbers.setSpacing(5);
        hBoxNumbers.setPadding(new Insets(0,0,0,30));
        hBoxNumbers.setPrefHeight(height);
        hBoxNumbers.setAlignment(Pos.CENTER_LEFT);


        Circle toDelete = new Circle(height/1.5);
        toDeletePane = new StackPane(toDelete);
        toDelete.setFill(Color.INDIANRED);
        toDeletePane.setTranslateX(width/1.6);
        tray.getChildren().add(toDeletePane);

        trayBox = new HBox(tray);
        trayBox.setAlignment(Pos.CENTER);
        node = trayBox;
        root.getChildren().add(node);
        draw();
    }

    public void draw(){
        int cell_radius = ((int) (Game.stage.getScene().getHeight() * CELL_RADIUS));

        hBoxNumbers.getChildren().clear();
        for (int number: numbersInTray){
            hBoxNumbers.getChildren().add(createBarrel(cell_radius,number));
        }
    }

    private void placeBarrelToBin(){
        int number_to_delete = numbersInTray.pollLast();
        root.fireEvent(new UserEvent(number_to_delete, UserEventType.MISSING_NUMBER));
        int cell_radius = ((int) (Game.stage.getScene().getHeight() * CELL_RADIUS*1.2));

        StackPane pane = createBarrel(cell_radius, number_to_delete);

        int toDeletePaneSize = toDeletePane.getChildren().size();
        if (toDeletePaneSize == 2) toDeletePane.getChildren().remove(toDeletePaneSize-1);
        toDeletePane.getChildren().add(pane);

        FadeTransition ft = new FadeTransition(Duration.millis(500), pane);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.play();
    }

    private StackPane createBarrel(double cell_radius, int number){
        Circle circle = new Circle(cell_radius);
        circle.setFill(Color.GREEN);
        circle.setStroke(Color.ORANGE);
        circle.setStrokeWidth(2);

        Label label = new Label(String.valueOf(number));
        label.getStyleClass().add("ball-font");

        return new StackPane(circle, label);
    }

    @Override
    public void handle(UserEvent event) {
        switch (event.eventType){
            case NEW_BARREL -> {
                if (event.data == null) root.fireEvent(new UserEvent(UserEventType.NOBODY_WINS));
                Double number = ((Double) event.data);
                if (numbersInTray.size() == 5) placeBarrelToBin();

                numbersInTray.addFirst(number.intValue());
                draw();
            }
            case CHECK_NUMBER_IN_TRAY -> {
                int number = ((int) event.from);
                System.out.println("==="+number);
                if (numbersInTray.contains(number)) root.fireEvent(new UserEvent(event.data,UserEventType.NUMBER_EXISTS));
            }
        }
    }
}
