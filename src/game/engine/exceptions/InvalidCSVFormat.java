package game.engine.exceptions;
import java.io.IOException;


public class InvalidCSVFormat extends IOException {
    public static final String MSG = "Invalid input detected while reading csv file, input = \n";
    private String inputLine;

    public InvalidCSVFormat(String inputLine) {
        super(MSG + inputLine);
        this.inputLine = inputLine;
    }

    public InvalidCSVFormat(String message, String inputLine){
        super(message);
        this.inputLine = inputLine;
    }

    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    public String getInputLine(){
        return inputLine;
    }
}
