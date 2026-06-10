package game.gui;

import game.engine.cards.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public final class Theme {

    private Theme() {}

    // ── Brand colors ──────────────────────────────────────────────────────
    public static final String P1         = "#00b894";
    public static final String P2         = "#e84393";
    public static final String ACCENT     = "#66fcf1";
    public static final String GOLD       = "#f1c40f";
    public static final String DANGER     = "#e74c3c";
    public static final String SUCCESS    = "#2ecc71";
    public static final String PURPLE     = "#9b59b6";
    public static final String BLUE       = "#3498db";
    public static final String ORANGE     = "#f39c12";

    // ── Background layers ─────────────────────────────────────────────────
    public static final String BG_DEEP    = "#05050f";
    public static final String BG_DARK    = "#0b0c10";
    public static final String SURFACE    = "rgba(22, 33, 62, 0.93)";
    public static final String SURFACE_2  = "rgba(31, 40, 51, 0.88)";
    public static final String OVERLAY    = "rgba(0, 0, 0, 0.82)";

    // ── Cell tints ────────────────────────────────────────────────────────
    public static final String CELL_DOOR_S  = "rgba(232, 67, 147, 0.13)";
    public static final String CELL_DOOR_L  = "rgba(0, 184, 148, 0.13)";
    public static final String CELL_CARD    = "rgba(102, 252, 241, 0.11)";
    public static final String CELL_BELT    = "rgba(241, 196, 15, 0.11)";
    public static final String CELL_SOCK    = "rgba(231, 76, 60, 0.15)";
    public static final String CELL_MONSTER = "rgba(155, 89, 182, 0.13)";
    public static final String CELL_START   = "rgba(46, 204, 113, 0.19)";
    public static final String CELL_FINISH  = "rgba(241, 196, 15, 0.23)";

    // ── Spacing ───────────────────────────────────────────────────────────
    public static final int PAD_LG = 30;
    public static final int PAD_MD = 20;
    public static final int PAD_SM = 10;
    public static final String RADIUS = "14";

    // ── CSS string builders ───────────────────────────────────────────────
    public static String panel(String borderColor) {
        return "-fx-background-color:" + SURFACE + ";" +
               "-fx-border-color:" + borderColor + ";" +
               "-fx-border-width:3px;" +
               "-fx-border-radius:" + RADIUS + ";" +
               "-fx-background-radius:" + RADIUS + ";";
    }

    public static String buttonFilled(String bgColor, String textColor) {
        return "-fx-background-color:" + bgColor + ";" +
               "-fx-text-fill:" + textColor + ";" +
               "-fx-font-weight:bold;" +
               "-fx-font-size:15px;" +
               "-fx-padding:10 22;" +
               "-fx-background-radius:8;" +
               "-fx-cursor:hand;";
    }

    public static String buttonFilled(String bgColor) {
        return buttonFilled(bgColor, "white");
    }

    public static String buttonDisabled() {
        return "-fx-background-color:#2a2a3a;" +
               "-fx-text-fill:#44445a;" +
               "-fx-font-weight:bold;" +
               "-fx-font-size:15px;" +
               "-fx-padding:10 22;" +
               "-fx-background-radius:8;" +
               "-fx-cursor:default;";
    }

    public static String buttonOutline(String color) {
        return "-fx-background-color:transparent;" +
               "-fx-border-color:" + color + ";" +
               "-fx-border-width:2px;" +
               "-fx-text-fill:" + color + ";" +
               "-fx-font-weight:bold;" +
               "-fx-font-size:15px;" +
               "-fx-padding:10 22;" +
               "-fx-border-radius:8;" +
               "-fx-background-radius:8;" +
               "-fx-cursor:hand;";
    }

    public static String badge(String color) {
        return "-fx-background-color:" + color + ";" +
               "-fx-text-fill:white;" +
               "-fx-font-size:11px;" +
               "-fx-font-weight:bold;" +
               "-fx-padding:3 9;" +
               "-fx-background-radius:12;";
    }

    // ── DropShadow helpers ────────────────────────────────────────────────
    public static DropShadow glow(String hex, double radius) {
        return new DropShadow(radius, Color.web(hex));
    }

    public static DropShadow glow(String hex) {
        return glow(hex, 28);
    }

    // ── Card color — used by CardRevealAnimation and console log ──────────
    public static String cardColor(Card card) {
        if (card instanceof EnergyStealCard) return DANGER;
        if (card instanceof ShieldCard)      return BLUE;
        if (card instanceof ConfusionCard)   return PURPLE;
        if (card instanceof SwapperCard)     return ORANGE;
        if (card instanceof StartOverCard)   return card.isLucky() ? SUCCESS : "#c0392b";
        return ACCENT;
    }

    // ── Utility ───────────────────────────────────────────────────────────
    /** Applies the stylesheet to a scene once, idempotent. */
    public static void applyTo(javafx.scene.Scene scene) {
        try {
            String path = Theme.class.getResource("/assets/game.css").toExternalForm();
            if (!scene.getStylesheets().contains(path))
                scene.getStylesheets().add(path);
        } catch (Exception e) {
            System.out.println("[Theme] game.css not found — skipping stylesheet.");
        }
    }
}