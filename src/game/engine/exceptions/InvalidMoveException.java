package game.engine.exceptions;

@SuppressWarnings("serial")
public class InvalidMoveException extends GameActionException{
	private static final String MSG = "Invalid move attempted";

	public InvalidMoveException() {
		super(MSG);
	}

	public InvalidMoveException(String message){
		super(message);
	}

}
