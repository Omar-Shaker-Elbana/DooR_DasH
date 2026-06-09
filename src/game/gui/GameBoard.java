package game.gui;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Game;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.Random;

public class GameBoard {

    // Track previous energy values to drive the animated counter
    private int lastKnownPlayerEnergy   = -1;
    private int lastKnownOpponentEnergy = -1;

    private Scene scene;
    private Game game;
    private GridPane grid;
    private Main app;

    private Button pRollBtn, pPowerBtn, oRollBtn, oPowerBtn, exitBtn;
    private ImageView pDiceView, oDiceView;

    private VBox pCardBox, oCardBox;
    private ImageView pImageView, oImageView;
    private Label pName, pDesc, pEnergy, pPos;
    private Label oName, oDesc, oEnergy, oPos;
    private HBox pStatusBox, oStatusBox;
    private ProgressBar pEnergyBar, oEnergyBar;

    private Pane raceTrackPane;
    private Circle p1Marker;
    private Circle p2Marker;

    private ScrollPane consoleScroll;
    private VBox consoleBox;
    private TextFlow logTextFlow;

    private java.util.HashMap<String, Image> imageCache = new java.util.HashMap<>();
    private Rectangle[] highlights = new Rectangle[100];
    private StackPane[] cellPanes  = new StackPane[100];

    private Integer overrideCurrentMonsterPos = null;
    private Monster animatingMonster = null;

    private final int POWERUP_COST = 500;
    private boolean isVsComputer;

    private boolean isPaused = false;
    private StackPane pauseOverlay;
    private StackPane victoryOverlay;
    private Random random = new Random();

    // ─────────────────────────────────────────────────────────────────────────
    //  Ghost trail
    // ─────────────────────────────────────────────────────────────────────────
    private void addGhostTrail(StackPane cellStack) {
        if (animatingMonster == null) return;
        try {
            Image img = imageCache.get(animatingMonster.getName() + ".png");
            if (img == null) return;

            ImageView ghost = new ImageView(img);
            ghost.setFitWidth(36); ghost.setFitHeight(36);
            ghost.setOpacity(0.55);
            ghost.setMouseTransparent(true);

            boolean isP1 = animatingMonster.getName().equals(game.getPlayer().getName());
            ghost.setEffect(new DropShadow(12, Color.web(isP1 ? "#00b894" : "#e84393", 0.7)));
            StackPane.setAlignment(ghost, isP1 ? Pos.TOP_LEFT : Pos.BOTTOM_RIGHT);

            cellStack.getChildren().add(ghost);

            FadeTransition fade = new FadeTransition(Duration.millis(480), ghost);
            fade.setFromValue(0.55); fade.setToValue(0);
            fade.setDelay(Duration.millis(80));
            fade.setOnFinished(e -> cellStack.getChildren().remove(ghost));
            fade.play();
        } catch (Exception ignored) {}
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Possible moves preview
    // ─────────────────────────────────────────────────────────────────────────
    private void highlightPossibleMoves() {
        if (animatingMonster != null || game.getWinner() != null) return;

        boolean isP1Turn = game.getCurrent().getName().equals(game.getPlayer().getName());
        String hex = isP1Turn ? "#00b894" : "#e84393";
        int pos = game.getCurrent().getPosition();

        for (int roll = 1; roll <= 6; roll++) {
            int target = (pos + roll) % 100;
            Rectangle h = highlights[target];
            if (h == null) continue;

            h.setStroke(Color.web(hex, 0.65));
            h.setStrokeWidth(2.5);
            h.setFill(Color.web(hex, 0.08));
            h.setVisible(true);

            FadeTransition pulse = new FadeTransition(Duration.millis(560), h);
            pulse.setFromValue(0.18); pulse.setToValue(0.92);
            pulse.setCycleCount(6);
            pulse.setAutoReverse(true);
            pulse.setOnFinished(e -> { h.setVisible(false); h.setFill(Color.TRANSPARENT); });
            pulse.play();
        }
    }

    private void clearHighlights() {
        for (Rectangle h : highlights) {
            if (h != null) { h.setVisible(false); h.setFill(Color.TRANSPARENT); }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Animated energy counter
    // ─────────────────────────────────────────────────────────────────────────
    private void animateEnergyCount(Label label, int from, int to) {
        int duration = Math.min(900, Math.abs(to - from) * 2 + 200);
        int frames   = 28;
        String dirColor  = to >= from ? "#00ff88" : "#ff4444";
        String baseColor = "#66fcf1";

        Timeline t = new Timeline();
        for (int i = 0; i <= frames; i++) {
            double pct = (double) i / frames;
            int val = (int) (from + (to - from) * pct);
            t.getKeyFrames().add(new KeyFrame(Duration.millis(duration * pct), e -> {
                label.setText("Energy: " + val);
                label.setStyle("-fx-font-weight:bold;-fx-font-size:20px;-fx-text-fill:" + dirColor + ";");
            }));
        }
        t.setOnFinished(e -> {
            label.setText("Energy: " + to);
            label.setStyle("-fx-font-weight:bold;-fx-font-size:20px;-fx-text-fill:" + baseColor + ";");
        });
        t.play();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Constructor
    // ─────────────────────────────────────────────────────────────────────────
    public GameBoard(Main app, Monster p1, Monster p2, boolean isVsComputer) {
        this.app = app;
        this.isVsComputer = isVsComputer;

        try {
            if (isVsComputer) {
                game = new Game(p1);
            } else {
                game = new Game(p1, p2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 75%, #1f2833, #050608);");

        BorderPane root = new BorderPane();

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(2);
        grid.setVgap(2);
        root.setCenter(grid);

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(15, 0, 0, 0));

        exitBtn = new Button("EXIT TO MENU");
        exitBtn.setStyle("-fx-background-color: rgba(231, 76, 60, 0.8); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-color: #c0392b; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5 20;");
        exitBtn.setOnAction(e -> new StartMenu(app));
        addHoverEffect(exitBtn);

        topBar.getChildren().addAll(exitBtn);
        root.setTop(topBar);

        pCardBox = createPlayerCard("PLAYER 1", "#00b894", true);
        pDiceView = new ImageView(getDiceImage(1));
        pDiceView.setFitWidth(80); pDiceView.setFitHeight(80);

        pRollBtn = new Button("ROLL DICE (Q)");
        pRollBtn.setPrefWidth(200);
        addHoverEffect(pRollBtn);

        pPowerBtn = new Button("USE POWERUP (E)");
        pPowerBtn.setPrefWidth(200);
        addHoverEffect(pPowerBtn);

        pRollBtn.setOnAction(e -> handleRoll());
        pPowerBtn.setOnAction(e -> handlePowerup());

        pCardBox.getChildren().addAll(pDiceView, pRollBtn, pPowerBtn);
        root.setLeft(pCardBox);
        BorderPane.setMargin(pCardBox, new Insets(10, 10, 20, 20));

        String p2Title = isVsComputer ? "COMPUTER" : "PLAYER 2";
        oCardBox = createPlayerCard(p2Title, "#e84393", false);
        oDiceView = new ImageView(getDiceImage(1));
        oDiceView.setFitWidth(80); oDiceView.setFitHeight(80);

        oRollBtn = new Button("ROLL DICE (O)");
        oRollBtn.setPrefWidth(200);
        addHoverEffect(oRollBtn);

        oPowerBtn = new Button("USE POWERUP (P)");
        oPowerBtn.setPrefWidth(200);
        addHoverEffect(oPowerBtn);

        oRollBtn.setOnAction(e -> handleRoll());
        oPowerBtn.setOnAction(e -> handlePowerup());

        oCardBox.getChildren().add(oDiceView);
        if (!isVsComputer) oCardBox.getChildren().addAll(oRollBtn, oPowerBtn);

        root.setRight(oCardBox);
        BorderPane.setMargin(oCardBox, new Insets(10, 20, 20, 10));

        raceTrackPane = new Pane();
        raceTrackPane.setPrefSize(800, 20);
        raceTrackPane.setMaxWidth(800);
        raceTrackPane.setStyle("-fx-background-color: #0b0c10; -fx-border-color: #45a29e; -fx-border-radius: 10; -fx-background-radius: 10;");

        p1Marker = new Circle(8, Color.web("#00b894"));
        p1Marker.setCenterY(10);

        p2Marker = new Circle(8, Color.web("#e84393"));
        p2Marker.setCenterY(10);

        raceTrackPane.getChildren().addAll(p1Marker, p2Marker);

        consoleBox = new VBox(5);
        consoleBox.setPadding(new Insets(10));
        consoleBox.setStyle("-fx-background-color: rgba(11, 12, 16, 0.8);");
        logTextFlow = new TextFlow();
        consoleBox.getChildren().add(logTextFlow);

        consoleScroll = new ScrollPane(consoleBox);
        consoleScroll.setPrefSize(800, 100);
        consoleScroll.setMaxWidth(800);
        consoleScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        consoleScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        consoleScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: #45a29e; -fx-border-radius: 5;");

        consoleBox.heightProperty().addListener((observable, oldValue, newValue) -> consoleScroll.setVvalue(1.0));

        VBox bottomControlPanel = new VBox(15);
        bottomControlPanel.setAlignment(Pos.CENTER);
        bottomControlPanel.setPadding(new Insets(15));
        bottomControlPanel.setStyle("-fx-background-color: rgba(31, 40, 51, 0.9); -fx-border-color: #45a29e; -fx-border-width: 4px 0 0 0;");

        bottomControlPanel.getChildren().addAll(raceTrackPane, consoleScroll);
        root.setBottom(bottomControlPanel);

        mainContainer.getChildren().add(root);

        scene = app.getWindow().getScene();
        scene.setRoot(mainContainer);

        try {
            Image cursorImg = new Image(getClass().getResourceAsStream("/assets/cursor.png"));
            scene.setCursor(new javafx.scene.ImageCursor(cursorImg));
        } catch (Exception e) { System.out.println("Cursor asset not found."); }

        scene.setOnKeyPressed(e -> {
            if (isPaused && e.getCode() != KeyCode.ESCAPE) return;

            boolean p1Turn = game.getCurrent().getName().equals(game.getPlayer().getName());
            boolean p2Turn = !p1Turn;

            if (e.getCode() == KeyCode.ESCAPE) {
                e.consume();
                togglePause();
            } else if (e.getCode() == KeyCode.W) {
                game.getPlayer().setPosition(99);
                game.getPlayer().setEnergy(Math.max(game.getPlayer().getEnergy(), 1000));
                update();
                checkWinnerStatus();
            } else if (e.getCode() == KeyCode.L && isVsComputer) {
                game.getOpponent().setPosition(99);
                game.getOpponent().setEnergy(Math.max(game.getOpponent().getEnergy(), 1000));
                update();
                checkWinnerStatus();
            } else if (e.getCode() == KeyCode.Q && p1Turn && !pRollBtn.isDisabled()) {
                handleRoll();
            } else if (e.getCode() == KeyCode.E && p1Turn && !pPowerBtn.isDisabled()) {
                handlePowerup();
            } else if (e.getCode() == KeyCode.O && p2Turn && !isVsComputer && !oRollBtn.isDisabled()) {
                handleRoll();
            } else if (e.getCode() == KeyCode.P && p2Turn && !isVsComputer && !oPowerBtn.isDisabled()) {
                handlePowerup();
            }
        });

        Platform.runLater(() -> app.getWindow().setFullScreen(true));

        appendToConsole("System", "Game Started! " + game.getCurrent().getName() + " turns first.", "#f1c40f");
        update();

        if (isVsComputer && !game.getCurrent().getName().equals(game.getPlayer().getName())) {
            appendToConsole("System", "Computer is calculating its first move...", "#888888");
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> handleRoll());
            pause.play();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────────────
    private Cell getCellAt(int position) {
        if (position < 0 || position > 99) return null;
        int r = position / 10;
        int c = (r % 2 == 0) ? (position % 10) : (9 - (position % 10));
        return game.getBoard().getBoardCells()[r][c];
    }

    private void addHoverEffect(Button btn) {
        btn.setOnMouseEntered(e -> { if (!btn.isDisabled()) btn.setOpacity(0.7); });
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
    }

    private Image getDiceImage(int number) {
        String dName = "dice" + number + ".png";
        if (!imageCache.containsKey(dName)) {
            imageCache.put(dName, new Image(getClass().getResourceAsStream("/assets/" + dName), 120, 120, true, true));
        }
        return imageCache.get(dName);
    }

    private void appendToConsole(String source, String message, String colorHex) {
        Text time = new Text(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + " ");
        time.setFill(Color.GRAY);
        time.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");

        Text src = new Text("[" + source + "] ");
        src.setFill(Color.web(colorHex));
        src.setStyle("-fx-font-weight: bold; -fx-font-family: monospace; -fx-font-size: 13px;");

        Text msg = new Text(message + "\n");
        msg.setFill(Color.WHITE);
        msg.setStyle("-fx-font-family: monospace; -fx-font-size: 13px;");

        Platform.runLater(() -> logTextFlow.getChildren().addAll(time, src, msg));
    }

    private VBox createPlayerCard(String title, String colorHex, boolean isPlayer) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(280);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(31, 40, 51, 0.8); -fx-border-color: " + colorHex + "; -fx-border-width: 3px; -fx-background-radius: 15; -fx-border-radius: 15;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: " + colorHex + ";");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120); imageView.setFitHeight(120);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 15, 0, 0, 5);");
        if (isPlayer) pImageView = imageView; else oImageView = imageView;

        Label nameLabel = new Label();
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white; -fx-text-alignment: center;");
        if (isPlayer) pName = nameLabel; else oName = nameLabel;

        Label descLabel = new Label();
        descLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #bdc3c7; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(260);
        descLabel.setMinHeight(50);
        if (isPlayer) pDesc = descLabel; else oDesc = descLabel;

        Label energyLabel = new Label();
        energyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #66fcf1;");
        if (isPlayer) pEnergy = energyLabel; else oEnergy = energyLabel;

        ProgressBar energyBar = new ProgressBar(0);
        energyBar.setPrefWidth(200); energyBar.setPrefHeight(15);
        if (isPlayer) pEnergyBar = energyBar; else oEnergyBar = energyBar;

        Label posLabel = new Label();
        posLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #c5c6c7;");
        if (isPlayer) pPos = posLabel; else oPos = posLabel;

        HBox statusBox = new HBox(5);
        statusBox.setAlignment(Pos.CENTER); statusBox.setMinHeight(40);
        if (isPlayer) pStatusBox = statusBox; else oStatusBox = statusBox;

        card.getChildren().addAll(titleLabel, imageView, nameLabel, descLabel, energyBar, energyLabel, posLabel, statusBox);
        return card;
    }

    private Label createBadge(String text, String color) {
        Label badge = new Label(text);
        badge.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 12;");
        return badge;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  handleRoll  (frozen-turn uses Toast instead of modal popup)
    // ─────────────────────────────────────────────────────────────────────────
    private void handleRoll() {
        clearHighlights();

        Monster actingMonster = game.getCurrent();
        animatingMonster = actingMonster;
        int startPos  = actingMonster.getPosition();
        int oldEnergy = actingMonster.getEnergy();
        boolean wasFrozen = actingMonster.isFrozen();

        String pColor = actingMonster.getName().equals(game.getPlayer().getName()) ? "#00b894" : "#e84393";

        pRollBtn.setDisable(true); pPowerBtn.setDisable(true);
        oRollBtn.setDisable(true); oPowerBtn.setDisable(true);

        // ── Frozen turn: toast, no modal ─────────────────────────────────────
        if (wasFrozen) {
            try { game.playTurn(); } catch (Exception e) {}
            appendToConsole(actingMonster.getName(), "Is FROZEN and skips their turn!", "#00cec9");
            ToastNotification.show((StackPane) scene.getRoot(),
                "Turn Skipped",
                actingMonster.getName() + " was frozen and loses this turn!",
                ToastNotification.Level.INFO);
            finishTurn();
            return;
        }

        try {
            game.playTurn();
            int actualRoll = game.getLastRoll();
            int finalPos   = actingMonster.getPosition();

            appendToConsole(actingMonster.getName(), "Rolled a " + actualRoll, pColor);

            int stepAmount  = (actingMonster instanceof game.engine.monsters.Dasher) ? (actualRoll * 2) : actualRoll;
            int expectedPos = startPos + stepAmount;
            if (expectedPos > 99) expectedPos = 99;

            boolean usedTransport = false;
            if (expectedPos != finalPos && expectedPos < 100) {
                Cell cell = getCellAt(expectedPos);
                if (cell instanceof TransportCell) {
                    int dest = expectedPos + ((TransportCell) cell).getEffect();
                    if (dest == finalPos) usedTransport = true;
                }
            }

            final int animIntermediatePos = usedTransport ? expectedPos : finalPos;
            final boolean isTransportJump = usedTransport;

            ImageView activeDiceView = actingMonster.getName().equals(game.getPlayer().getName()) ? pDiceView : oDiceView;
            VBox activeCardBox = actingMonster.getName().equals(game.getPlayer().getName()) ? pCardBox : oCardBox;

            showInlineDiceAnimation(activeDiceView, actualRoll, () -> {
                animateStepping(startPos, animIntermediatePos, () -> {
                    if (isTransportJump) {
                        Cell landedCell = getCellAt(animIntermediatePos);
                        appendToConsole("Board",
                            actingMonster.getName() + " landed on a " +
                            (landedCell instanceof ConveyorBelt ? "Conveyor Belt" : "Contamination Sock") +
                            " and transports to " + finalPos, "#f1c40f");

                        highlights[finalPos].setStroke(finalPos < animIntermediatePos ? Color.web("#e74c3c") : Color.web("#66fcf1"));
                        highlights[finalPos].setStrokeWidth(6);

                        Timeline flashTimeline = new Timeline(
                            new KeyFrame(Duration.ZERO,         new KeyValue(highlights[finalPos].visibleProperty(), true)),
                            new KeyFrame(Duration.millis(200),  new KeyValue(highlights[finalPos].visibleProperty(), false)),
                            new KeyFrame(Duration.millis(400),  new KeyValue(highlights[finalPos].visibleProperty(), true))
                        );
                        flashTimeline.setCycleCount(3);
                        flashTimeline.play();

                        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
                        pause.setOnFinished(e -> {
                            highlights[finalPos].setVisible(false);
                            overrideCurrentMonsterPos = finalPos;
                            drawBoard();
                            processEndTurn(actingMonster, oldEnergy, activeCardBox, finalPos);
                        });
                        pause.play();
                    } else {
                        processEndTurn(actingMonster, oldEnergy, activeCardBox, finalPos);
                    }
                });
            });

        } catch (InvalidMoveException ex) {
            SoundManager.playSound("collision.wav");
            Monster defender = actingMonster.getName().equals(game.getPlayer().getName()) ? game.getOpponent() : game.getPlayer();
            appendToConsole("System", "Invalid Move! " + actingMonster.getName() + " collided with " + defender.getName(), "#e74c3c");

            if (isVsComputer && actingMonster.getName().equals(game.getOpponent().getName())) {
                PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
                pause.setOnFinished(e -> handleRoll());
                pause.play();
            } else {
                showCustomPopup("Invalid Move", "Cell occupied by " + defender.getName() + "!\nTurn forfeited.", this::finishTurn);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            appendToConsole("Error", ex.getMessage(), "#e74c3c");
            showCustomPopup("Logic Error", "Error: " + ex.getMessage(), this::finishTurn);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  processEndTurn  (encounters use Toast; card draw uses CardRevealAnimation)
    // ─────────────────────────────────────────────────────────────────────────
    private void processEndTurn(Monster actingMonster, int oldEnergy, VBox activeCardBox, int finalPos) {
        Cell landedCell = getCellAt(finalPos);

        // ── Monster encounter ─────────────────────────────────────────────────
        if (landedCell instanceof MonsterCell) {
            Monster stationed = null;
            for (int i = 0; i < Constants.MONSTER_CELL_INDICES.length; i++) {
                if (finalPos == Constants.MONSTER_CELL_INDICES[i] && i < Board.getStationedMonsters().size()) {
                    stationed = Board.getStationedMonsters().get(i); break;
                }
            }
            if (stationed != null) {
                if (stationed.getRole() == actingMonster.getRole()) {
                    appendToConsole("Encounter", actingMonster.getName() + " met ally " + stationed.getName() + "!", "#3498db");
                    ToastNotification.show((StackPane) scene.getRoot(),
                        "Ally Encountered!",
                        actingMonster.getName() + " met " + stationed.getName() + " — Powerup activated!",
                        ToastNotification.Level.SUCCESS);
                } else if (oldEnergy > stationed.getEnergy()) {
                    appendToConsole("Encounter", "Energy swapped between " + actingMonster.getName() + " and " + stationed.getName(), "#e74c3c");
                    triggerCameraShake();
                    ToastNotification.show((StackPane) scene.getRoot(),
                        "Energy Swap!",
                        actingMonster.getName() + " lost energy to " + stationed.getName(),
                        ToastNotification.Level.DANGER);
                }
            }
        }

        // ── Door cell console log ─────────────────────────────────────────────
        if (landedCell instanceof DoorCell) {
            DoorCell dc = (DoorCell) landedCell;
            boolean wasShielded = actingMonster.isShielded();
            if (actingMonster.getRole() == dc.getRole() || !wasShielded) {
                for (Monster m : Board.getStationedMonsters()) {
                    if (m.getRole() == actingMonster.getRole()) {
                        int gain = dc.getRole() == m.getRole() ? dc.getEnergy() : -dc.getEnergy();
                        if (gain != 0) {
                            String color = gain > 0 ? "#00b894" : "#e74c3c";
                            appendToConsole("Door", " -> " + m.getName() + " got " + gain + " energy!", color);
                        }
                    }
                }
            }
        }

        // ── Energy diff feedback ──────────────────────────────────────────────
        int newEnergy  = actingMonster.getEnergy();
        int energyDiff = newEnergy - oldEnergy;

        if (energyDiff != 0 && finalPos < 100 && cellPanes[finalPos] != null) {
            showFloatingNumber(cellPanes[finalPos], energyDiff);
        }
        if (energyDiff > 0) {
            SoundManager.playSound("gain.wav");
            flashCard(activeCardBox, Color.web("#00b894"));
            appendToConsole(actingMonster.getName(), "Gained " + energyDiff + " Energy.", "#00b894");
        } else if (energyDiff < 0) {
            SoundManager.playSound("lose.wav");
            flashCard(activeCardBox, Color.web("#e74c3c"));
            appendToConsole(actingMonster.getName(), "Lost " + Math.abs(energyDiff) + " Energy.", "#e74c3c");
        }

        // ── Card draw: 3D animated reveal ─────────────────────────────────────
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Cell cell = game.getBoard().getBoardCells()[r][c];
                if (cell instanceof CardCell) {
                    Card drawnCard = ((CardCell) cell).getLastDrawnCard();
                    if (drawnCard != null) {
                        ((CardCell) cell).clearLastDrawnCard();
                        appendToConsole("Card",
                            actingMonster.getName() + " drew: " + drawnCard.getName()
                            + " — " + drawnCard.getDescription(),
                            CardRevealAnimation.colorFor(drawnCard));
                        StackPane rootContainer = (StackPane) scene.getRoot();
                        CardRevealAnimation.reveal(rootContainer, drawnCard, this::finishTurn);
                        return;
                    }
                }
            }
        }

        finishTurn();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Visual helpers
    // ─────────────────────────────────────────────────────────────────────────
    private void showFloatingNumber(StackPane cellStack, int amount) {
        Label floatLabel = new Label((amount > 0 ? "+" : "") + amount);
        floatLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " +
            (amount > 0 ? "#00ff00" : "#ff3333") + "; -fx-effect: dropshadow(two-pass-box, black, 4, 1.0, 0, 0);");

        cellStack.getChildren().add(floatLabel);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.5), floatLabel);
        tt.setByY(-60);
        FadeTransition ft = new FadeTransition(Duration.seconds(1.5), floatLabel);
        ft.setFromValue(1.0); ft.setToValue(0.0);
        tt.play(); ft.play();
        ft.setOnFinished(e -> cellStack.getChildren().remove(floatLabel));
    }

    private void flashCard(VBox card, Color flashColor) {
        String originalStyle = card.getStyle();
        String rgba = String.format("rgba(%d, %d, %d, 0.4)",
            (int)(flashColor.getRed() * 255),
            (int)(flashColor.getGreen() * 255),
            (int)(flashColor.getBlue() * 255));
        card.setStyle(originalStyle + "; -fx-background-color: " + rgba + ";");
        PauseTransition pt = new PauseTransition(Duration.millis(500));
        pt.setOnFinished(e -> card.setStyle(originalStyle));
        pt.play();
    }

    private void showInlineDiceAnimation(ImageView diceView, int actualRoll, Runnable onFinished) {
        SoundManager.playSound("dice.wav");
        Timeline timeline = new Timeline();
        for (int i = 0; i < 15; i++) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(80 * i),
                e -> diceView.setImage(getDiceImage((int)(Math.random() * 6) + 1))));
        }
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(80 * 15), e -> {
            diceView.setImage(getDiceImage(actualRoll));
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(event -> onFinished.run());
            delay.play();
        }));
        timeline.play();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  animateStepping  (adaptive speed + ghost trail + per-step highlight)
    // ─────────────────────────────────────────────────────────────────────────
    private void animateStepping(int from, int to, Runnable onFinished) {
        int steps = (to - from + 100) % 100;
        if (steps == 0) { onFinished.run(); return; }

        int intervalMs = Math.max(110, 255 - steps * 9);

        Timeline timeline = new Timeline();
        for (int i = 1; i <= steps; i++) {
            final int cellIdx = (from + i) % 100;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis((long) intervalMs * i), e -> {
                if (overrideCurrentMonsterPos != null && cellPanes[overrideCurrentMonsterPos] != null)
                    addGhostTrail(cellPanes[overrideCurrentMonsterPos]);

                Rectangle h = highlights[cellIdx];
                if (h != null) {
                    boolean isP1 = animatingMonster != null &&
                                   animatingMonster.getName().equals(game.getPlayer().getName());
                    String stepHex = isP1 ? "#00b894" : "#e84393";
                    h.setStroke(Color.web(stepHex));
                    h.setStrokeWidth(4);
                    h.setFill(Color.web(stepHex, 0.18));
                    h.setVisible(true);
                    FadeTransition flash = new FadeTransition(Duration.millis((long) intervalMs * 2), h);
                    flash.setFromValue(1); flash.setToValue(0);
                    flash.setOnFinished(fe -> { h.setVisible(false); h.setFill(Color.TRANSPARENT); });
                    flash.play();
                }

                overrideCurrentMonsterPos = cellIdx;
                drawBoard();
            }));
        }
        timeline.setOnFinished(e -> onFinished.run());
        timeline.play();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  finishTurn  (triggers possible-moves preview for next player)
    // ─────────────────────────────────────────────────────────────────────────
    private void finishTurn() {
        overrideCurrentMonsterPos = null;
        animatingMonster = null;
        update();
        checkWinnerStatus();

        if (game.getWinner() == null) {
            boolean isP1Turn = game.getCurrent().getName().equals(game.getPlayer().getName());
            if (!isP1Turn && isVsComputer) {
                appendToConsole("System", "Computer is calculating its move...", "#888888");
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(e -> handleRoll());
                pause.play();
            } else {
                updateControlsVisibility();
                Platform.runLater(this::highlightPossibleMoves);
            }
        }
    }

    private void updateControlsVisibility() {
        boolean p1Turn = game.getCurrent().getName().equals(game.getPlayer().getName());
        boolean p2Turn = !p1Turn;

        pRollBtn.setDisable(!p1Turn);
        styleButton(pRollBtn, p1Turn, "#00b894");

        boolean p1CanPower = p1Turn && game.getPlayer().getEnergy() >= POWERUP_COST;
        pPowerBtn.setDisable(!p1CanPower);
        styleButton(pPowerBtn, p1CanPower, "#f1c40f");

        if (!isVsComputer) {
            oRollBtn.setDisable(!p2Turn);
            styleButton(oRollBtn, p2Turn, "#e84393");

            boolean p2CanPower = p2Turn && game.getOpponent().getEnergy() >= POWERUP_COST;
            oPowerBtn.setDisable(!p2CanPower);
            styleButton(oPowerBtn, p2CanPower, "#f1c40f");
        }
    }

    private void styleButton(Button btn, boolean isEnabled, String activeColor) {
        if (isEnabled) {
            btn.setStyle("-fx-background-color: " + activeColor + "; -fx-text-fill: " +
                (activeColor.equals("#f1c40f") ? "#2c3e50" : "white") +
                "; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10; -fx-background-radius: 8;");
        } else {
            btn.setStyle("-fx-background-color: #555555; -fx-text-fill: #888888; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10; -fx-background-radius: 8;");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  handlePowerup  (success/error use Toast instead of modal popup)
    // ─────────────────────────────────────────────────────────────────────────
    private void handlePowerup() {
        try {
            game.usePowerup();
            SoundManager.playSound("powerup.wav");
            String pColor = game.getCurrent().getName().equals(game.getPlayer().getName()) ? "#00b894" : "#e84393";
            appendToConsole(game.getCurrent().getName(), "Activated Special Powerup!", pColor);
            triggerCameraShake();
            ToastNotification.show((StackPane) scene.getRoot(),
                "Powerup Activated!",
                game.getCurrent().getName() + " used their special ability.",
                ToastNotification.Level.POWER);
            update();
        } catch (OutOfEnergyException ex) {
            ToastNotification.show((StackPane) scene.getRoot(),
                "Not Enough Energy",
                ex.getMessage(),
                ToastNotification.Level.WARN);
        } catch (Throwable ex) {
            showCustomPopup("Logic Error", "Error: " + ex.getMessage(), null);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  update  (adds animated energy counter after updateCardData calls)
    // ─────────────────────────────────────────────────────────────────────────
    private void update() {
        drawBoard();

        int p1Score = game.getPlayer().getPosition() * 10 + game.getPlayer().getEnergy();
        int p2Score = game.getOpponent().getPosition() * 10 + game.getOpponent().getEnergy();

        String p1Title = (p1Score >= p2Score) ? "(LEAD) PLAYER 1" : "PLAYER 1";
        String p2Title = isVsComputer ? "COMPUTER" : "PLAYER 2";
        if (p2Score > p1Score) p2Title = "(LEAD) " + p2Title;

        boolean p1Turn = game.getCurrent().getName().equals(game.getPlayer().getName());

        updateCardData(game.getPlayer(),   pCardBox, pImageView, pName, pDesc, pEnergyBar, pEnergy, pPos, pStatusBox, p1Title,  p1Turn,  "#00b894");
        updateCardData(game.getOpponent(), oCardBox, oImageView, oName, oDesc, oEnergyBar, oEnergy, oPos, oStatusBox, p2Title, !p1Turn, "#e84393");

        // ── Animated energy counters ──────────────────────────────────────────
        int pNewE = game.getPlayer().getEnergy();
        int oNewE = game.getOpponent().getEnergy();

        if (lastKnownPlayerEnergy >= 0 && pNewE != lastKnownPlayerEnergy) {
            pEnergy.setText("Energy: " + lastKnownPlayerEnergy);   // reset before animating
            animateEnergyCount(pEnergy, lastKnownPlayerEnergy, pNewE);
        }
        if (lastKnownOpponentEnergy >= 0 && oNewE != lastKnownOpponentEnergy) {
            oEnergy.setText("Energy: " + lastKnownOpponentEnergy);
            animateEnergyCount(oEnergy, lastKnownOpponentEnergy, oNewE);
        }

        lastKnownPlayerEnergy   = pNewE;
        lastKnownOpponentEnergy = oNewE;
        // ─────────────────────────────────────────────────────────────────────

        p1Marker.setCenterX(15 + (game.getPlayer().getPosition()   / 99.0) * 770);
        p2Marker.setCenterX(15 + (game.getOpponent().getPosition() / 99.0) * 770);

        if (animatingMonster == null) updateControlsVisibility();
    }

    private void updateCardData(Monster m, VBox cardBox, ImageView iv, Label name, Label desc,
                                ProgressBar bar, Label energy, Label pos, HBox statusBox,
                                String crownTitle, boolean isTurn, String glowColor) {
        if (isTurn) {
            cardBox.setEffect(new DropShadow(40, Color.web(glowColor)));
        } else {
            cardBox.setEffect(null);
        }

        try {
            String imgName = m.getName() + ".png";
            Image img = imageCache.get("HD_" + imgName);
            if (img == null) {
                img = new Image(getClass().getResourceAsStream("/assets/" + imgName), 200, 200, true, true);
                imageCache.put("HD_" + imgName, img);
            }
            iv.setImage(img);
        } catch (Exception e) {}

        if (m.isConfused()) {
            name.setText(crownTitle + "\n" + m.getName() + "\n(Role: " + m.getRole() + " - SWAPPED!)");
            name.setTextFill(Color.web("#9b59b6"));
        } else {
            name.setText(crownTitle + "\n" + m.getName() + " (" + m.getRole() + ")");
            name.setTextFill(Color.WHITE);
        }

        String passiveAbility = "";
        if (m instanceof game.engine.monsters.MultiTasker) {
            passiveAbility = "\n[ MultiTasker ] +200 Bonus on changes.";
        } else if (m instanceof game.engine.monsters.Dasher) {
            passiveAbility = "\n[ Dasher ] Moves double the dice roll.";
        } else if (m instanceof game.engine.monsters.Dynamo) {
            passiveAbility = "\n[ Dynamo ] Doubles all gains & losses.";
        } else if (m instanceof game.engine.monsters.Schemer) {
            passiveAbility = "\n[ Schemer ] Steals energy from rivals.";
        }

        desc.setText("*" + m.getDescription() + "*" + passiveAbility);

        energy.setText("Energy: " + m.getEnergy());
        pos.setText("Position: " + m.getPosition());

        double fillProgress = Math.min(1.0, Math.max(0, m.getEnergy() / 1000.0));
        bar.setProgress(fillProgress);
        String barColor = m.getRole() == Role.SCARER ? "#e74c3c" : "#f1c40f";
        bar.setStyle("-fx-accent: " + barColor + "; -fx-control-inner-background: #1a1a2e;");

        statusBox.getChildren().clear();
        if (m.isConfused()) statusBox.getChildren().add(createBadge("Confused (" + m.getConfusionTurns() + ")", "#9b59b6"));
        if (m.isShielded()) statusBox.getChildren().add(createBadge("Shielded", "#3498db"));
        if (m.isFrozen())   statusBox.getChildren().add(createBadge("Frozen", "#00cec9"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  drawBoard  (adds START badge on cell 0, FINISH badge on cell 99)
    // ─────────────────────────────────────────────────────────────────────────
    private void drawBoard() {
        grid.getChildren().clear();
        Cell[][] cells = game.getBoard().getBoardCells();

        int pDrawPos = game.getPlayer().getPosition();
        int oDrawPos = game.getOpponent().getPosition();

        if (animatingMonster != null && overrideCurrentMonsterPos != null) {
            if (animatingMonster.getName().equals(game.getPlayer().getName()))   pDrawPos = overrideCurrentMonsterPos;
            else if (animatingMonster.getName().equals(game.getOpponent().getName())) oDrawPos = overrideCurrentMonsterPos;
        }

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                int index      = (r % 2 == 0) ? (r * 10 + c) : (r * 10 + (9 - c));
                int displayCol = c;
                int displayRow = 9 - r;

                Cell cell = cells[r][c];
                StackPane cellStack = new StackPane();
                cellPanes[index] = cellStack;

                Button b = new Button();
                b.setPrefSize(75, 75);

                boolean isDark  = (r + displayCol) % 2 == 0;
                String bgColor  = isDark ? "rgba(31, 40, 51, 0.6)" : "rgba(11, 12, 16, 0.6)";
                b.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 0;");

                String tooltipText  = "Cell " + index + "\nType: Regular";
                String cellImageName = null;

                if (cell instanceof DoorCell) {
                    cellImageName = "door.png";
                    DoorCell dc = (DoorCell) cell;
                    tooltipText = "Door Cell " + index + "\nRole: " + dc.getRole() + "\nEnergy: " + dc.getEnergy();
                } else if (cell instanceof CardCell) {
                    cellImageName = "card.png";
                    tooltipText = "Special Card Cell\nDraws a random card.";
                    if (animatingMonster == null) {
                        ScaleTransition st = new ScaleTransition(Duration.seconds(1), b);
                        st.setByX(0.05); st.setByY(0.05);
                        st.setCycleCount(Animation.INDEFINITE); st.setAutoReverse(true);
                        st.play();
                    }
                } else if (cell instanceof ContaminationSock) {
                    cellImageName = "sock.png";
                    tooltipText = "Contamination Sock!\nPushes you backwards.";
                } else if (cell instanceof ConveyorBelt) {
                    cellImageName = "conveyor.png";
                    tooltipText = "Conveyor Belt\nPushes you " + ((TransportCell) cell).getEffect() + " steps forward.";
                } else if (cell instanceof MonsterCell) {
                    Monster stationed = null;
                    for (int i = 0; i < Constants.MONSTER_CELL_INDICES.length; i++) {
                        if (index == Constants.MONSTER_CELL_INDICES[i] && i < Board.getStationedMonsters().size()) {
                            stationed = Board.getStationedMonsters().get(i); break;
                        }
                    }
                    if (stationed != null) {
                        tooltipText = "Monster Cell\nStationed: " + stationed.getName() + "\nRole: " + stationed.getRole();
                        try {
                            String imgName = stationed.getName() + ".png";
                            Image mImg = imageCache.get("cell_" + imgName);
                            if (mImg == null) {
                                mImg = new Image(getClass().getResourceAsStream("/assets/" + imgName), 100, 100, true, true);
                                imageCache.put("cell_" + imgName, mImg);
                            }
                            ImageView mIcon = new ImageView(mImg);
                            mIcon.setFitWidth(65); mIcon.setFitHeight(65);
                            applyStatusAura(stationed, mIcon);
                            b.setGraphic(mIcon);
                            cellStack.getChildren().add(b);

                            Label nameBadge = new Label(stationed.getName().split(" ")[0]);
                            nameBadge.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-font-size: 9px; -fx-font-weight: bold; -fx-padding: 2 4; -fx-background-radius: 3;");
                            StackPane.setAlignment(nameBadge, Pos.BOTTOM_CENTER);
                            StackPane.setMargin(nameBadge, new Insets(0, 0, 5, 0));
                            cellStack.getChildren().add(nameBadge);
                        } catch (Exception e) {}
                    } else cellImageName = "monster_cell.png";
                }

                if (animatingMonster == null) {
                    Tooltip t = new Tooltip(tooltipText);
                    t.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                    Tooltip.install(b, t);
                }

                if (cellImageName != null && !(cell instanceof MonsterCell)) {
                    try {
                        Image cellImg = imageCache.get(cellImageName);
                        if (cellImg == null) {
                            cellImg = new Image(getClass().getResourceAsStream("/assets/" + cellImageName), 100, 100, true, true);
                            imageCache.put(cellImageName, cellImg);
                        }
                        ImageView cellIcon = new ImageView(cellImg);
                        cellIcon.setFitWidth(65); cellIcon.setFitHeight(65);
                        b.setGraphic(cellIcon);
                        cellStack.getChildren().add(b);

                        if (cell instanceof DoorCell && !((DoorCell) cell).isActivated()) {
                            DoorCell dc = (DoorCell) cell;
                            String roleColor = dc.getRole() == Role.SCARER ? "#e84393" : "#00b894";
                            Label doorBadge = new Label((dc.getRole() == Role.SCARER ? "S" : "L") + " | " + dc.getEnergy());
                            doorBadge.setStyle("-fx-background-color: " + roleColor + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 4; -fx-background-radius: 4;");
                            StackPane.setAlignment(doorBadge, Pos.BOTTOM_CENTER);
                            StackPane.setMargin(doorBadge, new Insets(0, 0, 5, 0));
                            cellStack.getChildren().add(doorBadge);
                        } else if (cell instanceof DoorCell && ((DoorCell) cell).isActivated()) {
                            cellIcon.setOpacity(0.3);
                        }

                        if (cell instanceof TransportCell) {
                            int effect = ((TransportCell) cell).getEffect();
                            int dest   = index + effect;
                            if (dest >= 0 && dest < 100 && effect != 0) {
                                Label beltBadge = new Label("To " + dest);
                                beltBadge.setStyle("-fx-background-color: " + (effect > 0 ? "#00b894" : "#e74c3c") + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 4; -fx-background-radius: 4;");
                                StackPane.setAlignment(beltBadge, Pos.BOTTOM_CENTER);
                                StackPane.setMargin(beltBadge, new Insets(0, 0, 5, 0));
                                cellStack.getChildren().add(beltBadge);
                            }
                        }
                    } catch (Exception e) {}
                } else if (!(cell instanceof MonsterCell)) {
                    cellStack.getChildren().add(b);
                }

                Label numLabel = new Label(String.valueOf(index));
                numLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.4); -fx-font-size: 11px; -fx-font-weight: bold;");
                StackPane.setAlignment(numLabel, Pos.TOP_RIGHT);
                StackPane.setMargin(numLabel, new Insets(2, 4, 0, 0));
                cellStack.getChildren().add(numLabel);

                Rectangle highlight = new Rectangle(75, 75);
                highlight.setFill(Color.TRANSPARENT);
                highlight.setStrokeWidth(4);
                highlight.setVisible(false);
                highlights[index] = highlight;
                cellStack.getChildren().add(highlight);

                if (pDrawPos == index) addMonsterToCell(cellStack, game.getPlayer(), Pos.TOP_LEFT);
                if (oDrawPos == index) addMonsterToCell(cellStack, game.getOpponent(), Pos.BOTTOM_RIGHT);

                // ── START / FINISH badges ─────────────────────────────────────
                if (index == 0) {
                    b.setStyle(b.getStyle() + ";-fx-background-color:rgba(46,204,113,0.18);");
                    Label startBadge = new Label("START");
                    startBadge.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;" +
                        "-fx-font-size:9px;-fx-font-weight:bold;-fx-padding:2 5;-fx-background-radius:3;");
                    StackPane.setAlignment(startBadge, Pos.TOP_LEFT);
                    StackPane.setMargin(startBadge, new Insets(3, 0, 0, 3));
                    cellStack.getChildren().add(startBadge);
                }
                if (index == 99) {
                    b.setStyle(b.getStyle() + ";-fx-background-color:rgba(241,196,15,0.22);");
                    Label finishBadge = new Label("FINISH \u2605");
                    finishBadge.setStyle("-fx-background-color:#f1c40f;-fx-text-fill:#1a1a2e;" +
                        "-fx-font-size:9px;-fx-font-weight:bold;-fx-padding:2 5;-fx-background-radius:3;");
                    StackPane.setAlignment(finishBadge, Pos.TOP_LEFT);
                    StackPane.setMargin(finishBadge, new Insets(3, 0, 0, 3));
                    cellStack.getChildren().add(finishBadge);
                }
                // ─────────────────────────────────────────────────────────────

                grid.add(cellStack, displayCol, displayRow);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Monster rendering
    // ─────────────────────────────────────────────────────────────────────────
    private void addMonsterToCell(StackPane stack, Monster monster, Pos position) {
        try {
            String imageName = monster.getName() + ".png";
            Image img = imageCache.get(imageName);
            if (img == null) {
                img = new Image(getClass().getResourceAsStream("/assets/" + imageName), 80, 80, true, true);
                imageCache.put(imageName, img);
            }
            ImageView iv = new ImageView(img);
            iv.setFitWidth(45); iv.setFitHeight(45);
            applyStatusAura(monster, iv);

            boolean isHisTurn = monster.getName().equals(game.getCurrent().getName());
            boolean isP1      = monster.getName().equals(game.getPlayer().getName());

            if (isHisTurn && animatingMonster == null) {
                iv.setEffect(new DropShadow(25, Color.web(isP1 ? "#00b894" : "#e84393")));
                ScaleTransition st = new ScaleTransition(Duration.seconds(0.8), iv);
                st.setFromX(1.0); st.setFromY(1.0); st.setToX(1.15); st.setToY(1.15);
                st.setCycleCount(Animation.INDEFINITE); st.setAutoReverse(true);
                st.play();
            }

            stack.getChildren().add(iv);
            StackPane.setAlignment(iv, position);
        } catch (Exception e) {}
    }

    private void applyStatusAura(Monster monster, ImageView iv) {
        if (monster.isFrozen()) {
            ColorAdjust ca = new ColorAdjust();
            ca.setHue(-0.5); ca.setBrightness(0.3); ca.setSaturation(-0.5);
            iv.setEffect(ca);
        } else if (monster.isShielded()) {
            iv.setEffect(new DropShadow(15, Color.web("#3498db")));
        } else if (monster.isConfused()) {
            iv.setEffect(new DropShadow(15, Color.web("#9b59b6")));
        } else {
            iv.setEffect(new DropShadow(5, Color.BLACK));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Win / Pause / Popups
    // ─────────────────────────────────────────────────────────────────────────
    private void checkWinnerStatus() {
        Monster w = game.getWinner();
        if (w != null) {
            pRollBtn.setDisable(true); oRollBtn.setDisable(true);
            pPowerBtn.setDisable(true); oPowerBtn.setDisable(true);
            appendToConsole("GAME OVER", w.getName() + " HAS WON THE MATCH!", "#f1c40f");
            playVictoryCelebration(w);
        }
    }

    private void showCustomPopup(String title, String message, Runnable onConfirm) {
        StackPane rootContainer = (StackPane) scene.getRoot();
        VBox overlay = new VBox();
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setOnMouseClicked(e -> e.consume());

        VBox dialog = new VBox(20);
        dialog.setAlignment(Pos.CENTER);
        dialog.setPadding(new Insets(30));
        dialog.setMaxWidth(450);
        dialog.setMaxHeight(250);
        dialog.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #45a29e; -fx-border-width: 3px; -fx-border-radius: 15; -fx-background-radius: 15;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px; -fx-text-fill: " +
            (title.contains("Error") || title.contains("Invalid") ? "#e74c3c" : "#66fcf1") + ";");
        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-text-alignment: center;");
        msgLabel.setWrapText(true);

        Button okBtn = new Button("CONTINUE");
        okBtn.setStyle("-fx-background-color: #45a29e; -fx-text-fill: #0b0c10; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10 30; -fx-background-radius: 8;");
        okBtn.setOnAction(e -> { rootContainer.getChildren().remove(overlay); if (onConfirm != null) onConfirm.run(); });

        dialog.getChildren().addAll(titleLabel, msgLabel, okBtn);
        overlay.getChildren().add(dialog);
        rootContainer.getChildren().add(overlay);
    }

    private void togglePause() {
        StackPane rootContainer = (StackPane) scene.getRoot();
        BorderPane gameContent  = (BorderPane) rootContainer.getChildren().get(0);

        if (!isPaused) {
            isPaused = true;
            gameContent.setEffect(new GaussianBlur(15));

            pauseOverlay = new StackPane();
            pauseOverlay.setStyle("-fx-background-color: rgba(10, 10, 20, 0.7);");

            VBox menu = new VBox(25);
            menu.setAlignment(Pos.CENTER);
            menu.setPadding(new Insets(40));
            menu.setMaxSize(400, 300);
            menu.setStyle("-fx-background-color: rgba(31, 40, 51, 0.9); -fx-border-color: #66fcf1; -fx-border-width: 3; -fx-border-radius: 15; -fx-background-radius: 15;");

            Label head = new Label("GAME PAUSED");
            head.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, #66fcf1, 10, 0, 0, 0);");

            Button resumeBtn = createCustomButton("RESUME (ESC)", "#66fcf1");
            resumeBtn.setOnAction(e -> togglePause());

            Button menuExitBtn = createCustomButton("EXIT TO MENU", "#e74c3c");
            menuExitBtn.setOnAction(e -> new StartMenu(app));

            menu.getChildren().addAll(head, resumeBtn, menuExitBtn);
            pauseOverlay.getChildren().add(menu);

            pauseOverlay.setOpacity(0);
            rootContainer.getChildren().add(pauseOverlay);
            FadeTransition ft = new FadeTransition(Duration.millis(300), pauseOverlay);
            ft.setToValue(1); ft.play();

        } else {
            isPaused = false;
            gameContent.setEffect(null);
            FadeTransition fade = new FadeTransition(Duration.millis(200), pauseOverlay);
            fade.setToValue(0);
            fade.setOnFinished(e -> rootContainer.getChildren().remove(pauseOverlay));
            fade.play();
        }
    }

    private Button createCustomButton(String text, String color) {
        Button b = new Button(text);
        b.setPrefWidth(250);
        b.setStyle("-fx-background-color: transparent; -fx-border-color: " + color + "; -fx-text-fill: " + color + "; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 5;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 5;"));
        b.setOnMouseExited( e -> b.setStyle("-fx-background-color: transparent; -fx-border-color: " + color + "; -fx-text-fill: " + color + "; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 5;"));
        return b;
    }

    private void playVictoryCelebration(Monster winner) {
        StackPane rootContainer = (StackPane) scene.getRoot();
        rootContainer.getChildren().get(0).setEffect(new GaussianBlur(25));

        victoryOverlay = new StackPane();
        victoryOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");
        rootContainer.getChildren().add(victoryOverlay);

        VBox winnerContent = new VBox(20);
        winnerContent.setAlignment(Pos.CENTER);

        boolean isComputerWin = isVsComputer && winner.getName().equalsIgnoreCase(game.getOpponent().getName());

        if (isComputerWin) {
            SoundManager.playSound("game_over.wav");

            Label loseIcon = new Label("\u2620\uFE0F");
            loseIcon.setFont(Font.font(100));

            Label head = new Label("GAME OVER");
            head.setStyle("-fx-font-size: 42px; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(231, 76, 60, 0.6), 15, 0, 0, 0);");

            ImageView iv = new ImageView();
            try { iv.setImage(new Image(getClass().getResourceAsStream("/assets/" + winner.getName() + ".png"))); } catch (Exception e) {}
            iv.setFitHeight(280); iv.setFitWidth(280); iv.setPreserveRatio(true);
            iv.setStyle("-fx-effect: dropshadow(three-pass-box, #e74c3c, 40, 0.8, 0, 0);");

            Label name = new Label(winner.getName().toUpperCase() + " (COMPUTER) WINS");
            name.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold;");

            Label finalScores = new Label("FINAL SCORES:\n" +
                game.getPlayer().getName()   + " Energy: " + game.getPlayer().getEnergy()   + "\n" +
                game.getOpponent().getName() + " Energy: " + game.getOpponent().getEnergy());
            finalScores.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-text-alignment: center; -fx-background-color: rgba(255,255,255,0.1); -fx-padding: 10; -fx-background-radius: 10;");

            Button mainBtn = createCustomButton("RETURN TO MAIN MENU", "#e74c3c");
            mainBtn.setPrefSize(300, 50);
            mainBtn.setOnAction(e -> new StartMenu(app));
            VBox.setMargin(mainBtn, new Insets(30, 0, 0, 0));

            winnerContent.getChildren().addAll(loseIcon, head, iv, name, finalScores, mainBtn);

        } else {
            SoundManager.playSound("victory.wav");

            VBox trophyBox = new VBox(-2);
            trophyBox.setAlignment(Pos.CENTER);
            LinearGradient goldGradient = new LinearGradient(0, 0, 1, 1, true,
                javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0, Color.web("#ffe066")),
                new javafx.scene.paint.Stop(0.5, Color.web("#f1c40f")),
                new javafx.scene.paint.Stop(1, Color.web("#d4af37")));
            javafx.scene.shape.SVGPath cup = new javafx.scene.shape.SVGPath();
            cup.setContent("M 0 0 L 80 0 L 70 60 C 60 80, 20 80, 10 60 Z");
            cup.setFill(goldGradient);
            cup.setEffect(new DropShadow(20, Color.web("#f1c40f", 0.7)));
            Rectangle stem      = new Rectangle(14, 32, goldGradient);
            Rectangle baseShape = new Rectangle(75, 15, goldGradient);
            baseShape.setArcWidth(10); baseShape.setArcHeight(10);
            trophyBox.getChildren().addAll(cup, stem, baseShape);

            Label head = new Label("VICTORY! THE WINNER IS");
            head.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(102, 252, 241, 0.5), 15, 0, 0, 0);");

            ImageView iv = new ImageView();
            try { iv.setImage(new Image(getClass().getResourceAsStream("/assets/" + winner.getName() + ".png"))); } catch (Exception e) {}
            iv.setFitHeight(280); iv.setFitWidth(280); iv.setPreserveRatio(true);
            iv.setStyle("-fx-effect: dropshadow(three-pass-box, #00b894, 40, 0.8, 0, 0);");

            Label name = new Label(winner.getName().toUpperCase());
            name.setStyle("-fx-font-size: 46px; -fx-text-fill: #f1c40f; -fx-font-weight: bold; -fx-effect: dropshadow(one-pass-box, black, 5, 5, 0, 0);");

            Label finalScores = new Label("FINAL SCORES:\n" +
                game.getPlayer().getName()   + " Energy: " + game.getPlayer().getEnergy()   + "\n" +
                game.getOpponent().getName() + " Energy: " + game.getOpponent().getEnergy());
            finalScores.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-text-alignment: center; -fx-background-color: rgba(255,255,255,0.1); -fx-padding: 10; -fx-background-radius: 10;");

            Button mainBtn = createCustomButton("RETURN TO MAIN MENU", "#66fcf1");
            mainBtn.setPrefSize(300, 50);
            mainBtn.setOnAction(e -> new StartMenu(app));
            VBox.setMargin(mainBtn, new Insets(30, 0, 0, 0));

            winnerContent.getChildren().addAll(trophyBox, head, iv, name, finalScores, mainBtn);
        }

        victoryOverlay.getChildren().add(winnerContent);

        winnerContent.setScaleX(0); winnerContent.setScaleY(0);
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), winnerContent);
        st.setToX(1.0); st.setToY(1.0);
        st.setOnFinished(e -> { if (!isComputerWin) spawnConfetti(); });
        st.play();
    }

    private void spawnConfetti() {
        Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK, Color.ORANGE };
        for (int i = 0; i < 150; i++) {
            Rectangle c = new Rectangle(random.nextInt(10) + 5, random.nextInt(5) + 2, colors[random.nextInt(colors.length)]);
            c.setTranslateX(random.nextInt(1400) - 700);
            c.setTranslateY(-500);
            c.setRotate(random.nextInt(360));
            victoryOverlay.getChildren().add(0, c);

            TranslateTransition fall = new TranslateTransition(Duration.seconds(random.nextDouble() * 2 + 3), c);
            fall.setToY(600);
            RotateTransition rot = new RotateTransition(Duration.seconds(1), c);
            rot.setByAngle(random.nextInt(360) + 360);
            rot.setCycleCount(Animation.INDEFINITE);
            new ParallelTransition(fall, rot).play();
        }
    }

    private void triggerCameraShake() {
        SoundManager.playSound("collision.wav");
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), grid);
        tt.setByX(10); tt.setByY(10); tt.setCycleCount(6); tt.setAutoReverse(true);

        ScaleTransition st = new ScaleTransition(Duration.millis(150), grid);
        st.setByX(0.05); st.setByY(0.05); st.setCycleCount(2); st.setAutoReverse(true);

        new ParallelTransition(tt, st).play();
    }

    public Scene getScene() { return scene; }
}