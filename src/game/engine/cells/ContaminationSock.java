package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {
	
	public ContaminationSock(String name, int effect) {
		super(name, effect);
	}

	@Override
    public void transport(Monster monster) {
		super.transport(monster);
        
		modifyCanisterEnergy(monster, -Constants.SLIP_PENALTY);
    }

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.alterEnergy(canisterValue);
	}

}

