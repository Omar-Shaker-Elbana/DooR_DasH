package game.engine.monsters;

import game.engine.Role;
public abstract class Monster implements Comparable<Monster> {
	
		//Read-Only Attributes
		private final String name;
		private final String description;
		private final Role originalRole;
		
		//Read and Write Attributes
		private Role role;
		private int energy;
		private int position;
		private boolean frozen;
		private boolean shielded;
		private int confusionTurns;
		
		public Monster(String name, String description, Role originalRole, int energy){
			this.name = name;
			this.description = description;
			this.originalRole = originalRole;
			this.role = originalRole;
			this.energy = energy;
			this.position=0;
			this.confusionTurns=0;
			this.frozen = false;
			this.shielded = false;
		}
		
	@Override
	public int compareTo(Monster o)
	{
		return this.position - o.position;
	}
	
	
	//-----------GETTERS-----------
	public String getName()
	{
		return this.name;
	}
	public String getDescription()
	{
		return this.description;
	}
	public Role getOriginalRole(){
		return this.originalRole;
	}
	public Role getRole()
	{
		return this.role;
	}
	public int getEnergy()
	{
		return this.energy;
	}
	public int getPosition()
	{
		return this.position;
	}
	public int getConfusionTurns()
	{
		return this.confusionTurns;
	}
	public boolean isFrozen()
	{
		return this.frozen;
	}
	public boolean isShielded()
	{
		return this.shielded;
	}
	
	
	//----------SETTERS------------
	public void setRole(Role r)
	{
		this.role=r;
	}
	public void setEnergy(int e)
	{
		if(e>=0)
		{
			this.energy=e;
		}
		else
		{
			this.energy=0;
		}
	}
	public void setPosition(int p)
	{
		if(p>=0 && p<=99)
		{
			this.position=p;
		}
		else
		{
			this.position = p%100;
		}
	}
	public void setFrozen(boolean f)
	{
		this.frozen=f;
	}
	public void setShielded(boolean s)
	{
		this.shielded=s;
	}
	public void setConfusionTurns(int n)
	{
		this.confusionTurns=n;
	}
	
}
