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

    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        
        if (this.cellMonster.getRole() == landingMonster.getRole()) {
        	landingMonster.executePowerupEffect(opponentMonster);
        } else {
            if (landingMonster.getEnergy() > this.cellMonster.getEnergy()) {
                int landingEnergy = landingMonster.getEnergy();
                int cellEnergy = this.cellMonster.getEnergy();
                
                if (landingMonster.isShielded()) {
                    landingMonster.setShielded(false);
                } else {
                    landingMonster.setEnergy(cellEnergy);
                }
                this.cellMonster.setEnergy(landingEnergy);
            }
        }
    }

}
