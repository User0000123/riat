import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.List;

public class CardsField extends GameObject{
    private List<GameCard> cards = new LinkedList<>();
    private VBox vBox;
    private final double HEIGHT_SIZE = 0.8;
    private final double WIDTH_SIZE = 0.8;
    private static final int NUMBER_OF_CARDS = 3;

    public CardsField(){
        node = (vBox = new VBox());
        root.getChildren().add(node);

        vBox.setLayoutY(Game.stage.getScene().getHeight()*(1-HEIGHT_SIZE));
        vBox.setPrefHeight(Game.stage.getScene().getHeight()*HEIGHT_SIZE);
        vBox.setPrefWidth(Game.stage.getScene().getWidth()*WIDTH_SIZE);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color:skyblue;");
        vBox.setSpacing(20);

        for (int i = 0; i < NUMBER_OF_CARDS; i++){
            GameCard gameCard = new GameCard();
            vBox.getChildren().add(gameCard.node);
        }

    }
}
