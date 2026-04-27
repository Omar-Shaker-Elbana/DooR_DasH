package game.engine.cards;
import game.engine.monsters.Monster;
public abstract class Card {
	private String name;
	private String description;
	private int rarity;
	private boolean lucky;
	
	public Card(String name, String description, int rarity, boolean lucky) {
		super();
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
