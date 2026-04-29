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
	            if(stationed != this && stationed != opponentMonster)
	            {
	            totalEnergy+= this.stealEnergyFrom(stationed);
	            }
	            
	            }
	        totalEnergy+=this.stealEnergyFrom(opponentMonster);
	        this.alterEnergy(totalEnergy);
	    }
	    public void setEnergy(int e) {
	        
	           super.setEnergy(e+10);
	    }
}
