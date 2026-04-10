package game.engine.cells;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.*;
public class DoorCell extends Cell implements CanisterModifier{
		
	    private final Role role;
		private final int energy;
		private boolean activated;
		
		public DoorCell(String name, Role role, int energy)
		{
			super(name);
			this.role = role;
			this.energy=energy;
			this.activated = false;
		}
		
		//-------GETTERS----------
		public Role getRole()
		{
			return this.role;
		}
		public int getEnergy()
		{
			return this.energy;
		}
		public boolean isActivated()
		{
			return this.activated;
		}
		
		//--------SETTERS----------
		public void setActivated(boolean active)
		{
			this.activated=active;
		}
		
		@Override
		public void modifyCanister(Monster monster) {

		}
		

		
}
