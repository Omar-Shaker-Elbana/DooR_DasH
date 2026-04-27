package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}
	public void performAction(Monster player, Monster opponent) {
	if(opponent.isShielded()==true)	{
		opponent.setShielded(false);
	}
	else if (opponent.getEnergy()<this.getEnergy()) {
		int stolenamount= opponent.getEnergy();
		opponent.setEnergy(0) ;
		player.setEnergy(player.getEnergy()+stolenamount) ;
	
	}
		
	else {
  int stolenamount = this.getEnergy();
        opponent.setEnergy(opponent.getEnergy() - stolenamount);
        player.setEnergy(player.getEnergy() + stolenamount);	
	}
	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int cantiserValue) {
		// TODO Auto-generated method stub
		
	}}
