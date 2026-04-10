package game.engine.cells;

public class ConveyorBelt extends TransportCell{
	public  ConveyorBelt(String name, int effect){
		super(name,Math.abs(effect));
	}
}
