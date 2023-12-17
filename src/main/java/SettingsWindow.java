import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsWindow extends GameObject{
    private VBox vBox;
    private Label label;
    private Button backToMenu;
    private Button applySettings;
    private TextField textFieldUserName;
    private TextField textFieldServerName;

    private static final double BUTTON_WIDTH = 0.4;
    private static final double HEIGHT = 0.4;

    public SettingsWindow(){
        HBox labelBox = new HBox();
        labelBox.setPrefHeight(Game.stage.getScene().getHeight()*HEIGHT);
        labelBox.setPrefWidth(Game.stage.getScene().getWidth());
        labelBox.setAlignment(Pos.CENTER);

        label = new Label("НАСТРОЙКИ");
        label.getStyleClass().add("menu-label");
        labelBox.getChildren().add(label);
        root.getChildren().add(labelBox);

        backToMenu = new Button("Назад");
        backToMenu.getStyleClass().add("menu-button");
        backToMenu.setFocusTraversable(false);
        backToMenu.setPrefWidth(Game.stage.getScene().getWidth()*BUTTON_WIDTH/2);
        backToMenu.setOnMouseClicked(e->root.fireEvent(new UserEvent(UserEventType.BACK_TO_MENU)));

        applySettings = new Button("Применить настройки");
        applySettings.getStyleClass().add("menu-button");
        applySettings.setFocusTraversable(false);
        applySettings.setPrefWidth(Game.stage.getScene().getWidth()*BUTTON_WIDTH);
        applySettings.setOnMouseClicked(e->applySettings());

        textFieldUserName = new TextField();
        textFieldUserName.setPromptText("Имя игрока");
        textFieldUserName.setFocusTraversable(false);
        textFieldUserName.setMaxWidth(Game.stage.getScene().getWidth()*BUTTON_WIDTH);
        textFieldUserName.getStyleClass().add("tf-user-name");
        textFieldUserName.setOnKeyPressed(e->{if (e.getCode() == KeyCode.ENTER) applyUserName();});

        textFieldServerName = new TextField();
        textFieldServerName.setPromptText("Имя сервера");
        textFieldServerName.setFocusTraversable(false);
        textFieldServerName.setMaxWidth(Game.stage.getScene().getWidth()*BUTTON_WIDTH);
        textFieldServerName.getStyleClass().add("tf-user-name");
        textFieldServerName.setOnKeyPressed(e->{if (e.getCode() == KeyCode.ENTER) applyServerName();});

        //загрузка аудио клипов

        vBox = new VBox();
        vBox.getChildren().addAll(textFieldUserName,textFieldServerName, applySettings, backToMenu);
        vBox.setSpacing(20);
        vBox.setLayoutY(Game.stage.getScene().getHeight()*HEIGHT);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(Game.stage.getScene().getWidth());
        vBox.setPrefHeight(Game.stage.getScene().getHeight()*(1*HEIGHT));

        node = vBox;
        root.getChildren().add(node);
    }

    private void applySettings(){
        if (!Game.player.isConnected) {
            applyUserName();
            applyServerName();
            Game.messageBox.show("Настройки применены");
        } else Game.messageBox.show("Нельзя сменить эти настройки время игры");

    }

    private void applyServerName(){
        if (Game.player.isConnected) {
            Game.messageBox.show("Нельзя сменить эти настройки время игры");
            return;
        }
        String serverName = textFieldServerName.getText();
        if (!serverName.isBlank()) root.fireEvent(new UserEvent(serverName, UserEventType.APPLY_SERVER_NAME));
    }

    private void applyUserName(){
        if (Game.player.isConnected) {
            Game.messageBox.show("Нельзя сменить эти настройки время игры");
            return;
        }
        String newName = textFieldUserName.getText();
        if (!newName.isBlank()) root.fireEvent(new UserEvent(newName, UserEventType.APPLY_USER_NAME));
    }

    public void setSettingsVisible(boolean option){
        vBox.setVisible(option);
        label.setVisible(option);
    }
}
