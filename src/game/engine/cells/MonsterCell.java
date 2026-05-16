package game.engine.cells;

import game.engine.monsters.*;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	public Monster getCellMonster() {
		return cellMonster;
	}

	@Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);
		
        if (cellMonster.getRole() == landingMonster.getRole()) {
        	System.out.println(landingMonster.getName() + " encountered ally " + cellMonster.getName() + "!");
        	landingMonster.executePowerupEffect(opponentMonster);
        }
        
        else {
        	if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
        	    int landingEnergy = landingMonster.getEnergy();
        	    int cellEnergy = cellMonster.getEnergy();
        	    int diff = landingEnergy - cellEnergy;

        	    landingMonster.alterEnergy(-diff); // shield will block this if active
        	    cellMonster.alterEnergy(diff);     // cell monster always gets the gain
        	    System.out.println("Energy swapped between " + landingMonster.getName() + " and " + cellMonster.getName());
        	}
        }
    }
}
