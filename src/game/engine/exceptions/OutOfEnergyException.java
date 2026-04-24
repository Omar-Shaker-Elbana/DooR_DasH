package game.engine.exceptions;

@SuppressWarnings("serial")
public class OutOfEnergyException extends GameActionException {
	private static final String MSG = "Not Enough Energy for Power Up";

	public OutOfEnergyException() {
		super(MSG);
	}

	public OutOfEnergyException(String message) {
		super(message);
	}

}
