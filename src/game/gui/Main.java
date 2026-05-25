package game.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage window;
    

    @Override
    public void start(Stage primaryStage) {
    	SoundManager.preloadAllSounds();
        this.window = primaryStage;
        window.setTitle("DooR DasH: Scare vs Laugh Touchdown");
        
        StartMenu startMenu = new StartMenu(this);
        window.setScene(startMenu.getScene());
        
        window.setOnCloseRequest(e -> System.exit(0));
        
        window.show();
    }

    public Stage getWindow() {
        return window;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
