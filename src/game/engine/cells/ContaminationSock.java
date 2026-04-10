package game.engine.cells;


public class ContaminationSock extends TransportCell {
	 public ContaminationSock(String name, int effect){
		 super(name,-Math.abs(effect));
	 }
}
