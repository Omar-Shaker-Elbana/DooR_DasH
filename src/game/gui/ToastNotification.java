package game.gui;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class ToastNotification {

    public enum Level {
        INFO   ("#3498db"),
        SUCCESS("#2ecc71"),
        WARN   ("#f39c12"),
        DANGER ("#e74c3c"),
        POWER  ("#9b59b6"),
        CARD   ("#66fcf1");

        final String hex;
        Level(String h) { this.hex = h; }
    }

    private static final double W   = 370;
    private static final double H   = 70;
    private static final double GAP = 10;

    /** All active toasts currently on screen (static — one global list per app). */
    private static final List<StackPane> active = new ArrayList<>();

    // ────────────────────────────────────────────────────────────────────────
    //  Public API
    // ────────────────────────────────────────────────────────────────────────
    public static void show(StackPane root, String title, String message, Level level) {
        StackPane toast = build(title, message, level);

        // Stack below any existing toasts
        int idx = active.size();
        StackPane.setAlignment(toast, Pos.TOP_RIGHT);
        toast.setTranslateY(20 + idx * (H + GAP));
        toast.setTranslateX(W + 30);  // start off-screen right

        active.add(toast);
        root.getChildren().add(toast);

        // Slide in
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(320), toast);
        slideIn.setToX(-20);
        slideIn.setInterpolator(Interpolator.EASE_OUT);
        slideIn.play();

        // Auto-dismiss after 3.5 s
        PauseTransition hold = new PauseTransition(Duration.seconds(3.5));
        hold.setOnFinished(e -> dismiss(root, toast));
        hold.play();

        // Click-to-dismiss
        toast.setOnMouseClicked(e -> { hold.stop(); dismiss(root, toast); });
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Private helpers
    // ────────────────────────────────────────────────────────────────────────
    private static void dismiss(StackPane root, StackPane toast) {
        TranslateTransition out = new TranslateTransition(Duration.millis(280), toast);
        out.setByX(W + 50);
        out.setInterpolator(Interpolator.EASE_IN);
        out.setOnFinished(e -> {
            root.getChildren().remove(toast);
            active.remove(toast);
            restack();
        });
        out.play();
    }

    /** Slide surviving toasts up after one is removed. */
    private static void restack() {
        for (int i = 0; i < active.size(); i++) {
            TranslateTransition slide = new TranslateTransition(Duration.millis(220), active.get(i));
            slide.setToY(20 + i * (H + GAP));
            slide.setInterpolator(Interpolator.EASE_OUT);
            slide.play();
        }
    }

    private static StackPane build(String title, String message, Level level) {
        StackPane toast = new StackPane();
        toast.setMaxSize(W, H);
        toast.setMinSize(W, H);

        // Background card
        Rectangle bg = new Rectangle(W, H);
        bg.setArcWidth(12); bg.setArcHeight(12);
        bg.setFill(Color.web("#12121e", 0.96));
        bg.setStroke(Color.web(level.hex, 0.75));
        bg.setStrokeWidth(1.5);

        // Left accent bar
        Rectangle accent = new Rectangle(5, H - 16);
        accent.setArcWidth(4); accent.setArcHeight(4);
        accent.setFill(Color.web(level.hex));
        StackPane.setAlignment(accent, Pos.CENTER_LEFT);
        StackPane.setMargin(accent, new Insets(0, 0, 0, 7));

        // Text
        VBox text = new VBox(4);
        text.setPadding(new Insets(10, 16, 10, 22));
        text.setAlignment(Pos.CENTER_LEFT);

        Label titleLbl = new Label(title);
        titleLbl.setStyle(
            "-fx-text-fill:" + level.hex + ";-fx-font-weight:bold;-fx-font-size:14px;");

        Label msgLbl = new Label(message);
        msgLbl.setStyle("-fx-text-fill:rgba(255,255,255,0.85);-fx-font-size:12px;");
        msgLbl.setWrapText(true);
        msgLbl.setMaxWidth(W - 44);

        text.getChildren().addAll(titleLbl, msgLbl);

        toast.getChildren().addAll(bg, accent, text);
        toast.setEffect(new DropShadow(14, Color.web(level.hex, 0.35)));
        toast.setStyle("-fx-cursor:hand;");
        return toast;
    }
}