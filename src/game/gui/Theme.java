package game.gui;

import game.engine.cards.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * Central design token system for DooR DasH.
 * Aesthetic: "Neon Grid" — deep-space dark, electric cyan + hot-pink player duality,
 * warm gold chrome, glassmorphism surfaces.
 */
public final class Theme {

    private Theme() {}

    // ── Player identity ───────────────────────────────────────────────────
    /** Electric cyan  — Player 1 / Laugher */
    public static final String P1        = "#00d4aa";
    /** Hot pink       — Player 2 / Scarer  */
    public static final String P2        = "#ff2d78";

    // ── UI chrome ─────────────────────────────────────────────────────────
    public static final String ACCENT    = "#f5c518";   // warm gold
    public static final String SUCCESS   = "#10d48e";   // emerald
    public static final String DANGER    = "#ef4444";   // red
    public static final String PURPLE    = "#8b5cf6";   // violet
    public static final String BLUE      = "#3b82f6";   // blue
    public static final String ORANGE    = "#f97316";   // orange
    public static final String MUTED     = "#5a6480";   // quiet gray
    public static final String WHITE     = "#f0f4ff";

    // ── Backgrounds ───────────────────────────────────────────────────────
    public static final String BG_BASE   = "#070711";                    // near-black
    public static final String BG_DARK   = "#0b0d1a";                    // cell dark
    public static final String BG_SURFACE= "#0e1228";                    // panel fill
    public static final String BG_GLASS  = "rgba(12, 16, 36, 0.92)";    // glassmorphism
    public static final String BG_OVERLAY= "rgba(4, 4, 18, 0.90)";      // modal dim

    // ── Board cell tints (overlaid on dark cell button) ───────────────────
    public static final String CELL_DOOR_S   = "rgba(255, 45, 120, 0.14)";
    public static final String CELL_DOOR_L   = "rgba(0, 212, 170, 0.12)";
    public static final String CELL_CARD     = "rgba(139, 92, 246, 0.16)";
    public static final String CELL_BELT     = "rgba(245, 197, 24, 0.13)";
    public static final String CELL_SOCK     = "rgba(239, 68, 68, 0.17)";
    public static final String CELL_MONSTER  = "rgba(139, 92, 246, 0.15)";
    public static final String CELL_START    = "rgba(16, 212, 142, 0.18)";
    public static final String CELL_FINISH   = "rgba(245, 197, 24, 0.22)";
    public static final String CELL_DARK     = "rgba(18, 22, 48, 0.70)";
    public static final String CELL_LIGHT    = "rgba(12, 15, 34, 0.70)";

    // ── Border radius tokens ──────────────────────────────────────────────
    public static final String R_SM  = "6";
    public static final String R_MD  = "10";
    public static final String R_LG  = "16";

    // ── Spacing ───────────────────────────────────────────────────────────
    public static final int PAD_SM = 10;
    public static final int PAD_MD = 18;
    public static final int PAD_LG = 28;

    // ── CSS builders ──────────────────────────────────────────────────────
    public static String panel(String borderColor) {
        return "-fx-background-color:" + BG_GLASS + ";" +
               "-fx-border-color:" + borderColor + "55;" +
               "-fx-border-width:1.5;" +
               "-fx-border-radius:" + R_LG + ";" +
               "-fx-background-radius:" + R_LG + ";";
    }

    public static String panelActive(String borderColor) {
        return "-fx-background-color:" + BG_GLASS + ";" +
               "-fx-border-color:" + borderColor + ";" +
               "-fx-border-width:1.5;" +
               "-fx-border-radius:" + R_LG + ";" +
               "-fx-background-radius:" + R_LG + ";" +
               "-fx-effect:dropshadow(three-pass-box," + borderColor + ",22,0.35,0,0);";
    }

    public static String buttonFilled(String bg, String text) {
        return "-fx-background-color:" + bg + ";" +
               "-fx-text-fill:" + text + ";" +
               "-fx-font-weight:bold;" +
               "-fx-font-size:14px;" +
               "-fx-padding:10 24;" +
               "-fx-background-radius:" + R_MD + ";" +
               "-fx-cursor:hand;";
    }

    public static String buttonFilled(String bg) {
        return buttonFilled(bg, BG_BASE);
    }

    public static String buttonOutline(String color) {
        return "-fx-background-color:transparent;" +
               "-fx-border-color:" + color + ";" +
               "-fx-border-width:1.5;" +
               "-fx-text-fill:" + color + ";" +
               "-fx-font-weight:bold;" +
               "-fx-font-size:14px;" +
               "-fx-padding:9 22;" +
               "-fx-border-radius:" + R_MD + ";" +
               "-fx-background-radius:" + R_MD + ";" +
               "-fx-cursor:hand;";
    }

    public static String buttonDisabled() {
        return "-fx-background-color:#191c35;" +
               "-fx-text-fill:#3a3f62;" +
               "-fx-font-weight:bold;" +
               "-fx-font-size:14px;" +
               "-fx-padding:10 24;" +
               "-fx-background-radius:" + R_MD + ";" +
               "-fx-cursor:default;";
    }

    /** Pill badge with a semi-transparent tinted background. */
    public static String badge(String color) {
        return "-fx-background-color:" + color + "22;" +
               "-fx-text-fill:" + color + ";" +
               "-fx-font-size:11px;" +
               "-fx-font-weight:bold;" +
               "-fx-padding:3 10;" +
               "-fx-background-radius:20;";
    }

    // ── Effect helpers ────────────────────────────────────────────────────
    public static DropShadow glow(String hex, double radius) {
        return new DropShadow(radius, Color.web(hex));
    }

    public static DropShadow glow(String hex) { return glow(hex, 24); }

    // ── Card color — used by CardRevealAnimation, console log, Theme ──────
    public static String cardColor(Card card) {
        if (card instanceof EnergyStealCard) return DANGER;
        if (card instanceof ShieldCard)      return BLUE;
        if (card instanceof ConfusionCard)   return PURPLE;
        if (card instanceof SwapperCard)     return ORANGE;
        if (card instanceof StartOverCard)   return card.isLucky() ? SUCCESS : "#c0392b";
        return P1;
    }

    /** Idempotently attach game.css to the given scene (no-op if missing). */
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