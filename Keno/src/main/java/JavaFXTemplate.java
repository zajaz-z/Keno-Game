import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import javafx.scene.image.ImageView;


public class JavaFXTemplate extends Application {

    //  Scenes and main stage
    private Scene welcomeScene, gameScene; // two main screens

    private Stage primaryStage; // main window reference

    //  Core game logic objects
    private BetCard betCard = new BetCard(); // stores selected numbers & handles max spots
    private KenoGameLogic gameLogic = new KenoGameLogic(); // generates random draws
    private PayoutTable payoutTable = new PayoutTable(); // calculates winnings
    private Player player = new Player(); // keeps track of total money

    //  UI elements
    private GridPane betGrid; // 8x10 grid of numbers
    private ToggleGroup spotGroup; // radio buttons for number of spots
    private Spinner<Integer> drawingSpinner; // how many rounds to play
    private Label selectedInfo, winningsLabel; // info display

    //  game state
    private int totalDrawings = 1; // how many rounds player picked
    private int currentDrawing = 0; // counter for current round

    //  themes for color/font changes
    private Theme[] themes = {
            new Theme(Color.ALICEBLUE, "#ffffff", "Arial", Color.DARKBLUE),
            new Theme(Color.ANTIQUEWHITE, "#ffeeee", "Verdana", Color.DARKRED),
            new Theme(Color.LAVENDER, "#f5f0ff", "Georgia", Color.PURPLE),
            new Theme(Color.BEIGE, "#fffaf0", "Trebuchet MS", Color.SADDLEBROWN),
            new Theme(Color.LIGHTBLUE, "#e6e6fa", "Arial", Color.RED),
            new Theme(Color.AZURE, "#f5f0ff", "Georgia", Color.PURPLE),
            new Theme(Color.BISQUE, "#fffaf0", "Trebuchet MS", Color.SADDLEBROWN),
            new Theme(Color.CADETBLUE, "#ffffff", "Brush Script MT", Color.DARKGREEN)
    };
    private int themeIndex = 0; // which theme is active
    private Theme currentTheme; // store active theme
    private BorderPane gameRootRef, welcomeRootRef; // keep roots to update theme later

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // save stage for later

        // WELCOME SCREEN
        VBox welcomeLayout = new VBox(20); // vertical layout with spacing
        welcomeLayout.setAlignment(Pos.CENTER);

        Label title = new Label("Keno Lottery Game");
        title.setFont(new Font("Times New Roman", 60)); // big title

        Button playButton = new Button("PLAY GAME");
        playButton.setFont(new Font(20)); // bigger text
        playButton.setOnAction(e -> switchToGameScene()); // move to game screen

        welcomeLayout.getChildren().addAll(title, playButton);

        BorderPane welcomeRoot = new BorderPane();
        welcomeRoot.setTop(createMenuBar(false)); // add menu bar (no 'New Look' here)
        welcomeRoot.setCenter(welcomeLayout);

        welcomeScene = new Scene(welcomeRoot, 900, 700); // main welcome scene





        // GAME SCREEN
        BorderPane gameRoot = new BorderPane();
        gameRoot.setTop(createMenuBar(true)); // menu bar w/ 'New Look'
        gameRoot.setCenter(createGameLayout()); // build main game layout
        gameRoot.setPadding(new Insets(10)); // padding around edges

        StackPane gameWrapper = new StackPane(gameRoot); // wrapper for overlay animations
        gameScene = new Scene(gameWrapper, 900, 700);

        //  theme setup
        gameRootRef = gameRoot; // store refs to change theme later
        welcomeRootRef = welcomeRoot;
        currentTheme = themes[themeIndex]; // start with first theme
        applyTheme(currentTheme); // apply initial theme

        // show window
        primaryStage.setTitle("Keno Lottery Game");
        primaryStage.setScene(welcomeScene); // start with welcome screen
        primaryStage.show();
    }

    /** Build main game layout */
    private VBox createGameLayout() {
        VBox layout = new VBox(15); // main vertical container
        layout.setAlignment(Pos.CENTER);

        //  spot selection
        Label spotLabel = new Label("Choose number of spots to play:");
        spotLabel.setFont(new Font("Verdana",20));
        spotGroup = new ToggleGroup();

        HBox spotsBox = new HBox(15); // horizontal row of radio buttons
        spotsBox.setAlignment(Pos.CENTER);

        for (int spots : new int[]{1, 4, 8, 10}) {
            RadioButton rb = new RadioButton(spots + " Spots");
            rb.setToggleGroup(spotGroup);
            rb.setUserData(spots); // store number of spots in button
            spotsBox.getChildren().add(rb);
        }

        //  number of drawings selection
        Label drawLabel = new Label("Number of drawings (1–4):");
        drawLabel.setFont(new Font("Verdana",20));

        drawingSpinner = new Spinner<>(1, 4, 1); // min 1, max 4, default 1
        drawingSpinner.setEditable(false);
        HBox drawBox = new HBox(10, drawLabel, drawingSpinner);
        drawBox.setAlignment(Pos.CENTER);

        //  number grid (8x10)
        betGrid = new GridPane();
        betGrid.setHgap(5);
        betGrid.setVgap(5);
        betGrid.setAlignment(Pos.CENTER);
        betGrid.setDisable(true); // disable until spot count picked

        int number = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                Button btn = new Button(String.valueOf(number++));
                btn.setPrefSize(60, 40);
                btn.setOnAction(e -> handleGridClick(btn)); // click logic
                betGrid.add(btn, col, row);
            }
        }

        //  enable grid when spot amount picked
        spotGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int spots = (int) newVal.getUserData();
                betCard.setMaxSpots(spots); // save max spots
                clearGridStyles(); // reset button colors
                betGrid.setDisable(false); // enable grid
                selectedInfo.setText("You can pick up to " + spots + " numbers."); // info label
            }
        });

        //  info labels
        selectedInfo = new Label("Select your spots and numbers to begin.");
        winningsLabel = new Label("Total Winnings: $0.00");
        winningsLabel.setFont(new Font("Verdana", 25));

        //  control buttons
        Button autoPickBtn = new Button("Auto Pick"); // pick numbers randomly
        autoPickBtn.setOnAction(e -> handleAutoPick());

        Button startBtn = new Button("Start Drawing"); // start all rounds
        startBtn.setOnAction(e -> startDrawingSequence());

        HBox controlBox = new HBox(20, autoPickBtn, startBtn);
        controlBox.setAlignment(Pos.CENTER);

        //  assemble layout
        layout.getChildren().addAll(spotLabel, spotsBox, drawBox, betGrid, selectedInfo, controlBox, winningsLabel);
        layout.setPadding(new Insets(10, 10, 20, 10));

        return layout;
    }

    /** click on a number button */
    private void handleGridClick(Button btn) {
        int num = Integer.parseInt(btn.getText()); // get number from button
        if (betCard.selectNumber(num)) { // toggle selection
            if (betCard.getSelectedNumbers().contains(num)) {
                btn.setStyle("-fx-background-color: #7be69d;"); // green if selected
            } else {
                btn.setStyle(""); // clear if deselected
            }
        }
        // update info
        selectedInfo.setText("Selected " + betCard.getSelectedNumbers().size() + " numbers.");
    }

    /** randomly pick numbers for player */
    private void handleAutoPick() {
        betCard.autoPick(); // pick numbers internally
        clearGridStyles(); // clear old colors
        for (var node : betGrid.getChildren()) { // update grid colors
            if (node instanceof Button b) {
                int n = Integer.parseInt(b.getText());
                if (betCard.getSelectedNumbers().contains(n))
                    b.setStyle("-fx-background-color: #6ccd8b;"); // green highlight
            }
        }
        selectedInfo.setText("Auto-picked " + betCard.getSelectedNumbers().size() + " numbers.");
    }

    /** start all drawing rounds */
    private void startDrawingSequence() {
        if (betCard.getSelectedNumbers().isEmpty()) {
            selectedInfo.setText("Pick your numbers first!");
            return;
        }

        totalDrawings = drawingSpinner.getValue(); // how many rounds
        currentDrawing = 0; // reset counter
        runSingleDrawingRound(); // start first round
    }

    /** one animated drawing round */
    private void runSingleDrawingRound() {
        currentDrawing++;
        clearGridStyles(); // reset grid colors

        List<Integer> draw = gameLogic.drawNumbers(); // 20 numbers
        Set<Integer> playerNums = betCard.getSelectedNumbers(); // player picks

        // overlay for dark background during draw
        StackPane overlay = new StackPane();
        Rectangle dim = new Rectangle(900, 700, Color.rgb(0, 0, 0, 0.6));
        dim.widthProperty().bind(gameScene.widthProperty());
        dim.heightProperty().bind(gameScene.heightProperty());

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);

        // labels
        Label header = new Label("Drawing " + currentDrawing + " of " + totalDrawings);
        header.setTextFill(Color.WHITE);
        header.setFont(new Font("Verdana", 36));

        Label yourNumsLabel = new Label("Your Numbers: " + playerNums);
        yourNumsLabel.setTextFill(Color.LIGHTGREEN);
        yourNumsLabel.setFont(new Font("Consolas", 26));

        Label drawnSoFarLabel = new Label("Drawn Numbers: ");
        drawnSoFarLabel.setTextFill(Color.LIGHTBLUE);
        drawnSoFarLabel.setFont(new Font("Consolas", 22));

        Label numDisplay = new Label();
        numDisplay.setTextFill(Color.GOLD);
        numDisplay.setFont(new Font("Consolas", 72)); // big number display

        content.getChildren().addAll(header, yourNumsLabel, drawnSoFarLabel, numDisplay);
        overlay.getChildren().addAll(dim, content);
        ((StackPane) gameScene.getRoot()).getChildren().add(overlay); // add overlay to scene

        //  drawing animation
        new Thread(() -> {
            Set<Integer> matched = new HashSet<>(); // track matches
            StringBuilder drawnList = new StringBuilder(); // for display

            for (int num : draw) {
                drawnList.append(num).append(" "); // build display text
                Platform.runLater(() -> {
                    drawnSoFarLabel.setText("Drawn Numbers: " + drawnList);
                    numDisplay.setText(String.valueOf(num));

                    // update grid highlights
                    for (var node : betGrid.getChildren()) {
                        if (node instanceof Button b && b.getText().equals(String.valueOf(num))) {
                            if (playerNums.contains(num)) {
                                b.setStyle("-fx-background-color: gold; -fx-font-weight: bold;"); // match
                                matched.add(num);
                            } else {
                                b.setStyle("-fx-background-color: lightgray;"); // just drawn
                            }
                        }
                    }
                });

                try { Thread.sleep(800); } catch (InterruptedException ignored) {} // pause animation
            }

            // calculate winnings
            double winnings = payoutTable.calculateWinnings(playerNums.size(), matched.size());
            player.addWinnings(winnings);

            Platform.runLater(() -> {
                numDisplay.setText("Matched " + matched.size() + " — Won $" + String.format("%.2f", winnings));
                winningsLabel.setText("Total Winnings: $" + String.format("%.2f", player.getTotalWinnings()));

                HBox btnBox = new HBox(20); // buttons below animation
                btnBox.setAlignment(Pos.CENTER);

                if (currentDrawing < totalDrawings) { // if more rounds
                    Button next = new Button("Next Drawing");
                    next.setOnAction(e -> {
                        ((StackPane) gameScene.getRoot()).getChildren().remove(overlay);
                        runSingleDrawingRound();
                    });
                    btnBox.getChildren().add(next);
                } else { // last round
                    Button newGame = new Button("New Game");
                    newGame.setOnAction(e -> {
                        ((StackPane) gameScene.getRoot()).getChildren().remove(overlay);
                        resetGame();
                    });
                    btnBox.getChildren().add(newGame);
                }

                content.getChildren().add(btnBox); // show buttons
            });
        }).start();
    }

    /** reset game state */
    private void resetGame() {
        betCard.setMaxSpots(0); // clear selection
        clearGridStyles();
        spotGroup.selectToggle(null); // clear radio buttons
        betGrid.setDisable(true);
        selectedInfo.setText("Select your spots to begin again.");
        //winningsLabel.setText("Total Winnings: $0.00");//NNot sure if total winnings sould be reset to 0 every game
    }

    /** clear all button colors */
    private void clearGridStyles() {
        for (var node : betGrid.getChildren()) {
            if (node instanceof Button b) b.setStyle("");
        }
    }

    /** build menu bar */
    private MenuBar createMenuBar(boolean includeNewLook) {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");


        MenuItem rules = new MenuItem("Rules of the Game");
        rules.setOnAction(e -> showRulesDialog());
        MenuItem odds = new MenuItem("Odds of Winning");
        odds.setOnAction(e -> showOddsDialog());
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> primaryStage.close());

        menu.getItems().addAll(rules, odds, new SeparatorMenuItem());



        if (includeNewLook) { // only on game screen
            MenuItem newLook = new MenuItem("New Look");
            newLook.setOnAction(e -> switchTheme());

            menu.getItems().addAll(newLook, new SeparatorMenuItem());
        }

        menu.getItems().add(exit);
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    /** switch to game screen */
    private void switchToGameScene() {
        primaryStage.setScene(gameScene);
    }

    /** show rules dialog */
    private void showRulesDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rules");
        alert.setHeaderText("How to Play Keno");
        alert.setContentText("• Pick 1, 4, 8, or 10 spots to play. Then choose the number of drawings you want.\n• 20 numbers are drawn from 1–80\n• The more matches, the higher your prize.");
        alert.showAndWait();
    }

    /** show odds dialog */
    private void showOddsDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Odds of Winning");
        alert.setHeaderText(null);
        alert.setContentText("1 Spot: 1 in 4.0\n4 Spots: 1 in 3.9\n8 Spots: 1 in 9.8\n10 Spots: 1 in 9.1");
        alert.showAndWait();
    }

    /** switch color/font theme */
    private void switchTheme() {
        themeIndex = (themeIndex + 1) % themes.length; // rotate theme
        currentTheme = themes[themeIndex];
        applyTheme(currentTheme);



        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Look");
        alert.setHeaderText("Theme Changed!");
        alert.showAndWait();
    }

    /** apply theme with fade animation */
    private void applyTheme(Theme theme) {
        applyFadeTransition(gameRootRef, theme.backgroundColor);

        applyFadeTransition(welcomeRootRef, theme.backgroundColor);
    }

    /** fade color transition */
    private void applyFadeTransition(Region region, Color newColor) {
        if (region == null) return;
        Color currentColor = Color.WHITE;
        Background bg = region.getBackground();
        if (bg != null && !bg.getFills().isEmpty()) {
            var fill = bg.getFills().get(0).getFill();
            if (fill instanceof Color c) currentColor = c;
        }

        var color = new javafx.beans.property.SimpleObjectProperty<>(currentColor);
        color.addListener((obs, oldVal, newVal) ->
                region.setBackground(new Background(new BackgroundFill(newVal, CornerRadii.EMPTY, Insets.EMPTY)))
        );

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(color, currentColor)),
                new KeyFrame(Duration.seconds(0.6), new KeyValue(color, newColor))
        );
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args); // start JavaFX
    }
}
