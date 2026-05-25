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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        rootContainer.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 75%, #1a1a2e, #05050f);");

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
            File bgmFile = new File("src/assets/sounds/menu_bgm.mp3");
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

    private Pane createParticlesBackground() {
        Pane pane = new Pane();
        for (int i = 0; i < 50; i++) {
            Circle c = new Circle(Math.random() * 4 + 2, Color.web("#66fcf1", 0.4));
            double startX = Math.random() * 1600;
            c.setCenterX(startX);
            c.setCenterY(Math.random() * 1000 + 900); 
            
            if (startX < 800) leftParticles.add(c);
            else rightParticles.add(c);
            
            TranslateTransition tt = new TranslateTransition(Duration.seconds(Math.random() * 15 + 10), c);
            tt.setByY(-1600); 
            tt.setCycleCount(Animation.INDEFINITE);
            tt.play();
            pane.getChildren().add(c);
        }
        return pane;
    }

    private void updateAuraColor(boolean isPlayer1, Role role) {
        Color targetColor = (role == Role.SCARER) ? Color.web("#e74c3c", 0.7) : Color.web("#f1c40f", 0.7);
        ArrayList<Circle> targetList = isPlayer1 ? leftParticles : rightParticles;
        
        for (Circle c : targetList) {
            FillTransition ft = new FillTransition(Duration.seconds(1), c);
            ft.setToValue(targetColor);
            ft.play();
        }
    }

    private void showModeSelection() {
        uiContainer.getChildren().clear();

        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("DooR_DasH");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 55));
        title.setTextFill(Color.web("#66fcf1"));
        title.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(102, 252, 241, 0.8), 20, 0, 0, 0);");
        
        Label subtitle = new Label("CHOOSE GAME MODE");
        subtitle.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        subtitle.setTextFill(Color.WHITE);

        HBox buttonsBox = new HBox(30);
        buttonsBox.setAlignment(Pos.CENTER);

        Button vsComputerBtn = createMenuButton("1 PLAYER\n(VS COMPUTER)", "#00b894");
        vsComputerBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            isVsComputer = true;
            showCharacterSelection();
        });

        Button vsPlayerBtn = createMenuButton("2 PLAYERS\n(LOCAL MATCH)", "#e84393");
        vsPlayerBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            isVsComputer = false;
            showCharacterSelection();
        });

        buttonsBox.getChildren().addAll(vsComputerBtn, vsPlayerBtn);
        
        Button howToPlayBtn = new Button("HOW TO PLAY");
        howToPlayBtn.setPrefSize(300, 45);
        howToPlayBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #f1c40f; -fx-border-width: 3px; -fx-text-fill: #f1c40f; -fx-font-weight: bold; -fx-font-size: 18px; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        howToPlayBtn.setOnMouseEntered(e -> {
            howToPlayBtn.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: #1a1a2e; -fx-font-weight: bold; -fx-font-size: 18px; -fx-border-radius: 10; -fx-background-radius: 10;");
        });
        howToPlayBtn.setOnMouseExited(e -> {
            howToPlayBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #f1c40f; -fx-border-width: 3px; -fx-text-fill: #f1c40f; -fx-font-weight: bold; -fx-font-size: 18px; -fx-border-radius: 10; -fx-background-radius: 10;");
        });
        howToPlayBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            showHowToPlayDossier();
        });

        Button exitGameBtn = new Button("EXIT GAME");
        exitGameBtn.setPrefSize(300, 45);
        exitGameBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #e74c3c; -fx-border-width: 3px; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 18px; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        exitGameBtn.setOnMouseEntered(e -> {
            exitGameBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-border-radius: 10; -fx-background-radius: 10;");
        });
        exitGameBtn.setOnMouseExited(e -> {
            exitGameBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #e74c3c; -fx-border-width: 3px; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 18px; -fx-border-radius: 10; -fx-background-radius: 10;");
        });
        exitGameBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            Platform.exit();
        });

        layout.getChildren().addAll(title, subtitle, buttonsBox, howToPlayBtn, exitGameBtn);
        
        title.setOpacity(0); subtitle.setOpacity(0); buttonsBox.setOpacity(0); howToPlayBtn.setOpacity(0); exitGameBtn.setOpacity(0);
        buttonsBox.setTranslateY(50); 
        howToPlayBtn.setTranslateY(30);
        exitGameBtn.setTranslateY(30);

        FadeTransition ftTitle = new FadeTransition(Duration.seconds(1.5), title);
        ftTitle.setToValue(1);
        ScaleTransition stTitle = new ScaleTransition(Duration.seconds(1.5), title);
        stTitle.setFromX(0.8); stTitle.setFromY(0.8); stTitle.setToX(1); stTitle.setToY(1);

        FadeTransition ftSub = new FadeTransition(Duration.seconds(1), subtitle);
        ftSub.setToValue(1);

        FadeTransition ftBtn = new FadeTransition(Duration.seconds(1), buttonsBox);
        ftBtn.setToValue(1);
        TranslateTransition ttBtn = new TranslateTransition(Duration.seconds(1), buttonsBox);
        ttBtn.setToY(0);

        FadeTransition ftHowTo = new FadeTransition(Duration.seconds(1), howToPlayBtn);
        ftHowTo.setToValue(1);
        TranslateTransition ttHowTo = new TranslateTransition(Duration.seconds(1), howToPlayBtn);
        ttHowTo.setToY(0);

        FadeTransition ftExit = new FadeTransition(Duration.seconds(1), exitGameBtn);
        ftExit.setToValue(1);
        TranslateTransition ttExit = new TranslateTransition(Duration.seconds(1), exitGameBtn);
        ttExit.setToY(0);

        ParallelTransition intro = new ParallelTransition(ftTitle, stTitle, ftSub);
        intro.setOnFinished(e -> new ParallelTransition(ftBtn, ttBtn, ftHowTo, ttHowTo, ftExit, ttExit).play());
        intro.play();
        
        uiContainer.getChildren().add(layout);
    }

    private void showHowToPlayDossier() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(10, 10, 20, 0.85);");
        
        uiContainer.getChildren().get(0).setEffect(new GaussianBlur(15));

        VBox dossier = new VBox(15);
        dossier.setMaxSize(850, 650); 
        dossier.setPadding(new Insets(30));
        dossier.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #f1c40f; -fx-border-width: 4px; -fx-border-radius: 15; -fx-background-radius: 15;");
        
        Label header = new Label("TOP SECRET: HOW TO PLAY");
        header.setFont(Font.font("Verdana", FontWeight.BOLD, 32));
        header.setTextFill(Color.web("#f1c40f"));
        header.setAlignment(Pos.CENTER);

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
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400); 
        scrollPane.setStyle("-fx-background: #1a1a2e; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Button closeBtn = new Button("UNDERSTOOD");
        closeBtn.setPrefSize(300, 45);
        closeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-background-radius: 8;");
        
        closeBtn.setOnMouseEntered(e -> {
            closeBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-background-radius: 8;");
        });
        closeBtn.setOnMouseExited(e -> {
            closeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-background-radius: 8;");
        });
        
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
        dossier.setScaleX(0.8); dossier.setScaleY(0.8);
        rootContainer.getChildren().add(overlay);
        
        ParallelTransition pt = new ParallelTransition();
        FadeTransition ftIn = new FadeTransition(Duration.millis(300), overlay);
        ftIn.setToValue(1);
        ScaleTransition stIn = new ScaleTransition(Duration.millis(300), dossier);
        stIn.setToX(1); stIn.setToY(1);
        pt.getChildren().addAll(ftIn, stIn);
        pt.play();
    }

    private VBox createRuleSection(String titleText, String descText) {
        VBox box = new VBox(8);
        Label title = new Label(titleText);
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#66fcf1"));
        
        Label desc = new Label(descText);
        desc.setFont(Font.font("System", FontWeight.NORMAL, 15));
        desc.setTextFill(Color.WHITE);
        desc.setWrapText(true);
        desc.setMinHeight(Region.USE_PREF_SIZE); 
        
        box.getChildren().addAll(title, desc);
        return box;
    }

    private void showCharacterSelection() {
        uiContainer.getChildren().clear();

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Label title = new Label(isVsComputer ? "SELECT YOUR CHAMPION" : "CHOOSE YOUR FIGHTERS");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#e94560"));
        title.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(233, 69, 96, 0.8), 15, 0, 0, 0);");

        HBox selectionArea = new HBox(50);
        selectionArea.setAlignment(Pos.CENTER);

        VBox p1Box = createPlayerSelectionBox("PLAYER 1", true);
        selectionArea.getChildren().add(p1Box);

        if (!isVsComputer) {
            VBox p2Box = createPlayerSelectionBox("PLAYER 2", false);
            selectionArea.getChildren().add(p2Box);
        }

        Button startBtn = new Button("ENTER ARENA");
        startBtn.setPrefWidth(300);
        startBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 22px; -fx-padding: 15; -fx-background-radius: 10; -fx-effect: dropshadow(two-pass-box, black, 5, 0, 0, 3);");
        
        startBtn.setOnMouseEntered(e -> {
            startBtn.setOpacity(0.8);
            ScaleTransition st = new ScaleTransition(Duration.millis(100), startBtn);
            st.setToX(1.05); st.setToY(1.05); st.play();
        });
        startBtn.setOnMouseExited(e -> {
            startBtn.setOpacity(1.0);
            ScaleTransition st = new ScaleTransition(Duration.millis(100), startBtn);
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
            
            playVsCinematic(() -> {
                new GameBoard(app, player1Monster, player2Monster, isVsComputer);
            });
        });

        Button backBtn = new Button("BACK TO MODE SELECTION");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #bdc3c7; -fx-font-size: 16px; -fx-font-weight: bold;");
        backBtn.setOnMouseEntered(e -> {
            backBtn.setTextFill(Color.WHITE);
        });
        backBtn.setOnMouseExited(e -> backBtn.setTextFill(Color.web("#bdc3c7")));
        backBtn.setOnAction(e -> {
            SoundManager.playSound("click.wav");
            player1Monster = null;
            player2Monster = null;
            
            for (Circle c : leftParticles) { c.setFill(Color.web("#66fcf1", 0.4)); }
            for (Circle c : rightParticles) { c.setFill(Color.web("#66fcf1", 0.4)); }
            
            showModeSelection();
        });

        layout.getChildren().addAll(title, selectionArea, startBtn, backBtn);
        
        layout.setOpacity(0);
        ScaleTransition st = new ScaleTransition(Duration.millis(400), layout);
        st.setFromX(0.9); st.setFromY(0.9); st.setToX(1.0); st.setToY(1.0);
        FadeTransition ft = new FadeTransition(Duration.millis(400), layout);
        ft.setToValue(1);
        new ParallelTransition(st, ft).play();

        uiContainer.getChildren().add(layout);
    }

    private void playVsCinematic(Runnable onFinished) {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }

        uiContainer.getChildren().clear(); 
        
        Rectangle bg = new Rectangle(2000, 1500, Color.web("#050000")); 
        uiContainer.getChildren().add(bg);
        
        ImageView p1Img = new ImageView();
        try { p1Img.setImage(new Image("file:src/assets/" + player1Monster.getName() + ".png")); } catch(Exception e){}
        p1Img.setFitWidth(400); p1Img.setFitHeight(400); p1Img.setPreserveRatio(true);
        p1Img.setStyle("-fx-effect: dropshadow(three-pass-box, #00b894, 30, 0.5, 0, 0);"); 
        
        ImageView p2Img = new ImageView();
        if (isVsComputer) {
            Monster mystery = availableMonsters.get(new Random().nextInt(availableMonsters.size()));
            try { p2Img.setImage(new Image("file:src/assets/" + mystery.getName() + ".png")); } catch(Exception e){}
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-1.0); 
            p2Img.setEffect(blackout);
        } else {
            try { p2Img.setImage(new Image("file:src/assets/" + player2Monster.getName() + ".png")); } catch(Exception e){}
            p2Img.setStyle("-fx-effect: dropshadow(three-pass-box, #e84393, 30, 0.5, 0, 0);"); 
        }
        
        p2Img.setFitWidth(400); p2Img.setFitHeight(400); p2Img.setPreserveRatio(true);
        p2Img.setScaleX(-1); 
        
        StackPane.setAlignment(p1Img, Pos.CENTER_LEFT);
        StackPane.setMargin(p1Img, new Insets(0, 0, 0, 150));
        StackPane.setAlignment(p2Img, Pos.CENTER_RIGHT);
        StackPane.setMargin(p2Img, new Insets(0, 150, 0, 0));
        
        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font("Impact", FontWeight.BOLD, 180));
        vsLabel.setTextFill(Color.web("#e74c3c"));
        vsLabel.setStyle("-fx-effect: dropshadow(two-pass-box, #f1c40f, 20, 0.8, 0, 0);");
        
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

    private Button createMenuButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefSize(280, 130);
        btn.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-border-color: " + color + "; -fx-border-width: 3px; -fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 22px; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-alignment: center; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #1a1a2e; -fx-font-weight: bold; -fx-font-size: 22px; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-alignment: center; -fx-effect: dropshadow(three-pass-box, " + color + ", 20, 0, 0, 0);");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.05); st.setToY(1.05); st.play();
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-border-color: " + color + "; -fx-border-width: 3px; -fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 22px; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-alignment: center; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });
        
        return btn;
    }


    private VBox createPlayerSelectionBox(String titleText, boolean isPlayer1) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(20));
        card.setPrefSize(350, 480);
        String playerColor = isPlayer1 ? "#00b894" : "#e84393";
        card.setStyle("-fx-background-color: rgba(22, 33, 62, 0.9); -fx-border-color: " + playerColor + "; -fx-border-width: 3px; -fx-background-radius: 15; -fx-border-radius: 15; -fx-effect: dropshadow(two-pass-box, black, 15, 0, 0, 10);");

        Label title = new Label(titleText);
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(playerColor));

        ImageView imageView = new ImageView();
        imageView.setFitHeight(180);
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        Label nameLabel = new Label("WAITING...");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        nameLabel.setTextFill(Color.WHITE);

        Label roleLabel = new Label("Role: ???");
        roleLabel.setTextFill(Color.LIGHTGRAY);

        Label passiveLabel = new Label();
        passiveLabel.setTextFill(Color.web("#bdc3c7"));
        passiveLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        passiveLabel.setWrapText(true);
        passiveLabel.setMaxWidth(300);
        passiveLabel.setAlignment(Pos.CENTER);
        passiveLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        passiveLabel.setMinHeight(40); 

        Label energyLabel = new Label("Energy: 0");
        energyLabel.setTextFill(Color.web("#f1c40f"));
        energyLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        ComboBox<String> picker = new ComboBox<>();
        for (Monster m : availableMonsters) {
            picker.getItems().add(m.getName());
        }
        picker.setPromptText("Select Monster");
        picker.setPrefWidth(220);
        
        picker.setStyle("-fx-background-color: #0f3460; -fx-border-color: " + playerColor + "; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        picker.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(picker.getPromptText());
                    setStyle("-fx-text-fill: #aaaaaa; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-color: transparent;");
                }
            }
        });

        picker.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #0f3460;");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5 10;");
                    setOnMouseEntered(e -> {
                        setStyle("-fx-background-color: " + playerColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5 10;");
                    });
                    setOnMouseExited(e -> setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5 10;"));
                }
            }
        });

        picker.setOnAction(e -> {
            int index = picker.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                Monster selected = availableMonsters.get(index);
                if (isPlayer1) player1Monster = selected;
                else player2Monster = selected;
                
                SoundManager.playSound(selected.getName() + ".mp3");

                updateAuraColor(isPlayer1, selected.getRole());

                nameLabel.setText(selected.getName().toUpperCase());
                roleLabel.setText("Role: " + selected.getRole());
                energyLabel.setText("Energy: " + selected.getEnergy());

                if (selected instanceof game.engine.monsters.MultiTasker) {
                    passiveLabel.setText("MultiTasker: Gains +200 bonus on energy changes.");
                } else if (selected instanceof game.engine.monsters.Dasher) {
                    passiveLabel.setText("Dasher: Travels double the distance of dice roll.");
                } else if (selected instanceof game.engine.monsters.Dynamo) {
                    passiveLabel.setText("Dynamo: Multiplies all gained/lost energy by 2.");
                } else if (selected instanceof game.engine.monsters.Schemer) {
                    passiveLabel.setText("Schemer: Steals energy from the opponent.");
                } else {
                    passiveLabel.setText("");
                }

                try {
                    String imagePath = "file:src/assets/" + selected.getName() + ".png";
                    imageView.setImage(new Image(imagePath));
                    
                    ScaleTransition bump = new ScaleTransition(Duration.millis(300), imageView);
                    bump.setFromX(0.7); bump.setFromY(0.7);
                    bump.setToX(1.0); bump.setToY(1.0);
                    bump.play();
                } catch (Exception ex) {}
            }
        });

        card.getChildren().addAll(title, picker, imageView, nameLabel, roleLabel, passiveLabel, energyLabel);
        return card;
    }

    public Scene getScene() { return scene; }

    private void showWarningPopup(String message) {
        SoundManager.playSound("error.mp3");

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);
        
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: rgba(26, 26, 46, 0.95); -fx-border-color: #e74c3c; -fx-border-width: 4px; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 20, 0, 0, 0);");
        
        Label lbl = new Label(message);
        lbl.setTextFill(Color.web("#e74c3c"));
        lbl.setFont(Font.font("System", FontWeight.BOLD, 18));
        lbl.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lbl.setWrapText(true);
        
        Button btn = new Button("ACKNOWLEDGE");
        btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8 25; -fx-background-radius: 5;");
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8 25; -fx-background-radius: 5;");
        });
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8 25; -fx-background-radius: 5;"));
        
        btn.setOnAction(ev -> {
            SoundManager.playSound("click.wav");
            popupStage.close();
        });
        
        layout.getChildren().addAll(lbl, btn);
        
        Scene popupScene = new Scene(layout, 400, 180);
        popupScene.setFill(Color.TRANSPARENT);
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