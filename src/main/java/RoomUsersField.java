import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class RoomUsersField extends GameObject{
    private Label label;
    private VBox vBox;
    private ListView<String> usersListView;

    private static double HEIGHT = 0.4;
    private static double WIDTH = 0.2;

    public RoomUsersField(){
        int width = ((int) (Game.stage.getScene().getWidth()*WIDTH));
        int height = ((int) (Game.stage.getScene().getHeight()*HEIGHT));

        vBox = new VBox();
        vBox.setPrefHeight(height);
        vBox.setPrefWidth(width);
        vBox.setLayoutX(Game.stage.getScene().getWidth()*0.8);
        vBox.setLayoutY(Game.stage.getScene().getHeight()*0.2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color:khaki;");

        label = new Label("Игроки в комнате");
        label.getStyleClass().add("players-table-font");
        label.setPrefHeight(height*0.2);
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        usersListView = new ListView<>();
        usersListView.setPrefHeight(height*0.8);
        usersListView.setPrefWidth(width);
        usersListView.setFixedCellSize(50);
        usersListView.getStyleClass().add("users-list-view");

        vBox.getChildren().addAll(label, usersListView);
        node = vBox;
        root.getChildren().add(node);
        this.setVisible(false);
    }

    public void setVisible(boolean option){
        node.setVisible(option);
    }

    public void update(ArrayList<String> users){
        usersListView.setItems(FXCollections.observableArrayList(users));
    }
}
