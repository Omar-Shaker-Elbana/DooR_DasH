package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

	public ContaminationSock(String name, int effect) {
		super(name, effect);
	}

	public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        if(!landingMonster.isShielded()){
        this.transport(landingMonster); // 
        landingMonster.alterEnergy(-Constants.SLIP_PENALTY); 
        }
        else
        {
        	landingMonster.setShielded(false);
        }
        }
    
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }
    public void transport(Monster monster) {
        int newPosition = monster.getPosition() + (-Math.abs(this.getEffect()));
        if (newPosition < 0) {
            newPosition = (newPosition % Constants.BOARD_SIZE + Constants.BOARD_SIZE) % Constants.BOARD_SIZE;
        }
        monster.setPosition(newPosition);
    }
    
}

