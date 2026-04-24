package game.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import java.util.HashMap;
import java.util.Random;

import org.junit.Test;

public class Milestone1PrivateTests {

	private String gamePath = "game.engine.Game";
	private String boardPath = "game.engine.Board";
	private String constantsPath = "game.engine.Constants";
	private String rolePath = "game.engine.Role";

	private String cardPath = "game.engine.cards.Card";
	private String swapperCardPath = "game.engine.cards.SwapperCard";
	private String startOverCardPath = "game.engine.cards.StartOverCard";
	private String shieldCardPath = "game.engine.cards.ShieldCard";
	private String energyStealCardPath = "game.engine.cards.EnergyStealCard";
	private String confusionCardPath = "game.engine.cards.ConfusionCard";

	private String cellPath = "game.engine.cells.Cell";
	private String contaminationSockPath = "game.engine.cells.ContaminationSock";
	private String cardCellPath = "game.engine.cells.CardCell";
	private String conveyorBeltPath = "game.engine.cells.ConveyorBelt";
	private String doorCellPath = "game.engine.cells.DoorCell";
	private String monsterCellPath = "game.engine.cells.MonsterCell";
	private String transportCellPath = "game.engine.cells.TransportCell";

	private String dataLoaderPath = "game.engine.dataloader.DataLoader";

	private String gameActionExceptionPath = "game.engine.exceptions.GameActionException";
	private String invalidCSVFormatPath = "game.engine.exceptions.InvalidCSVFormat";
	private String invalidMoveExceptionPath = "game.engine.exceptions.InvalidMoveException";
	private String invalidTurnExceptionPath = "game.engine.exceptions.InvalidTurnException";
	private String OutOfEnergyExceptionPath = "game.engine.exceptions.OutOfEnergyException";

	private String dasherPath = "game.engine.monsters.Dasher";
	private String dynamoPath = "game.engine.monsters.Dynamo";
	private String monsterPath = "game.engine.monsters.Monster";
	private String multiTaskerPath = "game.engine.monsters.MultiTasker";
	private String schemerPath = "game.engine.monsters.Schemer";

	private String canisterModifierPath = "game.engine.interfaces.CanisterModifier";
	

	private static ArrayList<String> cards_csv;
	private static ArrayList<String> cells_csv;
	private static ArrayList<String> monsters_csv;


	@Test(timeout = 1000)
	public void testCellInstanceVariableNameIsPresent() throws ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(cellPath), "name");

	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableNameOfTypeString() throws ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(cellPath), "name", String.class);

	}

	@Test(timeout = 1000)
	public void testCellInstanceVariableNameGetterLogic() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String input_name = generateRandomString(10);
		Constructor<?> constructor = Class.forName(cellPath).getConstructor(String.class);
		Object instance = constructor.newInstance(input_name);
		testGetterMethodLogic(instance, "name", input_name);
	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableMonsterIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(cellPath), "monster");
	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableMonsterGetterExists() throws ClassNotFoundException {
		testGetterMethodExistInClass(Class.forName(cellPath), "getMonster",Class.forName(monsterPath));
	}
	@Test(timeout = 1000)
	public void testDoorCellExtendsCell() throws ClassNotFoundException{			
		Class cellClass = Class.forName(cellPath);
		Class doorCellClass = Class.forName(doorCellPath);
		assertEquals("DoorCell class should extend Cell class",cellClass, doorCellClass.getSuperclass());
	}
	@Test(timeout = 1000)
	public void testInitializationNameInConstructorDoorCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
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

		Field name_field_in_cell = doorcell_class.getSuperclass().getDeclaredField("name");
		name_field_in_cell.setAccessible(true); 

		assertTrue("The constructor of DoorCell should initialize the instance variable name correctly", (input_name.equals(name_field_in_cell.get(doorcell_Object))) );
	}

	@Test(timeout = 1000)
	public void testDoorCellConstructorExists() throws ClassNotFoundException {
		Class[] parameters = {String.class,Class.forName(rolePath),int.class};
		testConstructorExists(Class.forName(doorCellPath), parameters);
	}

	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableRoleOfTypeRole() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(doorCellPath), "role", Class.forName(rolePath));
	}
	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableroleIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(doorCellPath), "role");

	}
	@Test(timeout = 1000)
	public void testDoorCellInstanceVariableEnergyOfTypeInt() throws SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(doorCellPath), "energy", int.class);
	}

	@Test(timeout = 1000)
	public void testCellInstanceVariableActivatedGetterExists() throws ClassNotFoundException {
		testGetterMethodExistInClass(Class.forName(doorCellPath), "isActivated",boolean.class);
	}
	@Test(timeout = 1000)
	public void testCellInstanceVariableActivatedSetterExists() throws ClassNotFoundException {
		testSetterMethodExistInClass(Class.forName(doorCellPath), "setActivated", boolean.class);

	}


	@Test(timeout = 1000)
	public void testTransportCellInstanceVariableEffectGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class conveyorBelt_class  = Class.forName(conveyorBeltPath);

		Random rand = new Random();
		String input_name = generateRandomString(10);

		int input_energy = rand.nextInt(50); 	


		Constructor<?> conveyorBelt_constructor = Class.forName(conveyorBeltPath).getConstructor(String.class,int.class);
		Object conveyorBelt_Object = conveyorBelt_constructor.newInstance(input_name,input_energy);

		Field effect = conveyorBelt_class.getSuperclass().getDeclaredField("effect");
		effect.setAccessible(true); 
		effect.set(conveyorBelt_Object, input_energy);

		Method getEffect = conveyorBelt_class.getSuperclass().getDeclaredMethod("getEffect");
		int effectValue = (int) getEffect.invoke(conveyorBelt_Object);

		assertEquals("The method getEffect should return the correct value of the instance variable effect",input_energy,effectValue);
	}

	@Test(timeout = 1000)
	public void testConveyorBeltConstructorExists() throws ClassNotFoundException {
		Class[] parameters = {String.class,int.class};
		testConstructorExists(Class.forName(conveyorBeltPath), parameters);
	}
	@Test(timeout = 1000)
	public void testInitializationEffectInConstructorContaminationSock() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class contaminationSock_class  = Class.forName(contaminationSockPath);

		Random rand = new Random();
		String input_name = generateRandomString(10);

		int input_energy = - (rand.nextInt(50) + 1);


		Constructor<?> contaminationSockPath_constructor = Class.forName(contaminationSockPath).getConstructor(String.class,int.class);
		Object contaminationSock_Object = contaminationSockPath_constructor.newInstance(input_name,input_energy);

		Field effect_field_in_cell = contaminationSock_class.getSuperclass().getDeclaredField("effect");
		effect_field_in_cell.setAccessible(true); 

		assertEquals("The constructor of ContaminationSock should initialize the instance variable effect correctly", input_energy, effect_field_in_cell.get(contaminationSock_Object));
	}

	@Test(timeout = 1000)
	public void testMonsterCellExtendsCell() throws ClassNotFoundException{
		Class cellClass = Class.forName(cellPath);
		Class monsterCellClass = Class.forName(monsterCellPath);
		assertEquals("monsterCell class should extends cell class",cellClass, monsterCellClass.getSuperclass());

	}

	@Test(timeout = 1000)
	public void testMonsterCellInstanceVariableCellMonsterGetterExists() throws ClassNotFoundException{
		testGetterMethodExistInClass(Class.forName(monsterCellPath), "getCellMonster",Class.forName(monsterPath));
	}
	@Test(timeout = 1000)
	public void testMonsterCellInstanceVariableCellMonsterSetterIsAbsent() throws ClassNotFoundException {
		testSetterMethodIsAbsentInClass(Class.forName(monsterCellPath), "setCellMonster");
	}
	@Test(timeout = 1000)
	public void testCardCellExtendsCell() throws ClassNotFoundException{
		Class cellClass = Class.forName(cellPath);
		Class cardCellClass = Class.forName(cardCellPath);
		assertEquals("cardCell class should extends cell class",cellClass, cardCellClass.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testGameActionExceptionConstructorExists() throws ClassNotFoundException  {
		testConstructorExists(Class.forName(gameActionExceptionPath), new Class[] {String.class});
	}

	@Test(timeout = 1000)
	public void testInvalidMoveExceptionMSGOfTypeString() throws ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(invalidMoveExceptionPath), "MSG", String.class);
	}
	@Test(timeout = 1000)
	public void testInvalidMoveExceptionMSGIsFinal() throws ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(invalidMoveExceptionPath), "MSG");
	}

	@Test(timeout = 1000)
	public void testInvalidTurnExceptionEmptyConstructorExists() throws ClassNotFoundException {
		testConstructorExists(Class.forName(invalidTurnExceptionPath), new Class[] {});
	}

	@Test(timeout = 1000)
	public void testInvalidTurnExceptionConstructorExists() throws ClassNotFoundException  {
		testConstructorExists(Class.forName(invalidTurnExceptionPath), new Class[] {String.class});
	}

	@Test(timeout = 1000)
	public void testInvalidTurnExceptionPassesMessageToSuper() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		Class<?> c = Class.forName(invalidTurnExceptionPath);

		Object obj = c.getConstructor().newInstance();

		Field msgField = c.getDeclaredField("MSG");
		msgField.setAccessible(true);
		String expected = (String) msgField.get(null);

		assertEquals(
				"InvalidTurnException should pass the correct MSG to super.",
				expected,
				((Exception) obj).getMessage()
				);
	}




	//////////////////////////////////////BOARD_SIZE VARIABLE///////////////////////////////////////
	@Test(timeout = 1000)
	public void testBoardSizeVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"BOARD_SIZE");
	}

	@Test(timeout = 1000)
	public void testBoardSizeVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "BOARD_SIZE");
	}

	/////////////////////////////////////BOARD_ROWS Variable/////////////////////////////////////////
	@Test(timeout = 1000)
	public void testBoardRowsVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "BOARD_ROWS");
	}

	@Test(timeout = 1000)
	public void testBoardRowsVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "BOARD_ROWS");
	}

	/////////////////////////////////////BOARD_COLS Variable////////////////////////////////////////	
	@Test(timeout = 1000)
	public void testBoardColsVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "BOARD_COLS");
	}

	@Test(timeout = 1000)
	public void testBoardColsVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "BOARD_COLS");
	}

	///////////////////////////////////// WINNING_POSITION Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testWinningPositionVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "WINNING_POSITION");
	}

	@Test(timeout = 1000)
	public void testWinningPositionVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "WINNING_POSITION", int.class);
	}

	/////////////////////////////////////STARTING_POSITION Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testStartingPositionVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "STARTING_POSITION", int.class);
	}

	@Test(timeout = 1000)
	public void testStartingPositionVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("STARTING_POSITION");
		assertEquals("The STARTING_POSITION attribute should initially be 0",0,f.get(constants));
	}

	/////////////////////////////////////MONSTER_CELL_INDICES Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testMonsterCellIndicesVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"MONSTER_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testMonsterCellIndicesVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("MONSTER_CELL_INDICES");
		int [] fValues = (int [])f.get(constants);
		int [] expectedValue = {2, 18, 34, 54, 82, 88};
		assertTrue("The MONSTER_CELL_INDICES attribute should initially be {2, 18, 34, 54, 82, 88}",Arrays.equals(expectedValue, fValues));		
	}

	/////////////////////////////////////CONVEYOR_CELL_INDICES Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testConveyorCellIndicesVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"CONVEYOR_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testConveyorCellIndicesVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "CONVEYOR_CELL_INDICES");
	}

	/////////////////////////////////////SOCK_CELL_INDICES Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testSockCellIndicesVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "SOCK_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testSockCellIndicesVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "SOCK_CELL_INDICES");
	}

	/////////////////////////////////////CARD_CELL_INDICES Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testCardCellIndicesVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "CARD_CELL_INDICES");
	}

	@Test(timeout = 1000)
	public void testCardCellIndicesVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "CARD_CELL_INDICES");
	}

	/////////////////////////////////////WINNING_ENERGY Variable////////////////////////////////////////	
	@Test(timeout = 1000)
	public void testWinningEnergyVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "WINNING_ENERGY");
	}

	@Test(timeout = 1000)
	public void testWinningEnergyVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "WINNING_ENERGY", int.class);
	}

	/////////////////////////////////////MIN_ENERGY Variable////////////////////////////////////////	
	@Test(timeout = 1000)
	public void testMinEnergyVariableIsInteger() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(constantsPath), "MIN_ENERGY", int.class);
	}

	@Test(timeout = 1000)
	public void testMinEnergyVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("MIN_ENERGY");
		assertEquals("The MIN_ENERGY attribute should initially be 0",0,f.get(constants));
	}

	/////////////////////////////////////MULTITASKER_BONUS Variable////////////////////////////////////
	@Test(timeout = 1000)
	public void testMultiTaskerBonusVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"MULTITASKER_BONUS");
	}

	@Test(timeout = 1000)
	public void testMultiTaskerBonusVariableValue() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Constructor<?> Constantsconstructor = Class.forName(constantsPath).getConstructor();
		Object constants = Constantsconstructor.newInstance();
		Field f = constants.getClass().getDeclaredField("MULTITASKER_BONUS");
		assertEquals("The MULTITASKER_BONUS attribute should initially be 200",200,f.get(constants));
	}

	/////////////////////////////////////SCHEMER_STEAL Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testSchemerStealVariableExistsInConstantsClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(constantsPath),"SCHEMER_STEAL");
	}

	@Test(timeout = 1000)
	public void testSchemerStealVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "SCHEMER_STEAL");
	}

	/////////////////////////////////////SLIP_PENALTY Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testSlipPenaltyVariableIsPublic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsPublic(Class.forName(constantsPath), "SLIP_PENALTY");
	}

	@Test(timeout = 1000)
	public void testSlipPenaltyVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "SLIP_PENALTY");
	}

	/////////////////////////////////////POWERUP_COST Variable////////////////////////////////////////
	@Test(timeout = 1000)
	public void testPowerUpCostVariableIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsStatic(Class.forName(constantsPath), "POWERUP_COST");
	}

	@Test(timeout = 1000)
	public void testPowerUpCostVariableIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		testInstanceVariableIsFinal(Class.forName(constantsPath), "POWERUP_COST");
	}

	/////////////////////////////////////////Monster Class Attributes//////////////////////

	////////////////////////////////////////Name Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testNameVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"name");
	}

	////////////////////////////////////////Description Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testDescriptionVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "description");
	}

	////////////////////////////////////////Role Variable//////////////////////////////////	
	@Test(timeout = 1000)
	public void testRoleVariableIsRoleEnum() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "role", Class.forName(rolePath));
	}

	////////////////////////////////////////OriginalRole Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testOriginalRoleVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"originalRole");
	}

	////////////////////////////////////////Energy Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testEnergyVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "energy");
	}

	////////////////////////////////////////Position Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testPositionVariableIsInt() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "position", int.class);
	}

	////////////////////////////////////////Frozen Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testFrozenVariableExistsInMonsterClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(monsterPath),"frozen");
	}

	////////////////////////////////////////Shielded Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testShieldedVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(monsterPath), "shielded");
	}

	////////////////////////////////////////ConfusionTurn Variable//////////////////////////////////
	@Test(timeout = 1000)
	public void testConfusionTurnsVariableIsInt() throws SecurityException, ClassNotFoundException{
		testInstanceVariableOfType(Class.forName(monsterPath), "confusionTurns", int.class);
	}

	////////////////////////////////////////Getters//////////////////////////////////////
	///////////////////////////////////////Name Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testNameGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "getName", String.class, true);
	}

	///////////////////////////////////////description Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testDescriptionGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "description", description);		
	}

	///////////////////////////////////////role Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testRoleGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "role", role);		
	}

	///////////////////////////////////////energy Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testEnergyGetterMethodExists() throws ClassNotFoundException{
		testGetterMethodExistsInClass(Class.forName(monsterPath), "getEnergy", int.class, true);
	}

	///////////////////////////////////////position Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testPositionGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "position", 0);		
	}

	///////////////////////////////////////shielded Getter/////////////////////////////////
	@Test(timeout = 1000)
	public void testShieldedGetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dynamoMonster, "shielded", false);		
	}

	//////////////////////////////////////////////Setters///////////////////////////////////	
	//////////////////////////////////////////////description Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testDescriptionSetterMethodNotExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setDescription", String.class, false);
	}

	//////////////////////////////////////////////role Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testRoleSetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setRole", Class.forName(rolePath), true);
	}

	//////////////////////////////////////////////energy Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testEnergySetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testSetterLogic(dynamoMonster, "energy", energy, energy, int.class);		
	}

	//////////////////////////////////////////////frozen Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testFrozenSetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(monsterPath), "setFrozen", boolean.class, true);
	}

	//////////////////////////////////////////////shielded Setter///////////////////////////////
	@Test(timeout = 1000)
	public void testShieldedSetterMethodLogic() throws Exception{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		testSetterLogic(dynamoMonster, "shielded", true, true, boolean.class);		
	}

	//////////////////////////////////////Monster SubClasses/////////////////////////
	///////////////////////SubClasses Exist//////////////////////////////////////////
	@Test(timeout = 1000)
	public void testDasherClassExists(){
		testClassExists(dasherPath);
	}

	/////////////////////////////Subclasses extend Monster////////////////////////////
	@Test(timeout = 1000)
	public void testDynamoClassIsSubclassOfMonster() throws ClassNotFoundException{
		testClassIsSubclass(Class.forName(dynamoPath), Class.forName(monsterPath));
	}

	//////////////////////////////////Subclasses Constructor////////////////////////////
	/////////////////////////////////Constructors Exist//////////////////////////
	@Test(timeout = 1000)
	public void testMultiTaskerConstructorExists() throws ClassNotFoundException{
		Class[] inputs = {String.class, String.class, Class.forName(rolePath), int.class};
		testConstructorExists(Class.forName(multiTaskerPath), inputs);
	}

	///////////////////////////////////////Subclasses does not have an empty constructor////////////////////////	
	@Test(timeout = 1000)
	public void testSchemerConstructorWithZeroParametersDoesNotExist() throws ClassNotFoundException{
		Class [] inputs = {};
		testEmptyConstructorDoesNotExist(Class.forName(schemerPath),inputs );
	}

	///////////////////////////////////Constructors Logic///////////////////////////////////
	@Test(timeout = 1000)
	public void testDynamoConstructorLogic() throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException{
		Constructor<?> dynamoConstructor = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String [] roleValues = {"SCARER","LAUGHER"};
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dynamoMonster = dynamoConstructor.newInstance(name,description,role,energy);
		String [] variableNames = {"name", "description", "role", "originalRole", "energy", "position", "frozen", "shielded", "confusionTurns"};
		Object [] variableValues = {name, description, role, role, energy, 0, false, false, 0};
		testConstructorInitialization(dynamoMonster, variableNames, variableValues);
	}

	///////////////////////////////Subclasses Attributes//////////////////////////
	///////////////////////////////Dasher Extra Attribute/////////////////////////
	@Test(timeout = 1000)
	public void testMomentumTurnsVariableExistsInDasherClass() throws SecurityException, ClassNotFoundException{
		testInstanceVariableIsPresent(Class.forName(dasherPath),"momentumTurns");
	}

	@Test(timeout = 1000)
	public void testMomentumTurnsGetterMethodLogic() throws Exception{
		Constructor<?> dasherConstructor = Class.forName(dasherPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		String name = "monster" + new Random().nextInt(10);
		String description = "description" + new Random().nextInt(10);
		String [] roleValues = {"SCARER","LAUGHER"};
		Object role = Enum.valueOf((Class<Enum>)Class.forName(rolePath), roleValues[new Random().nextInt(1)]);
		int energy = new Random().nextInt(10)+1;
		Object dasherMonster = dasherConstructor.newInstance(name,description,role,energy);
		testGetterLogic(dasherMonster, "momentumTurns", 0);		
	}

	///////////////////////////////MultiTasker Extra Attribute/////////////////////////
	@Test(timeout = 1000)
	public void testNormalSpeedTurnsVariableIsPrivate() throws SecurityException, ClassNotFoundException, NoSuchFieldException{
		testInstanceVariableIsPrivate(Class.forName(multiTaskerPath), "normalSpeedTurns");
	}

	@Test(timeout = 1000)
	public void testNormalSpeedTurnsSetterMethodExists() throws ClassNotFoundException{
		testSetterMethodExistsInClass(Class.forName(multiTaskerPath), "setNormalSpeedTurns", int.class, true);
	}	


	///////////////////////////	Testing 5.13 Card Class
	//Attributes existence
	@Test(timeout = 100)
	public void testCardInstanceVariableNameIsPresent() throws NoSuchFieldException, ClassNotFoundException {

		testInstanceVariableIsPresent(Class.forName(cardPath), "name");
	}

	@Test(timeout = 100)
	public void testCardInstanceVariableDescriptionIsPresent() throws NoSuchFieldException, ClassNotFoundException {

		testInstanceVariableIsPresent(Class.forName(cardPath), "description");
	}

	//Attributes private

	@Test(timeout = 1000)
	public void testCardInstanceVariableRarityIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(cardPath), "rarity");
	}

	@Test(timeout = 1000)
	public void testCardInstanceVariableLuckyIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(cardPath), "lucky");
	}

	/*--------------------------testing getters existence -----------------------------------*/


	@Test(timeout = 100)
	public void testNameGetter() throws ClassNotFoundException {
		testGetterMethodExistsInClass(Class.forName(cardPath),"getName",String.class,true);
	}

	/*--------------------------testing setters absence -----------------------------------*/

	@Test(timeout = 1000)
	public void testCardInstanceVariableLuckyNoSetter() throws ClassNotFoundException {
		testSetterMethodExistsInClass(Class.forName(cardPath), "setLucky",boolean.class , false);
	}

	///////////////////////////	Testing 5.14 SwapperCard Class
	//Constructor Initializes correctly

	@Test(timeout = 1000)
	public void testSwapperCardConstructorInitialization() throws Exception{
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		int input_rarity=(int) (Math.random()*5);	
		Constructor<?> swapperCard_constructor = Class.forName(swapperCardPath).getConstructor(String.class,String.class,int.class);
		Object SwapperCard_Object = swapperCard_constructor.newInstance(input_name, input_description,input_rarity);
		String[] names = {"name", "description","rarity","lucky"};
		Object[] values = {input_name,input_description,input_rarity,true};
		testConstructorInitialization(SwapperCard_Object, names, values);
	}
	//Subclass of Card
	@Test(timeout = 1000)
	public void testSwapperCardIsSubClassOfCard() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(cardPath), Class.forName(swapperCardPath));
	}

	///////////////////////////	Testing 5.15 EnergyStealCard Class
	//Test Can Modify Energy
	@Test(timeout = 10000)
	public void testEnergyStealCardClassImplementsCanisterModifierInterface() throws ClassNotFoundException {
		testClassImplementsInterface(Class.forName(energyStealCardPath), Class.forName(canisterModifierPath));
	}

	/*--------------------------testing getters logic -----------------------------------*/

	@Test(timeout = 1000)
	public void testEnergyStealCardInstanceVariableEnergyGetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		int input_rarity=(int) (Math.random()*5);	
		int input_energy=(int) (Math.random()*5);	
		Constructor<?> EnergyStealCardconstructor = Class.forName(energyStealCardPath).getConstructor(String.class,String.class,int.class,int.class);
		Object EnergyStealCardinstance = EnergyStealCardconstructor.newInstance(input_name,input_description,input_rarity,input_energy);
		testGetterMethodLogic(EnergyStealCardinstance, "energy", input_energy);	
	}

	///////////////////////////	Testing 5.16 StartOverCard Class
	//Constructor Initializes correctly
	@Test(timeout = 1000)
	public void testStartOverCardConstructorInitialization() throws Exception{
		String input_name = generateRandomString(10);
		String input_description = generateRandomString(10);
		int input_rarity=(int) (Math.random()*5);	
		Random random = new Random();
		boolean input_lucky = random.nextBoolean();
		Constructor<?> startOverCard_constructor = Class.forName(startOverCardPath).getConstructor(String.class,String.class,int.class,boolean.class);
		Object startOverCard_Object = startOverCard_constructor.newInstance(input_name, input_description,input_rarity,input_lucky);
		String[] names = {"name", "description","rarity","lucky"};
		Object[] values = {input_name,input_description,input_rarity,input_lucky};
		testConstructorInitialization(startOverCard_Object, names, values);
	}

	///////////////////////////	Testing 5.17 ConfusionCard Class

	//Attributes present
	@Test(timeout = 100)
	public void testConfusionCardInstanceVariableDurationIsPresent() throws NoSuchFieldException, ClassNotFoundException {
		testInstanceVariableIsPresent(Class.forName(confusionCardPath), "duration");
	}

	//Subclass of Card
	@Test(timeout = 1000)
	public void testConfusionCardIsSubClassOfCard() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(cardPath), Class.forName(confusionCardPath));
	}

	/*--------------------------testing setters absence -----------------------------------*/


	@Test(timeout = 1000)
	public void testConfusionCardInstanceVariableDurationNoSetter() throws ClassNotFoundException {
		testSetterMethodExistsInClass(Class.forName(confusionCardPath), "setDuration",int.class , false);
	}

	///////////////////////////	Testing 5.18 ShieldCard Class
	//Constructor present
	@Test(timeout = 1000)
	public void testConstructorShieldCard() throws ClassNotFoundException {
		Class[] inputs = {String.class, String.class, int.class};
		testConstructorExists(Class.forName(shieldCardPath), inputs);
	}

	//Subclass of Card
	@Test(timeout = 1000)
	public void testShieldCardIsSubClassOfCard() throws ClassNotFoundException {
		testClassIsSubClass(Class.forName(cardPath), Class.forName(shieldCardPath));
	}

	///////////////////////////	Testing 6.4 OutOfEnergyException Class
	//Second Constructor present
	@Test(timeout = 1000)
	public void testSecondConstructorOutOfEnergyException() throws ClassNotFoundException {
		Class[] inputs = {String.class};
		testConstructorExists(Class.forName(OutOfEnergyExceptionPath), inputs);
	}

	//Empty Constructor Initializes correctly
	@Test(timeout = 1000)
	public void testEmptyConstructorOutOfEnergyExceptionInitialization() throws Exception{
		Constructor<?> outOfEnergyException_constructor = Class.forName(OutOfEnergyExceptionPath).getConstructor();
		Object outOfEnergyException_Object = outOfEnergyException_constructor.newInstance();
		String[] names = {"detailMessage"};
		Object[] values = {"Not Enough Energy for Power Up"};
		testExceptionConstructorInitialization(outOfEnergyException_Object, names, values);
	}

	//	Attribute MSG is final
	@Test(timeout = 1000)
	public void testOutOfEnergyExceptionInstanceVariableMSGIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsFinal(Class.forName(OutOfEnergyExceptionPath), "MSG");
	}

	///////////////////////////	Testing 6.5 InvalidCSVFormatException Class
	//First	Constructor present
	@Test(timeout = 1000)
	public void testFirstConstructorInvalidCSVFormatException() throws ClassNotFoundException {
		Class[] inputs = {String.class};
		testConstructorExists(Class.forName(invalidCSVFormatPath), inputs);
	}

	@Test(timeout = 1000)
	public void testFirstConstructorInvalidCSVFormatInitialization() throws Exception {
		String input_Line = generateRandomString(10);
		Constructor<?> constructor = Class.forName(invalidCSVFormatPath).getConstructor(String.class);
		Object exception = constructor.newInstance(input_Line);
		// First constructor must set inputLine from the argument, not a hardcoded value
		Method getInputLine = exception.getClass().getMethod("getInputLine");
		assertEquals("InvalidCSVFormat(String) must set inputLine from the constructor argument.",
				input_Line, getInputLine.invoke(exception));
		// getMessage() should be MSG + inputLine
		String message = (String) exception.getClass().getMethod("getMessage").invoke(exception);
		assertTrue("InvalidCSVFormat(String) message should contain the input line.",
				message != null && message.contains(input_Line));
	}

	@Test(timeout = 1000)
	public void testFirstConstructorInvalidCSVFormatInputLineSentToSuper() throws Exception {
		String input_Line = generateRandomString(10);
		Class<?> cls = Class.forName(invalidCSVFormatPath);
		Field msgField = cls.getDeclaredField("MSG");
		msgField.setAccessible(true);
		String expectedPrefix = (String) msgField.get(null);
		Constructor<?> constructor = cls.getConstructor(String.class);
		Object exception = constructor.newInstance(input_Line);
		String actualMessage = (String) exception.getClass().getMethod("getMessage").invoke(exception);
		String expectedMessage = expectedPrefix + input_Line;
		assertEquals("InvalidCSVFormat(String) must pass inputLine to super(MSG + inputLine); getMessage() should equal MSG + inputLine.",
				expectedMessage, actualMessage);
	}

	@Test(timeout = 1000)
	public void testSecondConstructorInvalidCSVFormatInitialization() throws Exception{
		String input_message = generateRandomString(10);
		String input_Line = generateRandomString(10);
		Constructor<?> invalidCSVFormatException_constructor = Class.forName(invalidCSVFormatPath).getConstructor(String.class,String.class);
		Object invalidCSVFormatException_Object = invalidCSVFormatException_constructor.newInstance(input_message, input_Line);

		// Test that the exception stores message and inputLine correctly via public API
		Method getMessageMethod = invalidCSVFormatException_Object.getClass().getMethod("getMessage");
		assertEquals("InvalidCSVFormat(String, String) should set the exception message correctly.",
				input_message, getMessageMethod.invoke(invalidCSVFormatException_Object));

		Method getInputLineMethod = invalidCSVFormatException_Object.getClass().getMethod("getInputLine");
		assertEquals("InvalidCSVFormat(String, String) should set inputLine correctly and getInputLine() should return it.",
				input_Line, getInputLineMethod.invoke(invalidCSVFormatException_Object));

		// Test that the exception works when thrown and caught
		try {
			throw (Throwable) invalidCSVFormatException_Object;
		} catch (Throwable caught) {
			assertTrue("InvalidCSVFormat should be throwable and catchable.", Class.forName(invalidCSVFormatPath).isInstance(caught));
			assertEquals("Caught InvalidCSVFormat should preserve the message.", input_message, caught.getMessage());
			Object caughtInputLine = caught.getClass().getMethod("getInputLine").invoke(caught);
			assertEquals("Caught InvalidCSVFormat should preserve inputLine via getInputLine().", input_Line, caughtInputLine);
		}
	}

	//	Attribute MSG is static
	@Test(timeout = 1000)
	public void testInvalidCSVFormatExceptionInstanceVariableMSGIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(invalidCSVFormatPath), "MSG");
	}

	/*--------------------------testing getters existence -----------------------------------*/

	@Test(timeout = 100)
	public void testInvalidCSVFormatInstanceVariableInputLineGetter() throws ClassNotFoundException {
		testGetterMethodExistsInClass(Class.forName(invalidCSVFormatPath), "getInputLine",String.class,true);
	}

	/*--------------------------testing setters logic -----------------------------------*/

	@Test(timeout = 1000)
	public void testInvalidCSVFormatInstanceVariableInputSetterLogic() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String input_Line = generateRandomString(10);
		Constructor<?> invalidCSVFormatException_constructor = Class.forName(invalidCSVFormatPath).getConstructor(String.class);
		Object InvalidCSVFormatExceptioninstance = invalidCSVFormatException_constructor.newInstance(input_Line);
		testSetterMethodLogic(InvalidCSVFormatExceptioninstance, "inputLine", input_Line,String.class);	
	}


	
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableCardsIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(dataLoaderPath), "CARDS_FILE_NAME");
	}
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileCellsIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(dataLoaderPath), "CELLS_FILE_NAME");
	}
	
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileCellsIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(dataLoaderPath), "CELLS_FILE_NAME");
	}
	
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileCellsIsFinal() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsFinal(Class.forName(dataLoaderPath), "CELLS_FILE_NAME");
	}
	
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableCardsIsString() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(dataLoaderPath), "CARDS_FILE_NAME", String.class);
	}
	
	@Test(timeout = 1000)
	public void testDataLoaderInstanceVariableFileCellsIsString() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableOfType(Class.forName(dataLoaderPath), "CELLS_FILE_NAME", String.class);
	}
	
	@Test(timeout = 1000)
	public void testCellsFileInDataLoaderValue() {

		try {

			Field cardsFileField = Class.forName(dataLoaderPath).getDeclaredField("CELLS_FILE_NAME");
			cardsFileField.setAccessible(true);
			assertEquals("cells.csv",cardsFileField.get(null));
		} catch ( NoSuchFieldException| SecurityException|
				ClassNotFoundException| IllegalArgumentException| IllegalAccessException  e) {
			e.printStackTrace();
			fail("An error "+e.getMessage()+" occured while testing the CELLS_FILE_NAME attribute");
		}
	}
	
	@Test(timeout = 1000)
	public void testreadCardsInDataLoaderThrowsIOException() {

		Method m;
		try {
			m = Class.forName(dataLoaderPath).getMethod("readCards");
			Class<?>[] exceptionsThrown= m.getExceptionTypes();
			boolean IOExceptionFound=false;
			for (Class<?> excep : exceptionsThrown) {
				if(excep.equals(IOException.class))
					IOExceptionFound=true;
			}

			assertTrue("readCards expected to throw IOException",IOExceptionFound);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			fail("an error occured while testing the readCards, error cause is "+e.getMessage()+" please check the console");
		}

	}
	@Test(timeout = 1000)
	public void testInitializationEffectInConstructorContaminationSockIsAlwaysNegative() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Class contaminationSock_class  = Class.forName(contaminationSockPath);

		Random rand = new Random();
		String input_name = generateRandomString(10);

		int input_energy = - (rand.nextInt(50) + 1);


		Constructor<?> contaminationSockPath_constructor = Class.forName(contaminationSockPath).getConstructor(String.class,int.class);
		Object contaminationSock_Object = contaminationSockPath_constructor.newInstance(input_name,input_energy);

		Field effect_field_in_cell = contaminationSock_class.getSuperclass().getDeclaredField("effect");
		effect_field_in_cell.setAccessible(true); 

		assertTrue("The effect value in ContaminationSock should always be negative", 0 >  (int)effect_field_in_cell.get(contaminationSock_Object));

	}
	
	
	@Test(timeout = 1000)
	public void testInputLineInstanceVariableNameIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(invalidCSVFormatPath), "inputLine");
	}
	
	@Test(timeout = 1000)
	public void testDataLoaderReadCellsReadingCSV() throws IOException {
		savingCellsCSV();
		try {
			ArrayList<String> cellsList = writeCellsCSVForDataLoader();
			Method readCells = Class.forName(dataLoaderPath).getMethod("readCells");
			ArrayList<?> loadedCells = (ArrayList<?>) readCells.invoke(null);
			assertEquals("readCells() should load one cell per CSV line.", cellsList.size(), loadedCells.size());
			Field nameField = Class.forName(cellPath).getDeclaredField("name");
			nameField.setAccessible(true);
			Field effectField = Class.forName(transportCellPath).getDeclaredField("effect");
			effectField.setAccessible(true);
			Field roleField = Class.forName(doorCellPath).getDeclaredField("role");
			Field energyDoorField = Class.forName(doorCellPath).getDeclaredField("energy");
			Field activatedField = Class.forName(doorCellPath).getDeclaredField("activated");
			roleField.setAccessible(true);
			energyDoorField.setAccessible(true);
			activatedField.setAccessible(true);
			
			// Map expected CSV rows by cell name to avoid depending on list ordering
			HashMap<String, String[]> expectedByName = new HashMap<>();
			for (String row : cellsList) {
				String[] csvRow = row.split(",", -1);
				String name = csvRow[0].trim();
				expectedByName.put(name, csvRow);
			}
			
			for (int i = 0; i < loadedCells.size(); i++) {
				Object cell = loadedCells.get(i);
				String actualName = ((String) nameField.get(cell)).trim();
				assertTrue("Loaded cell name \"" + actualName + "\" should exist in CSV.", expectedByName.containsKey(actualName));
				String[] csvRow = expectedByName.get(actualName);
				
				if (csvRow.length == 2) {
					int effectVal = Integer.parseInt(csvRow[1].trim());
					int expectedEffect = effectVal > 0 ? effectVal : effectVal;
					assertEquals("Transport cell effect for cell \"" + actualName + "\"", expectedEffect, effectField.get(cell));
				} else {
					Object expectedRole = Enum.valueOf((Class<Enum>) Class.forName(rolePath), csvRow[1].trim());
					int expectedEnergy = Integer.parseInt(csvRow[2].trim());
					assertEquals("DoorCell role for cell \"" + actualName + "\"", expectedRole, roleField.get(cell));
					assertEquals("DoorCell energy for cell \"" + actualName + "\"", expectedEnergy, energyDoorField.get(cell));
					assertEquals("DoorCell activated for cell \"" + actualName + "\" (should be false initially)", false, activatedField.get(cell));
				}
			}
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
			fail("Error while testing DataLoader.readCells(): " + e.getMessage());
		} finally {
			reWriteCellsCSVForLoadCards();
		}
	}
//	Read Monsters
	@Test(timeout = 1000)
	public void testreadMonstersInDataLoaderisStatic() {

		Method m;
		try {
			m = Class.forName(dataLoaderPath).getMethod("readMonsters");
			assertTrue("readMonsters expected to be Public",Modifier.isPublic(m.getModifiers()));
			assertTrue("readMonsters expected to be Static",Modifier.isStatic(m.getModifiers()));
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			fail("error occured while testing readMonsters method check the console "+e.getMessage());
		}

	}


	@Test(timeout = 1000)
	public void testBoardInstanceVariableBoardCellsIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(boardPath), "boardCells");
	}
	@Test(timeout = 1000)
	public void testBoardInstanceVariableStationedMonstersIsStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsStatic(Class.forName(boardPath), "stationedMonsters");
	}
	@Test(timeout = 1000)
	public void testBoardGetterLogicBoardCells() throws Exception {
		Constructor<?> boardConstructor = Class.forName(boardPath).getConstructor(ArrayList.class);
		Object boardObject = boardConstructor.newInstance(new ArrayList<>());
		Object boardCellsValue = Array.newInstance(Class.forName(cellPath), 2, 2);
		Array.set(Array.get(boardCellsValue, 0), 0, createCell());
		Array.set(Array.get(boardCellsValue, 0), 1, createDoorCell());
		Array.set(Array.get(boardCellsValue, 1), 0, createCardCell());
		Array.set(Array.get(boardCellsValue, 1), 1, createConveyorBelt());
		testGetterLogic(boardObject, "boardCells", boardCellsValue);
	}

	@Test(timeout = 1000)
	public void testBoardGetterLogicCards() throws Exception {
		Constructor<?> boardConstructor = Class.forName(boardPath).getConstructor(ArrayList.class);
		Object boardObject = boardConstructor.newInstance(new ArrayList<>());
		ArrayList<Object> value = new ArrayList<>();
		value.add(createStartOverCard());
		value.add(createConfusionCard());
		testGetterLogic(boardObject, "cards", value);
	}
	
	@Test(timeout = 1000)
	public void testBoardSetterLogicStationedMonsters() throws Exception {
		Constructor<?> boardConstructor = Class.forName(boardPath).getConstructor(ArrayList.class);
		Object boardObject = boardConstructor.newInstance(new ArrayList<>());
		ArrayList<Object> value = new ArrayList<>();
		value.add(createDasher());
		value.add(createMultiTasker());
		testSetterLogic(boardObject, "stationedMonsters", value, value, ArrayList.class);
	}
	
	@Test(timeout = 1000)
	public void testGameInstanceVariableAllMonstersIsNotStatic() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsNotStatic(Class.forName(gamePath), "allMonsters");
	}
	// player
	@Test(timeout = 1000)
	public void testGameInstanceVariablePlayerIsPrivate() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		testInstanceVariableIsPrivate(Class.forName(gamePath), "player");
	}
	// READ ONLY: board, allMonsters, player, opponent — getter logic + setter absent
		@Test(timeout = 1000)
		public void testGameBoardSetterAbsent() throws SecurityException, ClassNotFoundException {
			testSetterAbsent("board", new String[] { gamePath });
		}
		
		@Test(timeout = 1000)
		public void testGameGetterLogicBoard() throws Exception {
			Object gameObject = createGameForTesting();
			Object boardValue = Class.forName(boardPath).getConstructor(ArrayList.class).newInstance(new ArrayList<>());
			testGetterLogic(gameObject, "board", boardValue);
		}
		@Test(timeout = 1000)
		public void testGameGetterLogicOpponent() throws Exception {
			Object gameObject = createGameForTesting();
			Object monsterValue = createMultiTasker();
			testGetterLogic(gameObject, "opponent", monsterValue);
		}
		@Test(timeout = 1000)
		public void testGameSelectRandomMonsterByRoleIsPrivate() throws NoSuchMethodException, SecurityException, ClassNotFoundException {
			Method m = Class.forName(gamePath).getDeclaredMethod("selectRandomMonsterByRole", Class.forName(rolePath));
			assertTrue("selectRandomMonsterByRole should be private.", Modifier.isPrivate(m.getModifiers()));
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
