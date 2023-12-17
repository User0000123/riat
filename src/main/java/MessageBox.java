import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MessageBox extends GameObject{
    private HBox hBox;
    private StackPane boxPane;
    private VBox vBox;
    private Label label;
    private Button button;

    private static final double HEIGHT = 0.4;
    private static final double WIDTH = 0.4;

    public MessageBox(){
        int height = ((int) Game.stage.getScene().getHeight());
        int width  = ((int) Game.stage.getScene().getWidth());

        hBox = new HBox();
        hBox.setPrefHeight(height);
        hBox.setPrefWidth(width);
        hBox.setAlignment(Pos.CENTER);

        boxPane = new StackPane();
        hBox.getChildren().add(boxPane);
        boxPane.setPrefWidth(width*WIDTH);
        boxPane.setPrefHeight(height*HEIGHT);

        Rectangle rect = new Rectangle();
        rect.getStyleClass().add("message-box");
        rect.setWidth(width*WIDTH);
        rect.setHeight(height*HEIGHT);
        boxPane.getChildren().add(rect);

        vBox = new VBox();
        boxPane.getChildren().add(vBox);
        vBox.setPrefHeight(height*HEIGHT);
        vBox.setPrefWidth(width*WIDTH);
        vBox.setAlignment(Pos.CENTER);

        label = new Label();
        label.setFont(new Font(width*WIDTH*0.05));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPrefHeight(height*HEIGHT*0.8);
        label.getStyleClass().add("message-box-label");

        button = new Button("Ок");
        button.getStyleClass().add("message-box-button");
        button.setPrefWidth(width*WIDTH*0.5);
        button.setOnMouseClicked(e->setVisible(false));
        vBox.getChildren().addAll(label,button);

        node = hBox;
        root.getChildren().add(node);
        this.setVisible(false);
    }

    private void setVisible(boolean option){
        node.setVisible(option);
        if (option) node.toFront();
    }

    public void show(String message){
        label.setText(message);
        setVisible(true);
    }
}