package game.engine.exceptions;

@SuppressWarnings("serial")
public class InvalidMoveException extends GameActionException{
	private static final String MSG = "Invalid move attempted";

	public InvalidMoveException() {
		super(getMsg());
	}

	public InvalidMoveException(String message){
		super(message);
	}

	public static String getMsg() {
		return MSG;
	}

}
