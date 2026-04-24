package game.engine.monsters;

import game.engine.Role;
import game.engine.Constants;
public class MultiTasker extends Monster {
	private int normalSpeedTurns;
	
	public MultiTasker(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.normalSpeedTurns = 0;
	}

	public int getNormalSpeedTurns() {
		return normalSpeedTurns;
	}

	public void setNormalSpeedTurns(int normalSpeedTurns) {
		this.normalSpeedTurns = normalSpeedTurns;
	}
	void executePowerupEffect(Monster opponentMonster) {
		this.normalSpeedTurns = 2;
		
	}
	public void move(int distance)
	{
		if(this.getNormalSpeedTurns()>0)
		{
			super.move(distance);
			this.normalSpeedTurns--;
		}
		else
		{
			super.move(distance/2);
		}
	}
	public void setEnergy(int e)
	{
		int diff = e - this.getEnergy();
		if(diff != 0)
		{
			super.setEnergy(e+this.getEnergy()+Constants.MULTITASKER_BONUS);
		}
		else
		{
			super.setEnergy(e);
		}
	}
	
}