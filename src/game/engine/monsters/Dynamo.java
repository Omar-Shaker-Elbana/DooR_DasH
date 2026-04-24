package game.engine.monsters;

import game.engine.Role;

public class Dynamo extends Monster {
	
	public Dynamo(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	public void executePowerupEffect(Monster opponentMonster) {
		opponentMonster.isFrozen();
	}
	public void setEnergy(int e) {
		int diff = e - this.getEnergy();
		super.setEnergy(this.getEnergy()+diff*2);
}
}
