package game.engine;

//DON'T TOUCH OR CHANGE ANY VARIABLES
//TOTAL GAME CRASH FALIURE EXPECTED
public final class Constants {
	/* Board Attributes*/
	public static final int BOARD_SIZE = 100;
	public static final int BOARD_ROWS = 10;
	public static final int BOARD_COLS = 10;
	public static final int WINNING_POSITION = 99;
	public static final int STARTING_POSITION = 0;
	
	/* Special Cells Position*/
	public static final int[] MONSTER_CELL_INDICES = {2, 18, 34, 54, 82, 88};
	public static final int[] CONVEYOR_CELL_INDICES = {6, 22, 44, 52, 66};
	public static final int[] SOCK_CELL_INDICES = {32, 42, 74, 84, 98};
	public static final int[] CARD_CELL_INDICES = {4, 12, 28, 36, 48, 56, 60, 76, 86, 90};
	
	/* Energy Constants */
	 public final static int WINNING_ENERGY = 1000;
	 public final static int MIN_ENERGY = 0;
	
	/* Monsters Constants */ 
	 public final static int MULTITASKER_BONUS = 200;
	 public final static int SCHEMER_STEAL = 10;
	 
	/* Cell Constants */
	 public static final int SLIP_PENALTY = 100;
	 
	/* Power Constants */
	public static final int POWERUP_COST = 500;
	
}
