package game.gui;

import game.engine.cards.*;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.Random;

public class CardRevealAnimation {

    private static final double W = 300;
    private static final double H = 420;

    /**
     * Shows a 3D-flip card reveal over the given root StackPane.
     * Calls onFinish after the user clicks CONTINUE.
     */
    public static void reveal(StackPane root, Card card, Runnable onFinish) {
        String hex = colorFor(card);

        // ── Dim overlay ─────────────────────────────────────────────────────
        Rectangle dimmer = new Rectangle();
        dimmer.widthProperty().bind(root.widthProperty());
        dimmer.heightProperty().bind(root.heightProperty());
        dimmer.setFill(Color.TRANSPARENT);

        // ── Card wrapper (holds back + front) ───────────────────────────────
        StackPane holder = new StackPane();
        holder.setMaxSize(W, H);
        holder.setMinSize(W, H);

        StackPane back  = buildBack(hex);
        StackPane front = buildFront(card, hex);
        front.setVisible(false);

        DropShadow glow = new DropShadow(50, Color.web(hex));
        holder.getChildren().addAll(back, front);
        holder.setEffect(glow);

        StackPane overlay = new StackPane(dimmer, holder);
        root.getChildren().add(overlay);

        // ── 1. Dim the background ────────────────────────────────────────────
        Timeline dimAnim = new Timeline(
            new KeyFrame(Duration.ZERO,        new KeyValue(dimmer.fillProperty(), Color.TRANSPARENT)),
            new KeyFrame(Duration.millis(300), new KeyValue(dimmer.fillProperty(), Color.rgb(0, 0, 0, 0.82)))
        );

        // ── 2. Card entrance: spring-scale + float up ────────────────────────
        holder.setScaleX(0.05);
        holder.setScaleY(0.05);
        holder.setTranslateY(60);
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(420), holder);
        scaleIn.setToX(1); scaleIn.setToY(1);
        scaleIn.setInterpolator(Interpolator.SPLINE(0.17, 0.67, 0.34, 1.27)); // overshoot spring

        TranslateTransition floatIn = new TranslateTransition(Duration.millis(420), holder);
        floatIn.setToY(0);
        floatIn.setInterpolator(Interpolator.EASE_OUT);

        // ── 3. Flip: shrink on X → swap faces → grow on X ───────────────────
        //    (ScaleX 1→0→1 is the correct way to simulate Y-axis card flip
        //     without needing a PerspectiveCamera on the scene)
        ScaleTransition shrinkX = new ScaleTransition(Duration.millis(220), holder);
        shrinkX.setFromX(1); shrinkX.setToX(0);
        shrinkX.setInterpolator(Interpolator.EASE_IN);

        ScaleTransition growX = new ScaleTransition(Duration.millis(220), holder);
        growX.setFromX(0); growX.setToX(1);
        growX.setInterpolator(Interpolator.EASE_OUT);

        shrinkX.setOnFinished(e -> {
            back.setVisible(false);
            front.setVisible(true);
            SoundManager.playSound("card.wav");
            growX.play();
        });

        growX.setOnFinished(e -> {
            // Glow surge
            Timeline glowPulse = new Timeline(
                new KeyFrame(Duration.ZERO,        new KeyValue(glow.radiusProperty(), 50)),
                new KeyFrame(Duration.millis(300), new KeyValue(glow.radiusProperty(), 115)),
                new KeyFrame(Duration.millis(650), new KeyValue(glow.radiusProperty(), 50))
            );
            glowPulse.play();

            // Particle burst
            burstParticles(overlay, hex);

            // Continue button (appears below the card)
            Button btn = new Button("CONTINUE  ›");
            btn.setStyle(
                "-fx-background-color:" + hex + ";" +
                "-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:16px;" +
                "-fx-padding:12 38;-fx-background-radius:25;-fx-cursor:hand;"
            );
            btn.setTranslateY(H / 2.0 + 40);
            btn.setOpacity(0);
            overlay.getChildren().add(btn);

            btn.setOnMouseEntered(ev -> btn.setOpacity(0.72));
            btn.setOnMouseExited( ev -> btn.setOpacity(1.0));
            btn.setOnAction(ev -> {
                FadeTransition fo = new FadeTransition(Duration.millis(220), overlay);
                fo.setToValue(0);
                fo.setOnFinished(fe -> {
                    root.getChildren().remove(overlay);
                    if (onFinish != null) onFinish.run();
                });
                fo.play();
            });

            FadeTransition btnAppear = new FadeTransition(Duration.millis(360), btn);
            btnAppear.setToValue(1);
            btnAppear.play();
        });

        // ── Sequence: dim + entrance simultaneously → pause → flip ──────────
        dimAnim.play();
        ParallelTransition entrance = new ParallelTransition(scaleIn, floatIn);
        entrance.setOnFinished(e -> {
            PauseTransition wait = new PauseTransition(Duration.millis(560));
            wait.setOnFinished(p -> shrinkX.play());
            wait.play();
        });
        entrance.play();
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Card Back
    // ────────────────────────────────────────────────────────────────────────
    private static StackPane buildBack(String hex) {
        StackPane p = new StackPane();
        p.setPrefSize(W, H);

        p.getChildren().addAll(rx(W, H, "#0b0c10"), rxBorder(W, H, hex));

        // Dot-grid pattern background
        GridPane dots = new GridPane();
        dots.setHgap(20); dots.setVgap(20);
        dots.setPadding(new Insets(36));
        dots.setAlignment(Pos.CENTER);
        for (int r = 0; r < 7; r++)
            for (int c = 0; c < 5; c++)
                dots.add(new Circle(3, Color.web(hex, 0.20)), c, r);

        Label q = new Label("?");
        q.setStyle(
            "-fx-font-size:92px;-fx-font-weight:bold;-fx-text-fill:" + hex + ";" +
            "-fx-effect:dropshadow(three-pass-box," + hex + ",24,0.5,0,0);"
        );

        Label brand = new Label("D O O R  D A S H");
        brand.setStyle("-fx-font-size:13px;-fx-letter-spacing:3;" +
                       "-fx-text-fill:rgba(255,255,255,0.28);-fx-font-weight:bold;");
        StackPane.setAlignment(brand, Pos.BOTTOM_CENTER);
        StackPane.setMargin(brand, new Insets(0, 0, 18, 0));

        p.getChildren().addAll(dots, q, brand);
        return p;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Card Front
    // ────────────────────────────────────────────────────────────────────────
    private static StackPane buildFront(Card card, String hex) {
        StackPane p = new StackPane();
        p.setPrefSize(W, H);
        p.getChildren().addAll(rx(W, H, "#0b0c10"), rxBorder(W, H, hex));

        // Accent header strip
        Rectangle header = new Rectangle(W - 4, 58);
        header.setFill(Color.web(hex, 0.22));
        StackPane.setAlignment(header, Pos.TOP_CENTER);
        StackPane.setMargin(header, new Insets(2, 0, 0, 0));
        p.getChildren().add(header);

        VBox content = new VBox(16);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(12, 24, 20, 24));

        Label type = new Label(typeName(card));
        type.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:" + hex +
                      ";-fx-letter-spacing:2;");

        Label icon = new Label(iconFor(card));
        icon.setStyle("-fx-font-size:68px;" +
                      "-fx-effect:dropshadow(three-pass-box," + hex + ",26,0.6,0,0);");

        Rectangle sep = new Rectangle(W - 72, 2);
        sep.setFill(Color.web(hex, 0.4));

        Label desc = new Label(card.getDescription());
        desc.setStyle("-fx-font-size:14px;-fx-text-fill:rgba(255,255,255,0.88);" +
                      "-fx-text-alignment:center;");
        desc.setWrapText(true);
        desc.setMaxWidth(W - 48);
        desc.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label stars = new Label(rarityStars(card.getRarity()));
        stars.setStyle("-fx-font-size:20px;-fx-text-fill:#f1c40f;");

        content.getChildren().addAll(type, icon, sep, desc, stars);
        p.getChildren().add(content);
        return p;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Particle burst at card reveal
    // ────────────────────────────────────────────────────────────────────────
    private static void burstParticles(StackPane root, String hex) {
        Random rng = new Random();
        Color c = Color.web(hex);
        for (int i = 0; i < 28; i++) {
            Circle particle = new Circle(rng.nextInt(5) + 3, c);
            root.getChildren().add(particle);

            double angle = rng.nextDouble() * 360;
            double dist  = rng.nextDouble() * 170 + 70;

            TranslateTransition move = new TranslateTransition(
                Duration.millis(rng.nextInt(400) + 500), particle);
            move.setByX(Math.cos(Math.toRadians(angle)) * dist);
            move.setByY(Math.sin(Math.toRadians(angle)) * dist);

            FadeTransition fade = new FadeTransition(
                Duration.millis(rng.nextInt(400) + 600), particle);
            fade.setFromValue(0.9);
            fade.setToValue(0);
            fade.setOnFinished(e -> root.getChildren().remove(particle));

            new ParallelTransition(move, fade).play();
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Helpers
    // ────────────────────────────────────────────────────────────────────────
    private static Rectangle rx(double w, double h, String fill) {
        Rectangle r = new Rectangle(w, h);
        r.setArcWidth(18); r.setArcHeight(18);
        r.setFill(Color.web(fill));
        return r;
    }

    private static Rectangle rxBorder(double w, double h, String stroke) {
        Rectangle r = new Rectangle(w, h);
        r.setArcWidth(18); r.setArcHeight(18);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.web(stroke));
        r.setStrokeWidth(3);
        return r;
    }

    // package-visible so GameBoard can also use it for console colouring
    static String colorFor(Card card) {
        if (card instanceof EnergyStealCard) return "#e74c3c";
        if (card instanceof ShieldCard)      return "#3498db";
        if (card instanceof ConfusionCard)   return "#9b59b6";
        if (card instanceof SwapperCard)     return "#f39c12";
        if (card instanceof StartOverCard)   return card.isLucky() ? "#2ecc71" : "#c0392b";
        return "#66fcf1";
    }

    private static String typeName(Card card) {
        if (card instanceof EnergyStealCard) return "ENERGY STEAL";
        if (card instanceof ShieldCard)      return "SHIELD";
        if (card instanceof ConfusionCard)   return "CONFUSION";
        if (card instanceof SwapperCard)     return "SWAPPER";
        if (card instanceof StartOverCard)   return card.isLucky() ? "LUCKY START" : "START OVER";
        return card.getName().toUpperCase();
    }

    private static String iconFor(Card card) {
        if (card instanceof EnergyStealCard) return "💸";
        if (card instanceof ShieldCard)      return "🛡";
        if (card instanceof ConfusionCard)   return "🌀";
        if (card instanceof SwapperCard)     return "🔄";
        if (card instanceof StartOverCard)   return card.isLucky() ? "🍀" : "💀";
        return "🃏";
    }

    private static String rarityStars(int rarity) {
        int n = Math.max(1, Math.min(5, rarity));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(i < n ? "★" : "☆");
        return sb.toString();
    }
}