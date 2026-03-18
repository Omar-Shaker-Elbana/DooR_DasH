package game.engine.exceptions;

public class OutOfEnergyException extends GameActionException {

    public static final String MSG = "Not enough energy for power up";

    public OutOfEnergyException() {
        super(MSG);
    }

    public OutOfEnergyException(String message) {
        super(message);
    }
}