package game.gui;

import javafx.scene.media.AudioClip;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    private static HashMap<String, AudioClip> clips = new HashMap<>();

    public static void preloadAllSounds() {
        try {
            File folder = new File("src/assets/sounds/");
            if (folder.exists() && folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    String name = file.getName();
                    if (name.endsWith(".mp3") || name.endsWith(".wav")) {
                        clips.put(name, new AudioClip(file.toURI().toString()));
                    }
                }
                System.out.println("AudioClips preloaded successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error preloading sounds.");
        }
    }

    public static void playSound(String fileName) {
        new Thread(() -> {
            try {
                AudioClip clip;
                if (clips.containsKey(fileName)) {
                    clip = clips.get(fileName);
                } else {
                    File soundFile = new File("src/assets/sounds/" + fileName);
                    if (soundFile.exists()) {
                        clip = new AudioClip(soundFile.toURI().toString());
                        clips.put(fileName, clip);
                    } else {
                        System.out.println("Sound not found: " + fileName);
                        return;
                    }
                }
                clip.play();
            } catch (Exception e) {
                System.out.println("Error playing: " + fileName);
            }
        }).start();
    }
}