package game.engine;
import game.engine.cells.*;
import game.engine.monsters.Monster;
import game.engine.cards.Card;
import game.engine.exceptions.InvalidMoveException;
import java.util.ArrayList;
import java.util.Collections;

public class Board {
    private Cell[][] boardCells;
    private static ArrayList<Monster> stationedMonsters = new ArrayList<>();
    private static ArrayList<Card> originalCards = new ArrayList<>();
    public static ArrayList<Card> cards = new ArrayList<>();

    public Board(ArrayList<Card> readCards) {
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];

        stationedMonsters = new ArrayList<>();
        cards = new ArrayList<>();
        originalCards = new ArrayList<>();

        // Expand by rarity into originalCards
        for (Card c : readCards) {
            for (int i = 0; i < c.getRarity(); i++) {
                originalCards.add(c);
            }
        }
        setCardsByRarity();
        reloadCards();
    }
    private void setCardsByRarity() {
        ArrayList<Card> expanded = new ArrayList<>();
        for (Card c : originalCards) {
            for (int i = 0; i < c.getRarity(); i++) {
                expanded.add(c);
            }
        }
        originalCards = expanded;
    }
    private int[] indexToRowCol(int index) {
        int row = index / Constants.BOARD_COLS;
        int col = index % Constants.BOARD_COLS;

        if (row % 2 != 0) {
            col = (Constants.BOARD_COLS - 1) - col;
        }
        return new int[]{row, col};
    }

    private Cell getCell(int index) {
        int[] coords = indexToRowCol(index);
        return boardCells[coords[0]][coords[1]];
    }

    private void setCell(int index, Cell cell) {
        int[] coords = indexToRowCol(index);
        boardCells[coords[0]][coords[1]] = cell;
    }

    public void initializeBoard(ArrayList<Cell> specialCells) {
        if (this.boardCells == null) {
            this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
        }

        ArrayList<DoorCell> doors = new ArrayList<>();
        ArrayList<ConveyorBelt> conveyors = new ArrayList<>();
        ArrayList<ContaminationSock> socks = new ArrayList<>();

        if (specialCells != null) {
            for (Cell cell : specialCells) {
                if (cell instanceof DoorCell) doors.add((DoorCell) cell);
                else if (cell instanceof ConveyorBelt) conveyors.add((ConveyorBelt) cell);
                else if (cell instanceof ContaminationSock) socks.add((ContaminationSock) cell);
            }
        }

        int doorIndex = 0;
        int conveyorIndex = 0;
        int sockIndex = 0;
        int monsterIndex = 0;

        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            if (contains(Constants.MONSTER_CELL_INDICES, i)) {
                Monster stationed = null;
                if (stationedMonsters != null && monsterIndex < stationedMonsters.size()) {
                    stationed = stationedMonsters.get(monsterIndex++);
                    stationed.setPosition(i);
                }
                setCell(i, new MonsterCell("Monster Cell", stationed));

            } else if (contains(Constants.CARD_CELL_INDICES, i)) {
                setCell(i, new CardCell("Card Cell"));

            } else if (contains(Constants.CONVEYOR_CELL_INDICES, i)) {
                if (conveyorIndex < conveyors.size()) {
                    setCell(i, conveyors.get(conveyorIndex++));
                } else {
                    setCell(i, new ConveyorBelt("Fallback Conveyor", 1));
                }

            } else if (contains(Constants.SOCK_CELL_INDICES, i)) {
                if (sockIndex < socks.size()) {
                    setCell(i, socks.get(sockIndex++));
                } else {
                    setCell(i, new ContaminationSock("Fallback Sock", -1));
                }

            } else if (i % 2 != 0) {
                if (doorIndex < doors.size()) {
                    setCell(i, doors.get(doorIndex++));
                } else {
                    setCell(i, new DoorCell("Fallback Door", Role.SCARER, 0));
                }

            } else {
                setCell(i, new Cell("Regular Cell"));
            }
        }
    }

    private boolean contains(int[] arr, int targetValue) {
        if (arr == null) return false;
        for (int val : arr) {
            if (val == targetValue) return true;
        }
        return false;
    }

    public static void reloadCards() {
        if (cards == null) cards = new ArrayList<>();
        if (originalCards == null) originalCards = new ArrayList<>();

        cards.clear();
        cards.addAll(originalCards); // already expanded, just copy

        if (!cards.isEmpty()) {
            Collections.shuffle(cards);
        }
    }

    public static Card drawCard() {
        if (cards == null) cards = new ArrayList<>();

        if (cards.isEmpty()) {
            reloadCards();
        }

        if (cards.isEmpty()) {
            return null;
        }

        return cards.remove(0);
    }

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
        int oldPosition = currentMonster.getPosition();

        currentMonster.move(roll);
        int newPosition = currentMonster.getPosition();

        Cell landedCell = getCell(newPosition);
        landedCell.onLand(currentMonster, opponentMonster);

        int finalPosition = currentMonster.getPosition();

        if (finalPosition == opponentMonster.getPosition() && finalPosition != 0) {
            currentMonster.setPosition(oldPosition);
            throw new InvalidMoveException(InvalidMoveException.getMsg());
        }

        if (currentMonster.isConfused()) currentMonster.decrementConfusion();
        if (opponentMonster.isConfused()) opponentMonster.decrementConfusion();

        updateMonsterPositions(currentMonster, opponentMonster);
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            getCell(i).setMonster(null);
        }

        getCell(player.getPosition()).setMonster(player);
        getCell(opponent.getPosition()).setMonster(opponent);
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
}