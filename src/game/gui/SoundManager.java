package game.gui;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class SoundManager {

    // ── Sounds to preload on startup ──────────────────────────────────────
    private static final String[] PRELOAD = {
        "dice.wav", "card.wav", "gain.wav", "lose.wav",
        "powerup.wav", "collision.wav", "victory.wav",
        "game_over.wav", "click.wav"
    };

    private static final Map<String, AudioClip> clips = new HashMap<>();
    private static boolean muted  = false;
    private static double  volume = 0.85;

    private SoundManager() {}

    // ── Startup preload ───────────────────────────────────────────────────
    public static void preloadAllSounds() {
        for (String name : PRELOAD) loadClip(name);
        System.out.println("[SoundManager] Preloaded " + clips.size() + " clips.");
    }

    // ── Playback ──────────────────────────────────────────────────────────
    /**
     * Plays a sound file from /assets/sounds/.
     * Non-blocking — AudioClip.play() does not need a wrapper thread.
     */
    public static void playSound(String fileName) {
        if (muted) return;
        AudioClip clip = clips.containsKey(fileName)
                         ? clips.get(fileName)
                         : loadClip(fileName);
        if (clip != null) clip.play(volume);
    }

    // ── Volume & mute controls ────────────────────────────────────────────
    public static void toggleMute() {
        muted = !muted;
        System.out.println("[SoundManager] Muted: " + muted);
    }

    public static boolean isMuted() { return muted; }

    /**
     * Sets master volume 0.0 – 1.0 and updates all cached clips immediately.
     */
    public static void setVolume(double v) {
        volume = Math.max(0.0, Math.min(1.0, v));
        clips.values().forEach(c -> c.setVolume(volume));
    }

    public static double getVolume() { return volume; }

    // ── Internal loader ───────────────────────────────────────────────────
    private static AudioClip loadClip(String fileName) {
        try {
            URL url = SoundManager.class.getResource("/assets/sounds/" + fileName);
            if (url == null) {
                System.out.println("[SoundManager] Not found: " + fileName);
                return null;
            }
            AudioClip clip = new AudioClip(url.toExternalForm());
            clip.setVolume(volume);
            clips.put(fileName, clip);
            return clip;
        } catch (Exception e) {
            System.out.println("[SoundManager] Error loading " + fileName
                               + " — " + e.getMessage());
            return null;
        }
    }
}