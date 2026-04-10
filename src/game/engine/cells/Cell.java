package game.engine.cells;

import game.engine.monsters.Monster;
public class Cell {
	
	private final String name;
	private Monster monster;
	
	public Cell(String name)
	{
		this.name = name;
		monster = null;
	}
	
	//------GETTERS-----------
	public String getName()
	{
		return this.name;
	}
	public Monster getMonster()
	{
		return this.monster;
	}
	
	//------------SETTERS--------
	public void setMonster(Monster monster)
	{ 
		this.monster = monster;
	}
}
