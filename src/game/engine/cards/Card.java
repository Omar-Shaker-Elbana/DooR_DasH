package game.engine.cards;

import game.engine.monsters.Monster;

public abstract class Card {
	private String name;
	private String description;
	private int rarity;
	private boolean lucky; // رجعناها هنا عشان التست بيفتش عليها

	// ده الـ Constructor للكروت العادية (3 متغيرات)
	public Card(String name, String description, int rarity) {
		this.name = name;
		this.description = description;
		this.rarity = rarity;
	}

	// ده الـ Constructor لكارت StartOver (4 متغيرات)
	public Card(String name, String description, int rarity, boolean lucky) {
		this.name = name;
		this.description = description;
		this.rarity = rarity;
		this.lucky = lucky;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getRarity() {
		return rarity;
	}

	public boolean isLucky() {
		return lucky;
	}

	public abstract void performAction(Monster player, Monster opponent);
}