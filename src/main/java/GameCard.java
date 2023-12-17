import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Random;

public class GameCard extends GameObject{
    private HBox hBox;
    private GridPane gridPane;
    private int[][] gameCard;
    private static final int CARD_ROWS = 3;
    private static final int CARD_COLUMNS = 9;

    public GameCard() {
        gridPane = new GridPane();
        gridPane.getStyleClass().add("card-pane");
        hBox = new HBox(gridPane);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(Game.stage.getScene().getWidth());

        node = hBox;
        root.getChildren().add(node);

        initGameCard();
    }

    private void initGameCard(){
        int cellWidth = 50;
        int cellHeight = 50;
        gameCard = new int[3][9];

        for (int row = 0; row < CARD_ROWS; row++) {
            HashSet<Integer> randomColumns = generateNonRepeatingNumbers();
            for (int col = 0; col < CARD_COLUMNS; col++) {
                Rectangle rect = new Rectangle(cellWidth, cellHeight);

                StackPane stackPane = new StackPane(rect);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.BLACK);
                gridPane.add(stackPane, col, row);
                stackPane.toFront();
                stackPane.setOnMouseClicked(e->checkBarrel(((StackPane) e.getSource())));

                if (randomColumns.contains(col)) {
                    int number = getUniqueColumnNumber(row,col);
                    Label label = new Label(String.valueOf(number));

                    gameCard[row][col] = number;
                    label.getStyleClass().add("card-font");
                    stackPane.getChildren().add(label);
                }
                System.out.print(gameCard[row][col]+",");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void checkBarrel(StackPane stackPane){
        int row = GridPane.getRowIndex(stackPane);
        int col = GridPane.getColumnIndex(stackPane);

        System.out.print(row+"=="+col+"----"+gameCard[row][col]);
        root.fireEvent(new UserEvent(stackPane, UserEventType.CHECK_NUMBER_IN_TRAY, gameCard[row][col], null));
    }

    private HashSet<Integer> generateNonRepeatingNumbers(){
        HashSet<Integer> set = new HashSet<>();
        Random random = new Random();

        while (set.size() < 5){
            set.add(random.nextInt(9));
        }
        return set;
    }

    private int getUniqueColumnNumber(int row, int column){
        Random random = new Random();
        int number = -1;
        boolean isUniqueInColumn = false;

        while (!isUniqueInColumn){
            number = random.nextInt(column*10, column == 8 ? 91 : (column+1)*10);
            int temp_row = row;
            while (temp_row >= 0 && gameCard[temp_row][column] != number) temp_row--;
            if (temp_row == -1) isUniqueInColumn = true;
        }
        return number;
    }

    private StackPane findPane(int row, int column){
        for (Node child:gridPane.getChildren()){
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == column){
                return ((StackPane) child);
            }
        }
        return null;
    }

    private boolean isFull(){
        for (int i = 0; i<3; i++){
            for (int j = 0; j<9;j++){
                if (gameCard[i][j] != 0) return false;
            }
        }
        return true;
    }

    @Override
    public void handle(UserEvent event) {
        switch (event.eventType){
            case NUMBER_EXISTS -> {
                StackPane pane = ((StackPane) event.data);
                if (!gridPane.getChildren().contains(pane)) return;
                int row = GridPane.getRowIndex(pane);
                int col = GridPane.getColumnIndex(pane);

                gameCard[row][col] = 0;
                Circle circle = new Circle(pane.getHeight()/2.5);
                circle.setFill(Color.BLUE);
                pane.getChildren().add(circle);
                circle.toFront();
                if (isFull()) root.fireEvent(new UserEvent(UserEventType.PLAYER_WINS));
            }
            case MISSING_NUMBER -> {
                int number = ((int) event.data);
                int column = number == 90 ? 8 : number/10;
                for (int row = 0;row < 3;row++){
                    if (gameCard[row][column] == number) {
                        StackPane targetPane = findPane(row, column);
                        if (targetPane.getChildren().size() == 2)
                            ((Rectangle) findPane(row, column).getChildren().get(0)).setFill(Color.RED);
                    }
                }
            }
        }
    }
}
