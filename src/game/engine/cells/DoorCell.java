package game.engine.cells;

import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
	private Role role;
	private int energy;
	private boolean activated;
	
	public DoorCell(String name, Role role, int energy) {
		super(name);
		this.role = role;
		this.energy = energy;
		this.activated = false;
	}
	
	public Role getRole() {
		return role;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}

	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);
		
		if(isActivated())
			return; 
		
		System.out.println(landingMonster.getName() + " landed on " + role + " door!");
		
		boolean wasShielded = landingMonster.isShielded();
	     
		modifyCanisterEnergy(landingMonster, this.energy);

		if (wasShielded && landingMonster.getRole() != this.role) 
			return;

	    
		for (Monster monster : Board.getStationedMonsters()) {
			if (monster.getRole() == landingMonster.getRole()) {
				modifyCanisterEnergy(monster, this.energy);
				System.out.println("  -> " + monster.getName() + " got " + this.energy + " energy!");
			}
		}
		
		setActivated(true);
	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.alterEnergy(this.role == monster.getRole() ? canisterValue : -canisterValue);
	}
}
