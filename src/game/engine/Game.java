package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.*;

public class Game {
    private Board board;
    private ArrayList<Monster> allMonsters; 
    private Monster player;
    private Monster opponent;
    private Monster current;
    private int lastRoll; 
    
    public Game(Role playerRole) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        
        this.player = selectRandomMonsterByRole(playerRole);
        
        Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
        this.opponent = selectRandomOpponent(opponentRole, player.getName());
        
        this.current = player;
        
        allMonsters.removeIf(m -> m.getName().equals(player.getName()) || m.getName().equals(opponent.getName()));
        
        Board.setStationedMonsters(allMonsters);
        board.initializeBoard(DataLoader.readCells());
    }

    public Game(Monster selectedMonster) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        
        this.player = selectedMonster;
        
        Role opponentRole = (player.getRole() == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
        this.opponent = selectRandomOpponent(opponentRole, player.getName());
        
        this.current = player;
        
        allMonsters.removeIf(m -> m.getName().equals(player.getName()) || m.getName().equals(opponent.getName()));
        
        Board.setStationedMonsters(allMonsters);
        board.initializeBoard(DataLoader.readCells());
    }

    public Game(Monster player1, Monster player2) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        
        this.player = player1;
        this.opponent = player2; 
        
        this.current = player; 
        
        allMonsters.removeIf(m -> m.getName().equals(player.getName()) || m.getName().equals(opponent.getName()));
        
        Board.setStationedMonsters(allMonsters);
        board.initializeBoard(DataLoader.readCells());
    }

    public Board getBoard() {
        return board;
    }
    
    public ArrayList<Monster> getAllMonsters() {
        return allMonsters; 
    }
    
    public Monster getPlayer() {
        return player;
    }
    
    public Monster getOpponent() {
        return opponent;
    }
    
    public Monster getCurrent() {
        return current;
    }
    
    public void setCurrent(Monster current) {
        this.current = current;
    }
    
    public int getLastRoll() {
        return lastRoll;
    }
    
    private Monster selectRandomMonsterByRole(Role role) {
        Collections.shuffle(allMonsters);
        return allMonsters.stream()
                .filter(m -> m.getRole() == role)
                .findFirst()
                .orElse(null);
    }
    
    private Monster getCurrentOpponent() {
        return current == player ? opponent : player;
    }

    private int rollDice() {
        Random rand = new Random();
        return rand.nextInt(6) + 1;
    }
    
    public void usePowerup() throws OutOfEnergyException {
        if (current.getEnergy() < Constants.POWERUP_COST)
            throw new OutOfEnergyException("Not enough energy to use powerup");
        
        current.executePowerupEffect(getCurrentOpponent());
        current.setEnergy(current.getEnergy() - Constants.POWERUP_COST);
    }
    
    public void playTurn() throws InvalidMoveException {
        if (current.isFrozen()) {
            System.out.println(current.getName() + " is frozen! Turn skipped.");
            current.setFrozen(false);
            switchTurn();
            return;
        }
        
        lastRoll = rollDice(); 
        
        board.moveMonster(current, lastRoll, getCurrentOpponent());
        
        switchTurn();
    }
    
    private void switchTurn() {
        this.setCurrent(getCurrentOpponent());
    }
    
    private boolean checkWinCondition(Monster monster) {
        return monster.getPosition() == Constants.WINNING_POSITION && 
               monster.getEnergy() >= Constants.WINNING_ENERGY;
    }
    
    public Monster getWinner() {
        if (checkWinCondition(player)) 
            return player;
        
        if (checkWinCondition(opponent)) 
            return opponent;
        
        return null;
    }

    private Monster selectRandomOpponent(Role role, String playerName) {
        ArrayList<Monster> candidates = new ArrayList<>();
        for (Monster m : allMonsters) {
            if (m.getRole() == role && !m.getName().equals(playerName)) {
                candidates.add(m);
            }
        }
        Collections.shuffle(candidates);
        return candidates.isEmpty() ? null : candidates.get(0);
    }
}