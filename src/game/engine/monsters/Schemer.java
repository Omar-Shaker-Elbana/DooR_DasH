package game.engine.monsters;

import game.engine.Role;
import game.engine.Constants;
import game.engine.Board;
import java.util.ArrayList;
public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	private int stealEnergyFrom(Monster target){
		int diff = Math.min(Constants.SCHEMER_STEAL,target.getEnergy());
		target.alterEnergy(-diff);
		return diff;
	}
	public void executePowerupEffect(Monster opponentMonster) {
		int totalEnergy = 0;
		ArrayList<Monster> stationedMonsters = Board.getStationedMonsters();
		for(Monster stationed : stationedMonsters)
		{
			totalEnergy+= this.stealEnergyFrom(stationed);
		}
		this.alterEnergy(totalEnergy);
	}
	public void setEnergy(int e) {
        int diff = e - this.getEnergy();
        if(diff != 0)
        {
        	super.setEnergy(this.getEnergy()+e+10);
        }
        else
        {
        	super.setEnergy(e);
        }
    }
}
