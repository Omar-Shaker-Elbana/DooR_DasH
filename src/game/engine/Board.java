package game.engine;

import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		cards = new ArrayList<Card>();
		setCardsByRarity();
		reloadCards();
	}

	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}

	private int[] indexToRowCol(int index) {
	    int cols = Constants.BOARD_COLS;

	    int row = index / cols;
	    int col = index % cols;

	    if (row % 2 == 1)
	        col = cols - 1 - col;

	    return new int[]{row, col};
	}

	
	private Cell getCell(int index) {
		int[] pos = indexToRowCol(index);
		return boardCells[pos[0]][pos[1]];
	}
	
	private void setCell(int index, Cell cell) {
		int[] pos = indexToRowCol(index);
		boardCells[pos[0]][pos[1]] = cell;
	}
	
	public void initializeBoard(ArrayList<Cell> specialCells) {
		ArrayList<Cell> doorCells = new ArrayList<>();
	    ArrayList<Cell> conveyorCells = new ArrayList<>();
		ArrayList<Cell> contaminationCells = new ArrayList<>();
		
	    for (Cell cell : specialCells) {
	        if (cell instanceof DoorCell) 
	            doorCells.add(cell);
	        else if (cell instanceof ConveyorBelt) 
	        	conveyorCells.add(cell);
	        else if (cell instanceof ContaminationSock) 
	            contaminationCells.add(cell);
	    }
	    
	    for (int i = 0; i < Constants.BOARD_SIZE; i++) 
	    	setCell(i, (i % 2 == 0) ? new Cell("Normal Rest Corridor") : doorCells.remove(0));
	       
	    for (int cardIndex : Constants.CARD_CELL_INDICES) 
	        setCell(cardIndex, new CardCell("Card Cell"));
	    
	    for (int conveyorIndex : Constants.CONVEYOR_CELL_INDICES) 
	        setCell(conveyorIndex, conveyorCells.remove(0));
	    
	    for (int contaminationIndex : Constants.SOCK_CELL_INDICES) 
	        setCell(contaminationIndex, contaminationCells.remove(0));

	    for (int i = 0; i < stationedMonsters.size(); i++) {
	        Monster monster = stationedMonsters.get(i);
	        monster.setPosition(Constants.MONSTER_CELL_INDICES[i]);
	        setCell(Constants.MONSTER_CELL_INDICES[i], new MonsterCell(monster.getName(), monster));
	    }
	}
	
	private void setCardsByRarity() {
	    ArrayList<Card> expandedCards = new ArrayList<>();

	    for (Card card : originalCards)
	        expandedCards.addAll(Collections.nCopies(card.getRarity(), card));
	    
	    originalCards = expandedCards;
	}
	
	public static void reloadCards() {
		cards = new ArrayList<>(originalCards);
		Collections.shuffle(cards);
    }
	
	public static Card drawCard() {
		if (cards.isEmpty()) 
			reloadCards();
		
		return cards.remove(0);
	}

	public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
	    Role oldRole = currentMonster.getRole();
	    int oldPosition = currentMonster.getPosition();
	    
	    currentMonster.move(roll);

	    getCell(currentMonster.getPosition()).onLand(currentMonster, opponentMonster);

	    if (currentMonster.getPosition() == opponentMonster.getPosition()) {
	        currentMonster.setPosition(oldPosition);
	        throw new InvalidMoveException("Cannot land on opponent!");
	    }
	    
		// To make sure we dont decrement right after getting the confusion card action upon on land
	
	    if (currentMonster.isConfused() && currentMonster.getRole() == oldRole) {
	        currentMonster.decrementConfusion();
	        opponentMonster.decrementConfusion();
	    }
	    
	    updateMonsterPositions(currentMonster, opponentMonster);
	}

	private void updateMonsterPositions(Monster player, Monster opponent) {
		for (int i = 0; i < Constants.BOARD_SIZE; i++) 
			getCell(i).setMonster(null);
		
		getCell(player.getPosition()).setMonster(player);
		getCell(opponent.getPosition()).setMonster(opponent);
	}
}
