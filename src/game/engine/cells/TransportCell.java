package game.engine.cells;

public abstract class TransportCell extends Cell {
	private int effect;

	public TransportCell(String name, int effect) {
		super(name);
		this.effect = effect;
	}

	public int getEffect() {
		return effect;
	}
	
}
