package game.engine.exceptions;

@SuppressWarnings("serial")
public class InvalidTurnException extends GameActionException{
	private static final String MSG = "Action done on wrong turn";

	public InvalidTurnException() {
		super(MSG);
	}

	public InvalidTurnException(String message){
		super(message);
	}

}
