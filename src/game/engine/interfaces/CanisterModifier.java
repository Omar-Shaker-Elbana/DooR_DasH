package game.engine.interfaces;

import game.engine.monsters.Monster;

public interface CanisterModifier {
	void modifyCanisterEnergy(Monster monster, int canisterValue);
}
