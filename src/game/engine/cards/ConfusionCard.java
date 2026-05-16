package game.engine.cards;

import game.engine.Role;
import game.engine.monsters.Monster;

public class ConfusionCard extends Card {
	private int duration;

	public ConfusionCard(String name, String description, int rarity, int duration) {
		super(name, description, rarity);
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public void performAction(Monster player, Monster opponent) {
		Role tempRole = player.getRole();
		player.setRole(opponent.getRole());
		opponent.setRole(tempRole);

		player.setConfusionTurns(duration);
		opponent.setConfusionTurns(duration);
	}
}