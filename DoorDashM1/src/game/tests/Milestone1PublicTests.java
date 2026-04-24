package game.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class Milestone1PublicTests {

	private String gamePath="game.engine.Game";
	private String boardPath="game.engine.Board";
	private String constantsPath="game.engine.Constants";
	private String rolePath="game.engine.Role";

	private String cardPath="game.engine.cards.Card";
	private String swapperCardPath="game.engine.cards.SwapperCard";
	private String startOverCardPath="game.engine.cards.StartOverCard";
	private String shieldCardPath="game.engine.cards.ShieldCard";
	private String energyStealCardPath="game.engine.cards.EnergyStealCard";
	private String confusionCardPath="game.engine.cards.ConfusionCard";

	private String cellPath="game.engine.cells.Cell";
	private String contaminationSockPath="game.engine.cells.ContaminationSock";
	private String cardCellPath="game.engine.cells.CardCell";
	private String conveyorBeltPath="game.engine.cells.ConveyorBelt";
	private String doorCellPath="game.engine.cells.DoorCell";
	private String monsterCellPath="game.engine.cells.MonsterCell";
	private String transportCellPath="game.engine.cells.TransportCell";

	private String dataLoaderPath="game.engine.dataloader.DataLoader";

	private String gameActionExceptionPath="game.engine.exceptions.GameActionException";
	private String invalidCSVFormatPath="game.engine.exceptions.InvalidCSVFormat";
	private String invalidMoveExceptionPath="game.engine.exceptions.InvalidMoveException";
	private String invalidTurnExceptionPath="game.engine.exceptions.InvalidTurnException";
	private String OutOfEnergyExceptionPath="game.engine.exceptions.OutOfEnergyException";

	private String dasherPath="game.engine.monsters.Dasher";
	private String dynamoPath="game.engine.monsters.Dynamo";
	private String monsterPath="game.engine.monsters.Monster";
	private String multiTaskerPath="game.engine.monsters.MultiTasker";
	private String schemerPath="game.engine.monsters.Schemer";

	private String canisterModifierPath="game.engine.interfaces.CanisterModifier";



	private static ArrayList<String> cards_csv;
	private static ArrayList<String> cells_csv;
	private static ArrayList<String> monsters_csv;



	//////////////////////////////// Class Cell /////////////////////////////////
	//////////////////////////////// Name attribute /////////////////////////////////


	@Test(timeout = 1000)
	public void testCellInstanceVariableNameIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPrivate(Class.forName(cellPath), "name");

	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableNameGetterExists() throws ClassNotFoundException{
		testGetterMethodExistInClass(Class.forName(cellPath), "getName",String.class);

	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableNameSetterIsAbsent() throws ClassNotFoundException {
		testSetterMethodIsAbsentInClass(Class.forName(cellPath), "setName");
	}


	//////////////////////////////// Monster attribute /////////////////////////////////

	@Test(timeout = 1000)
	public void testCellInstanceVariableMonsterIsPresent() throws  ClassNotFoundException {
		testInstanceVariableIsPresent(Class.forName(cellPath), "monster");
	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableMonsterOfTypeMonster() throws  ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(cellPath), "monster", Class.forName(monsterPath));
	}

	@Test(timeout = 1000)
	public void testCellInstanceVariableMonsterSetterExists() throws ClassNotFoundException{
		testSetterMethodExistInClass(Class.forName(cellPath), "setMonster", Class.forName(monsterPath));
	}

	@Test(timeout = 1000)
	public void testCellInstanceVariableMonsterGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		Random rand = new Random();
		Object input_role = null;
		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	
		Class role_class  = Class.forName(rolePath);
		Constructor<?> dynamo_constructor = Class.forName(dynamoPath).getConstructor(String.class,String.class,role_class,int.class);
		Object dynamo_object = dynamo_constructor.newInstance(input_name,input_description,input_role,input_energy);

		Class cell_class  = Class.forName(cellPath);
		Constructor<?> cell_constructor = cell_class.getConstructor(String.class);
		Object cell_Object = cell_constructor.newInstance(input_name);

		Field monster_field_in_cell = cell_class.getDeclaredField("monster");
		monster_field_in_cell.setAccessible(true);
		monster_field_in_cell.set(cell_Object, dynamo_object);

		Method getMonster = cell_class.getDeclaredMethod("getMonster");
		Object monster_object = getMonster.invoke(cell_Object );
		assertTrue("The method getMonster should return the correct value of the instance variable monster",compare2Monsters(dynamo_object,monster_object));
	}



	@Test(timeout = 1000)
	public void testCellInstanceVariableMonsterSetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		Random rand = new Random();
		Object input_role = null;
		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	
		Class role_class  = Class.forName(rolePath);
		Constructor<?> dynamo_constructor = Class.forName(dynamoPath).getConstructor(String.class,String.class,role_class,int.class);
		Object dynamo_object = dynamo_constructor.newInstance(input_name,input_description,input_role,input_energy);

		Class cell_class  = Class.forName(cellPath);
		Constructor<?> cell_constructor = cell_class.getConstructor(String.class);
		Object cell_Object = cell_constructor.newInstance(input_name);

		Class monster_class = Class.forName(monsterPath);

		Method setMonster = cell_class.getDeclaredMethod("setMonster",monster_class);
		setMonster.invoke(cell_Object,dynamo_object);

		Field monster_field_in_cell = cell_class.getDeclaredField("monster");
		monster_field_in_cell.setAccessible(true); 

		assertTrue("The method setMonster should correctly update the variable monster",compare2Monsters(dynamo_object,monster_field_in_cell.get(cell_Object)));
	}
	////////////////////////////////////////////constructor tests //////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testCellConstructorExists() throws ClassNotFoundException {

		Class[] parameters = {String.class};
		testConstructorExists(Class.forName(cellPath), parameters);
	}

	@Test(timeout = 1000)
	public void testCellConstructorInitialization() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String input_name = generateRandomString(10);
		Constructor<?> cell_constructor = Class.forName(cellPath).getConstructor(String.class);
		Object cell_Object = cell_constructor.newInstance(input_name);
		String[] names = {"name"};
		Object[] values = {input_name};
		testConstructorInitialization(cell_Object, names, values);
	}
	@Test(timeout = 1000)
	public void testMonsterInitializationInConstructor() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{

		String input_name = generateRandomString(10);
		Constructor<?> cell_constructor = Class.forName(cellPath).getConstructor(String.class);
		Object cell_Object = cell_constructor.newInstance(input_name);
		Class cell_class  = Class.forName(cellPath);

		Field monster_field_in_cell = cell_class.getDeclaredField("monster");
		monster_field_in_cell.setAccessible(true); 

		assertTrue("The instance variable monster should be initialized to null in the constructor",(monster_field_in_cell.get(cell_Object) == null));
	}


	////////////////////////////////////////////Door Cell//////////////////////////////////////////////////////////////
	////////////////////////////////////////////Door Cell constructor tests//////////////////////////////////////////////////////////////


	@Test(timeout = 1000)
	public void testDoorCellImplementsCanisterModifier() throws ClassNotFoundException{
		Class doorCellClass = Class.forName(doorCellPath);
		Class CanisterModifierInterface = Class.forName(canisterModifierPath);
		Class<?>[] interfaces = doorCellClass.getInterfaces();

		boolean found = false;
		for (Class<?> i : interfaces) {
			if (i.equals(CanisterModifierInterface)) {
				found = true;
				break;
			}
		}

		assertTrue("DoorCell should implement CanisterModifier interface", found);
	}



	@Test(timeout = 1000)
	public void testInitializationRoleInConstructorDoorCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class doorcell_class  = Class.forName(doorCellPath);
		Class role_class = Class.forName(rolePath);

		Random rand = new Random();
		Object input_role = null;
		String input_name = generateRandomString(10);


		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	


		Constructor<?> doorcell_constructor = Class.forName(doorCellPath).getConstructor(String.class,role_class,int.class);
		Object doorcell_Object = doorcell_constructor.newInstance(input_name,input_role,input_energy);

		Field role_field_in_cell = doorcell_class.getDeclaredField("role");
		role_field_in_cell.setAccessible(true); 

		assertEquals("The constructor of DoorCell should initialize the instance variable role correctly", input_role, role_field_in_cell.get(doorcell_Object));
	}
	@Test(timeout = 1000)
	public void testInitializationEnergyInConstructorDoorCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class doorcell_class  = Class.forName(doorCellPath);
		Class role_class = Class.forName(rolePath);

		Random rand = new Random();
		Object input_role = null;
		String input_name = generateRandomString(10);


		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	


		Constructor<?> doorcell_constructor = Class.forName(doorCellPath).getConstructor(String.class,role_class,int.class);
		Object doorcell_Object = doorcell_constructor.newInstance(input_name,input_role,input_energy);

		Field energy_field_in_cell = doorcell_class.getDeclaredField("energy");
		energy_field_in_cell.setAccessible(true); 

		assertEquals("The constructor of DoorCell should initialize the instance variable energy correctly",input_energy, (int)(energy_field_in_cell.get(doorcell_Object)));
	}

	@Test(timeout = 1000)
	public void testInitializationActivatedInConstructorDoorCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class doorcell_class  = Class.forName(doorCellPath);
		Class role_class = Class.forName(rolePath);

		Random rand = new Random();
		Object input_role = null;
		String input_name = generateRandomString(10);


		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	


		Constructor<?> doorcell_constructor = Class.forName(doorCellPath).getConstructor(String.class,role_class,int.class);
		Object doorcell_Object = doorcell_constructor.newInstance(input_name,input_role,input_energy);

		Field activated_field_in_cell = doorcell_class.getDeclaredField("activated");
		activated_field_in_cell.setAccessible(true); 

		assertEquals("The constructor of DoorCell should initialize the instance variable activated correctly", false , (boolean)(activated_field_in_cell.get(doorcell_Object)));
	}



	////////////////////////////////////////////Door Cell attribute Role//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableRoleIsPresent() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(doorCellPath), "role");
	}

	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableRoleGetterExists() throws ClassNotFoundException  {
		testGetterMethodExistInClass(Class.forName(doorCellPath), "getRole",Class.forName(rolePath));

	}
	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableRoleSetterIsAbsent() throws ClassNotFoundException {
		testSetterMethodIsAbsentInClass(Class.forName(doorCellPath), "setRole");
	}

	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableRoleGetterLogic() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class doorcell_class  = Class.forName(doorCellPath);
		Class role_class = Class.forName(rolePath);

		Random rand = new Random();
		Object input_role = null;
		String input_name = generateRandomString(10);


		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	


		Constructor<?> doorcell_constructor = Class.forName(doorCellPath).getConstructor(String.class,role_class,int.class);
		Object doorcell_Object = doorcell_constructor.newInstance(input_name,input_role,input_energy);

		testGetterMethodLogic(doorcell_Object, "role", input_role);	
	}

	////////////////////////////////////////////Door Cell attribute Energy//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableEnergyIsPresent() throws SecurityException, ClassNotFoundException {
		testInstanceVariableIsPresent(Class.forName(doorCellPath), "energy");
	}


	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableEnergyIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(doorCellPath), "energy");
	}
	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableEnergyGetterExists() throws ClassNotFoundException  {
		testGetterMethodExistInClass(Class.forName(doorCellPath), "getEnergy",int.class);
	}
	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableEnergySetterIsAbsent() throws ClassNotFoundException  {
		testSetterMethodIsAbsentInClass(Class.forName(doorCellPath), "setEnergy");
	}

	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableEnergyGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		Class doorcell_class  = Class.forName(doorCellPath);
		Class role_class = Class.forName(rolePath);

		Random rand = new Random();
		Object input_role = null;
		String input_name = generateRandomString(10);


		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	


		Constructor<?> doorcell_constructor = Class.forName(doorCellPath).getConstructor(String.class,role_class,int.class);
		Object doorcell_Object = doorcell_constructor.newInstance(input_name,input_role,input_energy);

		testGetterMethodLogic(doorcell_Object, "energy", input_energy);	
	}

	////////////////////////////////////////////Door Cell attribute Activated//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testCellInstanceVariableActivatedIsPresent() throws SecurityException, ClassNotFoundException {
		testInstanceVariableIsPresent(Class.forName(doorCellPath), "activated");
	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableActivatedOfTypeBoolean() throws SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(doorCellPath), "activated", boolean.class);
	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableActivatedIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(doorCellPath), "activated");
	}


	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableActivatedGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException  {
		Class doorcell_class  = Class.forName(doorCellPath);
		Class role_class = Class.forName(rolePath);

		Random rand = new Random();
		Object input_role = null;
		String input_name = generateRandomString(10);


		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	

		Constructor<?> doorcell_constructor = Class.forName(doorCellPath).getConstructor(String.class,role_class,int.class);
		Object doorcell_Object = doorcell_constructor.newInstance(input_name,input_role,input_energy);

		boolean activated_value = rand.nextInt(2)== 0 ? true: false;

		Field activated_field_in_cell = doorcell_class.getDeclaredField("activated");
		activated_field_in_cell.setAccessible(true); 
		activated_field_in_cell.set(doorcell_Object, activated_value);

		Method isActivated = doorcell_class.getDeclaredMethod("isActivated");
		boolean activated = (boolean) isActivated.invoke(doorcell_Object);

		assertEquals("The method isActivated should return the correct value of the instance variable activated",activated, activated_value);		
	}

	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableActivatedSetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class doorcell_class  = Class.forName(doorCellPath);
		Class role_class = Class.forName(rolePath);

		Random rand = new Random();
		Object input_role = null;
		String input_name = generateRandomString(10);


		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	

		Constructor<?> doorcell_constructor = Class.forName(doorCellPath).getConstructor(String.class,role_class,int.class);
		Object doorcell_Object = doorcell_constructor.newInstance(input_name,input_role,input_energy);

		boolean activated_value = rand.nextInt(2)== 0 ? true: false;
		testSetterMethodLogic(doorcell_Object,"activated",activated_value,boolean.class);	
	}


	////////////////////////////////////////////Transport Cell//////////////////////////////////////////////////////////////
	@Test(timeout = 1000)
	public void testTransportCellExtendsCell() throws ClassNotFoundException {
		Class cellClass = Class.forName(cellPath);
		Class transportClass = Class.forName(transportCellPath);
		assertEquals("transportCell class should extends cell class",cellClass, transportClass.getSuperclass());
	}
	@Test(timeout = 1000)
	public void testTransportCellIsAbstract() throws ClassNotFoundException{
		Class transportClass = Class.forName(transportCellPath);
		testClassIsAbstract(transportClass);
	}

	@Test(timeout = 1000)
	public void testTransportCellInstanceVariableEffectIsPresent() throws SecurityException, ClassNotFoundException {
		testInstanceVariableIsPresent(Class.forName(transportCellPath), "effect");
	}

	@Test(timeout = 1000)
	public void testTransportCellInstanceVariableEffectOfTypeInt() throws SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(transportCellPath), "effect", int.class);
	}

	@Test(timeout = 1000)
	public void testTransportCellInstanceVariableEffectIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPrivate(Class.forName(transportCellPath), "effect");

	}
	@Test(timeout = 1000)
	public void testTransportCellInstanceVariableEffectGetterExists() throws ClassNotFoundException {
		testGetterMethodExistInClass(Class.forName(transportCellPath), "getEffect",int.class);
	}
	@Test(timeout = 1000)
	public void testTransportCellInstanceVariableEffectSetterIsAbsent() throws ClassNotFoundException {
		testSetterMethodIsAbsentInClass(Class.forName(transportCellPath), "setEffect");
	}


	@Test(timeout = 1000)
	public void testTransportCellConstructorExists() throws ClassNotFoundException {
		Class[] parameters = {String.class,int.class};
		testConstructorExists(Class.forName(transportCellPath), parameters);
	}

	////////////////////////////////////////////ConveyorBelt//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testConveyorBeltExtendsTransportCell() throws ClassNotFoundException{
		Class conveyorBeltClass = Class.forName(conveyorBeltPath);
		Class transportClass = Class.forName(transportCellPath);
		assertEquals("ConveyorBelt class should extends TransportCell class",transportClass, conveyorBeltClass.getSuperclass());
	}
	@Test(timeout = 1000)
	public void testInitializationEffectInConstructorConveyorBelt() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class conveyorBelt_class  = Class.forName(conveyorBeltPath);

		Random rand = new Random();
		String input_name = generateRandomString(10);

		int input_energy = rand.nextInt(50); 	


		Constructor<?> conveyorBelt_constructor = Class.forName(conveyorBeltPath).getConstructor(String.class,int.class);
		Object conveyorBelt_Object = conveyorBelt_constructor.newInstance(input_name,input_energy);

		Field effect_field_in_cell = conveyorBelt_class.getSuperclass().getDeclaredField("effect");
		effect_field_in_cell.setAccessible(true); 

		assertEquals("The constructor of conveyorBelt should initialize the instance variable effect correctly",input_energy, effect_field_in_cell.get(conveyorBelt_Object));
	}
	@Test(timeout = 1000)
	public void testInitializationEffectInConstructorConveyorBeltIsAlwaysPositive() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class conveyorBelt_class  = Class.forName(conveyorBeltPath);

		Random rand = new Random();
		String input_name = generateRandomString(10);

		int input_energy = rand.nextInt(50); 	


		Constructor<?> conveyorBelt_constructor = Class.forName(conveyorBeltPath).getConstructor(String.class,int.class);
		Object conveyorBelt_Object = conveyorBelt_constructor.newInstance(input_name,input_energy);

		Field effect_field_in_cell = conveyorBelt_class.getSuperclass().getDeclaredField("effect");
		effect_field_in_cell.setAccessible(true); 

		assertTrue("The effect value in conveyorBelt should always be positive",0 <= (int) effect_field_in_cell.get(conveyorBelt_Object));
	}

	@Test(timeout = 1000)
	public void testInitializationNameInConstructorConveyorBelt() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class conveyorBelt_class  = Class.forName(conveyorBeltPath);

		Random rand = new Random();
		String input_name = generateRandomString(10);

		int input_energy = rand.nextInt(50); 	


		Constructor<?> conveyorBelt_constructor = Class.forName(conveyorBeltPath).getConstructor(String.class,int.class);
		Object conveyorBelt_Object = conveyorBelt_constructor.newInstance(input_name,input_energy);

		Field name_field_in_cell = conveyorBelt_class.getSuperclass().getSuperclass().getDeclaredField("name");
		name_field_in_cell.setAccessible(true); 

		assertTrue("The constructor of ConveyorBelt should initialize the instance variable name correctly", (input_name.equals(name_field_in_cell.get(conveyorBelt_Object))) );
	}



	////////////////////////////////////////////ContaminationSock//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testContaminationSockExtendsTransportCell() throws ClassNotFoundException{
		Class contaminationSocktClass = Class.forName(contaminationSockPath);
		Class transportClass = Class.forName(transportCellPath);
		assertEquals("ContaminationSock class should extends TransportCell class",transportClass, contaminationSocktClass.getSuperclass());
	}



	@Test(timeout = 1000)
	public void testInitializationNameInConstructorContaminationSock() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class contaminationSock_class  = Class.forName(contaminationSockPath);

		Random rand = new Random();
		String input_name = generateRandomString(10);

		int input_energy = rand.nextInt(50); 	


		Constructor<?> contaminationSock_constructor = Class.forName(contaminationSockPath).getConstructor(String.class,int.class);
		Object contaminationSock_Object = contaminationSock_constructor.newInstance(input_name,input_energy);

		Field name_field_in_cell = contaminationSock_class.getSuperclass().getSuperclass().getDeclaredField("name");
		name_field_in_cell.setAccessible(true); 

		assertTrue("The constructor of contaminationSock should initialize the instance variable name correctly", (input_name.equals(name_field_in_cell.get(contaminationSock_Object))) );
	}


	@Test(timeout = 1000)
	public void testContaminationSockConstructorExists() throws ClassNotFoundException {
		Class[] parameters = {String.class,int.class};
		testConstructorExists(Class.forName(contaminationSockPath), parameters);
	}
	////////////////////////////////////////////Monster Cell//////////////////////////////////////////////////////////////



	////////////////////////////////////////////Monster cell Constructor//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testInitializationNameInConstructorMonsterCell() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class monsterCell_class  = Class.forName(monsterCellPath);
		Class monster_class = Class.forName(monsterPath);
		Class role_class = Class.forName(rolePath);
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);



		Constructor<?> dynamo_constructor = Class.forName(dynamoPath).getConstructor(String.class,String.class,role_class,int.class);
		Random rand = new Random();
		Object input_role = null;			

		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	
		Object dynamo_object = dynamo_constructor.newInstance(input_name,input_description,input_role,input_energy);


		Constructor<?> monsterCell_constructor = Class.forName(monsterCellPath).getConstructor(String.class,monster_class);
		Object monsterCell_Object = monsterCell_constructor.newInstance(input_name,dynamo_object);

		Field name_field_in_cell = monsterCell_class.getSuperclass().getDeclaredField("name");
		name_field_in_cell.setAccessible(true); 

		assertTrue("The constructor of MonsterCell should initialize the instance variable name correctly", (input_name.equals(name_field_in_cell.get(monsterCell_Object))) );
	}

	@Test(timeout = 1000)
	public void testInitializationMonsterInConstructorMonsterCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class monsterCell_class  = Class.forName(monsterCellPath);
		Class monster_class = Class.forName(monsterPath);
		Class role_class = Class.forName(rolePath);
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);



		Constructor<?> dynamo_constructor = Class.forName(dynamoPath).getConstructor(String.class,String.class,role_class,int.class);
		Random rand = new Random();
		Object input_role = null;			

		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	
		Object dynamo_object = dynamo_constructor.newInstance(input_name,input_description,input_role,input_energy);


		Constructor<?> monsterCell_constructor = Class.forName(monsterCellPath).getConstructor(String.class,monster_class);
		Object monsterCell_Object = monsterCell_constructor.newInstance(input_name,dynamo_object);

		Field monster_field_in_cell = monsterCell_class.getDeclaredField("cellMonster");
		monster_field_in_cell.setAccessible(true); 

		assertTrue(" The constructor of MonsterCell should initialize the instance variable cellMonster correctly", compare2Monsters(dynamo_object,monster_field_in_cell.get(monsterCell_Object)) );
	}

	@Test(timeout = 1000)
	public void testMonsterCellConstructorExists() throws ClassNotFoundException {
		Class[] parameters = {String.class,Class.forName(monsterPath)};
		testConstructorExists(Class.forName(monsterCellPath), parameters);
	}
	////////////////////////////////////////////cellMonster attribute in Monster cell//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testMonsterCellInstanceVariableCellMonsterIsPresent() throws SecurityException, ClassNotFoundException {
		testInstanceVariableIsPresent(Class.forName(monsterCellPath), "cellMonster");
	}
	@Test(timeout = 1000)
	public void testMonsterCellInstanceVariableCellMonsterOfTypeMonster() throws SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(monsterCellPath), "cellMonster", Class.forName(monsterPath));
	}
	@Test(timeout = 1000)
	public void testMonsterCellInstanceVariableCellMonsterIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(monsterCellPath), "cellMonster");
	}


	@Test(timeout = 1000)
	public void testMonsterCellInstanceVariableCellMonsterGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {

		Class monsterCell_class  = Class.forName(monsterCellPath);
		Class monster_class = Class.forName(monsterPath);
		Class role_class = Class.forName(rolePath);
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);



		Constructor<?> dynamo_constructor = Class.forName(dynamoPath).getConstructor(String.class,String.class,role_class,int.class);
		Random rand = new Random();
		Object input_role = null;			

		int while_role = rand.nextInt(2); 
		int input_energy = rand.nextInt(50); 
		switch(while_role){
		case 0:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			break;
		}
		case 1:{
			input_role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			break;
		}
		}	
		Object dynamo_object = dynamo_constructor.newInstance(input_name,input_description,input_role,input_energy);


		Constructor<?> monsterCell_constructor = Class.forName(monsterCellPath).getConstructor(String.class,monster_class);
		Object monsterCell_Object = monsterCell_constructor.newInstance(input_name,dynamo_object);

		Field cellMonster_field_in_cell = monsterCell_class.getDeclaredField("cellMonster");
		cellMonster_field_in_cell.setAccessible(true); 
		cellMonster_field_in_cell.set(monsterCell_Object, dynamo_object);

		Method getCellMonster = monsterCell_class.getDeclaredMethod("getCellMonster");
		Object cellMonster = getCellMonster.invoke(monsterCell_Object);

		assertTrue("The method getCellMonster should return the correct value of the instance variable cellMonster", compare2Monsters(dynamo_object,cellMonster) );
	}


	////////////////////////////////////////////Card Cell//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testCardCellConstructorExists() throws ClassNotFoundException {
		Class[] parameters = {String.class};
		testConstructorExists(Class.forName(cardCellPath), parameters);
	}


	@Test(timeout = 1000)
	public void testInitializationNameInConstructorCardCell() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		Class cardCell_class  = Class.forName(cardCellPath);

		String input_name = generateRandomString(10);	


		Constructor<?> cardcell_constructor = Class.forName(cardCellPath).getConstructor(String.class);
		Object cardcell_Object = cardcell_constructor.newInstance(input_name);

		Field name_field_in_cell = cardCell_class.getSuperclass().getDeclaredField("name");
		name_field_in_cell.setAccessible(true); 

		assertTrue("The constructor of CardCell should initialize the instance variable name correctly", (input_name.equals(name_field_in_cell.get(cardcell_Object))) );
	}


	////////////////////////////////////////////Exceptions//////////////////////////////////////////////////////////////
	////////////////////////////////////////////GameActionException//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testGameActionExceptionIsAbstract() throws ClassNotFoundException {
		testClassIsAbstract(Class.forName(gameActionExceptionPath));

	}
	@Test(timeout = 1000)
	public void testGameActionExceptionIsSubClassOfException() throws ClassNotFoundException  {
		testClassIsSubClass(Exception.class, Class.forName(gameActionExceptionPath));
	}
	@Test(timeout = 1000)
	public void testGameActionExceptionEmptyConstructorExists() throws ClassNotFoundException  {
		testConstructorExists(Class.forName(gameActionExceptionPath), new Class[] {});
	}



	////////////////////////////////////////////InvalidMoveException//////////////////////////////////////////////////////////////
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionIsSubClassOfGameActionException() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(gameActionExceptionPath), Class.forName(invalidMoveExceptionPath));
	}
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionEmptyConstructorExists() throws ClassNotFoundException{
		testConstructorExists(Class.forName(invalidMoveExceptionPath), new Class[] {});
	}

	@Test(timeout = 1000)
	public void testInvalidMoveExceptionConstructorExists() throws ClassNotFoundException {
		testConstructorExists(Class.forName(invalidMoveExceptionPath), new Class[] {String.class});
	}
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionMSGPresent() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(invalidMoveExceptionPath), "MSG");
	}

	@Test(timeout = 1000)
	public void testInvalidMoveExceptionMSGIsStatic() throws ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(invalidMoveExceptionPath), "MSG");
	}
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionMSGIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(invalidMoveExceptionPath), "MSG");
	}
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionMSGValue() throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Class aClass = Class.forName(invalidMoveExceptionPath);
		Field f = aClass.getDeclaredField("MSG");
		f.setAccessible(true);
		String expected = "Invalid move attempted";
		String actual = (String) f.get(null);
		assertEquals("wrong value of MSG", expected, actual);
	}
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionPassesMessageToSuper() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		Class<?> c = Class.forName(invalidMoveExceptionPath);

		Object obj = c.getConstructor().newInstance();

		Field msgField = c.getDeclaredField("MSG");
		msgField.setAccessible(true);
		String expected = (String) msgField.get(null);

		assertEquals(
				"InvalidMoveException should pass MSG to super.",
				expected,
				((Exception) obj).getMessage()
				);
	}
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionMessageConstructor() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> c = Class.forName(invalidMoveExceptionPath);

		String msg = generateRandomString(10);
		Constructor<?> invalid_move_constructor = c.getConstructor(String.class);
		Object obj = invalid_move_constructor.newInstance(msg);
		String returned_message = ((Exception) obj).getMessage();

		assertEquals(
				"InvalidMoveException constructor should pass the correct message to super",
				msg,
				returned_message
				);
	}
	////////////////////////////////////////////InvalidTurnException//////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testInvalidTurnExceptionIsSubClassOfException() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(gameActionExceptionPath), Class.forName(invalidTurnExceptionPath));
	}


	@Test(timeout = 1000)
	public void testInvalidTurnExceptionMSGPresent() throws SecurityException, ClassNotFoundException {
		testInstanceVariableIsPresent(Class.forName(invalidTurnExceptionPath), "MSG");
	}

	@Test(timeout = 1000)
	public void testInvalidTurnExceptionMSGOfTypeString() throws ClassNotFoundException  {
		testInstanceVariableOfType(Class.forName(invalidTurnExceptionPath), "MSG", String.class);
	}
	@Test(timeout = 1000)
	public void testInvalidTurnExceptionMSGIsFinal() throws ClassNotFoundException {
		testInstanceVariableIsFinal(Class.forName(invalidTurnExceptionPath), "MSG");

	}
	@Test(timeout = 1000)
	public void testInvalidTurnExceptionMSGIsStatic() throws ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(invalidTurnExceptionPath), "MSG");
	}
	@Test(timeout = 1000)
	public void testInvalidTurnExceptionMSGIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(invalidTurnExceptionPath), "MSG");
	}

	@Test(timeout = 1000)
	public void testInvalidTurnExceptionMSGValue() throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Class aClass = Class.forName(invalidTurnExceptionPath);
		Field f = aClass.getDeclaredField("MSG");
		f.setAccessible(true);
		String expected = "Action done on wrong turn";
		String actual = (String) f.get(null);
		assertEquals("wrong value of MSG", expected, actual);
	}


	@Test(timeout = 1000)
	public void testInvalidTurnExceptionMessageConstructor() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> c = Class.forName(invalidTurnExceptionPath);

		String msg = generateRandomString(10);
		Constructor<?> invalid_turn_constructor = c.getConstructor(String.class);
		Object obj = invalid_turn_constructor.newInstance(msg);
		String returned_message = ((Exception) obj).getMessage();

		assertEquals(
				"InvalidTurnException constructor should pass the correct message to super",
				msg,
				returned_message
				);
	}




	/////////////////////////////////////// CanisterModifierInterface/////////////////////////////////////
	@Test(timeout = 1000)
	public void testCanisterModifierIsAnInterface() throws ClassNotFoundException{
		testIsInterface(Class.forName(canisterModifierPath));
	}

	////////////////////////////////////// Role Enum ////////////////////////////////////////////
	@Test(timeout = 1000)
	public void testRoleIsEnum() throws ClassNotFoundException{
		testIsEnum(Class.forName(rolePath));
	}

	@Test(timeout = 1000)
	public void testRoleEnumValues(){
		testEnumValues(rolePath, "Role", new String[]{ "SCARER","LAUGHER"});
	}
	////////////////////////////////////// Constants Class //////////////////////////////////////
	@Test(timeout = 1000)
	public void testConstantsClassExists() throws ClassNotFoundException{
		testClassExists(constantsPath);
	}

	@Test(timeout = 1000)
	public void testConstantsClassIsFinal() throws ClassNotFoundException{
		Class constantsClass = Class.forName(constantsPath);
		boolean isFinal =Modifier.isFinal(constantsClass.getModifiers());
		assertTrue("Constants Class should be final", isFinal);

	}

	//////////////////////////////////////BOARD_SIZE VARIABLE///////////////////////////////////////	
	@Test(timeout = 1000)
	public void testBoardSizeVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "BOARD_SIZE");
	}

	@Test(timeout = 1000)
	public void testBoardSizeVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "BOARD_SIZE");
	}

	@Test(timeout = 1000)
	public void testBoardSizeVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "BOARD_SIZE", int.class);
	}

	@Test(timeout = 1000)
	public void testBoardSizeVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("BOARD_SIZE");
		assertEquals("The BOARD_SIZE attribute should initially be 100",100,f.get(constants));
	}

	/////////////////////////////////////BOARD_ROWS Variable/////////////////////////////////////////
	@Test(timeout = 1000)
	public void testBoardRowsVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"BOARD_ROWS");
	}

	@Test(timeout = 1000)
	public void testBoardRowsVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "BOARD_ROWS");
	}

	@Test(timeout = 1000)
	public void testBoardRowsVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "BOARD_ROWS", int.class);
	}

	@Test(timeout = 1000)
	public void testBoardRowsVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("BOARD_ROWS");
		assertEquals("The BOARD_ROWS attribute should initially be 10",10,f.get(constants));
	}

	/////////////////////////////////////BOARD_COLS Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testBoardColsVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"BOARD_COLS");
	}

	@Test(timeout = 1000)
	public void testBoardColsVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "BOARD_COLS");
	}

	@Test(timeout = 1000)
	public void testBoardColsVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "BOARD_COLS", int.class);
	}

	@Test(timeout = 1000)
	public void testBoardColsVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("BOARD_COLS");
		assertEquals("The BOARD_COLS attribute should initially be 10",10,f.get(constants));
	}

	///////////////////////////////////// WINNING_POSITION Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testWinningPositionVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"WINNING_POSITION");
	}

	@Test(timeout = 1000)
	public void testWinningPositionVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "WINNING_POSITION");
	}

	@Test(timeout = 1000)
	public void testWinningPositionVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "WINNING_POSITION");
	}

	@Test(timeout = 1000)
	public void testWinningPositionVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("WINNING_POSITION");
		assertEquals("The WINNING_POSITION attribute should initially be 99",99,f.get(constants));
	}

	/////////////////////////////////////STARTING_POSITION Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testStartingPositionVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"WINNING_POSITION");
	}

	@Test(timeout = 1000)
	public void testStartingPositionVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "STARTING_POSITION");
	}

	@Test(timeout = 1000)
	public void testStartingPositionVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "STARTING_POSITION");
	}

	@Test(timeout = 1000)
	public void testStartingPositionVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "STARTING_POSITION");
	}
	/////////////////////////////////////MONSTER_CELL_INDICES Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testMonsterCellIndicesVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "MONSTER_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testMonsterCellIndicesVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "MONSTER_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testMonsterCellIndicesVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "MONSTER_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testMonsterCellIndicesVariableIsArrayOfInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "MONSTER_CELL_INDICES", int[].class);
	}

	/////////////////////////////////////CONVEYOR_CELL_INDICES Variable////////////////////////////////////////	
	@Test(timeout = 1000)
	public void testConveyorCellIndicesVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "CONVEYOR_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testConveyorCellIndicesVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "CONVEYOR_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testConveyorCellIndicesVariableIsArrayOfInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "CONVEYOR_CELL_INDICES", int[].class);
	}

	@Test(timeout = 1000)
	public void testConveyorCellIndicesVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("CONVEYOR_CELL_INDICES");
		int [] fValues = (int [])f.get(constants);
		int [] expectedValue = {6, 22, 44, 52, 66};
		assertTrue("The CONVEYOR_CELL_INDICES attribute should initially be {6, 22, 44, 52, 66}",Arrays.equals(expectedValue, fValues));		
	}

	/////////////////////////////////////SOCK_CELL_INDICES Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testSockCellIndicesVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"SOCK_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testSockCellIndicesVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "SOCK_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testSockCellIndicesVariableIsArrayOfInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "SOCK_CELL_INDICES", int[].class);
	}

	@Test(timeout = 1000)
	public void testSockCellIndicesVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("SOCK_CELL_INDICES");
		int [] fValues = (int [])f.get(constants);
		int [] expectedValue = {32, 42, 74, 84, 98};
		assertTrue("The SOCK_CELL_INDICES attribute should initially be {32, 42, 74, 84, 98}",Arrays.equals(expectedValue, fValues));		
	}

	/////////////////////////////////////CARD_CELL_INDICES Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testCardCellIndicesVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"CARD_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testCardCellIndicesVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "CARD_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testCardCellIndicesVariableIsArrayOfInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "CARD_CELL_INDICES", int[].class);
	}

	@Test(timeout = 1000)
	public void testCardCellIndicesVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("CARD_CELL_INDICES");
		int [] fValues = (int [])f.get(constants);
		int [] expectedValue = {4, 12, 28, 36, 48, 56, 60, 76, 86, 90};
		assertTrue("The CARD_CELL_INDICES attribute should initially be {4, 12, 28, 36, 48, 56, 60, 76, 86, 90}",Arrays.equals(expectedValue, fValues));		
	}

	/////////////////////////////////////WINNING_ENERGY Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testWinningEnergyVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"WINNING_ENERGY");
	}

	@Test(timeout = 1000)
	public void testWinningEnergyVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "WINNING_ENERGY");
	}

	@Test(timeout = 1000)
	public void testWinningEnergyVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "WINNING_ENERGY");
	}

	@Test(timeout = 1000)
	public void testWinningEnergyVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("WINNING_ENERGY");
		assertEquals("The WINNING_ENERGY attribute should initially be 1000",1000,f.get(constants));
	}

	/////////////////////////////////////MIN_ENERGY Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testMinEnergyVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"MIN_ENERGY");
	}

	@Test(timeout = 1000)
	public void testMinEnergyVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "MIN_ENERGY");
	}

	@Test(timeout = 1000)
	public void testMinEnergyVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "MIN_ENERGY");
	}

	@Test(timeout = 1000)
	public void testMinEnergyVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "MIN_ENERGY");
	}

	/////////////////////////////////////MULTITASKER_BONUS Variable////////////////////////////////////
	@Test(timeout = 1000)
	public void testMultiTaskerBonusVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "MULTITASKER_BONUS");
	}

	@Test(timeout = 1000)
	public void testMultiTaskerBonusVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "MULTITASKER_BONUS");
	}

	@Test(timeout = 1000)
	public void testMultiTaskerBonusVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "MULTITASKER_BONUS");
	}

	@Test(timeout = 1000)
	public void testMultiTaskerBonusVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "MULTITASKER_BONUS", int.class);
	}
	/////////////////////////////////////SCHEMER_STEAL Variable////////////////////////////////////////	
	@Test(timeout = 1000)
	public void testSchemerStealVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "SCHEMER_STEAL");
	}

	@Test(timeout = 1000)
	public void testSchemerStealVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "SCHEMER_STEAL");
	}

	@Test(timeout = 1000)
	public void testSchemerStealVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "SCHEMER_STEAL", int.class);
	}

	@Test(timeout = 1000)
	public void testSchemerStealVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("SCHEMER_STEAL");
		assertEquals("The SCHEMER_STEAL attribute should initially be 10",10,f.get(constants));
	}

	/////////////////////////////////////SLIP_PENALTY Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testSlipPenaltyVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"SLIP_PENALTY");
	}

	@Test(timeout = 1000)
	public void testSlipPenaltyVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "SLIP_PENALTY");
	}

	@Test(timeout = 1000)
	public void testSlipPenaltyVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "SLIP_PENALTY", int.class);
	}

	@Test(timeout = 1000)
	public void testSlipPenaltyVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("SLIP_PENALTY");
		assertEquals("The SLIP_PENALTY attribute should initially be 100",100,f.get(constants));
	}

	/////////////////////////////////////POWERUP_COST Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testPowerUpCostVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"POWERUP_COST");
	}

	@Test(timeout = 1000)
	public void testPowerUpCostVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "POWERUP_COST");
	}

	@Test(timeout = 1000)
	public void testPowerUpCostVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "POWERUP_COST", int.class);
	}

	@Test(timeout = 1000)
	public void testPowerUpCostVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("POWERUP_COST");
		assertEquals("The POWERUP_COST attribute should initially be 500",500,f.get(constants));
	}

	/////////////////////////////////////////Monster Class/////////////////////////////
	@Test(timeout = 1000)
	public void testMonsterClassExists() throws ClassNotFoundException{
		testClassExists(monsterPath);
	}

	@Test(timeout = 1000)
	public void testMonsterClassIsAbstract() throws ClassNotFoundException{
		Class monsterClass = Class.forName(monsterPath);
		testClassIsAbstract(monsterClass);		
	}

	@Test(timeout = 1000)
	public void testMonsterClassImplementsComparable() throws ClassNotFoundException{
		Class monsterClass = Class.forName(monsterPath);
		Class comparableInterface = Class.forName("java.lang.Comparable");
		testClassImplementsInterface(monsterClass, comparableInterface);	
	}

	/////////////////////////////////////////Monster Class Attributes//////////////////////

	////////////////////////////////////////Name Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testNameVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "name");
	}

	@Test(timeout = 1000)
	public void testNameVariableIsString() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "name", String.class);
	}

	////////////////////////////////////////Description Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testDescriptionVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"description");
	}

	@Test(timeout = 1000)
	public void testDescriptionVariableIsString() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "description", String.class);
	}

	////////////////////////////////////////Role Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testRoleVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"role");
	}

	@Test(timeout = 1000)
	public void testRoleVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "role");
	}

	////////////////////////////////////////OriginalRole Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testOriginalRoleVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "originalRole");
	}

	@Test(timeout = 1000)
	public void testOriginalRoleVariableIsRoleEnum() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "originalRole", Class.forName(rolePath));
	}

	////////////////////////////////////////Energy Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testEnergyVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"energy");
	}

	@Test(timeout = 1000)
	public void testEnergyVariableIsInt() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "energy", int.class);
	}

	////////////////////////////////////////Position Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testPositionVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"position");
	}

	@Test(timeout = 1000)
	public void testPositionVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "position");
	}

	////////////////////////////////////////Frozen Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testFrozenVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "frozen");
	}

	@Test(timeout = 1000)
	public void testFrozenVariableIsBoolean() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "frozen", boolean.class);
	}

	////////////////////////////////////////Shielded Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testShieldedVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"shielded");
	}

	@Test(timeout = 1000)
	public void testShieldedVariableIsBoolean() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "shielded", boolean.class);
	}

	////////////////////////////////////////ConfusionTurn Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testConfusionTurnsVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"confusionTurns");
	}

	@Test(timeout = 1000)
	public void testConfusionTurnsVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "confusionTurns");
	}

	////////////////////////////////////////Getters//////////////////////////////////////
	///////////////////////////////////////Name Getter/////////////////////////////////

	@Test(timeout = 1000)
	public void testNameGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "name", name);		
	}

	///////////////////////////////////////description Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testDescriptionGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "getDescription", String.class, true);
	}

	///////////////////////////////////////role Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testRoleGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "getRole", Class.forName(rolePath), true);
	}

	///////////////////////////////////////originalRole Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testOriginalRoleGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "getOriginalRole", Class.forName(rolePath), true);
	}

	@Test(timeout = 1000)
	public void testOriginalRoleGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "originalRole", role);		
	}

	///////////////////////////////////////energy Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testEnergyGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "energy", energy);		
	}

	///////////////////////////////////////position Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testPositionGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "getPosition", int.class, true);
	}

	///////////////////////////////////////frozen Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testFrozenGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "isFrozen", boolean.class, true);
	}

	@Test(timeout = 1000)
	public void testFrozenGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "frozen", false);		
	}

	///////////////////////////////////////shielded Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testShieldedGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "isShielded", boolean.class, true);
	}

	///////////////////////////////////////confusionTurns Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testConfusionTurnsGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "getConfusionTurns", int.class, true);
	}

	@Test(timeout = 1000)
	public void testConfusionTurnsGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "confusionTurns", 0);		
	}

	//////////////////////////////////////////////Setters///////////////////////////////////
	//////////////////////////////////////////////name Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testNameSetterMethodNotExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setName", String.class, false);
	}

	//////////////////////////////////////////////role Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testRoleSetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testSetterLogic(dynamoMonster, "role", role, role, Class.forName(rolePath));		
	}

	//////////////////////////////////////////////originalRole Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testOriginalRoleSetterMethodNotExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setOriginalRole", Class.forName(rolePath), false);
	}

	//////////////////////////////////////////////energy Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testEnergySetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setEnergy", int.class, true);
	}

	//////////////////////////////////////////////position Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testPositionSetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setPosition", int.class, true);
	}

	@Test(timeout = 1000)
	public void testPositionSetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		int position = new Random().nextInt(99);
		testSetterLogic(dynamoMonster, "position", position, position, int.class);		
	}

	//////////////////////////////////////////////frozen Setter//////////////////////////////	
	@Test(timeout = 1000)
	public void testFrozenSetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testSetterLogic(dynamoMonster, "frozen", true, true, boolean.class);		
	}

	//////////////////////////////////////////////shielded Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testShieldedSetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setShielded", boolean.class, true);
	}

	//////////////////////////////////////////////confusionTurns Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testConfusionTurnsSetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setConfusionTurns", int.class, true);
	}

	@Test(timeout = 1000)
	public void testConfusionTurnsSetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		int confusionTurn = new Random().nextInt(10);
		testSetterLogic(dynamoMonster, "confusionTurns", confusionTurn, confusionTurn, int.class);		
	}

	////////////////////////////////Energy Negative//////////////////////////
	@Test(timeout = 1000)
	public void testEnergyShouldNotBeNegative() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);


		Field f = Class.forName(monsterPath).getDeclaredField("energy");
		f.setAccessible(true);


		Method m = dynamoMonster.getClass().getMethod("setEnergy", int.class);

		m.invoke(dynamoMonster, new Random().nextInt(10) * (-1));
		assertEquals("The energy attribute should not be negative",0,f.get(dynamoMonster));
	}

	///////////////////////////////Position Boundaries///////////////////////////
	
	@Test(timeout = 1000)
	public void testPositionShouldNotBeGreaterThan99() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);

		Field positionField = Class.forName(monsterPath).getDeclaredField("position");
		positionField.setAccessible(true);

		Method m = dynamoMonster.getClass().getMethod("setPosition", int.class);
		int position = new Random().nextInt(10)+100;
		m.invoke(dynamoMonster, position);

		Field boardSizeField = Class.forName(constantsPath).getDeclaredField("BOARD_SIZE");
		boardSizeField.setAccessible(true);
		int boardSizeValue = (int) boardSizeField.get(constantsPath);
		int expectedValue = position % boardSizeValue;
		assertEquals("The position attribute should be within the board size; it should not be greater than 99",expectedValue,positionField.get(dynamoMonster));
	}

	/////////////////////////////Monster Constructor///////////////////////////////
	@Test(timeout = 1000)
	public void testMonsterConstructorExists() throws ClassNotFoundException{
		Class [] inputs = {String.class, String.class, Class.forName(rolePath), int.class};
		testConstructorExists(Class.forName(monsterPath),inputs );
	}

	@Test(timeout = 1000)
	public void testMonsterConstructorWithZeroParametersDoesNotExist() throws ClassNotFoundException{
		Class [] inputs = {};
		testEmptyConstructorDoesNotExist(Class.forName(monsterPath),inputs );
	}

	@Test(timeout = 1000)
	public void testMonsterConstructorInitialization() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		String [] variableNames = {"name", "description", "role", "originalRole", "energy", "position", "frozen", "shielded", "confusionTurns"};
		Object [] variableValues = {name, description, role, role, energy, 0, false, false, 0};
		testConstructorInitialization(dynamoMonster, variableNames , variableValues);
	}

	/////////////////////////////////////////Monster CompareTo Method//////////////////////
	@Test(timeout = 1000)
	public void testComparaToMethodExistsInMonsterClass() throws SecurityException{
		//first check if the method exists
		Class monsterClass = null;
		try {
			monsterClass = Class.forName(monsterPath);
		} catch (ClassNotFoundException e) {
			fail("Monster Class is not found");
		}

		Method[] methods = monsterClass.getDeclaredMethods();
		assertTrue("The monster class should override the method in the comparable interface", containsMethodName(methods, "compareTo"));

		//second check if takes a parameter or not
		boolean exist = true;
		Method m = null;
		try {
			m = monsterClass.getDeclaredMethod("compareTo",monsterClass);
		} catch (NoSuchMethodException e) {
			exist = false;
		}
		assertTrue("compareTo method should take a parameter of type : " + monsterClass.getSimpleName(), exist);

		//finally check if it is int
		assertTrue("comparTo method should return int",m.getReturnType().equals(int.class));
	}

		//////////////////////////////////////Monster SubClasses/////////////////////////
	///////////////////////SubClasses Exist//////////////////////////////////////////
	@Test(timeout = 1000)
	public void testDynamoClassExists(){
		testClassExists(dynamoPath);
	}
	@Test(timeout = 1000)
	public void testMultiTaskerClassExists(){
		testClassExists(multiTaskerPath);
	}
	@Test(timeout = 1000)
	public void testSchemerClassExists(){
		testClassExists(schemerPath);
	}

	/////////////////////////////Subclasses extend Monster////////////////////////////
	@Test(timeout = 1000)
	public void testDasherClassIsSubclassOfMonster() throws ClassNotFoundException{
		testClassIsSubclass(Class.forName(dasherPath), Class.forName(monsterPath));
	}

	@Test(timeout = 1000)
	public void testMultiTaskerClassIsSubclassOfMonster() throws ClassNotFoundException{
		testClassIsSubclass(Class.forName(multiTaskerPath), Class.forName(monsterPath));
	}

	@Test(timeout = 1000)
	public void testSchemerClassIsSubclassOfMonster() throws ClassNotFoundException{
		testClassIsSubclass(Class.forName(schemerPath), Class.forName(monsterPath));
	}

	//////////////////////////////////Subclasses Constructor////////////////////////////
	/////////////////////////////////Constructors Exist//////////////////////////
	@Test(timeout = 1000)
	public void testDasherConstructorExists() throws ClassNotFoundException{
		Class[] inputs = {String.class, String.class, Class.forName(rolePath), int.class};
		testConstructorExists(Class.forName(dasherPath), inputs);
	}

	@Test(timeout = 1000)
	public void testDynamoConstructorExists() throws ClassNotFoundException{
		Class[] inputs = {String.class, String.class, Class.forName(rolePath), int.class};
		testConstructorExists(Class.forName(dynamoPath), inputs);
	}

	@Test(timeout = 1000)
	public void testSchemerConstructorExists() throws ClassNotFoundException{
		Class[] inputs = {String.class, String.class, Class.forName(rolePath), int.class};
		testConstructorExists(Class.forName(schemerPath), inputs);
	}

	///////////////////////////////////////Subclasses does not have an empty constructor////////////////////////	
	@Test(timeout = 1000)
	public void testDasherConstructorWithZeroParametersDoesNotExist() throws ClassNotFoundException{
		Class [] inputs = {};
		testEmptyConstructorDoesNotExist(Class.forName(dasherPath),inputs );
	}	
	@Test(timeout = 1000)
	public void testDynamoConstructorWithZeroParametersDoesNotExist() throws ClassNotFoundException{
		Class [] inputs = {};
		testEmptyConstructorDoesNotExist(Class.forName(dynamoPath),inputs );
	}	
	@Test(timeout = 1000)
	public void testMultiTaskerConstructorWithZeroParametersDoesNotExist() throws ClassNotFoundException{
		Class [] inputs = {};
		testEmptyConstructorDoesNotExist(Class.forName(multiTaskerPath),inputs );
	}

	///////////////////////////////////Constructors Logic///////////////////////////////////
	@Test(timeout = 1000)
	public void testDasherConstructorLogic() throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException{
		Constructor<?> dasherConstructor = Class.forName(dasherPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String [] roleValues = {"SCARER","LAUGHER"};
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dasherMonster = dasherConstructor.newInstance(name,description,role,energy);
		String [] variableNames = {"name", "description", "role", "originalRole", "energy", "position", "frozen", "shielded", "confusionTurns","momentumTurns"};
		Object [] variableValues = {name, description, role, role, energy, 0, false, false, 0,0};
		testConstructorInitialization(dasherMonster, variableNames, variableValues);
	}

	@Test(timeout = 1000)
	public void testMultiTaskerConstructorLogic() throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException{
		Constructor<?> multiTaskerConstructor = Class.forName(multiTaskerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String [] roleValues = {"SCARER","LAUGHER"};
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object multiTaskerMonster = multiTaskerConstructor.newInstance(name,description,role,energy);
		String [] variableNames = {"name", "description", "role", "originalRole", "energy", "position", "frozen", "shielded", "confusionTurns","normalSpeedTurns"};
		Object [] variableValues = {name, description, role, role, energy, 0, false, false, 0,0};
		testConstructorInitialization(multiTaskerMonster, variableNames, variableValues);
	}
	@Test(timeout = 1000)
	public void testSchemerConstructorLogic() throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException{
		Constructor<?> schemerConstructor = Class.forName(schemerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String [] roleValues = {"SCARER","LAUGHER"};
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object schemerMonster = schemerConstructor.newInstance(name,description,role,energy);
		String [] variableNames = {"name", "description", "role", "originalRole", "energy", "position", "frozen", "shielded", "confusionTurns"};
		Object [] variableValues = {name, description, role, role, energy, 0, false, false, 0};
		testConstructorInitialization(schemerMonster, variableNames, variableValues);
	}

	///////////////////////////////Subclasses Attributes//////////////////////////
	///////////////////////////////Dasher Extra Attribute/////////////////////////
	@Test(timeout = 1000)
	public void testMomentumTurnsVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(dasherPath), "momentumTurns");
	}

	@Test(timeout = 1000)
	public void testMomentumTurnsVariableIsInt() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(dasherPath), "momentumTurns", int.class);
	}

	@Test(timeout = 1000)
	public void testMomentumTurnsGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(dasherPath), "getMomentumTurns", int.class, true);
	}

	@Test(timeout = 1000)
	public void testMomentumTurnsSetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(dasherPath), "setMomentumTurns", int.class, true);
	}

	@Test(timeout = 1000)
	public void testMomentumTurnsSetterMethodLogic() throws Exception{
		Constructor<?> dasherConstructor = Class.forName(dasherPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		int momentumTurns = new Random().nextInt(10)+1;
		Object dasherMonster = dasherConstructor.newInstance(name,description,role,energy);
		testSetterLogic(dasherMonster, "momentumTurns", momentumTurns, momentumTurns, int.class);		
	}


	///////////////////////////////MultiTasker Extra Attribute/////////////////////////
	@Test(timeout = 1000)
	public void testNormalSpeedTurnsVariableExistsInMultiTaskerClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(multiTaskerPath),"normalSpeedTurns");
	}

	@Test(timeout = 1000)
	public void testNormalSpeedTurnsVariableIsInt() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(multiTaskerPath), "normalSpeedTurns", int.class);
	}

	@Test(timeout = 1000)
	public void testNormalSpeedTurnsGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(multiTaskerPath), "getNormalSpeedTurns", int.class, true);
	}

	@Test(timeout = 1000)
	public void testNormalSpeedTurnsGetterMethodLogic() throws Exception{
		Constructor<?> multiTaskerConstructor = Class.forName(multiTaskerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object multiTaskerMonster = multiTaskerConstructor.newInstance(name,description,role,energy);
		testGetterLogic(multiTaskerMonster, "normalSpeedTurns", 0);		
	}

	@Test(timeout = 1000)
	public void testNormalSpeedTurnsSetterMethodLogic() throws Exception{
		Constructor<?> multiTaskerConstructor = Class.forName(multiTaskerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		int normalSpeedTurns = new Random().nextInt(10)+1;
		Object multiTaskerMonster = multiTaskerConstructor.newInstance(name,description,role,energy);
		testSetterLogic(multiTaskerMonster, "normalSpeedTurns", normalSpeedTurns, normalSpeedTurns, int.class);		
	}


	///////////////////////////	Testing 5.13 Card Class
	//	Present
	@Test(timeout = 1000)
	public void testCardExists() throws ClassNotFoundException {
		Class.forName(cardPath);
	}
	//	Abstract
	@Test(timeout = 1000)
	public void testWildCardIsAnAbstractClass() throws Exception {
		testClassIsAbstract(Class.forName(cardPath));
	}

	//	Constructor present
	@Test(timeout = 1000)
	public void testConstructorCard() throws ClassNotFoundException {
		Class[] inputs = {String.class, String.class, int.class, boolean.class};
		testConstructorExists(Class.forName(cardPath), inputs);
	}

	//	Attributes existence
	@Test(timeout = 100)
	public void testCardInstanceVariableRarityIsPresent() throws NoSuchFieldException, ClassNotFoundException {

		testInstanceVariableIsPresent(Class.forName(cardPath), "rarity");
	}

	@Test(timeout = 100)
	public void testCardInstanceVariableLuckyIsPresent() throws NoSuchFieldException, ClassNotFoundException {

		testInstanceVariableIsPresent(Class.forName(cardPath), "lucky");
	}

	//	Attributes private
	@Test(timeout = 1000)
	public void testCardInstanceVariableNameIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(cardPath), "name");
	}

	@Test(timeout = 1000)
	public void testCardInstanceVariableDescriptionIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(cardPath), "description");
	}

	/*--------------------------testing getters existence -----------------------------------*/


	@Test(timeout = 100)
	public void testDescriptionGetter() throws ClassNotFoundException {
		testGetterMethodExistsInClass(Class.forName(cardPath),"getDescription",String.class,true);
	}

	@Test(timeout = 100)
	public void testRarityGetter() throws ClassNotFoundException {
		testGetterMethodExistsInClass(Class.forName(cardPath),"getRarity",int.class,true);
	}

	@Test(timeout = 100)
	public void testLuckyGetter() throws ClassNotFoundException {
		testGetterMethodExistsInClass(Class.forName(cardPath),"isLucky",boolean.class,true);
	}



	/*--------------------------testing setters absence -----------------------------------*/


	@Test(timeout = 1000)
	public void testCardInstanceVariableNameNoSetter() throws ClassNotFoundException {
		testSetterMethodExistsInClass(Class.forName(cardPath), "setName",String.class , false);
	}

	@Test(timeout = 1000)
	public void testCardInstanceVariableDescriptionNoSetter() throws ClassNotFoundException {
		testSetterMethodExistsInClass(Class.forName(cardPath), "setDescription",String.class , false);
	}

	@Test(timeout = 1000)
	public void testCardInstanceVariableRarityNoSetter() throws ClassNotFoundException {
		testSetterMethodExistsInClass(Class.forName(cardPath), "setRarity",int.class , false);
	}


	///////////////////////////	Testing 5.14 SwapperCard Class
	//	Present
	@Test(timeout = 1000)
	public void testSwapperCardExists() throws ClassNotFoundException {
		Class.forName(swapperCardPath);
	}
	//	Constructor present
	@Test(timeout = 1000)
	public void testConstructorSwapperCard() throws ClassNotFoundException {
		Class[] inputs = {String.class, String.class, int.class};
		testConstructorExists(Class.forName(swapperCardPath), inputs);
	}

	///////////////////////////	Testing 5.15 EnergyStealCard Class
	//	Present
	@Test(timeout = 1000)
	public void testEnergyStealCardExists() throws ClassNotFoundException {
		Class.forName(energyStealCardPath);
	}
	//	Constructor present
	@Test(timeout = 1000)
	public void testConstructorEnergyStealCard() throws ClassNotFoundException {
		Class[] inputs = {String.class, String.class, int.class, int.class};
		testConstructorExists(Class.forName(energyStealCardPath), inputs);
	}

	//Constructor Initializes correctly
	@Test(timeout = 1000)
	public void testEnergyStealCardConstructorInitialization() throws Exception{
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		int input_rarity=(int) (Math.random()*5);	
		int input_energy=(int) (Math.random()*5);
		Constructor<?> energyStealCard_constructor = Class.forName(energyStealCardPath).getConstructor(String.class,String.class,int.class,int.class);
		Object energyStealCard_Object = energyStealCard_constructor.newInstance(input_name, input_description,input_rarity,input_energy);
		String[] names = {"name", "description","rarity","lucky","energy"};
		Object[] values = {input_name,input_description,input_rarity,true,input_energy};
		testConstructorInitialization(energyStealCard_Object, names, values);
	}

	//Subclass of Card
	@Test(timeout = 1000)
	public void testEnergyStealCardIsSubClassOfCard() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(cardPath), Class.forName(energyStealCardPath));
	}

	//Attributes present
	@Test(timeout = 100)
	public void testEnergyStealCardInstanceVariableEnergyIsPresent() throws NoSuchFieldException, ClassNotFoundException {

		testInstanceVariableIsPresent(Class.forName(energyStealCardPath), "energy");
	}
	//Attributes private
	@Test(timeout = 1000)
	public void testEnergyStealCardInstanceVariableEnergyIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(energyStealCardPath), "energy");
	}

	/*--------------------------testing getters existence -----------------------------------*/

	@Test(timeout = 100)
	public void testEnergyStealCardInstanceVariableEnergyGetter() throws ClassNotFoundException {
		testGetterMethodExistsInClass(Class.forName(energyStealCardPath),"getEnergy",int.class,true);
	}


	/*--------------------------testing setters absence -----------------------------------*/


	@Test(timeout = 1000)
	public void testEnergyStealCardInstanceVariableEnergyNoSetter() throws ClassNotFoundException {
		testSetterMethodExistsInClass(Class.forName(energyStealCardPath), "setEnergy",int.class , false);
	}

	///////////////////////////	Testing 5.16 StartOverCard Class
	//	Present
	@Test(timeout = 1000)
	public void testStartOverCardExists() throws ClassNotFoundException {
		Class.forName(startOverCardPath);
	}
	//	Constructor present
	@Test(timeout = 1000)
	public void testConstructorStartOverCard() throws ClassNotFoundException {
		Class[] inputs = {String.class, String.class, int.class, boolean.class};
		testConstructorExists(Class.forName(startOverCardPath), inputs);
	}

	//Subclass of Card
	@Test(timeout = 1000)
	public void testStartOverCardIsSubClassOfCard() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(cardPath), Class.forName(startOverCardPath));
	}

	///////////////////////////	Testing 5.17 ConfusionCard Class
	//	Present
	@Test(timeout = 1000)
	public void testConfusionCardExists() throws ClassNotFoundException {
		Class.forName(confusionCardPath);
	}
	//	Constructor present
	@Test(timeout = 1000)
	public void testConstructorConfusionCard() throws ClassNotFoundException {
		Class[] inputs = {String.class, String.class, int.class, int.class};
		testConstructorExists(Class.forName(confusionCardPath), inputs);
	}

	//Constructor Initializes correctly
	@Test(timeout = 1000)
	public void testconfusionCardConstructorInitialization() throws Exception{
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		int input_rarity=(int) (Math.random()*5);	
		int input_duration=(int) (Math.random()*5);	
		Constructor<?> confusionCard_constructor = Class.forName(confusionCardPath).getConstructor(String.class,String.class,int.class,int.class);
		Object confusionCard_Object = confusionCard_constructor.newInstance(input_name, input_description,input_rarity,input_duration);
		String[] names = {"name", "description","rarity","lucky","duration"};
		Object[] values = {input_name,input_description,input_rarity,false,input_duration};
		testConstructorInitialization(confusionCard_Object, names, values);
	}

	//Attributes private
	@Test(timeout = 1000)
	public void testConfusionCardInstanceVariableDurationIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(confusionCardPath), "duration");
	}

	/*--------------------------testing getters existence -----------------------------------*/

	@Test(timeout = 100)
	public void testConfusionCardInstanceVariableDurationGetter() throws ClassNotFoundException {
		testGetterMethodExistsInClass(Class.forName(confusionCardPath), "getDuration",int.class,true);
	}

	/*--------------------------testing getters logic -----------------------------------*/

	@Test(timeout = 1000)
	public void testConfusionCardInstanceVariableDurationGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		int input_rarity=(int) (Math.random()*5);	
		int input_duration=(int) (Math.random()*5);	
		Constructor<?> confusionCard_constructor = Class.forName(confusionCardPath).getConstructor(String.class,String.class,int.class,int.class);
		Object confusionCardinstance = confusionCard_constructor.newInstance(input_name, input_description,input_rarity,input_duration);
		testGetterMethodLogic(confusionCardinstance, "duration", input_duration);	
	}

	///////////////////////////	Testing 5.18 ShieldCard Class
	//	Present
	@Test(timeout = 1000)
	public void testShieldCardExists() throws ClassNotFoundException {
		Class.forName(shieldCardPath);
	}

	//Constructor Initializes correctly
	@Test(timeout = 1000)
	public void testShieldCardConstructorInitialization() throws Exception{
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		int input_rarity=(int) (Math.random()*5);		
		Constructor<?> shieldCard_constructor = Class.forName(shieldCardPath).getConstructor(String.class,String.class,int.class);
		Object shieldCard_Object = shieldCard_constructor.newInstance(input_name, input_description,input_rarity);
		String[] names = {"name", "description","rarity","lucky"};
		Object[] values = {input_name,input_description,input_rarity,true};
		testConstructorInitialization(shieldCard_Object, names, values);
	}

	///////////////////////////	Testing 6.4 OutOfEnergyException Class
	//	Present
	@Test(timeout = 1000)
	public void OutOfEnergyExceptionClassExists() throws ClassNotFoundException {
		Class.forName(OutOfEnergyExceptionPath);
	}
	//Empty	Constructor present
	@Test(timeout = 1000)
	public void testEmptyConstructorOutOfEnergyException() throws ClassNotFoundException {
		Class[] inputs = {};
		testConstructorExists(Class.forName(OutOfEnergyExceptionPath), inputs);
	}

	//Second Constructor Initializes correctly
	@Test(timeout = 1000)
	public void testSecondConstructorOutOfEnergyExceptionInitialization() throws Exception{
		String input_message = generateRandomString(10);	
		Constructor<?> outOfEnergyException_Secondconstructor = Class.forName(OutOfEnergyExceptionPath).getConstructor(String.class);
		Object outOfEnergyException_SecondObject = outOfEnergyException_Secondconstructor.newInstance(input_message);
		String[] names = {"detailMessage"};
		Object[] values = {input_message};
		testExceptionConstructorInitialization(outOfEnergyException_SecondObject, names, values);
	}


	//Subclass of gameActionException
	@Test(timeout = 1000)
	public void testOutOfEnergyExceptionIsSubClassOfGameActionException() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(gameActionExceptionPath), Class.forName(OutOfEnergyExceptionPath));
	}

	//Attribute MSG is Present
	@Test(timeout = 100)
	public void testOutOfEnergyExceptionInstanceVariableMSGIsPresent() throws NoSuchFieldException, ClassNotFoundException {

		testInstanceVariableIsPresent(Class.forName(OutOfEnergyExceptionPath), "MSG");
	}

	//	Attribute MSG is static
	@Test(timeout = 1000)
	public void testOutOfEnergyExceptionInstanceVariableMSGIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(OutOfEnergyExceptionPath), "MSG");
	}


	///////////////////////////	Testing 6.5 InvalidCSVFormatException Class
	//	Present
	@Test(timeout = 1000)
	public void InvalidCSVFormatExceptionClassExists() throws ClassNotFoundException {
		Class.forName(invalidCSVFormatPath);
	}

	//Second Constructor present
	@Test(timeout = 1000)
	public void testSecondConstructorInvalidCSVFormatException() throws ClassNotFoundException {
		Class[] inputs = {String.class,String.class};
		testConstructorExists(Class.forName(invalidCSVFormatPath), inputs);
	}

	//First Constructor Initializes correctly
	@Test(timeout = 1000)
	public void testFirstConstructorInvalidCSVFormatInitialization() throws Exception{
		String MSG = "Invalid input detected while reading csv file, input = \n";
		String input_Line = generateRandomString(10);
		Constructor<?> invalidCSVFormatException_constructor = Class.forName(invalidCSVFormatPath).getConstructor(String.class);
		Object InvalidCSVFormatException_Object = invalidCSVFormatException_constructor.newInstance(input_Line);
		String[] names = {"detailMessage"};
		Object[] values = {MSG+input_Line};
		testExceptionConstructorInitialization(InvalidCSVFormatException_Object, names, values);
	}


	//Attribute MSG is Present
	@Test(timeout = 100)
	public void testInvalidCSVFormatExceptionInstanceVariableMSGIsPresent() throws NoSuchFieldException, ClassNotFoundException {	
		testInstanceVariableIsPresent(Class.forName(invalidCSVFormatPath), "MSG");
	}

	//	Attribute MSG is final
	@Test(timeout = 1000)
	public void testInvalidCSVFormatExceptionInstanceVariableMSGIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsFinal(Class.forName(invalidCSVFormatPath), "MSG");
	}

	//Attribute inputLine is Present
	@Test(timeout = 100)
	public void testInvalidCSVFormatExceptionInstanceVariableInputLineIsPresent() throws NoSuchFieldException, ClassNotFoundException {	
		testInstanceVariableIsPresent(Class.forName(invalidCSVFormatPath), "inputLine");
	}


	/*--------------------------testing getters logic -----------------------------------*/

	@Test(timeout = 1000)
	public void testInvalidCSVFormatInstanceVariableInputGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String input_Line = generateRandomString(10);
		Constructor<?> invalidCSVFormatException_constructor = Class.forName(invalidCSVFormatPath).getConstructor(String.class);
		Object InvalidCSVFormatExceptioninstance = invalidCSVFormatException_constructor.newInstance(input_Line);
		testGetterMethodLogic(InvalidCSVFormatExceptioninstance, "inputLine", input_Line);	
	}

	/*--------------------------testing setters existence -----------------------------------*/

	@Test(timeout = 100)
	public void testInvalidCSVFormatInstanceVariableInputLineSetter() throws ClassNotFoundException {
		testSetterMethodExistsInClass(Class.forName(invalidCSVFormatPath), "setInputLine",String.class,true);
	}









	//	---------------------------5.19-5.22 Game Setup-------------------

	//	 Test is Private

	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileMonstersIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(dataLoaderPath), "MONSTERS_FILE_NAME");
	}
	//	 Test is Static
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableCardsIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(dataLoaderPath), "CARDS_FILE_NAME");
	}

	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileMonstersIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(dataLoaderPath), "MONSTERS_FILE_NAME");
	}

	//	Test is Final
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableCardsIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsFinal(Class.forName(dataLoaderPath), "CARDS_FILE_NAME");
	}

	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileMonstersIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsFinal(Class.forName(dataLoaderPath), "MONSTERS_FILE_NAME");
	}

	//	Test type is String


	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileMonstersIsString() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(dataLoaderPath), "MONSTERS_FILE_NAME", String.class);
	}

	// Test Initialised correctly
	@Test(timeout = 1000)
	public void testCardsFileInDataLoaderValue() {

		try {

			Field cardsFileField = Class.forName(dataLoaderPath).getDeclaredField("CARDS_FILE_NAME");
			cardsFileField.setAccessible(true);
			assertEquals("cards.csv",cardsFileField.get(null));
		} catch ( NoSuchFieldException| SecurityException|
				ClassNotFoundException| IllegalArgumentException| IllegalAccessException  e) {
			e.printStackTrace();
			fail("An error "+e.getMessage()+" occured while testing the CARDS_FILE_NAME attribute");
		}
	}


	@Test(timeout = 1000)
	public void testMonstersFileInDataLoaderValue() {

		try {

			Field cardsFileField = Class.forName(dataLoaderPath).getDeclaredField("MONSTERS_FILE_NAME");
			cardsFileField.setAccessible(true);
			assertEquals("monsters.csv",cardsFileField.get(null));
		} catch ( NoSuchFieldException| SecurityException|
				ClassNotFoundException| IllegalArgumentException| IllegalAccessException  e) {
			e.printStackTrace();
			fail("An error "+e.getMessage()+" occured while testing the MONSTERS_FILE_NAME attribute");
		}
	}



	//		readCards method static
	@Test(timeout = 1000)
	public void testreadCardsInDataLoaderisStatic() {

		Method m;
		try {
			m = Class.forName(dataLoaderPath).getMethod("readCards");
			assertTrue("readCards expected to be Public",Modifier.isPublic(m.getModifiers()));
			assertTrue("readCards expected to be Static",Modifier.isStatic(m.getModifiers()));
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			fail("error occured while testing readCards method check the console "+e.getMessage());
		}

	}


	//		readCards method throws IOException



	//	Read Cells

	@Test(timeout = 1000)
	public void testreadCellsInDataLoaderisStatic() {

		Method m;
		try {
			m = Class.forName(dataLoaderPath).getMethod("readCells");
			assertTrue("readCells expected to be Public",Modifier.isPublic(m.getModifiers()));
			assertTrue("readCells expected to be Static",Modifier.isStatic(m.getModifiers()));
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			fail("error occured while testing readCells method check the console "+e.getMessage());
		}

	}




	@Test(timeout = 1000)
	public void testreadCellsInDataLoaderThrowsIOException() {

		Method m;
		try {
			m = Class.forName(dataLoaderPath).getMethod("readCells");
			Class<?>[] exceptionsThrown= m.getExceptionTypes();
			boolean IOExceptionFound=false;
			for (Class<?> excep : exceptionsThrown) {
				if(excep.equals(IOException.class))
					IOExceptionFound=true;
			}

			assertTrue("readCells expected to throw IOException",IOExceptionFound);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			fail("an error occured while testing the readCells, error cause is "+e.getMessage()+" please check the console");
		}

	}




	@Test(timeout = 1000)
	public void testreadMonstersInDataLoaderThrowsIOException() {

		Method m;
		try {
			m = Class.forName(dataLoaderPath).getMethod("readMonsters");
			Class<?>[] exceptionsThrown= m.getExceptionTypes();
			boolean IOExceptionFound=false;
			for (Class<?> excep : exceptionsThrown) {
				if(excep.equals(IOException.class))
					IOExceptionFound=true;
			}

			assertTrue("readMonsters expected to throw IOException",IOExceptionFound);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			fail("an error occured while testing the readMonsters, error cause is "+e.getMessage()+" please check the console");
		}

	}

	@Test(timeout = 1000)
	public void testDataLoaderReadCardsReadingCSV() throws IOException {
		savingCardsCSV();
		try {
			ArrayList<String> cardsList = writeCardsCSVForDataLoader();
			Method readCards = Class.forName(dataLoaderPath).getMethod("readCards");
			ArrayList<?> loadedCards = (ArrayList<?>) readCards.invoke(null);
			assertEquals("readCards() should load one card per CSV line.", cardsList.size(), loadedCards.size());
			Class<?> cardClass = Class.forName(cardPath);
			Field nameField = cardClass.getDeclaredField("name");
			Field descriptionField = cardClass.getDeclaredField("description");
			Field rarityField = cardClass.getDeclaredField("rarity");
			Field luckyField = cardClass.getDeclaredField("lucky");
			nameField.setAccessible(true);
			descriptionField.setAccessible(true);
			rarityField.setAccessible(true);
			luckyField.setAccessible(true);
			for (int i = 0; i < cardsList.size(); i++) {
				String[] csvRow = cardsList.get(i).split(",", -1);
				String cardType = csvRow[0];
				String expectedName = csvRow[1];
				String expectedDescription = csvRow[2];
				int expectedRarity = Integer.parseInt(csvRow[3].trim());
				Object card = loadedCards.get(i);
				assertEquals("Card name at index " + i, expectedName, nameField.get(card));
				assertEquals("Card description at index " + i, expectedDescription, descriptionField.get(card));
				assertEquals("Card rarity at index " + i, expectedRarity, rarityField.get(card));
				if ("STARTOVER".equals(cardType)) {
					boolean expectedLucky = Boolean.parseBoolean(csvRow[4].trim());
					assertEquals("StartOverCard lucky at index " + i, expectedLucky, luckyField.get(card));
				} else if ("ENERGYSTEAL".equals(cardType)) {
					int expectedEnergy = Integer.parseInt(csvRow[4].trim());
					Field energyField = Class.forName(energyStealCardPath).getDeclaredField("energy");
					energyField.setAccessible(true);
					assertEquals("EnergyStealCard energy at index " + i, expectedEnergy, energyField.get(card));
				} else if ("CONFUSION".equals(cardType)) {
					int expectedDuration = Integer.parseInt(csvRow[4].trim());
					Field durationField = Class.forName(confusionCardPath).getDeclaredField("duration");
					durationField.setAccessible(true);
					assertEquals("ConfusionCard duration at index " + i, expectedDuration, durationField.get(card));
				}
			}
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
			fail("Error while testing DataLoader.readCards(): " + e.getMessage());
		} finally {
			reWriteCardsCSVForLoadCards();
		}
	}



	@Test(timeout = 1000)
	public void testDataLoaderReadMonstersReadingCSV() throws IOException {
		savingMonstersCSV();
		try {
			ArrayList<String> monstersList = writeMonstersCSVForDataLoader();
			Method readMonsters = Class.forName(dataLoaderPath).getMethod("readMonsters");
			ArrayList<?> loadedMonsters = (ArrayList<?>) readMonsters.invoke(null);
			assertEquals("readMonsters() should load one monster per CSV line.", monstersList.size(), loadedMonsters.size());
			Class<?> monsterClass = Class.forName(monsterPath);
			Field nameField = monsterClass.getDeclaredField("name");
			Field descriptionField = monsterClass.getDeclaredField("description");
			Field roleField = monsterClass.getDeclaredField("role");
			Field originalRoleField = monsterClass.getDeclaredField("originalRole");
			Field energyField = monsterClass.getDeclaredField("energy");
			Field positionField = monsterClass.getDeclaredField("position");
			Field frozenField = monsterClass.getDeclaredField("frozen");
			Field shieldedField = monsterClass.getDeclaredField("shielded");
			Field confusionTurnsField = monsterClass.getDeclaredField("confusionTurns");
			nameField.setAccessible(true);
			descriptionField.setAccessible(true);
			roleField.setAccessible(true);
			originalRoleField.setAccessible(true);
			energyField.setAccessible(true);
			positionField.setAccessible(true);
			frozenField.setAccessible(true);
			shieldedField.setAccessible(true);
			confusionTurnsField.setAccessible(true);
			for (int i = 0; i < monstersList.size(); i++) {
				String[] csvRow = monstersList.get(i).split(",", -1);
				String expectedName = csvRow[1].trim();
				String expectedDescription = csvRow[2].trim();
				Object expectedRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), csvRow[3].trim());
				int expectedEnergy = Integer.parseInt(csvRow[4].trim());
				Object monster = loadedMonsters.get(i);
				assertEquals("Monster name at index " + i, expectedName, nameField.get(monster));
				assertEquals("Monster description at index " + i, expectedDescription, descriptionField.get(monster));
				assertEquals("Monster role at index " + i, expectedRole, roleField.get(monster));
				assertEquals("Monster originalRole at index " + i, expectedRole, originalRoleField.get(monster));
				assertEquals("Monster energy at index " + i, expectedEnergy, energyField.get(monster));
				assertEquals("Monster position at index " + i + " (should be 0 initially)", 0, positionField.get(monster));
				assertEquals("Monster frozen at index " + i + " (should be false initially)", false, frozenField.get(monster));
				assertEquals("Monster shielded at index " + i + " (should be false initially)", false, shieldedField.get(monster));
				assertEquals("Monster confusionTurns at index " + i + " (should be 0 initially)", 0, confusionTurnsField.get(monster));
			}
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
			fail("Error while testing DataLoader.readMonsters(): " + e.getMessage());
		} finally {
			reWriteMonstersCSVForLoadCards();
		}
	}


	//	Test Board Class

	//	boardCells

	@Test(timeout = 1000)
	public void testBoardInstanceVariableBoardCellsIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsNotStatic(Class.forName(boardPath), "boardCells");
	}
	//	stationedMonsters
	@Test(timeout = 1000)
	public void testBoardInstanceVariableStationedMonstersIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(boardPath), "stationedMonsters");
	}

	//	originalCards
	@Test(timeout = 1000)
	public void testBoardInstanceVariableOriginalCardsIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(boardPath), "originalCards");
	}
	@Test(timeout = 1000)
	public void testBoardInstanceVariableOriginalCardsIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(boardPath), "originalCards");
	}
	//	cards
	@Test(timeout = 1000)
	public void testBoardInstanceVariableCardsIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPublic(Class.forName(boardPath), "cards");
	}
	@Test(timeout = 1000)
	public void testBoardInstanceVariableCardsIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(boardPath), "cards");
	}

	@Test(timeout = 1000)
	public void testBoardInstanceVariablesType() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableOfTypeDoubleArray(Class.forName(boardPath), "boardCells", Class.forName(cellPath));
		testInstanceVariableOfType(Class.forName(boardPath), "stationedMonsters", ArrayList.class );
		testInstanceVariableOfType(Class.forName(boardPath), "originalCards", ArrayList.class );
		testInstanceVariableOfType(Class.forName(boardPath), "cards", ArrayList.class );
	}


	//	Constructor
	@Test(timeout = 1000)
	public void testBoardConstructorInitializationGameManagerCorrect() throws Exception {
		Constructor<?> boardConstructor = Class.forName(boardPath).getConstructor(ArrayList.class);
		ArrayList<?> readCardsList = new ArrayList<>();
		Object boardObject = boardConstructor.newInstance(readCardsList);

		// originalCards must be the same reference as the constructor argument
		testConstructorInitialization(boardObject, new String[] { "originalCards" }, new Object[] { readCardsList });

		// boardCells: non-null 2D array of dimensions BOARD_ROWS x BOARD_COLS
		int rows = Class.forName(constantsPath).getField("BOARD_ROWS").getInt(null);
		int cols = Class.forName(constantsPath).getField("BOARD_COLS").getInt(null);
		Field boardCellsField = Class.forName(boardPath).getDeclaredField("boardCells");
		boardCellsField.setAccessible(true);
		Object boardCells = boardCellsField.get(boardObject);
		assertTrue("Board constructor should initialize boardCells.", boardCells != null);
		assertEquals("Board constructor should initialize boardCells with BOARD_ROWS rows.", rows, Array.getLength(boardCells));
		assertEquals("Board constructor should initialize boardCells with BOARD_COLS columns.", cols, Array.getLength(Array.get(boardCells, 0)));

		// stationedMonsters: non-null empty ArrayList
		Field stationedMonstersField = Class.forName(boardPath).getDeclaredField("stationedMonsters");
		stationedMonstersField.setAccessible(true);
		ArrayList<?> stationedMonsters = (ArrayList<?>) stationedMonstersField.get(null);
		assertTrue("Board constructor should initialize stationedMonsters.", stationedMonsters != null);
		assertEquals("Board constructor should initialize stationedMonsters as empty.", 0, stationedMonsters.size());

		// cards: non-null empty ArrayList
		Field cardsField = Class.forName(boardPath).getDeclaredField("cards");
		cardsField.setAccessible(true);
		ArrayList<?> cards = (ArrayList<?>) cardsField.get(null);
		assertTrue("Board constructor should initialize cards.", cards != null);
		assertEquals("Board constructor should initialize cards as empty.", 0, cards.size());
	}

	// Getter logic tests for Board

	@Test(timeout = 1000)
	public void testBoardGetterLogicStationedMonsters() throws Exception {
		Constructor<?> boardConstructor = Class.forName(boardPath).getConstructor(ArrayList.class);
		Object boardObject = boardConstructor.newInstance(new ArrayList<>());
		ArrayList<Object> value = new ArrayList<>();
		value.add(createDasher());
		value.add(createDynamo());
		value.add(createSchemer());
		testGetterLogic(boardObject, "stationedMonsters", value);
	}

	@Test(timeout = 1000)
	public void testBoardGetterLogicOriginalCards() throws Exception {
		ArrayList<Object> originalCardsList = new ArrayList<>();
		originalCardsList.add(createSwapperCard());
		originalCardsList.add(createShieldCard());
		originalCardsList.add(createEnergyStealCard());
		Constructor<?> boardConstructor = Class.forName(boardPath).getConstructor(ArrayList.class);
		Object boardObject = boardConstructor.newInstance(originalCardsList);
		testGetterLogic(boardObject, "originalCards", originalCardsList);
	}



	@Test(timeout = 1000)
	public void testBoardBoardCellsSetterAbsent() throws SecurityException, ClassNotFoundException {
		testSetterAbsent("boardCells", new String[] { boardPath });
	}

	@Test(timeout = 1000)
	public void testBoardOriginalCardsSetterAbsent() throws SecurityException, ClassNotFoundException {
		testSetterAbsent("originalCards", new String[] { boardPath });
	}



	@Test(timeout = 1000)
	public void testBoardSetterLogicCards() throws Exception {
		Constructor<?> boardConstructor = Class.forName(boardPath).getConstructor(ArrayList.class);
		Object boardObject = boardConstructor.newInstance(new ArrayList<>());
		ArrayList<Object> value = new ArrayList<>();
		value.add(createSwapperCard());
		value.add(createConfusionCard());
		testSetterLogic(boardObject, "cards", value, value, ArrayList.class);
	}

	////////////////////////////////////////////	Test Game Class	//////////////////////////////////////////////////////////////

	// board
	@Test(timeout = 1000)
	public void testGameInstanceVariableBoardIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(gamePath), "board");
	}
	@Test(timeout = 1000)
	public void testGameInstanceVariableBoardIsNotStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsNotStatic(Class.forName(gamePath), "board");
	}
	// allMonsters
	@Test(timeout = 1000)
	public void testGameInstanceVariableAllMonstersIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(gamePath), "allMonsters");
	}

	@Test(timeout = 1000)
	public void testGameInstanceVariablePlayerIsNotStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsNotStatic(Class.forName(gamePath), "player");
	}
	// opponent
	@Test(timeout = 1000)
	public void testGameInstanceVariableOpponentIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(gamePath), "opponent");
	}
	@Test(timeout = 1000)
	public void testGameInstanceVariableOpponentIsNotStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsNotStatic(Class.forName(gamePath), "opponent");
	}
	// current
	@Test(timeout = 1000)
	public void testGameInstanceVariableCurrentIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(gamePath), "current");
	}
	@Test(timeout = 1000)
	public void testGameInstanceVariableCurrentIsNotStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsNotStatic(Class.forName(gamePath), "current");
	}

	@Test(timeout = 1000)
	public void testGameInstanceVariablesType() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(gamePath), "board", Class.forName(boardPath));
		testInstanceVariableOfType(Class.forName(gamePath), "allMonsters", ArrayList.class);
		testInstanceVariableOfType(Class.forName(gamePath), "player", Class.forName(monsterPath));
		testInstanceVariableOfType(Class.forName(gamePath), "opponent", Class.forName(monsterPath));
		testInstanceVariableOfType(Class.forName(gamePath), "current", Class.forName(monsterPath));
	}

	// Constructor initialization using read CSV methods
	@Test(timeout = 1000)
	public void testGameConstructorInitializationUsingReadCSV() throws IOException {
		savingCardsCSV();
		savingMonstersCSV();
		try {
			ArrayList<String> cardsList = writeCardsCSVForDataLoader();
			ArrayList<String> monstersList = writeMonstersCSVForDataLoader();
			Object playerRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			Constructor<?> gameConstructor = Class.forName(gamePath).getConstructor(Class.forName(rolePath));
			Object gameObject = gameConstructor.newInstance(playerRole);

			Method getBoard = Class.forName(gamePath).getMethod("getBoard");
			Object board = getBoard.invoke(gameObject);
			assertTrue("Game constructor should initialize board from DataLoader.readCards().", board != null);
			Method getOriginalCards = Class.forName(boardPath).getMethod("getOriginalCards");
			ArrayList<?> originalCards = (ArrayList<?>) getOriginalCards.invoke(null);
			assertEquals("Board's originalCards should match number of cards loaded from CSV.", cardsList.size(), originalCards.size());

			Method getAllMonsters = Class.forName(gamePath).getMethod("getAllMonsters");
			ArrayList<?> allMonsters = (ArrayList<?>) getAllMonsters.invoke(gameObject);
			assertTrue("Game constructor should initialize allMonsters from DataLoader.readMonsters().", allMonsters != null);
			assertEquals("allMonsters should match number of monsters loaded from CSV.", monstersList.size(), allMonsters.size());

			Method getPlayer = Class.forName(gamePath).getMethod("getPlayer");
			Object player = getPlayer.invoke(gameObject);
			assertTrue("Game constructor should initialize player.", player != null);
			Method getRole = Class.forName(monsterPath).getMethod("getRole");
			Object playerMonsterRole = getRole.invoke(player);
			assertEquals("Player should have the chosen role (SCARER).", playerRole, playerMonsterRole);

			Method getOpponent = Class.forName(gamePath).getMethod("getOpponent");
			Object opponent = getOpponent.invoke(gameObject);
			assertTrue("Game constructor should initialize opponent.", opponent != null);
			Object opponentRole = getRole.invoke(opponent);
			Object laugherRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			assertEquals("Opponent should have the opposite role (LAUGHER).", laugherRole, opponentRole);

			Method getCurrent = Class.forName(gamePath).getMethod("getCurrent");
			Object current = getCurrent.invoke(gameObject);
			assertTrue("Game constructor should initialize current.", current != null);
			assertSame("Current should be the same reference as player initially.", player, current);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
			fail("Error while testing Game constructor initialization: " + e.getMessage());
		} finally {
			reWriteCardsCSVForLoadCards();
			reWriteMonstersCSVForLoadCards();
		}
	}

	@Test(timeout = 1000)
	public void testGameConstructorInitializationUsingReadCSVWithLaugherRole() throws IOException {
		savingCardsCSV();
		savingMonstersCSV();
		try {
			Object laugherRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
			Object scarerRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			Constructor<?> gameConstructor = Class.forName(gamePath).getConstructor(Class.forName(rolePath));
			Object gameObject = gameConstructor.newInstance(laugherRole);

			Method getPlayer = Class.forName(gamePath).getMethod("getPlayer");
			Method getOpponent = Class.forName(gamePath).getMethod("getOpponent");
			Method getRole = Class.forName(monsterPath).getMethod("getRole");

			Object player = getPlayer.invoke(gameObject);
			Object opponent = getOpponent.invoke(gameObject);

			assertTrue("Game constructor should initialize player.", player != null);
			assertTrue("Game constructor should initialize opponent.", opponent != null);

			Object playerMonsterRole = getRole.invoke(player);
			Object opponentRole = getRole.invoke(opponent);

			assertEquals("Player should have the chosen role (LAUGHER).", laugherRole, playerMonsterRole);
			assertEquals("Opponent should have the opposite role (SCARER).", scarerRole, opponentRole);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
			fail("Error while testing Game constructor initialization with LAUGHER role: " + e.getMessage());
		} finally {
			reWriteCardsCSVForLoadCards();
			reWriteMonstersCSVForLoadCards();
		}
	}


	@Test(timeout = 1000)
	public void testGameAllMonstersSetterAbsent() throws SecurityException, ClassNotFoundException {
		testSetterAbsent("allMonsters", new String[] { gamePath });
	}
	@Test(timeout = 1000)
	public void testGamePlayerSetterAbsent() throws SecurityException, ClassNotFoundException {
		testSetterAbsent("player", new String[] { gamePath });
	}
	@Test(timeout = 1000)
	public void testGameOpponentSetterAbsent() throws SecurityException, ClassNotFoundException {
		testSetterAbsent("opponent", new String[] { gamePath });
	}

	// Getter logic and setter logic for Game (requires Game instance — use DataLoader CSVs)


	@Test(timeout = 1000)
	public void testGameGetterLogicAllMonsters() throws Exception {
		Object gameObject = createGameForTesting();
		ArrayList<Object> value = new ArrayList<>();
		value.add(createDasher());
		value.add(createSchemer());
		testGetterLogic(gameObject, "allMonsters", value);
	}

	@Test(timeout = 1000)
	public void testGameGetterLogicPlayer() throws Exception {
		Object gameObject = createGameForTesting();
		Object monsterValue = createDynamo();
		testGetterLogic(gameObject, "player", monsterValue);
	}


	@Test(timeout = 1000)
	public void testGameGetterLogicCurrent() throws Exception {
		Object gameObject = createGameForTesting();
		Object monsterValue = createSchemer();
		testGetterLogic(gameObject, "current", monsterValue);
	}

	@Test(timeout = 1000)
	public void testGameSetterLogicCurrent() throws Exception {
		Object gameObject = createGameForTesting();
		Object monsterValue = createDasher();
		testSetterLogic(gameObject, "current", monsterValue, monsterValue, Class.forName(monsterPath));
	}

	// selectRandomMonsterByRole: private, returns monster by role, randomizes when multiple match


	@Test(timeout = 1000)
	public void testGameSelectRandomMonsterByRoleReturnsMonsterWithCorrectRole() throws Exception {
		Object gameObject = createGameForTesting();
		Object scarerRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
		Object laugherRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "LAUGHER");
		Method setRole = Class.forName(monsterPath).getMethod("setRole", Class.forName(rolePath));
		ArrayList<Object> monsters = new ArrayList<>();
		Object m1 = createDasher();
		setRole.invoke(m1, scarerRole);
		Object m2 = createDynamo();
		setRole.invoke(m2, scarerRole);
		Object m3 = createSchemer();
		setRole.invoke(m3, laugherRole);
		Object m4 = createMultiTasker();
		setRole.invoke(m4, laugherRole);
		monsters.add(m1);
		monsters.add(m2);
		monsters.add(m3);
		monsters.add(m4);
		Field allMonstersField = Class.forName(gamePath).getDeclaredField("allMonsters");
		allMonstersField.setAccessible(true);
		allMonstersField.set(gameObject, monsters);
		Method selectMethod = Class.forName(gamePath).getDeclaredMethod("selectRandomMonsterByRole", Class.forName(rolePath));
		selectMethod.setAccessible(true);
		Method getRole = Class.forName(monsterPath).getMethod("getRole");
		Object resultScarer = selectMethod.invoke(gameObject, scarerRole);
		assertTrue("selectRandomMonsterByRole(SCARER) should return a monster.", resultScarer != null);
		assertEquals("Returned monster should have role SCARER.", scarerRole, getRole.invoke(resultScarer));
		Object resultLaugher = selectMethod.invoke(gameObject, laugherRole);
		assertTrue("selectRandomMonsterByRole(LAUGHER) should return a monster.", resultLaugher != null);
		assertEquals("Returned monster should have role LAUGHER.", laugherRole, getRole.invoke(resultLaugher));
	}

	@Test(timeout = 2000)
	public void testGameSelectRandomMonsterByRoleRandomizesWhenMultipleMatch() throws Exception {
		Object gameObject = createGameForTesting();
		Object scarerRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
		Method setRole = Class.forName(monsterPath).getMethod("setRole", Class.forName(rolePath));
		ArrayList<Object> monstersWithScarer = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			Object m = createDynamo();
			setRole.invoke(m, scarerRole);
			monstersWithScarer.add(m);
		}
		Field allMonstersField = Class.forName(gamePath).getDeclaredField("allMonsters");
		allMonstersField.setAccessible(true);
		allMonstersField.set(gameObject, monstersWithScarer);
		Method selectMethod = Class.forName(gamePath).getDeclaredMethod("selectRandomMonsterByRole", Class.forName(rolePath));
		selectMethod.setAccessible(true);
		Set<Object> distinctResults = new HashSet<>();
		int iterations = 80;
		for (int i = 0; i < iterations; i++) {
			Object result = selectMethod.invoke(gameObject, scarerRole);
			assertTrue("Each call should return a monster.", result != null);
			distinctResults.add(result);
		}
		assertTrue("selectRandomMonsterByRole should return different monsters when multiple match (randomization). Got " + distinctResults.size() + " distinct over " + iterations + " calls.", distinctResults.size() >= 2);
	}






	////////////////////////////////////////////Helper Methods specific for objects//////////////////////////////////////////////////////////////



	private void testInstanceVariableIsPublic(Class aClass, String varName) throws NoSuchFieldException, SecurityException {
		boolean thrown = false;
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		}catch(NoSuchFieldException e){
			thrown = true;
		}
		if(!thrown) {
			boolean isPublic = (Modifier.isPublic(f.getModifiers()));
			assertTrue("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
			+ " should be accessible accross all packages.", isPublic);

		}
		else {
			assertFalse("There should be \"" + varName + "\" instance variable in class " + aClass.getSimpleName() + ".",thrown);
		}

	}

	////////////////////////////////////////////	Helper Methods//////////////////////////////////////////////////////////////


	private void testInstanceVariableOfType(Class aClass, String varName,
			Class expectedType) {
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		} catch (NoSuchFieldException e) {
			return;
		}
		Class varType = f.getType();
		assertEquals("The attribute " + varName
				+ " should be of the type " + expectedType.getSimpleName(),
				expectedType, varType);
	}
	private Object createGameForTesting() throws Exception {
		savingCardsCSV();
		savingMonstersCSV();
		try {
			writeCardsCSVForDataLoader();
			writeMonstersCSVForDataLoader();
			Object role = Enum.valueOf((Class<Enum>) Class.forName(rolePath), "SCARER");
			Constructor<?> gameConstructor = Class.forName(gamePath).getConstructor(Class.forName(rolePath));
			return gameConstructor.newInstance(role);
		} finally {
			reWriteCardsCSVForLoadCards();
			reWriteMonstersCSVForLoadCards();
		}
	}

	private Object createSwapperCard() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		Constructor<?> c = Class.forName(swapperCardPath).getConstructor(String.class, String.class, int.class);
		return c.newInstance(name, description, Integer.valueOf(rarity));
	}

	private Object createStartOverCard() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		boolean lucky = new Random().nextBoolean();
		Constructor<?> c = Class.forName(startOverCardPath).getConstructor(String.class, String.class, int.class, boolean.class);
		return c.newInstance(name, description, Integer.valueOf(rarity), Boolean.valueOf(lucky));
	}

	private Object createShieldCard() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		Constructor<?> c = Class.forName(shieldCardPath).getConstructor(String.class, String.class, int.class);
		return c.newInstance(name, description, Integer.valueOf(rarity));
	}

	private Object createEnergyStealCard() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		int energy = 1 + new Random().nextInt(200);
		Constructor<?> c = Class.forName(energyStealCardPath).getConstructor(String.class, String.class, int.class, int.class);
		return c.newInstance(name, description, Integer.valueOf(rarity), Integer.valueOf(energy));
	}

	private Object createConfusionCard() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		int duration = 1 + new Random().nextInt(5);
		Constructor<?> c = Class.forName(confusionCardPath).getConstructor(String.class, String.class, int.class, int.class);
		return c.newInstance(name, description, Integer.valueOf(rarity), Integer.valueOf(duration));
	}

	private Object randomRole() throws ClassNotFoundException {
		String value = new Random().nextBoolean() ? "SCARER" : "LAUGHER";
		return Enum.valueOf((Class<Enum>) Class.forName(rolePath), value);
	}

	private Object createDasher() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(dasherPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	private Object createDynamo() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	private Object createMultiTasker() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(multiTaskerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	private Object createSchemer() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(schemerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	// 
	private Object createCell() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Constructor<?> c = Class.forName(cellPath).getConstructor(String.class);
		return c.newInstance(name);
	}

	private Object createDoorCell() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Object role = randomRole();
		int energy = new Random().nextInt(201);
		Constructor<?> c = Class.forName(doorCellPath).getConstructor(String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, role, Integer.valueOf(energy));
	}

	private Object createCardCell() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Constructor<?> c = Class.forName(cardCellPath).getConstructor(String.class);
		return c.newInstance(name);
	}

	private Object createMonsterCell() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Object cellMonster = createDasher();
		Constructor<?> c = Class.forName(monsterCellPath).getConstructor(String.class, Class.forName(monsterPath));
		return c.newInstance(name, cellMonster);
	}

	private Object createConveyorBelt() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		int effect = 1 + new Random().nextInt(20);
		Constructor<?> c = Class.forName(conveyorBeltPath).getConstructor(String.class, int.class);
		return c.newInstance(name, Integer.valueOf(effect));
	}

	private Object createContaminationSock() throws Exception {
		String name = generateRandomString(5 + new Random().nextInt(11));
		int effect = -1 - new Random().nextInt(20);
		Constructor<?> c = Class.forName(contaminationSockPath).getConstructor(String.class, int.class);
		return c.newInstance(name, Integer.valueOf(effect));
	}

	private void testExceptionConstructorInitialization(Object createdObject, String[] names, Object[] values)
			throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {

		for (int i = 0; i < names.length; i++) {

			Field f = null;
			Class<?> curr = createdObject.getClass();
			String currName = names[i];
			Object currValue = values[i];

			while (f == null) {

				if (curr == Object.class)
					fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
							+ currName + "\".");
				try {
					f = curr.getDeclaredField(currName);
				} catch (NoSuchFieldException e) {
					curr = curr.getSuperclass();
				}
			}

			//f.setAccessible(true);
			Method messageGetter=curr.getDeclaredMethod("getMessage");
			messageGetter.setAccessible(true);
			Object message= messageGetter.invoke(createdObject);


			assertEquals(
					"The constructor of the " + createdObject.getClass().getSimpleName()
					+ " class should initialize the instance variable \"" + "message" + "\" correctly.",
					currValue, message);

		}
	}



	private static void savingCardsCSV() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("cards.csv"));
		cards_csv= new ArrayList<>();
		while (br.ready()) {
			String nextLine = br.readLine();
			cards_csv.add(nextLine);
		}
	}
	private static void savingCellsCSV() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("cells.csv"));
		cells_csv= new ArrayList<>();
		while (br.ready()) {
			String nextLine = br.readLine();
			cells_csv.add(nextLine);
		}
	}
	private static void savingMonstersCSV() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("monsters.csv"));
		monsters_csv= new ArrayList<>();
		while (br.ready()) {
			String nextLine = br.readLine();
			monsters_csv.add(nextLine);
		}
	}


	private static void reWriteCardsCSVForLoadCards() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("cards.csv");

		for (int i = 0; i < cards_csv.size(); i++) {
			csvWriter.println(cards_csv.get(i));
		}
		csvWriter.flush();
		csvWriter.close();

	}
	private static void reWriteCellsCSVForLoadCards() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("cells.csv");

		for (int i = 0; i < cells_csv.size(); i++) {
			csvWriter.println(cells_csv.get(i));
		}
		csvWriter.flush();
		csvWriter.close();

	}
	private static void reWriteMonstersCSVForLoadCards() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("monsters.csv");

		for (int i = 0; i < monsters_csv.size(); i++) {
			csvWriter.println(monsters_csv.get(i));
		}
		csvWriter.flush();
		csvWriter.close();

	}

	private ArrayList<String> writeCardsCSVForDataLoader() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("cards.csv");
		ArrayList<String> list = new ArrayList<>();
		csvWriter.println("SWAPPER,Position Swap,Swap places with opponent if behind,4");
		list.add("SWAPPER,Position Swap,Swap places with opponent if behind,4");
		csvWriter.println("SHIELD,Super Shield,Block next negative effect,5");
		list.add("SHIELD,Super Shield,Block next negative effect,5");
		csvWriter.println("ENERGYSTEAL,Small Snatcher,Steal 50 energy from opponent,3,50");
		list.add("ENERGYSTEAL,Small Snatcher,Steal 50 energy from opponent,3,50");
		csvWriter.println("ENERGYSTEAL,Sneaky Thief,Steal 100 energy from opponent,2,100");
		list.add("ENERGYSTEAL,Sneaky Thief,Steal 100 energy from opponent,2,100");
		csvWriter.println("STARTOVER,Contamination Code,Player return to first cell,2,false");
		list.add("STARTOVER,Contamination Code,Player return to first cell,2,false");
		csvWriter.println("STARTOVER,2319 Alert,Opponent returns to first cell,3,true");
		list.add("STARTOVER,2319 Alert,Opponent returns to first cell,3,true");
		csvWriter.println("CONFUSION,Mind Scramble,Both players confused for 2 turns,3,2");
		list.add("CONFUSION,Mind Scramble,Both players confused for 2 turns,3,2");
		csvWriter.println("CONFUSION,Total Confusion,Both players confused for 3 turns,2,3");
		list.add("CONFUSION,Total Confusion,Both players confused for 3 turns,2,3");
		csvWriter.flush();
		csvWriter.close();
		return list;
	}

	private ArrayList<String> writeCellsCSVForDataLoader() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("cells.csv");
		ArrayList<String> list = new ArrayList<>();
		csvWriter.println("Belt1,6");
		list.add("Belt1,6");
		csvWriter.println("Belt2,22");
		list.add("Belt2,22");
		csvWriter.println("Sock1,-32");
		list.add("Sock1,-32");
		csvWriter.println("Sock2,-42");
		list.add("Sock2,-42");
		csvWriter.println("DoorScarer,SCARER,50");
		list.add("DoorScarer,SCARER,50");
		csvWriter.println("DoorLaugher,LAUGHER,50");
		list.add("DoorLaugher,LAUGHER,50");
		csvWriter.flush();
		csvWriter.close();
		return list;
	}

	private ArrayList<String> writeMonstersCSVForDataLoader() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("monsters.csv");
		ArrayList<String> list = new ArrayList<>();
		csvWriter.println("DASHER,Mike Wazowski,Fast and funny the comedy speedster,LAUGHER,100");
		list.add("DASHER,Mike Wazowski,Fast and funny the comedy speedster,LAUGHER,100");
		csvWriter.println("DYNAMO,James P. Sullivan,The top scarer powerful and confident,SCARER,300");
		list.add("DYNAMO,James P. Sullivan,The top scarer powerful and confident,SCARER,300");
		csvWriter.println("SCHEMER,Randall Boggs,Sneaky and cunning always has an angle,SCARER,20");
		list.add("SCHEMER,Randall Boggs,Sneaky and cunning always has an angle,SCARER,20");
		csvWriter.println("MULTITASKER,Celia Mae,Organized receptionist handles everything,LAUGHER,50");
		list.add("MULTITASKER,Celia Mae,Organized receptionist handles everything,LAUGHER,50");
		csvWriter.println("DASHER,Fungus,Timid assistant quick but nervous,LAUGHER,50");
		list.add("DASHER,Fungus,Timid assistant quick but nervous,LAUGHER,50");
		csvWriter.println("DYNAMO,Yeti,Banished snow monster surprisingly cheerful,LAUGHER,100");
		list.add("DYNAMO,Yeti,Banished snow monster surprisingly cheerful,LAUGHER,100");
		csvWriter.flush();
		csvWriter.close();
		return list;
	}


	private void testIsEnum(Class eClass) {
		assertTrue(eClass.getSimpleName() + " should be an Enum", eClass.isEnum());
	}

	private void testEnumValues(String path, String name, String[] values) {
		try {
			Class eClass = Class.forName(path);
			for(int i=0;i<values.length;i++) {
				try {
					Enum.valueOf((Class<Enum>)eClass, values[i]);
				}
				catch(IllegalArgumentException e) {
					fail(eClass.getSimpleName() + " enum can be " + values[i]);
				}
			}
		}
		catch(ClassNotFoundException e) {
			fail("There should be an enum called " + name + "in package " + path);
		}

	}




	private void testGetterLogicWallBase(Object createdObject, String name,String name2, Object value) throws Exception {

		Field f = null;
		Class curr = createdObject.getClass();

		while (f == null) {

			if (curr == Object.class)
				fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
						+ name + "\".");
			try {
				f = curr.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}

		}

		f.setAccessible(true);


		Character c = name.charAt(0);

		String methodName = "get" + name2;

		if (value.getClass().equals(Boolean.class))
			methodName = "is" + Character.toUpperCase(c) + name.substring(1, name.length());

		Method m = createdObject.getClass().getMethod(methodName);
		assertEquals(
				"The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
				+ " should return the correct value of variable \"" + name + "\".",
				value, m.invoke(createdObject));

	}

	// NEW


	private void testGameConstructorInitialization(Object createdObject, String[] names, Object[] values) throws IllegalArgumentException, IllegalAccessException {
		for(int i=0;i<names.length;i++) {
			Class curr = createdObject.getClass();
			String currName = names[i];
			Object currValue = values[i];

			Field f = null;
			while(f == null) {
				if (curr == Object.class)
					fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
							+ currName + "\".");
				try {
					f = curr.getDeclaredField(currName);
				} catch (NoSuchFieldException e) {
					curr = curr.getSuperclass();
				}
			}
			f.setAccessible(true);
			if(currName.equals("firepit")) {
				ArrayList<Object> w = (ArrayList<Object>) f.get(createdObject);
				assertEquals("The constructor of the " + createdObject.getClass().getSimpleName()
						+ " class should initialize the instance variable \"" + currName
						+ "\" correctly. the size of weapons should be equals to the 0 initially.", 
						currValue, w.size());
			}	
			else	
				assertEquals("The constructor of the " + createdObject.getClass().getSimpleName()
						+ " class should initialize the instance variable \"" + currName + "\" correctly.", 
						currValue, f.get(createdObject));

		}
	}





	private void testIsGetterMethodLogic(Object createdObject, String varName, Object expectedValue) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, InvocationTargetException {
		Field f = null;
		Class curr = createdObject.getClass();

		while(f == null) {
			if(curr == Object.class)
				fail("Class should have " + varName + " as an instance variable in class " + curr.getSimpleName()
				+" or one of its superclasses");
			try {
				f = curr.getDeclaredField(varName);
			}
			catch(NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}
		}

		f.setAccessible(true);
		f.set(createdObject, expectedValue);

		String methodName = "";
		methodName = "is" + Character.toUpperCase(varName.charAt(0)) + varName.substring(1);

		Method m = createdObject.getClass().getDeclaredMethod(methodName);
		m.invoke(createdObject);
		assertTrue("The method \"" + methodName
				+ "\" in class "+ curr.getSimpleName()+" should return the correct value of variable \"" + varName + "\"."
				, m.invoke(createdObject).equals(expectedValue));
	}

	private ArrayList<String> writeStandartCSVForLoadCards() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("Cards.csv");

		//	code, frequency, name, description, rank, suit

		ArrayList<String> list_= new ArrayList<>();
		csvWriter.println("7,3,Seven,Moves 7 steps on one marble or a total of 7 steps on two marbles,7,HEART");
		list_.add("7,3,Seven,Moves 7 steps on one marble or a total of 7 steps on two marbles,7,HEART");


		csvWriter.println("0,2,Eight,Moves 8 steps.,8,DIAMOND");
		list_.add("0,2,Eight,Moves 8 steps.,8,DIAMOND");

		csvWriter.println("13,5,King,Places a marble in the base hole or moves 13 steps killing all marbles in their path.,13,DIAMOND");
		list_.add("13,5,King,Places a marble in the base hole or moves 13 steps killing all marbles in their path.,13,DIAMOND");

		csvWriter.println("0,5,Three,Moves 3 steps.,3,HEART");
		list_.add("0,5,Three,Moves 3 steps.,3,HEART");

		csvWriter.println("0,4,Six,Moves 6 steps.,6,CLUB");
		list_.add("0,4,Six,Moves 6 steps.,6,CLUB");

		csvWriter.println("1,4,Ace,Places a marble in the base hole or moves 1 step.,1,HEART");
		list_.add("1,4,Ace,Places a marble in the base hole or moves 1 step.,1,HEART");

		csvWriter.println("12,3,Queen,Selects a random card from next player to discard or moves 12 steps.,12,DIAMOND");
		list_.add("12,3,Queen,Selects a random card from next player to discard or moves 12 steps.,12,DIAMOND");

		csvWriter.println("0,2,Two,Moves 3 steps.,2,HEART");
		list_.add("0,2,Two,Moves 3 steps.,2,HEART");

		csvWriter.println("11,5,Jack,Swaps your own marble with another or moves 11 steps.,11,HEART");
		list_.add("11,5,Jack,Swaps your own marble with another or moves 11 steps.,11,HEART");

		csvWriter.println("0,5,Nine,Move9 steps.,9,HEART");
		list_.add("0,5,Nine,Move9 steps.,9,HEART");

		csvWriter.println("4,5,Four,Move4 steps.,4,HEART");
		list_.add("4,5,Four,Move4 steps.,4,HEART");

		csvWriter.println("5,1,Five,Move5 steps.,5,CLUB");
		list_.add("5,1,Five,Move5 steps.,5,CLUB");

		csvWriter.println("10,5,Ten,Move4 steps.,10,HEART");
		list_.add("10,5,Ten,Move4 steps.,10,HEART");

		csvWriter.flush();
		csvWriter.close();
		return list_;

	}
	private ArrayList<String> returnCSVOfCards() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("Cards.csv");

		//	code, frequency, name, description, rank, suit

		ArrayList<String> list_= new ArrayList<>();
		csvWriter.println("7,3,Seven,Moves 7 steps on one marble or a total of 7 steps on two marbles,7,HEART");
		list_.add("7,3,Seven,Moves 7 steps on one marble or a total of 7 steps on two marbles,7,HEART");


		csvWriter.println("0,2,Eight,Moves 8 steps.,8,DIAMOND");
		list_.add("0,2,Eight,Moves 8 steps.,8,DIAMOND");

		csvWriter.println("13,5,King,Places a marble in the base hole or moves 13 steps killing all marbles in their path.,13,DIAMOND");
		list_.add("13,5,King,Places a marble in the base hole or moves 13 steps killing all marbles in their path.,13,DIAMOND");

		csvWriter.println("0,5,Three,Moves 3 steps.,3,HEART");
		list_.add("0,5,Three,Moves 3 steps.,3,HEART");

		csvWriter.println("0,4,Six,Moves 6 steps.,6,CLUB");
		list_.add("0,4,Six,Moves 6 steps.,6,CLUB");

		csvWriter.println("1,4,Ace,Places a marble in the base hole or moves 1 step.,1,HEART");
		list_.add("1,4,Ace,Places a marble in the base hole or moves 1 step.,1,HEART");

		csvWriter.println("12,3,Queen,Selects a random card from next player to discard or moves 12 steps.,12,DIAMOND");
		list_.add("12,3,Queen,Selects a random card from next player to discard or moves 12 steps.,12,DIAMOND");

		csvWriter.println("0,2,Two,Moves 3 steps.,2,HEART");
		list_.add("0,2,Two,Moves 3 steps.,2,HEART");

		csvWriter.println("11,5,Jack,Swaps your own marble with another or moves 11 steps.,11,HEART");
		list_.add("11,5,Jack,Swaps your own marble with another or moves 11 steps.,11,HEART");

		csvWriter.println("0,5,Nine,Move9 steps.,9,HEART");
		list_.add("0,5,Nine,Move9 steps.,9,HEART");

		csvWriter.flush();
		csvWriter.close();
		return list_;

	}

	private ArrayList<String> writeCSVForLoadCards() throws FileNotFoundException {

		PrintWriter csvWriter = new PrintWriter("Cards.csv");
		ArrayList<String> cardsList=new ArrayList<>();
		int randomCount=(int) (Math.random()*(10)+1)+5;

		int[] standardCodes= {0,1,13,12,11,4,5,7,10};
		String[] standardNames= {"Standard","ACE","KING","QUEEN","JACK","FOUR","FIVE","SEVEN","TEN"};
		String[] suits= {"CLUB","HEART","DIAMOND","SPADE"};
		int[] ranks= {2,1,13,12,11,4,5,7,10};
		for (int i = 0; i < randomCount; i++) {

			int randomFreq=(int) (Math.random()*(10)+1)+5;
			int randomValue= (int) (Math.random()*2);
			//		0 means standard card 1 means wild card

			int randomWild= (int) (Math.random()*2);
			//		0 means burner Code 14, 1 means saver code 15

			if(randomValue==1) {
				if(randomWild==0) {

					String write_line= "14,"+randomFreq+",randomBurner,randomDescription inserting Random count"+
							randomCount+",,";
					csvWriter.println(write_line);
					cardsList.add(write_line);
				}
				else {

					String write_line= "15,"+randomFreq+",randomSaver,randomDescription inserting Random count"+
							randomCount+",,";
					csvWriter.println(write_line);
					cardsList.add(write_line);
				}
			}
			else {

				int randomSuitIndex=(int) (Math.random()*(suits.length));

				int randomIndex=(int) (Math.random()*(standardNames.length));


				String description= "genrating random description from suit "+suits[randomSuitIndex]+
						" and random standard card "+standardNames[randomIndex]+",";


				String write_line= standardCodes[randomIndex]+","+randomFreq+","+standardNames[randomIndex]+","+
						description+ranks[randomIndex]+","+suits[randomSuitIndex];

				csvWriter.println(write_line);
				cardsList.add(write_line);


			}
		}





		//	code, frequency, name, description, rank, suit


		csvWriter.flush();
		csvWriter.close();
		return cardsList;

	}

	private ArrayList<String> writeWildCardsCSVForLoadCards() throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter("Cards.csv");

		ArrayList<String> cardList= new ArrayList<>();
		//	code, frequency, name, description

		csvWriter.println("14,20,MarbleBurner,Selects an opponent marble, that is on track, to send home.,,");
		cardList.add("14,20,MarbleBurner,Selects an opponent marble, that is on track, to send home.,,");
		csvWriter.println("15,5,MarbleSaver,Selects one of your marbles, that is on track base, to send to a random empty safe zone cell.,,");
		cardList.add("15,5,MarbleSaver,Selects one of your marbles, that is on track base, to send to a random empty safe zone cell.,,\"");

		csvWriter.flush();
		csvWriter.close();

		return cardList;

	}





	// Helper method to generate a random string of specified length
	private String generateRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}
		return sb.toString();
	}


	private void testConstructorInitializationSafeZone(Object createdObject, String[] names, Object[] values)
			throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException {

		for (int i = 0; i < names.length; i++) {

			Field f = null;
			Class curr = createdObject.getClass();
			String currName = names[i];
			Object currValue = values[i];
			ArrayList<Object> h = (ArrayList<Object>) currValue;
			int len = h.size() ;


			while (f == null) {

				if (curr == Object.class)
					fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
							+ currName + "\".");
				try {
					f = curr.getDeclaredField(currName);
				} catch (NoSuchFieldException e) {
					curr = curr.getSuperclass();
				}

			}

			f.setAccessible(true);
			ArrayList<Object> h2 = (ArrayList<Object>) f.get(createdObject);
			int len2 = h2.size() ;


			assertEquals(
					"The constructor of the " + createdObject.getClass().getSimpleName()
					+ " class should initialize the instance variable \"" + currName + "\" with the correct size as required in the milestone.",
					len, len2);
		}

	}



	private void testLoadMethodExistsInClass(Class aClass, String methodName, Class inputType) {
		Method m = null;
		boolean found = true;
		try {
			m = aClass.getDeclaredMethod(methodName,inputType);
		} catch (NoSuchMethodException e) {
			found = false;
		}

		String varName = "";

		assertTrue(aClass.getSimpleName() + " class should have " + methodName + " method that takes one "
				+ inputType.getSimpleName() + " parameter.", found);

		assertTrue("Incorrect return type for " + methodName + " method in " + aClass.getSimpleName() + ".",
				m.getReturnType().equals(Void.TYPE));

	}


	private void testSetterAbsentInSubclasses(String varName,
			String[] subclasses) throws SecurityException,
	ClassNotFoundException {
		String methodName = "set" + varName.substring(0, 1).toUpperCase()
				+ varName.substring(1);
		boolean methodIsInSubclasses = false;
		for (String subclass : subclasses) {
			Method[] methods = Class.forName(subclass).getDeclaredMethods();
			methodIsInSubclasses = methodIsInSubclasses
					|| containsMethodName(methods, methodName);

		}
		assertFalse("The " + methodName
				+ " method should not be implemented in a subclasses.",
				methodIsInSubclasses);
	}
	private void testClassIsSubclass(Class subClass, Class superClass) {
		assertEquals(subClass.getSimpleName() + " class should be a subclass from " + superClass.getSimpleName() + ".",
				superClass, subClass.getSuperclass());
	}



	private void testClassIsSubClass(Class superClass, Class subClass) {
		assertEquals(subClass.getSimpleName() + " should be a subClass of Class : "+ superClass.getSimpleName(), 
				superClass, subClass.getSuperclass());
	}

	private static void testInstanceVariableOfTypeArrayList(Class<?> aClass, String varName, Class<?> expectedType) {
		try {
			Field field = aClass.getDeclaredField(varName);

			// Check if the field is of type ArrayList
			if (!ArrayList.class.isAssignableFrom(field.getType())) {
				fail("The attribute '" + varName + "' should be of type ArrayList<" + expectedType.getSimpleName() + ">");
			}

			// Check the generic type
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) genericType;
				Type[] actualTypeArguments = paramType.getActualTypeArguments();

				if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?>) {
					Class<?> actualType = (Class<?>) actualTypeArguments[0];

					assertEquals("The attribute '" + varName + "' should be of type ArrayList<" + expectedType.getSimpleName() + ">",
							expectedType, actualType);
					return; // Success
				}
			}

			fail("The attribute '" + varName + "' should be of type ArrayList<" + expectedType.getSimpleName() + ">");
		} catch (NoSuchFieldException e) {
			fail("Expected field '" + varName + "' was not found in class " + aClass.getSimpleName());
		}
	}


	private void testInstanceVariableIsProtected(Class aClass, String varName) {
		boolean thrown = false;
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		}
		catch(NoSuchFieldException e){
			thrown = true;
		}
		if(!thrown) {
			boolean isProtected = Modifier.isProtected(f.getModifiers());
			assertTrue(varName + " should be protected", isProtected);
		}
		else
			assertTrue("you should have" + varName + " as a protected variable", false);
	}


	private void testInterfaceMethod(Class iClass, String methodName, Class returnType, Class[] parameters) {
		Method[] methods = iClass.getDeclaredMethods();
		assertTrue(iClass.getSimpleName()+" interface should have " + methodName + "method", 
				containsMethodName(methods, methodName));

		Method m = null;
		boolean thrown = false;
		try {
			m = iClass.getDeclaredMethod(methodName,parameters);
		}
		catch(NoSuchMethodException e) {
			thrown = true;
		}

		assertTrue("Method " + methodName + " should have the following set of parameters : " + Arrays.toString(parameters),
				!thrown);
		assertTrue("wrong return type ",m.getReturnType().equals(returnType));

	}

	private void testIsInterface(Class iClass) {
		assertTrue(iClass.getSimpleName() + " should be interface",iClass.isInterface());
	}

	private void testClassImplementsInterface(Class aClass, Class iClass) {
		assertTrue(aClass.getSimpleName() +" should implement " + iClass.getSimpleName(), 
				iClass.isAssignableFrom(aClass));
	}



	private void testClassIsAbstract(Class aClass) {
		assertTrue(aClass.getSimpleName() + " should be an abtract class.", 
				Modifier.isAbstract(aClass.getModifiers()));
	}

	private void testSetterMethodLogic(Object createdObject, String varName, Object setValue, Class setType) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		Field f = null;
		Class curr = createdObject.getClass();

		while(f == null) {
			if(curr == Object.class)
				fail("There should be " + varName + " as an instance variable in class " + curr.getSimpleName()
				+" or one of its superclasses");
			try {
				f = curr.getDeclaredField(varName);
			}
			catch(NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}
		}

		f.setAccessible(true);
		String MethodName = "set" + Character.toUpperCase(varName.charAt(0)) + varName.substring(1);
		Method m = null;
		try {
			m = curr.getDeclaredMethod(MethodName, setType);
		}
		catch(NoSuchMethodException e) {
			assertTrue("No such method",false);
		}
		m.invoke(createdObject, setValue);
		if(f.getType().equals(int.class) && (int)setValue < 0 && varName.equals("currentHealth")) 
			assertEquals("current health should not be less than 0", 0, f.get(createdObject));
		else

			assertEquals("The method \"" + MethodName + "\" in class " + createdObject.getClass().getSimpleName()
					+ " should set the correct value of variable \"", setValue, f.get(createdObject));
	}

	private void testGetterMethodLogic(Object createdObject, String varName, Object expectedValue) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, InvocationTargetException {
		Field f = null;
		Class curr = createdObject.getClass();

		while(f == null) {
			if(curr == Object.class)
				fail("There should be " + varName + " as an instance variable in class " + curr.getSimpleName()
				+" or one of its superclasses");
			try {
				f = curr.getDeclaredField(varName);
			}
			catch(NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}
		}

		f.setAccessible(true);
		f.set(createdObject, expectedValue);

		String methodName = "";
		if(expectedValue.getClass().equals(boolean.class))
			methodName = "is" + Character.toUpperCase(varName.charAt(0)) + varName.substring(1);
		else
			methodName = "get" + Character.toUpperCase(varName.charAt(0)) + varName.substring(1);

		Method m = createdObject.getClass().getDeclaredMethod(methodName);
		m.invoke(createdObject);
		assertTrue("The method \"" + methodName
				+ "\" in class Character should return the correct value of variable \"" + varName + "\"."
				, m.invoke(createdObject).equals(expectedValue));
	}

	private void testSetterMethodIsAbsentInClass(Class aClass, String methodName) {
		Method[] methods = aClass.getDeclaredMethods();
		String varName = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		assertTrue(varName + "should not have a setter", !containsMethodName(methods, methodName));
	}
	private void testGetterMethodIsAbsentInClass(Class aClass, String methodName) {
		Method[] methods = aClass.getDeclaredMethods();
		String varName = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		assertTrue(varName + "should not have a getter", !containsMethodName(methods, methodName));
	}

	private void testSetterMethodExistInClass(Class aClass, String methodName, Class setType) {
		//first check if the method exists
		Method[] methods = aClass.getDeclaredMethods();
		String varName = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		assertTrue(varName + "should have a setter", containsMethodName(methods, methodName));

		//second check if takes a parameter or not
		Method m = null;
		boolean thrown = false;
		try {
			m = aClass.getDeclaredMethod(methodName,setType);
		}
		catch(NoSuchMethodException e) {
			thrown = true;
		}
		assertTrue(methodName + " method should take a parameter of type : " + setType, !thrown);

		//finally check if it is void
		assertTrue(methodName +" method should be void",m.getReturnType().equals(void.class));

	}


	private void testGetterMethodExistInClass(Class aClass, String methodName, Class returnType) {
		Method m = null;
		boolean thrown = false;
		try {
			m = aClass.getDeclaredMethod(methodName);
		}catch(NoSuchMethodException e) {
			thrown = true;
		}
		String varName = "";
		if(m.getReturnType().equals(boolean.class))
			varName = methodName.substring(2,3).toLowerCase() + methodName.substring(3);
		else
			varName = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		if(!thrown) {
			assertTrue("Incorrect return type for " + methodName + " method in " + aClass.getSimpleName() + " class.",
					m.getReturnType().equals(returnType));
		}
		else
			assertTrue("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
			+ " is not a READ variable.", false);
	}



	private void testInstanceVariableIsStatic(Class aClass, String varName) {
		boolean thrown = false;
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		}
		catch(NoSuchFieldException e){
			thrown = true;
		}
		if(!thrown) {
			boolean isStatic = Modifier.isStatic(f.getModifiers());
			assertTrue(varName + " should be static", isStatic);
		}
		else
			assertTrue("There should be " + varName + " as a static variable", false);
	}
	private void testInstanceVariableIsNotStatic(Class aClass, String varName) {
		boolean thrown = false;
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		}
		catch(NoSuchFieldException e){
			thrown = true;
		}
		if(!thrown) {
			boolean isStatic = Modifier.isStatic(f.getModifiers());
			assertFalse(varName + " should not be static", isStatic);
		}
		else
			assertFalse("There should not be " + varName + " as a static variable", false);
	}



	private void testInstanceVariableIsPresent(Class aClass, String varName) throws SecurityException {
		boolean thrown = false;
		try {
			aClass.getDeclaredField(varName);
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("There should be \"" + varName + "\" instance variable in class " + aClass.getSimpleName() + ".",thrown);
	}

	private void testInstanceVariableIsNotPresent(Class aClass, String varName) throws SecurityException {
		boolean thrown = false;
		try {
			aClass.getDeclaredField(varName);
		}catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertTrue("There should not be \"" + varName + "\" instance variable in class " + aClass.getSimpleName() + ".", thrown);
	}

	private void testInstanceVariableOfTypeDoubleArray(Class aClass, String varName, Class expectedType) {
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		} catch (NoSuchFieldException e) {
			return;
		}
		Class varType = f.getType();
		assertEquals(
				"the attribute: " + varType.getSimpleName() + " should be of the type: " + expectedType.getSimpleName(),
				expectedType.getTypeName() + "[][]", varType.getTypeName());
	}

	private void testInstanceVariableIsPresent(Class aClass, String varName, boolean implementedVar)
			throws SecurityException {
		boolean thrown = false;
		try {
			aClass.getDeclaredField(varName);
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		if (implementedVar) {
			assertFalse(
					"There should be \"" + varName + "\" instance variable in class " + aClass.getSimpleName() + ".",
					thrown);
		} else {
			assertTrue("The instance variable \"" + varName + "\" should not be declared in class "
					+ aClass.getSimpleName() + ".", thrown);
		}
	}



	private void testInstanceVariableIsPrivate(Class aClass, String varName) throws NoSuchFieldException, SecurityException {
		boolean thrown = false;
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		}catch(NoSuchFieldException e){
			thrown = true;
		}
		if(!thrown) {
			boolean isPrivate = (Modifier.isPrivate(f.getModifiers()));
			assertTrue("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
			+ " should not be accessed outside that class.", isPrivate);

		}
		else {
			assertFalse("There should be \"" + varName + "\" instance variable in class " + aClass.getSimpleName() + ".",thrown);
		}

	}



	private void testInstanceVariableIsFinal(Class aClass, String varName) {
		boolean thrown = false;
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		}
		catch(NoSuchFieldException e){
			thrown = true;
		}
		if(!thrown) {
			boolean isFinal = Modifier.isFinal(f.getModifiers());
			assertTrue(varName + " should be final", isFinal);
		}
		else
			assertTrue("There should have" + varName + " as a final variable", false);
	}
	private void testInstanceVariableIsNotFinal(Class aClass, String varName) {
		boolean thrown = false;
		Field f = null;
		try {
			f = aClass.getDeclaredField(varName);
		}
		catch(NoSuchFieldException e){
			thrown = true;
		}
		if(!thrown) {
			boolean isFinal = Modifier.isFinal(f.getModifiers());
			assertFalse(varName + " should not be final", isFinal);
		}
		else
			assertFalse("There should have" + varName + " as a not final variable", false);
	}


	private void testGetterMethodExistsInClass(Class aClass, String methodName, Class returnedType,
			boolean readvariable) {
		Method m = null;
		boolean found = true;
		try {
			m = aClass.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
			found = false;
		}

		String varName = "";
		if (returnedType == boolean.class)
			varName = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
		else
			varName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
		if (readvariable) {
			assertTrue("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
			+ " is a READ variable.", found);
			assertTrue("Incorrect return type for " + methodName + " method in " + aClass.getSimpleName() + " class.",
					m.getReturnType().isAssignableFrom(returnedType));
		} else {
			assertFalse("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
			+ " is not a READ variable.", found);
		}

	}

	private void testGetterLogic(Object createdObject, String name, Object value) throws Exception {

		Field f = null;
		Class curr = createdObject.getClass();

		while (f == null) {

			if (curr == Object.class)
				fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
						+ name + "\".");
			try {
				f = curr.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}

		}

		f.setAccessible(true);
		f.set(createdObject, value);

		Character c = name.charAt(0);

		String methodName = "get" + Character.toUpperCase(c) + name.substring(1, name.length());

		if (value.getClass().equals(Boolean.class))
			methodName = "is" + Character.toUpperCase(c) + name.substring(1, name.length());

		Method m = createdObject.getClass().getMethod(methodName);
		assertEquals(
				"The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
				+ " should return the correct value of variable \"" + name + "\".",
				value, m.invoke(createdObject));

	}

	private static boolean containsMethodName(Method[] methods, String name) {
		for (Method method : methods) {
			if (method.getName().equals(name))
				return true;
		}
		return false;
	}


	private void testSetterMethodExistsInClass(Class aClass, String methodName, Class inputType,
			boolean writeVariable) {

		Method[] methods = aClass.getDeclaredMethods();
		String varName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
		if (writeVariable) {
			assertTrue("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
			+ " is a WRITE variable.", containsMethodName(methods, methodName));
		} else {
			assertFalse("The \"" + varName + "\" instance variable in class " + aClass.getSimpleName()
			+ " is not a WRITE variable.", containsMethodName(methods, methodName));
			return;
		}
		Method m = null;
		boolean found = true;
		try {
			m = aClass.getDeclaredMethod(methodName, inputType);
		} catch (NoSuchMethodException e) {
			found = false;
		}

		assertTrue(aClass.getSimpleName() + " class should have " + methodName + " method that takes one "
				+ inputType.getSimpleName() + " parameter.", found);

		assertTrue("Incorrect return type for " + methodName + " method in " + aClass.getSimpleName() + ".",
				m.getReturnType().equals(Void.TYPE));

	}

	private void testSetterLogic(Object createdObject, String name, Object setValue, Object expectedValue, Class type)
			throws Exception {

		Field f = null;
		Class curr = createdObject.getClass();

		while (f == null) {

			if (curr == Object.class)
				fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
						+ name + "\".");
			try {
				f = curr.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}

		}

		f.setAccessible(true);

		Character c = name.charAt(0);
		String methodName = "set" + Character.toUpperCase(c) + name.substring(1, name.length());
		Method m = createdObject.getClass().getMethod(methodName, type);
		m.invoke(createdObject, setValue);
		if (name == "currentActionPoints" || name == "currentHP") {
			if ((int) setValue > (int) expectedValue) {
				assertEquals("The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
						+ " should set the correct value of variable \"" + name
						+ "\". It should not exceed the maximum value.", expectedValue, f.get(createdObject));
			} else if ((int) setValue == -1 && (int) expectedValue == 0) {
				assertEquals("The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
						+ " should set the correct value of variable \"" + name
						+ "\". It should not be less than zero.", expectedValue, f.get(createdObject));
			} else {
				assertEquals(
						"The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
						+ " should set the correct value of variable \"" + name + "\".",
						expectedValue, f.get(createdObject));
			}
		} else {
			assertEquals(
					"The method \"" + methodName + "\" in class " + createdObject.getClass().getSimpleName()
					+ " should set the correct value of variable \"" + name + "\".",
					expectedValue, f.get(createdObject));
		}
	}


	private void testConstructorExists(Class aClass, Class[] inputs) {
		boolean thrown = false;
		try {
			aClass.getConstructor(inputs);
		} catch (NoSuchMethodException e) {
			thrown = true;
		}

		if (inputs.length > 0) {
			String msg = "";
			int i = 0;
			do {
				msg += inputs[i].getSimpleName() + " and ";
				i++;
			} while (i < inputs.length);

			msg = msg.substring(0, msg.length() - 4);

			assertFalse(
					"Missing constructor with " + msg + " parameter" + (inputs.length > 1 ? "s" : "") + " in "
							+ aClass.getSimpleName() + " class.",

							thrown);
		} else
			assertFalse("Missing constructor with zero parameters in " + aClass.getSimpleName() + " class.",

					thrown);

	}

	private void testConstructorInitialization(Object createdObject, String[] names, Object[] values)
			throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException {

		for (int i = 0; i < names.length; i++) {

			Field f = null;
			Class curr = createdObject.getClass();
			String currName = names[i];
			Object currValue = values[i];

			while (f == null) {

				if (curr == Object.class)
					fail("Class " + createdObject.getClass().getSimpleName() + " should have the instance variable \""
							+ currName + "\".");
				try {
					f = curr.getDeclaredField(currName);
				} catch (NoSuchFieldException e) {
					curr = curr.getSuperclass();
				}
			}
			f.setAccessible(true);

			assertEquals(
					"The constructor of the " + createdObject.getClass().getSimpleName()
					+ " class should initialize the instance variable \"" + currName + "\" correctly.",
					currValue, f.get(createdObject));

		}

	}


	private void testGetterLogic(Object createdObject, String name, Object value,String currentMethodName)
			throws Exception {

		Field f = null;
		Class curr = createdObject.getClass();

		while (f == null) {

			if (curr == Object.class)
				fail("Class " + createdObject.getClass().getSimpleName()
						+ " should have the instance variable \"" + name
						+ "\".");
			try {
				f = curr.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				curr = curr.getSuperclass();
			}

		}

		f.setAccessible(true);
		f.set(createdObject, value);

		Character c = name.charAt(0);

		String methodName = currentMethodName;

		if (value.getClass().equals(Boolean.class)
				&& !name.substring(0, 2).equals("is"))
			methodName = "is" + Character.toUpperCase(c)
			+ name.substring(1, name.length());
		else if (value.getClass().equals(Boolean.class)
				&& name.substring(0, 2).equals("is"))
			methodName = "is" + Character.toUpperCase(name.charAt(2))
			+ name.substring(3, name.length());

		Method m = createdObject.getClass().getMethod(methodName);
		assertEquals("The method \"" + methodName + "\" in class "
				+ createdObject.getClass().getSimpleName()
				+ " should return the correct value of variable \"" + name
				+ "\".", value, m.invoke(createdObject));

	}

	private void testSetterAbsent(String varName,
			String[] subclasses) throws SecurityException,
	ClassNotFoundException {
		String methodName = "set" + varName.substring(0, 1).toUpperCase()
				+ varName.substring(1);
		boolean methodIsInSubclasses = false;
		for (String subclass : subclasses) {
			Method[] methods = Class.forName(subclass).getDeclaredMethods();
			methodIsInSubclasses = methodIsInSubclasses
					|| containsMethodName(methods, methodName);

		}
		assertFalse("The " + methodName
				+ " method should not be implemented.",
				methodIsInSubclasses);
	}


	private void testGetterAbsentInSubclasses(String varName,
			String[] subclasses) throws SecurityException,
	ClassNotFoundException {
		String methodName = "get" + varName.substring(0, 1).toUpperCase()
				+ varName.substring(1);
		boolean methodIsInSubclasses = false;
		for (String subclass : subclasses) {
			Method[] methods = Class.forName(subclass).getDeclaredMethods();
			methodIsInSubclasses = methodIsInSubclasses
					|| containsMethodName(methods, methodName);

		}
		assertFalse("The " + methodName
				+ " method should not be implemented in a subclasses.",
				methodIsInSubclasses);
	}


	private void testAttributeExistence(String givenAttributeName,String classPath) throws ClassNotFoundException {
		Class givenClass = Class.forName(classPath);
		String attributeName = givenAttributeName;
		try {
			Field field = givenClass.getDeclaredField(givenAttributeName);

			assertTrue("Attribute " + attributeName + " should exist in class " + givenClass.getSimpleName(),
					field != null);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}


	private void testClassExists(String classPath){
		Class expectedClass = null;
		try {
			expectedClass = Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			fail(expectedClass.getSimpleName()+" Class does not exist");
		}
	}

	private boolean compare2Monsters(Object Monster_1, Object Monster_2)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException {

		Class monster_class = Class.forName(monsterPath);

		Field name_field_in_monster = monster_class.getDeclaredField("name");
		name_field_in_monster.setAccessible(true);
		String name_1 = (String) name_field_in_monster.get(Monster_1);
		String name_2 = (String) name_field_in_monster.get(Monster_2);

		Field description_field_in_monster = monster_class
				.getDeclaredField("description");
		description_field_in_monster.setAccessible(true);
		String description_1 = (String) description_field_in_monster
				.get(Monster_1);
		String description_2 = (String) description_field_in_monster
				.get(Monster_2);

		Field energy_field_in_monster = monster_class
				.getDeclaredField("energy");
		energy_field_in_monster.setAccessible(true);
		int energy_1 = (int) energy_field_in_monster.get(Monster_1);
		int energy_2 = (int) energy_field_in_monster.get(Monster_2);

		Field role_field_in_monster = monster_class.getDeclaredField("role");
		role_field_in_monster.setAccessible(true);
		Object role_1 = role_field_in_monster.get(Monster_1);
		Object role_2 = role_field_in_monster.get(Monster_2);

		if ((name_1.equals(name_2)) && (description_1.equals(description_2))
				&& (energy_1 == energy_2) && (role_1 == role_2)) {
			return true;
		}
		return false;

	}



	private void testEmptyConstructorDoesNotExist(Class aClass, Class[] inputs) {
		boolean thrown = false;
		try {
			aClass.getConstructor(inputs);
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertTrue("Constructor with zero parameters in " + aClass.getSimpleName() + " class should not be there.",thrown);
	}

}
