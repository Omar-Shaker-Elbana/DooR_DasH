package game.engine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import game.engine.dataloader.DataLoader;
import game.engine.monsters.Monster;

public class Game {
	 private final Board board;
	 private final ArrayList<Monster> allMonsters;
	 private final Monster player;
	 private final Monster opponent;
	 private Monster current;

	 public Game(Role playerRole) throws IOException{
	        this.allMonsters = DataLoader.readMonsters();
	        
	        this.board = new Board(DataLoader.readCards());
	        
	        this.player = selectRandomMonsterByRole(playerRole);
	        
	        Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
	        this.opponent = selectRandomMonsterByRole(opponentRole);
	        
	        this.current = player;
	        }
	 
	 private Monster selectRandomMonsterByRole(Role role){
		 ArrayList<Monster> candidates = new ArrayList<>();
	        for (Monster m : allMonsters) {
	            if (m.getRole() == role) 
	                candidates.add(m);
	            }
            Random r = new Random();
            return candidates.get(r.nextInt(candidates.size()));
        }
        public ArrayList<Monster> getAllMonsters() { return allMonsters; }
        public Monster getPlayer() { return player; }
        public Monster getOpponent() { return opponent; }
        public Board getBoard() {return board;}
        public Monster getCurrent() { return current; }
        public void setCurrent(Monster current) { this.current = current; }
}
