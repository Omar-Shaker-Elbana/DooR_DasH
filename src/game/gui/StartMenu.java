package game.gui;

import game.engine.dataloader.DataLoader;
import game.engine.Role;
import game.engine.monsters.Monster;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main menu / mode select / character select / how-to-play flow.
 * All visuals are driven by {@link Theme} + game.css ("Neon Grid" design
 * system) — no ad-hoc inline hex colors except for dynamically computed
 * per-monster aura/particle colors, which can't live in a stylesheet.
 */
public class StartMenu {
    private Scene scene;
    private Main app;
    private ArrayList<Monster> availableMonsters;

    private Monster player1Monster;
    private Monster player2Monster;
    private boolean isVsComputer;

    private StackPane rootContainer;
    private Pane particlesPane;
    private StackPane uiContainer;

    private ArrayList<Circle> leftParticles = new ArrayList<>();
    private ArrayList<Circle> rightParticles = new ArrayList<>();

    private MediaPlayer bgmPlayer;

    public StartMenu(Main app) {
        this.app = app;
        try {
            availableMonsters = DataLoader.readMonsters();
        } catch (Exception e) {
            availableMonsters = new ArrayList<>();
        }

        rootContainer = new StackPane();
        rootContainer.getStyleClass().add("root");

        particlesPane = createParticlesBackground();
        uiContainer = new StackPane();

        rootContainer.getChildren().addAll(particlesPane, uiContainer);

        showModeSelection();
        playBackgroundMusic();

        scene = app.getWindow().getScene();
        if (scene == null) {
            scene = new Scene(rootContainer, 1400, 850);
        } else {
            scene.setRoot(rootContainer);
            scene.setOnKeyPressed(null);
        }
        Theme.applyTo(scene);

        Platform.runLater(() -> {
            app.getWindow().setFullScreenExitHint("");
            app.getWindow().setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            app.getWindow().setFullScreen(true);

            try {
                Image cursorImg = new Image(getClass().getResourceAsStream("/assets/cursor.png"));
                scene.setCursor(new javafx.scene.ImageCursor(cursorImg));
            } catch (Exception e) { System.out.println("Cursor asset not found."); }
        });
    }

    private void playBackgroundMusic() {
        try {
            File bgmFile = new File("assets/sounds/menu_bgm.mp3");
            if (bgmFile.exists()) {
                Media media = new Media(bgmFile.toURI().toString());
                bgmPlayer = new MediaPlayer(media);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.setVolume(0.4);
                bgmPlayer.play();
            }
        } catch (Exception e) {
            System.out.println("Background music not found.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Ambient particle field (split left/right so each half can take on
    //  a player's aura color once a monster with a role is selected)
    // ─────────────────────────────────────────────────────────────────────
    private Pane createParticlesBackground() {
        Pane pane = new Pane();
        for (int i = 0; i < 60; i++) {
            Circle c = new Circle(Math.random() * 3.5 + 1.5, Color.web(Theme.P1, 0.35));
            double startX = Math.random() * 1600;
            c.setCenterX(startX);
            c.setCenterY(Math.random() * 1000 + 900);

            if (startX < 800) leftParticles.add(c);
            else rightParticles.add(c);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(Math.random() * 16 + 12), c);
            tt.setByY(-1600);
            tt.setCycleCount(Animation.INDEFINITE);
            tt.play();
            pane.getChildren().add(c);
        }
        return pane;
    }

    private void updateAuraColor(boolean isPlayer1, Role role) {
        Color targetColor = (role == Role.SCARER) ? Color.web(Theme.P2, 0.65) : Color.web(Theme.P1, 0.65);
        ArrayList<Circle> targetList = isPlayer1 ? leftParticles : rightParticles;

        for (Circle c : targetList) {
            FillTransition ft = new FillTransition(Duration.seconds(1), c);
            ft.setToValue(targetColor);
            ft.play();
        }
    }

    private void resetAuraColors() {
        for (Circle c : leftParticles)  c.setFill(Color.web(Theme.P1, 0.35));
        for (Circle c : rightParticles) c.setFill(Color.web(Theme.P1, 0.35));
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Mode selection
    // ─────────────────────────────────────────────────────────────────────
    private void showModeSelection() {
        uiContainer.getChildren().clear();

        VBox layout = new VBox(26);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("DooR_DasH");
        title.getStyleClass().add("text-hero");

        Label subtitle = new Label("CHOOSE YOUR GAME MODE");
        subtitle.getStyleClass().add("text-subtitle");

        HBox buttonsBox = new HBox(30);
        buttonsBox.setAlignment(Pos.CENTER);

        Button vsComputerBtn = createMenuTile("1 PLAYER\nVS COMPUTER", true);
        vsComputerBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            isVsComputer = true;
            showCharacterSelection();
        });

        Button vsPlayerBtn = createMenuTile("2 PLAYERS\nLOCAL MATCH", false);
        vsPlayerBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            isVsComputer = false;
            showCharacterSelection();
        });

        buttonsBox.getChildren().addAll(vsComputerBtn, vsPlayerBtn);

        Button howToPlayBtn = new Button("HOW TO PLAY");
        howToPlayBtn.getStyleClass().addAll("btn", "btn-outline-accent");
        howToPlayBtn.setPrefSize(300, 48);
        howToPlayBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            showHowToPlayDossier();
        });

        Button exitGameBtn = new Button("EXIT GAME");
        exitGameBtn.getStyleClass().addAll("btn", "btn-outline-danger");
        exitGameBtn.setPrefSize(300, 48);
        exitGameBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            Platform.exit();
        });

        layout.getChildren().addAll(title, subtitle, buttonsBox, howToPlayBtn, exitGameBtn);
        playIntroAnimation(title, subtitle, buttonsBox, howToPlayBtn, exitGameBtn);

        uiContainer.getChildren().add(layout);
    }

    private void playIntroAnimation(Label title, Label subtitle, HBox buttonsBox, Button howToPlayBtn, Button exitGameBtn) {
        title.setOpacity(0); subtitle.setOpacity(0);
        buttonsBox.setOpacity(0); howToPlayBtn.setOpacity(0); exitGameBtn.setOpacity(0);
        buttonsBox.setTranslateY(40);
        howToPlayBtn.setTranslateY(24);
        exitGameBtn.setTranslateY(24);

        FadeTransition ftTitle = new FadeTransition(Duration.seconds(1.2), title);
        ftTitle.setToValue(1);
        ScaleTransition stTitle = new ScaleTransition(Duration.seconds(1.2), title);
        stTitle.setFromX(0.85); stTitle.setFromY(0.85); stTitle.setToX(1); stTitle.setToY(1);

        FadeTransition ftSub = new FadeTransition(Duration.seconds(0.9), subtitle);
        ftSub.setToValue(1);

        FadeTransition ftBtn = new FadeTransition(Duration.seconds(0.8), buttonsBox);
        ftBtn.setToValue(1);
        TranslateTransition ttBtn = new TranslateTransition(Duration.seconds(0.8), buttonsBox);
        ttBtn.setToY(0);

        FadeTransition ftHowTo = new FadeTransition(Duration.seconds(0.8), howToPlayBtn);
        ftHowTo.setToValue(1);
        TranslateTransition ttHowTo = new TranslateTransition(Duration.seconds(0.8), howToPlayBtn);
        ttHowTo.setToY(0);

        FadeTransition ftExit = new FadeTransition(Duration.seconds(0.8), exitGameBtn);
        ftExit.setToValue(1);
        TranslateTransition ttExit = new TranslateTransition(Duration.seconds(0.8), exitGameBtn);
        ttExit.setToY(0);

        ParallelTransition intro = new ParallelTransition(ftTitle, stTitle, ftSub);
        intro.setOnFinished(e -> new ParallelTransition(ftBtn, ttBtn, ftHowTo, ttHowTo, ftExit, ttExit).play());
        intro.play();
    }

    private Button createMenuTile(String text, boolean isP1) {
        Button btn = new Button(text);
        btn.getStyleClass().addAll("mode-tile", isP1 ? "mode-tile-p1" : "mode-tile-p2");
        btn.setPrefSize(290, 130);
        btn.setWrapText(true);

        btn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.05); st.setToY(1.05); st.play();
        });
        btn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });
        return btn;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  How to play dossier
    // ─────────────────────────────────────────────────────────────────────
    private void showHowToPlayDossier() {
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("overlay-dim");

        uiContainer.getChildren().get(0).setEffect(new GaussianBlur(15));

        VBox dossier = new VBox(16);
        dossier.getStyleClass().add("dossier-box");
        dossier.setMaxSize(850, 650);
        dossier.setPadding(new Insets(32));

        Label header = new Label("HOW TO PLAY");
        header.getStyleClass().add("text-section");

        VBox rulesContent = new VBox(20);
        rulesContent.setAlignment(Pos.TOP_LEFT);
        rulesContent.setPadding(new Insets(10, 20, 10, 10));

        rulesContent.getChildren().add(createRuleSection("1. THE OBJECTIVE",
            "The game is a race on a 100-cell board (0 to 99). To WIN the match, your monster must reach the final cell (99) AND have at least 1000 Energy! Manage your resources carefully."));

        rulesContent.getChildren().add(createRuleSection("2. MOVEMENT & COLLISION",
            "Roll the dice to advance. Be careful! If you land exactly on the same cell as your opponent, it's an 'Invalid Move' and you will forfeit your turn."));

        rulesContent.getChildren().add(createRuleSection("3. DOORS & ENERGY",
            "Landing on a Door Cell grants you Energy IF its role matches yours (SCARER or LAUGHER). If it mismatches, you LOSE that amount of energy! Doors deactivate after one use."));

        rulesContent.getChildren().add(createRuleSection("4. SPECIAL CELLS",
            "- Conveyor Belts: Instantly pushes you forward multiple steps.\n" +
            "- Contamination Socks: Pushes you backward!\n" +
            "- Card Cells: Draw a random card (Swap positions, Start Over, Gain/Lose Energy, etc.)."));

        rulesContent.getChildren().add(createRuleSection("5. STATUS EFFECTS",
            "- Frozen: You will skip your next turn.\n" +
            "- Confused: Your role (SCARER/LAUGHER) is swapped temporarily!\n" +
            "- Shielded: You are protected from the next energy loss."));

        rulesContent.getChildren().add(createRuleSection("6. POWERUPS & ABILITIES",
            "Spend 500 Energy to use your Powerup! Each monster has a unique passive:\n" +
            "- MultiTasker: Gets +200 Bonus on energy changes.\n" +
            "- Dasher: Travels double the distance of the dice roll.\n" +
            "- Dynamo: Multiplies all gained/lost energy by 2.\n" +
            "- Schemer: Steals energy from the opponent."));

        ScrollPane scrollPane = new ScrollPane(rulesContent);
        scrollPane.getStyleClass().add("console-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(420);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Button closeBtn = new Button("UNDERSTOOD");
        closeBtn.getStyleClass().addAll("btn", "btn-danger");
        closeBtn.setPrefSize(300, 46);

        closeBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            FadeTransition ftOut = new FadeTransition(Duration.millis(300), overlay);
            ftOut.setToValue(0);
            ftOut.setOnFinished(ev -> {
                rootContainer.getChildren().remove(overlay);
                uiContainer.getChildren().get(0).setEffect(null);
            });
            ftOut.play();
        });

        dossier.getChildren().addAll(header, new Separator(), scrollPane, new Separator(), closeBtn);
        dossier.setAlignment(Pos.TOP_CENTER);

        overlay.getChildren().add(dossier);

        overlay.setOpacity(0);
        dossier.setScaleX(0.85); dossier.setScaleY(0.85);
        rootContainer.getChildren().add(overlay);

        FadeTransition ftIn = new FadeTransition(Duration.millis(300), overlay);
        ftIn.setToValue(1);
        ScaleTransition stIn = new ScaleTransition(Duration.millis(300), dossier);
        stIn.setToX(1); stIn.setToY(1);
        new ParallelTransition(ftIn, stIn).play();
    }

    private VBox createRuleSection(String titleText, String descText) {
        VBox box = new VBox(8);
        Label title = new Label(titleText);
        title.getStyleClass().add("rule-title");

        Label desc = new Label(descText);
        desc.getStyleClass().add("rule-desc");
        desc.setWrapText(true);
        desc.setMinHeight(Region.USE_PREF_SIZE);

        box.getChildren().addAll(title, desc);
        return box;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Character selection
    // ─────────────────────────────────────────────────────────────────────
    private void showCharacterSelection() {
        uiContainer.getChildren().clear();

        VBox layout = new VBox(24);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Label title = new Label(isVsComputer ? "SELECT YOUR CHAMPION" : "CHOOSE YOUR FIGHTERS");
        title.getStyleClass().add("text-section");

        HBox selectionArea = new HBox(50);
        selectionArea.setAlignment(Pos.CENTER);

        VBox p1Box = createPlayerSelectionBox("PLAYER 1", true);
        selectionArea.getChildren().add(p1Box);

        if (!isVsComputer) {
            VBox p2Box = createPlayerSelectionBox("PLAYER 2", false);
            selectionArea.getChildren().add(p2Box);
        }

        Button startBtn = new Button("ENTER ARENA");
        startBtn.getStyleClass().addAll("btn", "btn-accent");
        startBtn.setPrefWidth(300);
        startBtn.setStyle("-fx-font-size:20px;-fx-padding:15 30;");

        startBtn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), startBtn);
            st.setToX(1.05); st.setToY(1.05); st.play();
        });
        startBtn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), startBtn);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });

        startBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            if (player1Monster == null) { showWarningPopup("Player 1 must select a monster!"); return; }
            if (!isVsComputer && player2Monster == null) { showWarningPopup("Player 2 must select a monster!"); return; }

            if (!isVsComputer) {
                if (player1Monster.getName().equals(player2Monster.getName())) {
                    showWarningPopup("Players cannot choose the exact same monster!"); return;
                }
                if (player1Monster.getRole() == player2Monster.getRole()) {
                    showWarningPopup("Teams must be balanced!\n(One Scarer and One Laugher)"); return;
                }
            }

            playVsCinematic(() -> new GameBoard(app, player1Monster, player2Monster, isVsComputer));
        });

        Button backBtn = new Button("BACK TO MODE SELECTION");
        backBtn.getStyleClass().addAll("btn", "btn-outline-muted");
        backBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            player1Monster = null;
            player2Monster = null;
            resetAuraColors();
            showModeSelection();
        });

        layout.getChildren().addAll(title, selectionArea, startBtn, backBtn);

        layout.setOpacity(0);
        ScaleTransition st = new ScaleTransition(Duration.millis(400), layout);
        st.setFromX(0.92); st.setFromY(0.92); st.setToX(1.0); st.setToY(1.0);
        FadeTransition ft = new FadeTransition(Duration.millis(400), layout);
        ft.setToValue(1);
        new ParallelTransition(st, ft).play();

        uiContainer.getChildren().add(layout);
    }

    private VBox createPlayerSelectionBox(String titleText, boolean isPlayer1) {
        String playerColor = isPlayer1 ? Theme.P1 : Theme.P2;
        String cardClass    = isPlayer1 ? "select-card-p1" : "select-card-p2";

        VBox card = new VBox(14);
        card.getStyleClass().addAll("select-card", cardClass);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(22));
        card.setPrefSize(350, 490);

        Label title = new Label(titleText);
        title.getStyleClass().add("text-section");
        title.setStyle("-fx-font-size:20px;-fx-text-fill:" + playerColor + ";-fx-effect:none;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(170);
        imageView.setFitWidth(170);
        imageView.setPreserveRatio(true);
        imageView.setEffect(new DropShadow(14, Color.web("#000000", 0.8)));

        Label nameLabel = new Label("WAITING...");
        nameLabel.getStyleClass().add("text-body");
        nameLabel.setStyle("-fx-font-weight:bold;-fx-font-size:18px;-fx-text-fill:#f0f4ff;");

        Label roleLabel = new Label("Role: ???");
        roleLabel.getStyleClass().add("text-muted");
        roleLabel.setStyle("-fx-font-size:13px;");

        Label passiveLabel = new Label();
        passiveLabel.getStyleClass().add("text-body");
        passiveLabel.setStyle("-fx-font-size:13px;-fx-text-fill:rgba(240,244,255,0.65);");
        passiveLabel.setWrapText(true);
        passiveLabel.setMaxWidth(300);
        passiveLabel.setAlignment(Pos.CENTER);
        passiveLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        passiveLabel.setMinHeight(42);

        Label energyLabel = new Label("Energy: 0");
        energyLabel.setStyle("-fx-font-weight:bold;-fx-font-size:17px;-fx-text-fill:" + Theme.ACCENT + ";");

        ComboBox<String> picker = new ComboBox<>();
        for (Monster m : availableMonsters) picker.getItems().add(m.getName());
        picker.setPromptText("Select Monster");
        picker.setPrefWidth(230);
        picker.getStyleClass().add("monster-picker");
        picker.setStyle("-fx-border-color:" + playerColor + "88;");

        picker.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(picker.getPromptText());
                    setStyle("-fx-text-fill:rgba(240,244,255,0.4);-fx-font-weight:bold;-fx-font-size:13px;-fx-background-color:transparent;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill:#f0f4ff;-fx-font-weight:bold;-fx-font-size:14px;-fx-background-color:transparent;");
                }
            }
        });

        picker.setOnAction(e -> {
            int index = picker.getSelectionModel().getSelectedIndex();
            if (index < 0) return;
            Monster selected = availableMonsters.get(index);
            if (isPlayer1) player1Monster = selected; else player2Monster = selected;

            SoundManager.playSound(selected.getName() + ".mp3");
            updateAuraColor(isPlayer1, selected.getRole());

            if (!card.getStyleClass().contains("filled")) card.getStyleClass().add("filled");

            nameLabel.setText(selected.getName().toUpperCase());
            roleLabel.setText("Role: " + selected.getRole());
            energyLabel.setText("Energy: " + selected.getEnergy());
            passiveLabel.setText(passiveDescription(selected));

            try {
                imageView.setImage(new Image("assets/" + selected.getName() + ".png"));
                ScaleTransition bump = new ScaleTransition(Duration.millis(300), imageView);
                bump.setFromX(0.7); bump.setFromY(0.7);
                bump.setToX(1.0); bump.setToY(1.0);
                bump.play();
            } catch (Exception ex) { /* missing portrait — keep placeholder */ }
        });

        card.getChildren().addAll(title, picker, imageView, nameLabel, roleLabel, passiveLabel, energyLabel);
        return card;
    }

    private String passiveDescription(Monster selected) {
        if (selected instanceof game.engine.monsters.MultiTasker)
            return "MultiTasker: Gains +200 bonus on energy changes.";
        if (selected instanceof game.engine.monsters.Dasher)
            return "Dasher: Travels double the distance of dice roll.";
        if (selected instanceof game.engine.monsters.Dynamo)
            return "Dynamo: Multiplies all gained/lost energy by 2.";
        if (selected instanceof game.engine.monsters.Schemer)
            return "Schemer: Steals energy from the opponent.";
        return "";
    }

    public Scene getScene() { return scene; }

    // ─────────────────────────────────────────────────────────────────────
    //  VS cinematic transition into the match
    // ─────────────────────────────────────────────────────────────────────
    private void playVsCinematic(Runnable onFinished) {
        if (bgmPlayer != null) bgmPlayer.stop();

        uiContainer.getChildren().clear();

        Rectangle bg = new Rectangle(2000, 1500, Color.web(Theme.BG_BASE));
        uiContainer.getChildren().add(bg);

        ImageView p1Img = new ImageView();
        try { p1Img.setImage(new Image("file:assets/" + player1Monster.getName() + ".png")); } catch (Exception e) {}
        p1Img.setFitWidth(400); p1Img.setFitHeight(400); p1Img.setPreserveRatio(true);
        p1Img.setEffect(Theme.glow(Theme.P1, 30));

        ImageView p2Img = new ImageView();
        if (isVsComputer) {
            Monster mystery = availableMonsters.get(new Random().nextInt(availableMonsters.size()));
            try { p2Img.setImage(new Image("file:assets/" + mystery.getName() + ".png")); } catch (Exception e) {}
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-1.0);
            p2Img.setEffect(blackout);
        } else {
            try { p2Img.setImage(new Image("file:assets/" + player2Monster.getName() + ".png")); } catch (Exception e) {}
            p2Img.setEffect(Theme.glow(Theme.P2, 30));
        }

        p2Img.setFitWidth(400); p2Img.setFitHeight(400); p2Img.setPreserveRatio(true);
        p2Img.setScaleX(-1);

        StackPane.setAlignment(p1Img, Pos.CENTER_LEFT);
        StackPane.setMargin(p1Img, new Insets(0, 0, 0, 150));
        StackPane.setAlignment(p2Img, Pos.CENTER_RIGHT);
        StackPane.setMargin(p2Img, new Insets(0, 150, 0, 0));

        Label vsLabel = new Label("VS");
        vsLabel.setStyle("-fx-font-family:'Segoe UI Black','Arial Black';-fx-font-size:180px;-fx-font-weight:bold;" +
            "-fx-text-fill:" + Theme.DANGER + ";-fx-effect:dropshadow(two-pass-box," + Theme.ACCENT + ",20,0.8,0,0);");

        uiContainer.getChildren().addAll(p1Img, p2Img, vsLabel);

        p1Img.setTranslateX(-800);
        p2Img.setTranslateX(800);
        vsLabel.setScaleX(0); vsLabel.setScaleY(0);

        TranslateTransition t1 = new TranslateTransition(Duration.millis(400), p1Img);
        t1.setToX(0);
        TranslateTransition t2 = new TranslateTransition(Duration.millis(400), p2Img);
        t2.setToX(0);

        ScaleTransition sVs = new ScaleTransition(Duration.millis(300), vsLabel);
        sVs.setToX(1.0); sVs.setToY(1.0);

        ParallelTransition slideIn = new ParallelTransition(t1, t2);
        slideIn.setOnFinished(e -> {
            SoundManager.playSound("boxing_bell.mp3");
            sVs.play();
            TranslateTransition shake = new TranslateTransition(Duration.millis(40), uiContainer);
            shake.setByX(20); shake.setByY(10);
            shake.setCycleCount(8); shake.setAutoReverse(true);
            shake.play();
        });

        sVs.setOnFinished(e -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(ev -> {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(400), rootContainer);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(evt -> onFinished.run());
                fadeOut.play();
            });
            pause.play();
        });

        slideIn.play();
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Validation popup
    // ─────────────────────────────────────────────────────────────────────
    private void showWarningPopup(String message) {
        SoundManager.playSound("error.mp3");

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox layout = new VBox(20);
        layout.getStyleClass().addAll("dialog-box", "dialog-box-danger");
        layout.setAlignment(Pos.CENTER);

        Label lbl = new Label(message);
        lbl.setStyle("-fx-text-fill:" + Theme.DANGER + ";-fx-font-weight:bold;-fx-font-size:17px;");
        lbl.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lbl.setWrapText(true);

        Button btn = new Button("ACKNOWLEDGE");
        btn.getStyleClass().addAll("btn", "btn-danger");
        btn.setOnAction(ev -> {
            SoundManager.playSound("click.wav");
            popupStage.close();
        });

        layout.getChildren().addAll(lbl, btn);

        Scene popupScene = new Scene(layout, 420, 200);
        popupScene.setFill(Color.TRANSPARENT);
        Theme.applyTo(popupScene);
        popupStage.setScene(popupScene);

        popupStage.setOnShown(e -> {
            TranslateTransition shake = new TranslateTransition(Duration.millis(50), layout);
            shake.setByX(10);
            shake.setCycleCount(6);
            shake.setAutoReverse(true);
            shake.play();
        });

        popupStage.showAndWait();
    }
}