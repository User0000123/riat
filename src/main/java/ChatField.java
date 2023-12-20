import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * The type Chat field. Responsible for displaying the chat in the game window.
 * @author Aleksej
 */
public class ChatField extends GameObject{
    private Label label;
    private VBox vBox;
    private TextField textField;
    private TextArea textArea;

    private static final double WIDTH = 0.2;
    private static final double HEIGHT = 0.4;

    /**
     * Instantiates a new Chat field.
     */
    public ChatField(){
        int width = ((int) (Game.stage.getScene().getWidth() * WIDTH));
        int height = ((int) (Game.stage.getScene().getHeight() * HEIGHT));

        vBox = new VBox();
        vBox.setPrefHeight(height);
        vBox.setLayoutX(Game.stage.getScene().getWidth()*0.8);
        vBox.setLayoutY(Game.stage.getScene().getHeight()*0.6);
        vBox.setPrefWidth(width);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color:khaki;");

        label = new Label("Чат комнаты");
        label.getStyleClass().add("players-table-font");
        label.setPrefHeight(height*0.15);
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        textArea = new TextArea();
        textArea.setPrefWidth(width);
        textArea.setPrefHeight(height*0.8);
        textArea.setEditable(false);
        textArea.getStyleClass().add("text-area");

        textField = new TextField();
        textField.setPromptText("Новое сообщение...");
        textField.setPrefHeight(height*0.05);
        textField.setMaxWidth(width);
        textField.setOnKeyPressed(e->{if (e.getCode() == KeyCode.ENTER) sendMessage();});

        vBox.getChildren().addAll(label, textArea,textField);
        node = vBox;
        root.getChildren().add(node);
        this.setVisible(false);
    }

    private void sendMessage(){
        String message = textField.getText();
        if (!message.isBlank()) {
            textField.setText("");
            root.fireEvent(new UserEvent(message, UserEventType.SEND_MESSAGE));
        }
    }

    /**
     * Set chat visible.
     *
     * @param option that chooses variant of visibility
     */
    public void setVisible(boolean option){
        node.setVisible(option);
    }

    @Override
    public void handle(UserEvent event) {
        switch (event.eventType){
            case APPEND_INCOMING_MESSAGE -> textArea.appendText(((String) event.data));
        }
    }
}
