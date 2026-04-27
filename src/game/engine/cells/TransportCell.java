package game.engine.cells;

import game.engine.Constants;
import game.engine.monsters.Monster;

public abstract class TransportCell extends Cell {
	private int effect;

	public TransportCell(String name, int effect) {
		super(name);
		this.effect = effect;
	}

	public int getEffect() {
		return effect;
	}
	public void transport(Monster monster) {
        int newPosition = monster.getPosition() + this.getEffect();
        
        if (newPosition >= Constants.BOARD_SIZE) {
            newPosition = newPosition % Constants.BOARD_SIZE;
        } else if (newPosition < 0) {
            newPosition = (newPosition % Constants.BOARD_SIZE + Constants.BOARD_SIZE) % Constants.BOARD_SIZE;
        }
        
        monster.setPosition(newPosition); 
    }

    
	public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster); 
        this.transport(landingMonster);
    }
	
}
