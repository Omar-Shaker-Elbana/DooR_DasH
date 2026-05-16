package game.engine.monsters;

import game.engine.Role;

public class Dasher extends Monster {
	private int momentumTurns;

	public Dasher(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.momentumTurns = 0;
	}
	
	public int getMomentumTurns() {
		return momentumTurns;
	}
	
	public void setMomentumTurns(int momentumTurns) {
		this.momentumTurns = momentumTurns;
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		this.setMomentumTurns(3);
		System.out.println(getName() + " activated Momentum Rush! 3x speed for 3 turns!");
	}
	
	@Override
	public void move(int distance) {
		if (momentumTurns > 0) {
	        System.out.println(getName() + " using Momentum! (" + momentumTurns + " turns left)");
	        momentumTurns--;
	        distance *= 3;
	    } 
		
		else 
	        distance *= 2;
	    
	    super.move(distance);
	}
	
}