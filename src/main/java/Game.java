import com.mysql.cj.log.Log;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.log4j.Level;

import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Game extends Application {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Game.class);
    public static Stage stage;
    public static Player player;
    public static EventManager eventManager;
    public static GameField gameField;
    public static MessageBox messageBox;
    public static Menu mainMenu;
    public static Menu gameStartMenu;
    public static Menu pauseGameMenu;
    public static LobbyConnectionPage lobbyNewGame;
    public static LobbyConnectionPage lobbyExistingGame;
    public static SettingsWindow settingsWindow;

    public static void main(String[] args) {
        Game.launch(args);
    }

    @Override
    public void start(Stage stage){
        Game.stage = stage;
        stage.setTitle("Лото");
        stage.setScene(new Scene(new Group()));
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.getScene().getStylesheets().add("styles.css");
        stage.setFullScreenExitHint("");
        stage.setOnCloseRequest(e->player.closeConnectionWithServer());
        stage.show();

        eventManager = new EventManager();
        eventManager.subscribe(this, this::handle);
        stage.addEventHandler(Event.ANY, eventManager);

        player = new Player();
        gameField = new GameField();
        gameField.setVisible(false);
        messageBox = new MessageBox();
        lobbyNewGame = new LobbyConnectionPage(true);
        lobbyExistingGame = new LobbyConnectionPage(false);
        initMainMenu();
        initGameStartMenu();
        initGameSettingsWindow();
        initPauseGameMenu();
    }

    private void initMainMenu(){
        LinkedHashMap<String, Consumer<MouseEvent>> buttonHandlers = new LinkedHashMap<>();
        buttonHandlers.put("Играть",(event)->stage.fireEvent(new UserEvent(UserEventType.MENU_PLAY_BUTTON)));
        buttonHandlers.put("Настройки",(event)->stage.fireEvent(new UserEvent(UserEventType.MENU_SETTINGS)));
        buttonHandlers.put("Выйти",(event)->stage.fireEvent(new UserEvent(UserEventType.MENU_EXIT)));
        mainMenu = new Menu("ЛОТО", buttonHandlers, 0.3,0.2);
        mainMenu.setMenuVisible(true);
    }

    private void initGameStartMenu(){
        LinkedHashMap<String, Consumer<MouseEvent>> buttonHandlers = new LinkedHashMap<>();
        buttonHandlers.put("Новая игровая комната", event-> stage.fireEvent(new UserEvent(UserEventType.CREATE_NEW_ROOM)));
        buttonHandlers.put("Присоединиться к существующей", event-> stage.fireEvent(new UserEvent(UserEventType.CONNECT_TO_EXISTING)));
        buttonHandlers.put("Назад", event-> stage.fireEvent(new UserEvent(UserEventType.BACK_TO_MENU)));
        gameStartMenu = new Menu("Параметры игры", buttonHandlers, 0.3,0.5);
        gameStartMenu.setMenuVisible(false);
        log.log(Level.INFO, "Player added");
    }

    private void initPauseGameMenu(){
        LinkedHashMap<String, Consumer<MouseEvent>> buttonHandlers = new LinkedHashMap<>();
        buttonHandlers.put("Продолжить", event-> stage.fireEvent(new UserEvent(UserEventType.BACK_TO_GAME)));
        buttonHandlers.put("Настройки", event-> stage.fireEvent(new UserEvent(UserEventType.MENU_SETTINGS)));
        buttonHandlers.put("Выйти из игры", event-> stage.fireEvent(new UserEvent(UserEventType.MENU_EXIT)));
        pauseGameMenu = new Menu("Параметры игры", buttonHandlers, 0.3,0.2);
        pauseGameMenu.setMenuVisible(false);
    }

    private void initGameSettingsWindow(){
        settingsWindow = new SettingsWindow();
        settingsWindow.setSettingsVisible(false);
    }

    public void handle(UserEvent event) {
        switch (event.eventType) {
            case MENU_PLAY_BUTTON -> {
                mainMenu.setMenuVisible(false);
                gameStartMenu.setMenuVisible(true);
            }
            case START_GAME -> {
                gameStartMenu.setMenuVisible(false);
                lobbyNewGame.setVisible(false);
                lobbyExistingGame.setVisible(false);
            }
            case CREATE_NEW_ROOM -> {
                player.setInviteCode();
                lobbyNewGame.update();
                gameStartMenu.setMenuVisible(false);
                lobbyNewGame.setVisible(true);
            }
            case CONNECT_TO_EXISTING -> {
                lobbyExistingGame.update();
                gameStartMenu.setMenuVisible(false);
                lobbyExistingGame.setVisible(true);
            }
            case MENU_SETTINGS -> {
                mainMenu.setMenuVisible(false);
                pauseGameMenu.setMenuVisible(false);
                settingsWindow.setSettingsVisible(true);
            }
            case SHOW_MAIN_MENU -> {
                lobbyExistingGame.setVisible(false);
                lobbyNewGame.setVisible(false);
                mainMenu.setMenuVisible(true);
            }
            case BACK_TO_MENU -> {
                settingsWindow.setSettingsVisible(false);
                gameStartMenu.setMenuVisible(false);
                if (player.isConnected) pauseGameMenu.setMenuVisible(true);
                else mainMenu.setMenuVisible(true);
            }
            case GAME_MENU_OPEN -> {
                gameField.setVisible(false);
                pauseGameMenu.setMenuVisible(true);
            }
            case BACK_TO_GAME -> {
                pauseGameMenu.setMenuVisible(false);
                gameField.setVisible(true);
            }
            case MENU_EXIT -> stage.close();
        }
    }
}
