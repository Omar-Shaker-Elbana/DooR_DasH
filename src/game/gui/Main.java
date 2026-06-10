package game.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

public class Main extends Application {

    private Stage window;
    

    @Override
    public void start(Stage primaryStage) {
        SoundManager.preloadAllSounds();
        this.window = primaryStage;
        window.setTitle("DooR DasH: Scare vs Laugh Touchdown");
        window.setMinWidth(1024);
        window.setMinHeight(700);

        StartMenu startMenu = new StartMenu(this);
        Scene scene = startMenu.getScene();

        // Apply the global stylesheet once here; it stays on the scene
        // even when GameBoard replaces the root node.
        Theme.applyTo(scene);

        window.setScene(scene);
        window.setOnCloseRequest(e -> Platform.exit()); // clean JavaFX shutdown
        window.show();
    }

    public Stage getWindow() {
        return window;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
