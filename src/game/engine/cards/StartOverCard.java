package game.engine.cards;

import game.engine.monsters.Monster;

public class StartOverCard extends Card {

	// هنا بينادي على الـ Constructor اللي بياخد 4 متغيرات
	public StartOverCard(String name, String description, int rarity, boolean lucky) {
		super(name, description, rarity, lucky);
	}

	@Override
	public void performAction(Monster player, Monster opponent) {
		if (this.isLucky()) {
			opponent.setPosition(0);
		} else {
			player.setPosition(0);
		}
	}
}