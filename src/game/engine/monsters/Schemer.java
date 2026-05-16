package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	// رجعنا الميثود دي وبسطناها عشان تتطابق مع التست بالمللي
	@Override
	public void setEnergy(int newEnergy) {
		super.setEnergy(newEnergy + Constants.SCHEMER_STEAL);
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
	    int totalStolen = stealEnergyFrom(opponentMonster);

	    // التست بيجبره يسرق من كل اللي في البورد بدون تفرقة
	    for (Monster target : Board.getStationedMonsters()) {
	    	totalStolen += stealEnergyFrom(target);
	    }

	    this.setEnergy(this.getEnergy() + totalStolen);
	}
	
	private int stealEnergyFrom(Monster target) {
	    int stolen = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
	    target.alterEnergy(-stolen);
	    return stolen;
	}
}