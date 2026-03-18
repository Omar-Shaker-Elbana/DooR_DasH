package game.engine.exceptions;

public abstract class GameActionException extends Exception{
	
	private static final long serialVersionUID = 1L;
		public GameActionException()
		{
			super();
		}

		public GameActionException(String message)
		{
			super(message);
		}
}
