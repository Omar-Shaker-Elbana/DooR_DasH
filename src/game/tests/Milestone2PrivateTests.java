package game.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;


public class Milestone2PrivateTests {

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


	@Test(timeout = 2000)
	public void testPlayTurnSwitchesTurnWhenNotFrozen() {
		try {
			Class<?> roleClass = Class.forName(rolePath);
			
			Enum<?> scarerRole = Enum.valueOf((Class<Enum>) roleClass, "SCARER");

			Class<?> gameClass = Class.forName(gamePath);
			Constructor<?> gameConstructor = gameClass.getConstructor(roleClass);
			Object gameObject = gameConstructor.newInstance(scarerRole);

			Method getCurrent = gameClass.getMethod("getCurrent");
			Method getOpponent = gameClass.getMethod("getOpponent");
			Class<?> monsterClass = Class.forName(monsterPath);
			Method getPosition = monsterClass.getMethod("getPosition");
			Method setPosition = monsterClass.getMethod("setPosition", int.class);
			Object currentBefore = getCurrent.invoke(gameObject);
			Object opponent = getOpponent.invoke(gameObject);

			// Ensure current is not frozen
			Method setFrozen = monsterClass.getMethod("setFrozen", boolean.class);
			setFrozen.invoke(currentBefore, false);
			
			// Use safe fixed positions to avoid random InvalidMoveException paths.
			// From position 95, any roll in [1..6] cannot collide with opponent at 50.
			setPosition.invoke(currentBefore, 95);
			setPosition.invoke(opponent, 50);

			int posBefore = (int) getPosition.invoke(currentBefore);

			Method playTurn = gameClass.getMethod("playTurn");
			playTurn.invoke(gameObject);

			Object currentAfter = getCurrent.invoke(gameObject);
			int posAfter = (int) getPosition.invoke(currentBefore);

			assertNotSame("When current is not frozen, playTurn should switch to the other monster",
					currentBefore, currentAfter);
			assertTrue("playTurn should move the current monster forward (position must change when move is valid)",
					posAfter != posBefore);
		} catch (Exception e) {
			fail("Unexpected exception in testPlayTurnSwitchesTurnWhenNotFrozen: " + e.getClass().getSimpleName()
					+ " - " + e.getMessage());
		}
	}

	
	@Test(timeout=1000)
	public void testContaminationSockModifyCanisterEnergyMethodExists(){
       try {
			
			Class contaminationSock = Class.forName(contaminationSockPath);
			Method modifyCanisterEnergyMethod = contaminationSock.getDeclaredMethod("modifyCanisterEnergy", Class.forName(monsterPath),int.class);
			assertEquals("modifyCanisterEnergy method in contamination sock class should be a void method", void.class, modifyCanisterEnergyMethod.getReturnType());
			assertTrue("modifyCanisterEnergy method in ContaminationSock class should be a public one.",Modifier.isPublic(modifyCanisterEnergyMethod.getModifiers()));

			
		} catch(ClassNotFoundException e){
			
			fail("The contamination sock class not found");
			
		} catch (NoSuchMethodException e) {
			
			fail("The contamination sock class does not contain modifyCanisterEnergy method which takes monster and canister energy as parameters");
			
		} catch (SecurityException e) {
			
			 fail(e.getClass() + " " + e.getMessage());
			 
		}
	}
	
	@Test(timeout = 1000)
	public void testContaminationSockModifyCanisterEnergyWithShieldAndNegativeEnergy() {
		
		//create dahser Monster
		Object dasherMonster = null;
		try {
			dasherMonster = createDasher();
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}
		
		 Field shieldedField = null;
		
		try {
		    //get the shielded attribute
		
		    shieldedField = dasherMonster.getClass().getSuperclass().getDeclaredField("shielded");
		    shieldedField.setAccessible(true);
		    
		    //get the setShielded method and set the shield attribute to true
		    Method  setShielded = dasherMonster.getClass().getSuperclass().getDeclaredMethod("setShielded", boolean.class);
		    setShielded.invoke(dasherMonster, true);
		    
		} 
		catch (NoSuchFieldException e) {
			fail("The monster class does not have shielded attribute");
		} 
		catch (NoSuchMethodException e) {
			fail("The monster class does not have a setShielded method");
		}
		catch (InvocationTargetException e) {
        	fail("The setShielded method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		catch (IllegalAccessException e) {
			fail(e.getClass() + " " + e.getMessage());
		}
        
		
		// get the energy attribute value
		
		Field energyField = null;
		
		try {
			 energyField = dasherMonster.getClass().getSuperclass().getDeclaredField("energy");
			 energyField.setAccessible(true);
			 int energyValueBeforeInvoking = energyField.getInt(dasherMonster);
				
			 //create a random negative number
			 int negativeEnergy = (new Random().nextInt(100) + 1) * -1;
				
			 //get the modifyCanisterEnergy method
			 Object contaminationSock = createContaminationSock();
			 Method modifyCanisterEnergyMethod = contaminationSock.getClass().getDeclaredMethod("modifyCanisterEnergy",Class.forName(monsterPath) ,int.class);
			 modifyCanisterEnergyMethod.invoke(contaminationSock, dasherMonster, negativeEnergy);
			 assertFalse("The monster should lose its shield if it receives a damage",shieldedField.getBoolean(dasherMonster));
			 assertEquals("The monster's energy should not be effected when it is shielded",energyValueBeforeInvoking ,energyField.getInt(dasherMonster));
		
		} catch (NoSuchFieldException e) {
			fail("The monster class does not have energy attribute");
		}
		catch(NoSuchMethodException e) {
			fail("The ContaminationSock class does not contain modifyCanisterEnergy method");
		}
		catch (InvocationTargetException e) {
			fail("The modifyCanisterEnergy method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		 catch (IllegalAccessException e) {
			 fail(e.getClass() + " " + e.getMessage());
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}	
	}
	
	@Test(timeout = 1000)
	public void testContaminationSockModifyCanisterEnergyWithoutShieldAndNegativeEnergy() {
		
		//create dahser Monster
		Object dynamoMonster = null;
		try {
			dynamoMonster = createDynamo();
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}
		
		 Field shieldedField = null;
		
		try {
		    //get the shielded attribute
		
		    shieldedField = dynamoMonster.getClass().getSuperclass().getDeclaredField("shielded");
		    shieldedField.setAccessible(true);
		    
		} 
		catch (NoSuchFieldException e) {
			fail("The monster class does not have shielded attribute");
		}
        
		// get the energy attribute value
		
		Field energyField = null;
		
		try {
			 energyField = dynamoMonster.getClass().getSuperclass().getDeclaredField("energy");
			 energyField.setAccessible(true);
			 int energyValueBeforeInvoking = energyField.getInt(dynamoMonster);
				
			 //create a random negative number
			 int negativeEnergy = (new Random().nextInt(100) + 1) * -1;
			 
			 //energy after invoking alter energy
			 int energyValueAfterInvoking = energyValueBeforeInvoking + negativeEnergy*2;
			 if(energyValueAfterInvoking < 0)
				 energyValueAfterInvoking = 0;
				
			 
			//get the modifyCanisterEnergy method
			 Object contaminationSock = createContaminationSock();
			 Method modifyCanisterEnergyMethod = contaminationSock.getClass().getDeclaredMethod("modifyCanisterEnergy",Class.forName(monsterPath) ,int.class);
			 modifyCanisterEnergyMethod.invoke(contaminationSock, dynamoMonster, negativeEnergy);
			 assertFalse("The monster's shield should still be deactivated ",shieldedField.getBoolean(dynamoMonster));
			 assertEquals("The monster should receive a damage that alters its energy when it is not shielded",energyValueAfterInvoking ,energyField.getInt(dynamoMonster));
		
		} catch (NoSuchFieldException e) {
			fail("The monster class does not have energy attribute");
		}
		catch(NoSuchMethodException e) {
			fail("The ContaminationSock class does not contain modifyCanisterEnergy method");
		}
		catch (InvocationTargetException e) {
			fail("The modifyCanisterEnergy method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		 catch (IllegalAccessException e) {
			 fail(e.getClass() + " " + e.getMessage());
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}	
	}
	
	/////////////////////////////EnergyStealCard's modifyCanisterEnergy method///////////////////////////
	
	@Test(timeout = 1000)
	public void testEnergyStealCardModifyCanisterEnergyWithShieldAndPositiveEnergy() {

		
		//create multiTasker Monster
		Object multiTaskerMonster = null;
		try {
			multiTaskerMonster = createMultiTasker();
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}
		
		 Field shieldedField = null;
		
		try {
		    //get the shielded attribute
		
		    shieldedField = multiTaskerMonster.getClass().getSuperclass().getDeclaredField("shielded");
		    shieldedField.setAccessible(true);
		    
		    //get the setShielded method and set the shield attribute to true
		    Method  setShielded = multiTaskerMonster.getClass().getSuperclass().getDeclaredMethod("setShielded", boolean.class);
		    setShielded.invoke(multiTaskerMonster, true);
		    
		} 
		catch (NoSuchFieldException e) {
			fail("The monster class does not have shielded attribute");
		} 
		catch (NoSuchMethodException e) {
			fail("The monster class does not have a setShielded method");
		}
		catch (InvocationTargetException e) {
        	fail("The setShielded method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		catch (IllegalAccessException e) {
			fail(e.getClass() + " " + e.getMessage());
		}
        
		
		// get the energy attribute value
		
		Field energyField = null;
		 int energyValueBeforeInvoking = 0;
		 int positiveEnergy = 0;
		
		try {
			 energyField = multiTaskerMonster.getClass().getSuperclass().getDeclaredField("energy");
			 energyField.setAccessible(true);
			 energyValueBeforeInvoking = energyField.getInt(multiTaskerMonster);
				
			 //create a random negative number
			 positiveEnergy = new Random().nextInt(200) + 1;
				
			//get the modifyCanisterEnergy method
			 Object energyStealCard = createEnergyStealCard();
			 Method modifyCanisterEnergyMethod = energyStealCard.getClass().getDeclaredMethod("modifyCanisterEnergy",Class.forName(monsterPath) ,int.class);
			 modifyCanisterEnergyMethod.invoke(energyStealCard, multiTaskerMonster, positiveEnergy);
			 assertTrue("The monster should not lose its shield if it receives a healing energy",shieldedField.getBoolean(multiTaskerMonster));
			 
		
		} catch (NoSuchFieldException e) {
			fail("The monster class does not have energy attribute");
		}
		catch(NoSuchMethodException e) {
			fail("The EnergyStealCard class does not contain modifyCanisterEnergy method");
		}
		catch (InvocationTargetException e) {
			fail("The modifyCanisterEnergy method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		 catch (IllegalAccessException e) {
			 fail(e.getClass() + " " + e.getMessage());
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}	
		
		try {
			
			Field MULTITASKER_BONUS_FIELD = Class.forName(constantsPath).getDeclaredField("MULTITASKER_BONUS");
			int energyValueAfterInvoking = energyValueBeforeInvoking + positiveEnergy + MULTITASKER_BONUS_FIELD.getInt(null);
			
			assertEquals("The monster's energy should be increased when it receives a healing energy",energyValueAfterInvoking ,energyField.getInt(multiTaskerMonster));
		
		} catch (NoSuchFieldException e) {
			fail("The constant class should contains a MULTITASKER_BONUS variable");
		} catch (IllegalAccessException e) {
			fail(e.getClass() + " " + e.getMessage());
		} catch (ClassNotFoundException e) {
			fail("The constants class not found");
		}
	}

	@Test(timeout = 1000)
	public void testEnergyStealCardModifyCanisterEnergyWithoutShieldAndPositiveEnergy() {
		
		//create dasher Monster
		Object dasherMonster = null;
		try {
			dasherMonster = createDasher();
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}
		
		 Field shieldedField = null;
		
		try {
		    //get the shielded attribute
		
		    shieldedField = dasherMonster.getClass().getSuperclass().getDeclaredField("shielded");
		    shieldedField.setAccessible(true);
		    
		} 
		catch (NoSuchFieldException e) {
			fail("The monster class does not have shielded attribute");
		}        
		
		// get the energy attribute value
		
		Field energyField = null;
		 int energyValueBeforeInvoking = 0;
		 int positiveEnergy = 0;
		
		try {
			 energyField = dasherMonster.getClass().getSuperclass().getDeclaredField("energy");
			 energyField.setAccessible(true);
			 energyValueBeforeInvoking = energyField.getInt(dasherMonster);
				
			 //create a random negative number
			 positiveEnergy = new Random().nextInt(200) + 1;
				
			 //get the alterEnergy method
			//get the modifyCanisterEnergy method
			 Object energyStealCard = createEnergyStealCard();
			 Method modifyCanisterEnergyMethod = energyStealCard.getClass().getDeclaredMethod("modifyCanisterEnergy",Class.forName(monsterPath) ,int.class);
			 modifyCanisterEnergyMethod.invoke(energyStealCard, dasherMonster, positiveEnergy);
			 int energyValueAfterInvoking = energyValueBeforeInvoking + positiveEnergy;
			 assertFalse("The monster's shield should still be deactivated when it receives healing energy",shieldedField.getBoolean(dasherMonster));
			 assertEquals("The monster's energy should be increased when it receives a healing energy",energyValueAfterInvoking ,energyField.getInt(dasherMonster));

		
		} catch (NoSuchFieldException e) {
			fail("The monster class does not have energy attribute");
		}
		catch(NoSuchMethodException e) {
			fail("The EnergyStealCard class does not contain modifyCanisterEnergy method");
		}
		catch (InvocationTargetException e) {
			fail("The modifyCanisterEnergy method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		 catch (IllegalAccessException e) {
			 fail(e.getClass() + " " + e.getMessage());
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());

		}	
		
	}
	
	@Test(timeout = 1000)
	public void testEnergyStealCardModifyCanisterEnergyWithShieldAndZeroEnergy() {
		
		//create dasher Monster
		Object dasherMonster = null;
		try {
			dasherMonster = createDasher();
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());
		}
		
		 Field shieldedField = null;
		
		try {
		    //get the shielded attribute
		
		    shieldedField = dasherMonster.getClass().getSuperclass().getDeclaredField("shielded");
		    shieldedField.setAccessible(true);
		    
		    //get the setShielded method and set the shield attribute to true
		    Method  setShielded = dasherMonster.getClass().getSuperclass().getDeclaredMethod("setShielded", boolean.class);
		    setShielded.invoke(dasherMonster, true);
		    
		} 
		catch (NoSuchFieldException e) {
			fail("The monster class does not have shielded attribute");
		} 
		catch (NoSuchMethodException e) {
			fail("The monster class does not have a setShielded method");
		}
		catch (InvocationTargetException e) {
        	fail("The setShielded method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		catch (IllegalAccessException e) {
			fail(e.getClass() + " " + e.getMessage());
		}
        
		
		// get the energy attribute value
		
		Field energyField = null;
		int energyValueBeforeInvoking = 0;
		
		try {
			 energyField = dasherMonster.getClass().getSuperclass().getDeclaredField("energy");
			 energyField.setAccessible(true);
			 energyValueBeforeInvoking = energyField.getInt(dasherMonster);
				
			 //get the alterEnergy method
			 Object energyStealCard = createEnergyStealCard();
			 Method modifyCanisterEnergyMethod = energyStealCard.getClass().getDeclaredMethod("modifyCanisterEnergy",Class.forName(monsterPath) ,int.class);
			 modifyCanisterEnergyMethod.invoke(energyStealCard, dasherMonster, 0);
			 assertTrue("The monster's shield should not be consumed when the energy value is zero",shieldedField.getBoolean(dasherMonster));
			 assertEquals("The monster's energy should be altered accordingly when neither damage nor healing is received",energyValueBeforeInvoking ,energyField.getInt(dasherMonster));
		
		} catch (NoSuchFieldException e) {
			fail("The monster class does not have energy attribute");
		}
		catch(NoSuchMethodException e) {
			fail("The EnergyStealCard class does not contain modifyCanisterEnergy method");
		}
		catch (InvocationTargetException e) {
			fail("The modifyCanisterEnergy method shouldn't throw any exception but got:  "
					+ e.getCause());
		}
		 catch (IllegalAccessException e) {
			 fail(e.getClass() + " " + e.getMessage());
		} catch (Exception e) {
			fail(e.getClass() + " " + e.getMessage());

		}	
			
	}
	
	//////////////////////////////////////////Game's checkWinCondition Method//////////////////////
	
	@Test(timeout=1000)
	public void testGameCheckWinConditionNotAWinSecondCase(){
		
		try {
			Object game = createGameForTesting();
			
			Method getCurrent = Class.forName(gamePath).getDeclaredMethod("getCurrent");
			Object currentPlayer = getCurrent.invoke(game);
			
			Method setPosition = Class.forName(monsterPath).getDeclaredMethod("setPosition", int.class);
			setPosition.invoke(currentPlayer, 99);
			
			Method checkWinCondition = Class.forName(gamePath).getDeclaredMethod("checkWinCondition", Class.forName(monsterPath));
			checkWinCondition.setAccessible(true);
			Object expectedOutput = checkWinCondition.invoke(game, currentPlayer);
			
			assertEquals("The player did not met all winning conditions yet",false,(boolean)expectedOutput);
			
		} catch (Exception e) {
			 fail(e.getClass() + " " + e.getMessage());
		}
		
	}
	
	
	@Test(timeout = 1000)
	public void testIndexToRowColInBoardIsPrivate() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("indexToRowCol",
					int.class);
			int modifiers = method.getModifiers();
			assertTrue(
					"Method indextToRowCol in class Board should be private",
					Modifier.isPrivate(modifiers));
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException e) {
			fail("Error while testing indexToRowCol in Board class: "
					+ e.getMessage());

		}

	}

	@Test(timeout = 1000)
	public void testIndexToRowColInBoardReturnsIntArray() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("indexToRowCol",
					int.class);
			Class<?> returnType = method.getReturnType();

			assertEquals(
					"Method indextToRowCol in class Board should return array of int",
					int[].class, returnType);
		} catch (Exception e) {
			fail("Error while testing indexToRowCol in Board class: "
					+ e.getMessage());
		}

	}

	// the return array size
	@Test(timeout = 1000)
	public void testIndexToRowColInBoardLogicInEvenCaseReturnArraySize() {

		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("indexToRowCol",
					int.class);
			method.setAccessible(true);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. generate random even index
			Random rand = new Random();
			int index = rand.nextInt(100);

			// 2. invoke the method
			int[] result = (int[]) method.invoke(board_object, index);

			// 3. check the output size
			assertEquals(
					"The output of method indextToRowCol in class Board should be 2",
					2, result.length);
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			fail("Error while testing indexToRowCol in Board class: "
					+ e.getMessage());
		}

	}

	@Test(timeout = 1000)
	public void testIndexToRowColInBoardRowLogicInOddCaseCheckCol() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("indexToRowCol",
					int.class);
			method.setAccessible(true);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. generate random index that is in odd row
			Random rand = new Random();
			int row = rand.nextInt(5) * 2 + 1; // 1, 3, 5, 7, 9

			int col;
			if (row == 9) {
				col = rand.nextInt(9); // 0 to 8 (skip 9 only on row 9)
			} else {
				col = rand.nextInt(10); // 0 to 9 (all columns fine)
			}

			int index = row * 10 + col;

			// 2. do logic of the column
			int tempCol = index % 10;
			int expected_column = 10 - 1 - tempCol;

			// 3. invoke the method
			int[] result = (int[]) method.invoke(board_object, index);

			// 4. check row
			assertEquals(
					"The column value of the output of method indextToRowCol in class Board isn't calculated correctly",
					expected_column, result[1]);
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			fail("Error while testing indexToRowCol in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testIndexToRowColInBoardLogicInZeroCaseCheckColumn() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("indexToRowCol",
					int.class);
			method.setAccessible(true);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			int index = 0;
			int[] result = (int[]) method.invoke(board_object, index);
			assertEquals(
					"The column value of the output of method indextToRowCol in class Board isn't calculated correctly",
					0, result[1]);
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			fail("Error while testing indexToRowCol in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testIndexToRowColInBoardLogicIn99CaseCheckColumn() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("indexToRowCol",
					int.class);
			method.setAccessible(true);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			int index = 99;
			int[] result = (int[]) method.invoke(board_object, index);
			assertEquals(
					"The column value of the output of method indextToRowCol in class Board isn't calculated correctly",
					0, result[1]);
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			fail("Error while testing indexToRowCol in Board class: "
					+ e.getMessage());
		}
	}

	// check if method private
	@Test(timeout = 1000)
	public void testGetCellInBoardIsPrivate() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("getCell", int.class);
			int modifiers = method.getModifiers();
			assertTrue("Method getCell in class Board should be private",
					Modifier.isPrivate(modifiers));
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException e) {
			fail("Error while testing getCell in Board class: "
					+ e.getMessage());
		}
	}

	// return the correct data type
	@Test(timeout = 1000)
	public void testGetCellInBoardReturnsIntArray() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("getCell", int.class);
			Class<?> returnType = method.getReturnType();
			assertEquals(
					"Method getCell in class Board should return array of int",
					Class.forName(cellPath), returnType);
		} catch (Exception e) {
			fail("Error while testing getCell in Board class: "
					+ e.getMessage());
		}
	}

	// check if method private
	@Test(timeout = 1000)
	public void testSetCellInClassBoardIsPrivate() {
		try {
			Class<?> board_class = Class.forName(boardPath);

			Method method = board_class.getDeclaredMethod("setCell", int.class,
					Class.forName(cellPath));
			int modifiers = method.getModifiers();

			assertTrue("setCell in class Board should be private",
					Modifier.isPrivate(modifiers));
		} catch (Exception e) {
			fail("Error while testing setCell in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testSetCellInBoardIsVoid() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("setCell", int.class,
					Class.forName(cellPath));
			method.setAccessible(true);
			assertEquals("setCell in class Board should return void",
					void.class, method.getReturnType());
		} catch (Exception e) {
			fail("Error while testing setCell in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testInitializeBoardLogicOddCellsTypePlacement() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("initializeBoard",
					ArrayList.class);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. call readCells methods from data loader to get special cells

			// 1.1. get data loader class and method readCells
			Class<?> dataloader_class = Class.forName(dataLoaderPath);
			Method read_cells_method = dataloader_class
					.getDeclaredMethod("readCells");
			// 1.2 create data loader object
			Constructor<?> data_loader_constructor = Class.forName(
					dataLoaderPath).getConstructor();
			Object dataloader = data_loader_constructor.newInstance();
			// 1.3 call method read cells and retrieve the array of special
			// cells
			ArrayList<Object> cells = (ArrayList<Object>) read_cells_method
					.invoke(dataloader);
			ArrayList<Object> expectedDoors = new ArrayList<>();
			for (Object cell : cells) {
				if (Class.forName(doorCellPath).isInstance(cell)) {
					expectedDoors.add(cell);
				}
			}
			// 2. call initialize board
			method.invoke(board_object, cells);

			// 3. get boardCells
			Field field = board_class.getDeclaredField("boardCells");
			field.setAccessible(true);
			Object[][] boardCells = (Object[][]) field.get(board_object);

			// 4. check the even indexes has normal cells (not all even)
			// evens only that don't exist in CARD_CELL_INDICES &
			// SOCK_CELL_INDICES
			// & CONVEYOR_CELL_INDICES & MONSTER_CELL_INDICES

			// 4.1 get the constants
			Class<?> constants_class = Class.forName(constantsPath);
			Field MONSTER_CELL_INDICES = constants_class
					.getDeclaredField("MONSTER_CELL_INDICES");
			int[] monster_cells_indices = (int[]) MONSTER_CELL_INDICES
					.get(null);
			Field CONVEYOR_CELL_INDICES = constants_class
					.getDeclaredField("CONVEYOR_CELL_INDICES");
			int[] conveyor_cells_indices = (int[]) CONVEYOR_CELL_INDICES
					.get(null);
			Field SOCK_CELL_INDICES = constants_class
					.getDeclaredField("SOCK_CELL_INDICES");
			int[] sock_cells_indices = (int[]) SOCK_CELL_INDICES.get(null);
			Field CARD_CELL_INDICES = constants_class
					.getDeclaredField("CARD_CELL_INDICES");
			int[] card_cells_indices = (int[]) CARD_CELL_INDICES.get(null);

			// 4.2 loop over board size and check if the index
			// even and if so get the cell in this index
			// and check if it's a door
			for (int i = 0; i < 100; i++) {

				if (i % 2 == 1 && !contains(monster_cells_indices, i)
						&& !contains(conveyor_cells_indices, i)
						&& !contains(sock_cells_indices, i)
						&& !contains(card_cells_indices, i)) {

					// 4.2.1 get the cell in this index
					int row = i / 10;
					int col = i % 10;
					if (row % 2 == 1)
						col = 10 - 1 - col;
					Object current_cell = boardCells[row][col];

					assertNotNull("Cell at index " + i + " should not be null",
							current_cell);
					assertEquals("Index " + i
							+ " in the board should contain a door Cell",
							Class.forName(doorCellPath),
							current_cell.getClass());
				}
			}
		} catch (Exception e) {
			fail("Error while testing initializeBoard in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testInitializeBoardLogicConveyorContentPlacement() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("initializeBoard",
					ArrayList.class);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. call readCells methods from data loader to get special cells

			// 1.1. get data loader class and method readCells
			Class<?> dataloader_class = Class.forName(dataLoaderPath);
			Method read_cells_method = dataloader_class
					.getDeclaredMethod("readCells");
			// 1.2 create data loader object
			Constructor<?> data_loader_constructor = Class.forName(
					dataLoaderPath).getConstructor();
			Object dataloader = data_loader_constructor.newInstance();
			// 1.3 call method read cells and retrieve the array of special
			// cells
			ArrayList<Object> cells = (ArrayList<Object>) read_cells_method
					.invoke(dataloader);
			ArrayList<Object> expectedConveyors = new ArrayList<>();
			for (Object cell : cells) {
				if (Class.forName(conveyorBeltPath).isInstance(cell)) {
					expectedConveyors.add(cell);
				}
			}
			// 2. call initialize board
			method.invoke(board_object, cells);

			// 3. get boardCells
			Field field = board_class.getDeclaredField("boardCells");
			field.setAccessible(true);
			Object[][] boardCells = (Object[][]) field.get(board_object);

			// 4. check the even indexes has normal cells (not all even)
			// evens only that don't exist in CARD_CELL_INDICES &
			// SOCK_CELL_INDICES
			// & CONVEYOR_CELL_INDICES & MONSTER_CELL_INDICES

			// 4.1 get the constants
			Class<?> constants_class = Class.forName(constantsPath);
			Field CONVEYOR_CELL_INDICES = constants_class
					.getDeclaredField("CONVEYOR_CELL_INDICES");
			int[] conveyor_cells_indices = (int[]) CONVEYOR_CELL_INDICES
					.get(null);
			// 4.2 loop over board size and check if the index
			// conveyor and if so get the cell in this index
			// and check its content
			for (int j = 0; j < conveyor_cells_indices.length; j++) {
				int index = conveyor_cells_indices[j];

				int row = index / 10;
				int col = index % 10;
				if (row % 2 == 1)
					col = 10 - 1 - col;

				Object current_cell = boardCells[row][col];

				assertNotNull("Cell at index " + index + " should not be null",
						current_cell);
				assertSame(
						"Index "
								+ index
								+ " should contain the exact conveyorCell originally assigned to that index",
						expectedConveyors.get(j), current_cell);
			}
		} catch (Exception e) {
			fail("Error while testing initializeBoard in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testInitializeBoardLogicContaminationSockContentPlacement() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("initializeBoard",
					ArrayList.class);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. call readCells methods from data loader to get special cells

			// 1.1. get data loader class and method readCells
			Class<?> dataloader_class = Class.forName(dataLoaderPath);
			Method read_cells_method = dataloader_class
					.getDeclaredMethod("readCells");
			// 1.2 create data loader object
			Constructor<?> data_loader_constructor = Class.forName(
					dataLoaderPath).getConstructor();
			Object dataloader = data_loader_constructor.newInstance();
			// 1.3 call method read cells and retrieve the array of special
			// cells
			ArrayList<Object> cells = (ArrayList<Object>) read_cells_method
					.invoke(dataloader);
			ArrayList<Object> expectedContaminationSock = new ArrayList<>();
			for (Object cell : cells) {
				if (Class.forName(contaminationSockPath).isInstance(cell)) {
					expectedContaminationSock.add(cell);
				}
			}
			// 2. call initialize board
			method.invoke(board_object, cells);

			// 3. get boardCells
			Field field = board_class.getDeclaredField("boardCells");
			field.setAccessible(true);
			Object[][] boardCells = (Object[][]) field.get(board_object);

			// 4. check the even indexes has normal cells (not all even)
			// evens only that don't exist in CARD_CELL_INDICES &
			// SOCK_CELL_INDICES
			// & CONVEYOR_CELL_INDICES & MONSTER_CELL_INDICES

			// 4.1 get the constants
			Class<?> constants_class = Class.forName(constantsPath);
			Field SOCK_CELL_INDICES = constants_class
					.getDeclaredField("SOCK_CELL_INDICES");
			int[] sock_cells_indices = (int[]) SOCK_CELL_INDICES.get(null);

			// 4.2 loop over board size and check if the index
			// even and if so get the cell in this index
			// and check if it's a door
			for (int j = 0; j < sock_cells_indices.length; j++) {
				int index = sock_cells_indices[j];
				// 4.2.1 get the cell in this index
				int row = index / 10;
				int col = index % 10;
				if (row % 2 == 1)
					col = 10 - 1 - col;
				Object current_cell = boardCells[row][col];

				assertNotNull("Cell at index " + index + " should not be null",
						current_cell);

				assertSame(
						"Index "
								+ index
								+ " should contain the exact sock Cell originally assigned to that index",
						expectedContaminationSock.get(j), current_cell);
			}
		} catch (Exception e) {
			fail("Error while testing initializeBoard in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testInitializeBoardLogicMonsterCellsMonster() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("initializeBoard",
					ArrayList.class);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. call readCells methods from data loader to get special cells
			// 1.1. get data loader class and method readCells
			Class<?> dataloader_class = Class.forName(dataLoaderPath);
			Method read_cells_method = dataloader_class
					.getDeclaredMethod("readCells");
			// 1.2 create data loader object
			Constructor<?> data_loader_constructor = Class.forName(
					dataLoaderPath).getConstructor();
			Object dataloader = data_loader_constructor.newInstance();
			// 1.3 call method read cells and retrieve the array of special
			// cells
			ArrayList<Object> cells = (ArrayList<Object>) read_cells_method
					.invoke(dataloader);

			// 2. get and initialize stationedMonsters
			Field stationed_monsters = board_class
					.getDeclaredField("stationedMonsters");
			stationed_monsters.setAccessible(true);
			ArrayList<Object> stationed_monsters_array = (ArrayList<Object>) stationed_monsters
					.get(null);
			stationed_monsters_array.clear();

			// 2.1 initialize 6 monsters (the size of indices array) and add
			// them in
			// stationed_monsters
			Random rand = new Random();
			int[] count = new int[5];
			int i = 0;
			while (i < 6) {
				int type = rand.nextInt(4) + 1;

				if (count[type] < 2) {

					Object monster = null;

					switch (type) {
					case 1:
						monster = createDasher();
						break;
					case 2:
						monster = createDynamo();
						break;
					case 3:
						monster = createMultiTasker();
						break;
					case 4:
						monster = createSchemer();
						break;
					}

					stationed_monsters_array.add(monster);
					count[type]++;

					i++;
				}
			}
			// 2.2 set the value of stationed
			stationed_monsters.set(null, stationed_monsters_array);

			// 3. call initialize board
			method.invoke(board_object, cells);

			// 4. get boardCells
			Field field = board_class.getDeclaredField("boardCells");
			field.setAccessible(true);
			Object[][] boardCells = (Object[][]) field.get(board_object);

			// 5. check the correct placement type
			// 5.1 get the monster cells indices
			Class<?> constants_class = Class.forName(constantsPath);
			Field MONSTER_CELL_INDICES = constants_class
					.getDeclaredField("MONSTER_CELL_INDICES");
			int[] monster_cells_indices = (int[]) MONSTER_CELL_INDICES
					.get(null);

			// 5.2 loop over the monster cells indices and get the correct cell
			// from
			// the board
			for (int j = 0; j < monster_cells_indices.length; j++) {
				int index = monster_cells_indices[j];
				int row = index / 10;
				int col = index % 10;
				if (row % 2 == 1)
					col = 10 - 1 - col;
				Object current_cell = boardCells[row][col];
				// 5.2.1 check the monster in cell is the same on as the monster
				// in
				// stationary
				// 5.2.1.1 get the corresponding monster from stationed monsters
				Object curent_monster = stationed_monsters_array.get(j);
				// 5.2.1.2 get the corresponding monster from monsterCell

				Field monster_in_cell_field = Class.forName(monsterCellPath)
						.getDeclaredField("cellMonster");
				monster_in_cell_field.setAccessible(true);
				Object monster_in_board = monster_in_cell_field
						.get(current_cell);

				assertNotNull("Cell at index " + index + " should not be null",
						current_cell);
				assertSame("MonsterCell at index " + index
						+ " should contain the same monster object",
						curent_monster, monster_in_board);
			}
		} catch (Exception e) {
			fail("Error while testing initializeBoard in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testUpdateMonsterPositionsIsPrivate() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod(
					"updateMonsterPositions", Class.forName(monsterPath),
					Class.forName(monsterPath));
			int modifiers = method.getModifiers();
			assertTrue(
					"Method updateMonsterPositions in class board should be private",
					Modifier.isPrivate(modifiers));
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException e) {
			fail("Error while testing updateMonsterPositions in board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testUpdateMonsterPositionsInClassBoardCheckOpponent() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod(
					"updateMonsterPositions", Class.forName(monsterPath),
					Class.forName(monsterPath));
			method.setAccessible(true);
			Constructor<?> board_constructor = Class.forName(boardPath)
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());
			// 1. get board and set some random cells with monsters
			Field boardCells_field = board_class.getDeclaredField("boardCells");
			boardCells_field.setAccessible(true);
			// 1.1 set board
			Object[][] boardCells = (Object[][]) boardCells_field
					.get(board_object);

			Constructor<?> cell_constructor = Class.forName(cellPath)
					.getConstructor(String.class);

			for (int row = 0; row < 10; row++)
				for (int col = 0; col < 10; col++)
					boardCells[row][col] = cell_constructor.newInstance("cell");
			boardCells_field.set(board_object, boardCells);
			// 2. create 2 monster
			Object player = createDynamo();
			Object opponent = createSchemer();
			// 3. generate random positions for 2 monster and set the positions
			// of
			// the 2 monsters
			// 3.1 generate random positions
			Random rand = new Random();
			int playerPosition, opponentPosition;
			playerPosition = 0;
			opponentPosition = rand.nextInt(99) + 1;

			// 3.2 get the position field and set the monster fields
			Field positionField = Class.forName(monsterPath).getDeclaredField(
					"position");
			positionField.setAccessible(true);
			positionField.set(player, playerPosition);
			positionField.set(opponent, opponentPosition);
			// 4. invoke method
			method.invoke(board_object, player, opponent);

			// 5. check if the player is placed on the correct cell in the board

			// 5.1 calculate the expected position in the board
			int row = opponentPosition / 10;
			int col = opponentPosition % 10;
			if (row % 2 == 1)
				col = 10 - 1 - col;
			Object expected_cell = boardCells[row][col];

			// 5.2 get monster in the expected cell
			Field montser_cell_field = Class.forName(cellPath)
					.getDeclaredField("monster");
			montser_cell_field.setAccessible(true);
			Object expected_monster_in_expected_cell = montser_cell_field
					.get(expected_cell);

			// 5.3 check if it's the same monster in the cell
			assertSame(
					"After updateMonsterPositions is called, opponent should be placed at cell index "
							+ opponentPosition + " but was not", opponent,
					expected_monster_in_expected_cell);
		} catch (Exception e) {
			fail("Error while testing updateMonsterPositions in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testMoveMonsterInClassBoardInvalidMoveException() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("moveMonster",
					Class.forName(monsterPath), int.class,
					Class.forName(monsterPath));
			method.setAccessible(true);

			Constructor<?> board_constructor = board_class
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. initialize all cells with plain Cell objects
			Field boardCells_field = board_class.getDeclaredField("boardCells");
			boardCells_field.setAccessible(true);
			Object[][] boardCells = (Object[][]) boardCells_field
					.get(board_object);
			Constructor<?> cell_constructor = Class.forName(cellPath)
					.getConstructor(String.class);
			for (int row = 0; row < 10; row++)
				for (int col = 0; col < 10; col++)
					boardCells[row][col] = cell_constructor.newInstance("cell");

			// 2. create player and opponent
			Object player = createSchemer();
			Object opponent = createDynamo();

			// 3. generate random positions guaranteeing collision
			Random rand = new Random();
			int roll = rand.nextInt(6) + 1;
			int opponentPosition = rand.nextInt(87) + 6;
			int playerPosition = opponentPosition - roll;

			// 4. set positions
			Field positionField = Class.forName(monsterPath).getDeclaredField(
					"position");
			positionField.setAccessible(true);
			positionField.set(player, playerPosition);
			positionField.set(opponent, opponentPosition);

			// 5. invoke and check exception
			try {
				method.invoke(board_object, player, roll, opponent);
				fail("Expected InvalidMoveException to be thrown but was not");
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				assertTrue("Expected InvalidMoveException but got "
						+ cause.getClass().getName(), cause.getClass()
						.getName().equals(invalidMoveExceptionPath));
			}
		} catch (Exception e) {
			fail("Error while testing moveMonster in Board class: "
					+ e.getMessage());
		}
	}

	// Case 1b: "opponentMonster confusionTurns decremented"
	@Test(timeout = 1000)
	public void testMoveMonsterInClassBoardConfusionDecrementOpponentMonster() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("moveMonster",
					Class.forName(monsterPath), int.class,
					Class.forName(monsterPath));
			method.setAccessible(true);

			Constructor<?> board_constructor = board_class
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. initialize all cells with plain Cell objects
			Field boardCells_field = board_class.getDeclaredField("boardCells");
			boardCells_field.setAccessible(true);
			Object[][] boardCells = (Object[][]) boardCells_field
					.get(board_object);
			Constructor<?> cell_constructor = Class.forName(cellPath)
					.getConstructor(String.class);
			for (int row = 0; row < 10; row++)
				for (int col = 0; col < 10; col++)
					boardCells[row][col] = cell_constructor.newInstance("cell");

			// 2. create player and opponent
			Object player = createSchemer();
			Object opponent = createDynamo();

			// 3. generate random positions — no collision
			Random rand = new Random();
			int roll = rand.nextInt(6) + 1;
			int playerPosition = rand.nextInt(87) + 1;
			int opponentPosition;
			do {
				opponentPosition = rand.nextInt(100);
			} while (opponentPosition == playerPosition + roll);

			// 4. set positions
			Field positionField = Class.forName(monsterPath).getDeclaredField(
					"position");
			positionField.setAccessible(true);
			positionField.set(player, playerPosition);
			positionField.set(opponent, opponentPosition);

			// 5. set confusionTurns for both monsters
			int confusionTurns = rand.nextInt(3) + 1;
			Field confusionTurnsField = Class.forName(monsterPath)
					.getDeclaredField("confusionTurns");
			confusionTurnsField.setAccessible(true);
			confusionTurnsField.set(player, confusionTurns);
			confusionTurnsField.set(opponent, confusionTurns);

			// 6. invoke moveMonster
			method.invoke(board_object, player, roll, opponent);

			// 7. get opponent confusionTurns after calling
			int opponentConfusionTurnsAfter = (int) confusionTurnsField
					.get(opponent);

			// 8. assert opponentMonster confusionTurns decremented
			assertEquals(
					"After moveMonster, opponentMonster confusionTurns should be decremented from ",
					confusionTurns - 1, opponentConfusionTurnsAfter);
		} catch (Exception e) {
			fail("Error while testing moveMonster in Board class: "
					+ e.getMessage());
		}
	}

	// Case 3a — currentMonster confusionTurns stays 0
	@Test(timeout = 1000)
	public void testMoveMonsterInClassBoardNoConfusionCurrentMonster() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("moveMonster",
					Class.forName(monsterPath), int.class,
					Class.forName(monsterPath));
			method.setAccessible(true);

			Constructor<?> board_constructor = board_class
					.getConstructor(ArrayList.class);
			Object board_object = board_constructor
					.newInstance(new ArrayList<>());

			// 1. initialize all cells with plain Cell objects
			Field boardCells_field = board_class.getDeclaredField("boardCells");
			boardCells_field.setAccessible(true);
			Object[][] boardCells = (Object[][]) boardCells_field
					.get(board_object);
			Constructor<?> cell_constructor = Class.forName(cellPath)
					.getConstructor(String.class);
			for (int row = 0; row < 10; row++)
				for (int col = 0; col < 10; col++)
					boardCells[row][col] = cell_constructor.newInstance("cell");

			// 2. create player and opponent
			Object player = createSchemer();
			Object opponent = createDynamo();

			// 3. generate random positions — no collision
			Random rand = new Random();
			int roll = rand.nextInt(6) + 1;
			int playerPosition = rand.nextInt(87) + 1;
			int opponentPosition;
			do {
				opponentPosition = rand.nextInt(100);
			} while (opponentPosition == playerPosition + roll);

			// 4. set positions
			Field positionField = Class.forName(monsterPath).getDeclaredField(
					"position");
			positionField.setAccessible(true);
			positionField.set(player, playerPosition);
			positionField.set(opponent, opponentPosition);

			// 5. set confusionTurns to 0 for both — not confused
			Field confusionTurnsField = Class.forName(monsterPath)
					.getDeclaredField("confusionTurns");
			confusionTurnsField.setAccessible(true);
			confusionTurnsField.set(player, 0);
			confusionTurnsField.set(opponent, 0);

			// 6. invoke moveMonster
			method.invoke(board_object, player, roll, opponent);

			// 7. assert currentMonster confusionTurns stays 0
			int confusionTurnsAfter = (int) confusionTurnsField.get(player);
			assertEquals(
					"Monster is not confused so confusionTurns should stay 0 but was ",
					0, confusionTurnsAfter);
		} catch (Exception e) {
			fail("Error while testing moveMonster in Board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testMoveInClassMonsterCaseBaseCase() {
		try {
			Class<?> monster_class = Class.forName(monsterPath);
			Method method = monster_class.getDeclaredMethod("move", int.class);
			Class<?> dynamo_class = Class.forName(dynamoPath);
			Object dynamo_object = createDynamo();

			// 1. calculate the expected position after moving
			int basePoistion = 0;
			Random rand = new Random();
			int distance = rand.nextInt(6) + 1;
			int expected_position = (basePoistion + distance) % 100;

			// 2. invoke method

			method.invoke(dynamo_object, distance);

			// 3. get the position after calling the method

			Field position = dynamo_class.getSuperclass().getDeclaredField(
					"position");
			position.setAccessible(true);
			int monster_position = (int) position.get(dynamo_object);

			assertEquals("After moving the monster from position " + 0
					+ " by distance " + distance
					+ " the new position should be", expected_position,
					monster_position);
		} catch (Exception e) {
			fail("Error while testing move in monster class: " + e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testSchemerDoesNotOverrideMove() {
		try {
			Class<?> schemer_class = Class.forName(schemerPath);
			try {
				schemer_class.getDeclaredMethod("move", int.class);
				fail("Schemer should NOT override move(int distance) method");
			} catch (NoSuchMethodException e) {
			}
		} catch (ClassNotFoundException e) {
			fail("Error while accessing Schemer class " + e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testDynamoDoesNotOverrideMove() {
		try {
			Class<?> dynamo_class = Class.forName(dynamoPath);
			try {
				dynamo_class.getDeclaredMethod("move", int.class);
				fail("Dynamo should NOT override move(int distance) method");
			} catch (NoSuchMethodException e) {
			}
		} catch (ClassNotFoundException e) {
			fail("Error while accessing Dynamo class " + e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testMoveInClassDasherCase2TransitionChangePositionCheck() {
		try {
			Class<?> dasher_class = Class.forName(dasherPath);
			Method method = dasher_class.getDeclaredMethod("move", int.class);
			Object dasher_object = createDasher();

			// 1. set the momentum turn and distance

			Random rand = new Random();
			int basePosition = rand.nextInt(60) + 1;
			int distance = rand.nextInt(6) + 1;
			int momentumTurns = 1;

			Field position = dasher_class.getSuperclass().getDeclaredField(
					"position");
			position.setAccessible(true);
			position.set(dasher_object, basePosition);

			Field momentumTurnsField = dasher_class
					.getDeclaredField("momentumTurns");
			momentumTurnsField.setAccessible(true);
			momentumTurnsField.set(dasher_object, momentumTurns);

			// 2. invoke method

			method.invoke(dasher_object, distance);
			position.set(dasher_object, ((distance * 3) + basePosition) % 100);
			int position_after_1_call = (int) position.get(dasher_object);
			distance = rand.nextInt(6) + 1;
			method.invoke(dasher_object, distance);

			// 3. calculate expected position

			int expected_position = ((distance * 2) + position_after_1_call) % 100;

			// 4. get the position after 2 calls
			int position_after_2_calls = (int) position.get(dasher_object);

			assertEquals(
					"Dasher with momentumTurns=1: first move should use 3x speed, second move should use 2x speed. "
							+ "After moving dasher 2 times the new position should be",
					(expected_position), position_after_2_calls);
		} catch (Exception e) {
			fail("Error while testing move in Dasher class: " + e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testMoveInClassMultiTaskerCase3CheckPosition() {
		try {
			Class<?> multiTasker_class = Class.forName(multiTaskerPath);
			Method method = multiTasker_class.getDeclaredMethod("move",
					int.class);
			Object multiTasker_object = createMultiTasker();

			// 1. set distance, monster position, and normalSpeedTurns

			Random rand = new Random();
			int basePosition = rand.nextInt(90) + 1;
			int distance = rand.nextInt(6) + 1;
			int normalSpeedTurns = 1;

			Field position = multiTasker_class.getSuperclass()
					.getDeclaredField("position");
			position.setAccessible(true);
			position.set(multiTasker_object, basePosition);

			Field normalSpeedTurnsField = multiTasker_class
					.getDeclaredField("normalSpeedTurns");
			normalSpeedTurnsField.setAccessible(true);
			normalSpeedTurnsField.set(multiTasker_object, normalSpeedTurns);

			// 2. invoke method

			method.invoke(multiTasker_object, distance);
			distance = rand.nextInt(6) + 1;
			int monster_position = (int) position.get(multiTasker_object);
			method.invoke(multiTasker_object, distance);

			// 4. get the new position after calling

			int expected_position = (monster_position + (distance / 2)) % 100;
			monster_position = (int) position.get(multiTasker_object);

			assertEquals(
					"MultiTasker with normalSpeedTurns=1: first move uses normal speed, "
							+ "so normalSpeedTurns becomes 0, and second move uses 1/2 speed. "
							+ "After moving multiTasker from position "
							+ basePosition + " by distance " + distance
							+ " the new position should be", expected_position,
					monster_position);
		} catch (Exception e) {
			fail("Error while testing move in MultiTasker class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testPerformActionInClassSwapperCardCase1OpponentCheck() {
		try {
			Class<?> swapperCard_class = Class.forName(swapperCardPath);
			Method method = swapperCard_class.getDeclaredMethod(
					"performAction", Class.forName(monsterPath),
					Class.forName(monsterPath));
			Object swapper_card = createSwapperCard();

			// 1. create 2 monster one player and the other is opponent

			Object current_player = createDasher();
			Object opponent_player = createDynamo();

			// 2. set the positions so the current play be behind the opponent
			Field position = Class.forName(monsterPath).getDeclaredField(
					"position");
			position.setAccessible(true);

			Random rand = new Random();
			int current_player_position, opponent_position;
			do {
				current_player_position = rand.nextInt(99);
				opponent_position = rand.nextInt(99);
			} while (current_player_position >= opponent_position);

			position.set(current_player, current_player_position);
			position.set(opponent_player, opponent_position);

			// 3. invoke method
			method.invoke(swapper_card, current_player, opponent_player);
			// 4. get the position of current player
			int opponent_player_position_after_calling = (int) position
					.get(opponent_player);

			// 5. check current player position after calling method

			assertEquals("Opponent was at " + opponent_position
					+ " (ahead of player at " + current_player_position
					+ "), after swap opponent should be at "
					+ current_player_position, current_player_position,
					opponent_player_position_after_calling);
		} catch (Exception e) {
			fail("Error while testing performAction in SwapperCard class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testPerformActionInClassSwapperCardCase2OpponentCheck() {
		try {
			Class<?> swapperCard_class = Class.forName(swapperCardPath);
			Method method = swapperCard_class.getDeclaredMethod(
					"performAction", Class.forName(monsterPath),
					Class.forName(monsterPath));
			Object swapper_card = createSwapperCard();

			// 1. create 2 monster one player and the other is opponent

			Object current_player = createDasher();
			Object opponent_player = createDynamo();

			// 2. set the positions so the current play be behind the opponent
			Field position = Class.forName(monsterPath).getDeclaredField(
					"position");
			position.setAccessible(true);

			Random rand = new Random();
			int current_player_position, opponent_position;
			do {
				current_player_position = rand.nextInt(99);
				opponent_position = rand.nextInt(99);
			} while (current_player_position < opponent_position);

			position.set(current_player, current_player_position);
			position.set(opponent_player, opponent_position);

			// 3. invoke method
			method.invoke(swapper_card, current_player, opponent_player);
			// 4. get the position of current player
			int opponent_player_position_after_calling = (int) position
					.get(opponent_player);

			// 5. check current player position after calling method

			assertEquals(
					"Opponent was at "
							+ opponent_position
							+ " (behind player at "
							+ current_player_position
							+ "), no swap should happen, opponent position should stay "
							+ opponent_position, opponent_position,
					opponent_player_position_after_calling);
		} catch (Exception e) {
			fail("Error while testing performAction in SwapperCard class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testPerformActionInClassStartOverCardCase1CurrentPlayerCheck() {
		try {
			Class<?> startOverCard_class = Class.forName(startOverCardPath);
			Method method = startOverCard_class.getDeclaredMethod(
					"performAction", Class.forName(monsterPath),
					Class.forName(monsterPath));
			Object startOver_card = createStartOverCard();

			// 1. create 2 monster one player and the other is opponent

			Object current_player = createDasher();
			Object opponent_player = createDynamo();

			// 2. set the positions
			Field position = Class.forName(monsterPath).getDeclaredField(
					"position");
			position.setAccessible(true);

			Random rand = new Random();
			int current_player_position, opponent_position;
			do {
				current_player_position = rand.nextInt(98) + 1;
				opponent_position = rand.nextInt(98) + 1;
			} while (current_player_position == opponent_position);

			position.set(current_player, current_player_position);
			position.set(opponent_player, opponent_position);

			// 3. set lucky
			Field lucky_Field = Class.forName(startOverCardPath)
					.getSuperclass().getDeclaredField("lucky");
			lucky_Field.setAccessible(true);
			boolean lucky = (boolean) lucky_Field.get(startOver_card);
			if (!lucky) {
				lucky_Field.set(startOver_card, true);
			}
			// 4. invoke the method

			method.invoke(startOver_card, current_player, opponent_player);

			// 5. check the position of current player

			int current_position_after_calling = (int) position
					.get(current_player);
			assertEquals("StartOverCard is lucky, player was at "
					+ current_player_position + " and should NOT move",
					current_player_position, current_position_after_calling);
		} catch (Exception e) {
			fail("Error while testing performAction in StartOverCard class: "
					+ e.getMessage());
		}

	}

	@Test(timeout = 1000)
	public void testInitializeBaordInBoardVoid() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("initializeBoard",
					ArrayList.class);
			assertEquals(
					"Method initializeBoard in class Board should be void",
					void.class, method.getReturnType());
		} catch (Exception e) {
			fail("Error while testing IndexToRowCol in board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testUpdateMonsterPositionsInBoardVoid() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod(
					"updateMonsterPositions", Class.forName(monsterPath),
					Class.forName(monsterPath));
			assertEquals(
					"Method updateMonsterPositions in class Board should be void",
					void.class, method.getReturnType());
		} catch (Exception e) {
			fail("Error while testing updateMonsterPositions in board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testMoveMonsterInBoardVoid() {
		try {
			Class<?> board_class = Class.forName(boardPath);
			Method method = board_class.getDeclaredMethod("moveMonster",
					Class.forName(monsterPath), int.class,
					Class.forName(monsterPath));
			assertEquals("Method moveMonster in class Board should be void",
					void.class, method.getReturnType());
		} catch (Exception e) {
			fail("Error while testing moveMonster in board class: "
					+ e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testMoveInMonsterVoid() {
		try {
			Class<?> monster_class = Class.forName(monsterPath);
			Method method = monster_class.getDeclaredMethod("move", int.class);
			assertEquals("Method move in class monster should be void",
					void.class, method.getReturnType());
		} catch (Exception e) {
			fail("Error while testing move in monster class: " + e.getMessage());
		}
	}

	@Test(timeout = 1000)
	public void testTransportInTransportCellVoid() {
		try {
			Class<?> monster_class = Class.forName(transportCellPath);
			Method method = monster_class.getDeclaredMethod("transport",
					Class.forName(monsterPath));
			assertEquals(
					"Method transport in class TransportCell should be void",
					void.class, method.getReturnType());
		} catch (Exception e) {
			fail("Error while testing move in monster class: " + e.getMessage());
		}
	}



//	Dasher PowerUp
	@Test(timeout = 1000)
	public void testDasherPowerUp() {
			try {
				Object dasher= createDasher();
				Object opponent = createSchemer();
				Field fieldMomentumTurns= Class.forName(dasherPath).getDeclaredField("momentumTurns");
				fieldMomentumTurns.setAccessible(true);
				fieldMomentumTurns.set(dasher, 0);
				
				Method methodExecutePowerupEffect = Class.forName(dasherPath).getDeclaredMethod("executePowerupEffect", Class.forName(monsterPath));
				methodExecutePowerupEffect.setAccessible(true);
				methodExecutePowerupEffect.invoke(dasher,opponent);
				assertEquals("The Dasher's momentum turns is not updated correctly after calling the executePowerupEffect, ", 3, fieldMomentumTurns.get(dasher));
				
				
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				fail(e.getClass()+ "Error while creating Dasher object, make sure you use are following milestone 1 requirement.");
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				fail("Attribute momentumTurns is expected to be in Dasher class");
			}
	}

	
//Test Monster decrementConfusion CaseNotOrigiallyConfused
	@Test(timeout = 1000)
	public void testmonsterDecrementConfusionCaseNotOrigiallyConfused() {
			try {
				Object player = createDynamo();
				
				Field fieldConfusionTurns= Class.forName(monsterPath).getDeclaredField("confusionTurns");
				fieldConfusionTurns.setAccessible(true);
				fieldConfusionTurns.set(player, 0);
				
				Method methodDecrementConfusion = Class.forName(monsterPath).getDeclaredMethod("decrementConfusion");
				methodDecrementConfusion.setAccessible(true);
				methodDecrementConfusion.invoke(player);
				assertEquals("The Player was originally not confused, ", 0, fieldConfusionTurns.get(player));
				
				
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				fail(e.getClass()+ "Error while creating Monster object, make sure you use are following milestone 1 requirement.");
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				fail("Attribute confusionTurns is expected to be in Monster class");
			} 
	}

//	Monster isConfused() Case True
	@Test(timeout = 1000)
	public void testMonsterIsConfusedCaseTrue() {
			try {
				Object monster = createDynamo();
				Field fieldConfusionTurns= Class.forName(monsterPath).getDeclaredField("confusionTurns");
				fieldConfusionTurns.setAccessible(true);
				fieldConfusionTurns.set(monster, (int)Math.random()+1);
				
				Method methodIsConfused = Class.forName(monsterPath).getDeclaredMethod("isConfused");
				methodIsConfused.setAccessible(true);
				Object confused_return = methodIsConfused.invoke(monster);
				assertTrue("The monster should be confused", (boolean)confused_return);
				
				
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				fail(e.getClass()+ "Error while creating Dynamo object, make sure you use are following milestone 1 requirement.");
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				fail("Attribute confusionTurns is expected to be in Monster class");
			}
	}
// test ExecutePowerupEffect Is An Abstract Method
	@Test(timeout = 1000)
	public void testExecutePowerupEffectIsAnAbstractMethod() throws Exception {
		Method methodExecutePowerupEffect = Class.forName(monsterPath).getDeclaredMethod("executePowerupEffect",Class.forName(monsterPath));
		testMethodIsAbstract(methodExecutePowerupEffect);
	}

	
	////////////////////////////////////////HELPER METHODS///////////////////

	private boolean contains(int[] arr, int value) {
		for (int x : arr) {
			if (x == value) return true;
		}
		return false;
	}
	private void testMethodIsAbstract(Method aMethod) {
		assertTrue(aMethod.getName() + " should be an abtract Method.", 
				Modifier.isAbstract(aMethod.getModifiers()));
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

	private Object createSwapperCard()  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		Constructor<?> c = Class.forName(swapperCardPath).getConstructor(String.class, String.class, int.class);
		return c.newInstance(name, description, Integer.valueOf(rarity));
	}

	private Object createStartOverCard()  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		boolean lucky = new Random().nextBoolean();
		Constructor<?> c = Class.forName(startOverCardPath).getConstructor(String.class, String.class, int.class, boolean.class);
		return c.newInstance(name, description, Integer.valueOf(rarity), Boolean.valueOf(lucky));
	}

	private Object createShieldCard()  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		Constructor<?> c = Class.forName(shieldCardPath).getConstructor(String.class, String.class, int.class);
		return c.newInstance(name, description, Integer.valueOf(rarity));
	}

	private Object createEnergyStealCard()  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		int rarity = 1 + new Random().nextInt(10);
		int energy = 1 + new Random().nextInt(200);
		Constructor<?> c = Class.forName(energyStealCardPath).getConstructor(String.class, String.class, int.class, int.class);
		return c.newInstance(name, description, Integer.valueOf(rarity), Integer.valueOf(energy));
	}

	private Object createConfusionCard()  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
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

	private Object createDasher()  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(dasherPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	private Object createDynamo() throws  ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(dynamoPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	private Object createMultiTasker() throws   ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(multiTaskerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	private Object createSchemer() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		String description = generateRandomString(10 + new Random().nextInt(21));
		Object role = randomRole();
		int energy = new Random().nextInt(501);
		Constructor<?> c = Class.forName(schemerPath).getConstructor(String.class, String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, description, role, Integer.valueOf(energy));
	}

	// 
	private Object createCell() throws  ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Constructor<?> c = Class.forName(cellPath).getConstructor(String.class);
		return c.newInstance(name);
	}

	private Object createDoorCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Object role = randomRole();
		int energy = new Random().nextInt(201);
		Constructor<?> c = Class.forName(doorCellPath).getConstructor(String.class, Class.forName(rolePath), int.class);
		return c.newInstance(name, role, Integer.valueOf(energy));
	}

	private Object createCardCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Constructor<?> c = Class.forName(cardCellPath).getConstructor(String.class);
		return c.newInstance(name);
	}

	private Object createMonsterCell() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		Object cellMonster = createDasher();
		Constructor<?> c = Class.forName(monsterCellPath).getConstructor(String.class, Class.forName(monsterPath));
		return c.newInstance(name, cellMonster);
	}

	private Object createConveyorBelt() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		int effect = 1 + new Random().nextInt(20);
		Constructor<?> c = Class.forName(conveyorBeltPath).getConstructor(String.class, int.class);
		return c.newInstance(name, Integer.valueOf(effect));
	}

	private Object createContaminationSock() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException   {
		String name = generateRandomString(5 + new Random().nextInt(11));
		int effect = -1 - new Random().nextInt(20);
		Constructor<?> c = Class.forName(contaminationSockPath).getConstructor(String.class, int.class);
		return c.newInstance(name, Integer.valueOf(effect));
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

	private Object createBoard() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ArrayList<Object> readCards = new ArrayList<Object>();
		Constructor<?> c = Class.forName(boardPath).getConstructor(ArrayList.class);
		return c.newInstance(readCards);
	}

}
