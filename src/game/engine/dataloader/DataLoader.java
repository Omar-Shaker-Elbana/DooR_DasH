package game.engine.dataloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import game.engine.exceptions.InvalidCSVFormat;
import game.engine.Role;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.monsters.*;

public class DataLoader {
	private static final String CARDS_FILE_NAME = "cards.csv";
	private static final String CELLS_FILE_NAME = "cells.csv";
	private static final String MONSTERS_FILE_NAME = "monsters.csv";
	
	@SuppressWarnings("resource")
	public static ArrayList<Card> readCards() throws IOException {
		ArrayList<Card> cards = new ArrayList<Card>();

		BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE_NAME));

		while (br.ready()) {
			String nextLine = br.readLine();
			String[] data = nextLine.split(",");
			

			if (data.length != 4 && data.length != 5) {
				System.out.println(data.length);
				throw new InvalidCSVFormat(nextLine);
			}
				
			
			String cardType = data[0];
			Card card;
			
			switch (cardType) {
				case "SWAPPER":
					card = new SwapperCard(data[1], data[2], Integer.parseInt(data[3])); break;
				case "ENERGYSTEAL":
					card = new EnergyStealCard(data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4])); break;
				case "STARTOVER":
					card = new StartOverCard(data[1], data[2], Integer.parseInt(data[3]), Boolean.parseBoolean(data[4])); break;
				case "SHIELD":
					card = new ShieldCard(data[1], data[2], Integer.parseInt(data[3])); break;
				case "CONFUSION":
					card = new ConfusionCard(data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4])); break;
			default:
				throw new InvalidCSVFormat("Unknown card type: " + cardType);
			}
			
			cards.add(card);
			
		}

		br.close();

		return cards;
	}
	
	@SuppressWarnings("resource")
	public static ArrayList<Cell> readCells() throws IOException {
		ArrayList<Cell> cells = new ArrayList<Cell>();

		BufferedReader br = new BufferedReader(new FileReader(CELLS_FILE_NAME));

		while (br.ready()) {
			String nextLine = br.readLine();
			String[] data = nextLine.split(",");
			

			if (data.length != 2 && data.length != 3)
				throw new InvalidCSVFormat(nextLine);
			
			Cell cell;
			
			if (data.length == 2) 
				cell = Integer.parseInt(data[1]) > 0 ? new ConveyorBelt(data[0], Integer.parseInt(data[1])) : new ContaminationSock(data[0], Integer.parseInt(data[1]));
				
			else 
				cell = new DoorCell(data[0], Role.valueOf(data[1]), Integer.parseInt(data[2]));
			
			cells.add(cell);
		}

		br.close();

		return cells;
	}
	
	@SuppressWarnings("resource")
	public static ArrayList<Monster> readMonsters() throws IOException {
		ArrayList<Monster> monsters = new ArrayList<Monster>();

		BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE_NAME));

		while (br.ready()) {
			String nextLine = br.readLine();
			String[] data = nextLine.split(",");
			

			if (data.length != 5)
				throw new InvalidCSVFormat(nextLine);
			
			String monsterType = data[0];
			Monster monster;
			
			switch (monsterType) {
				case "DYNAMO":
					monster = new Dynamo(data[1], data[2], Role.valueOf(data[3]), Integer.parseInt(data[4])); break;
				case "DASHER":
					monster = new Dasher(data[1], data[2], Role.valueOf(data[3]), Integer.parseInt(data[4])); break;
				case "MULTITASKER":
					monster = new MultiTasker(data[1], data[2], Role.valueOf(data[3]), Integer.parseInt(data[4])); break;
				case "SCHEMER":
					monster = new Schemer(data[1], data[2], Role.valueOf(data[3]), Integer.parseInt(data[4])); break;
			default:
				throw new InvalidCSVFormat("Unknown monster type: " + monsterType);
			}
			
			monsters.add(monster);
			
		}

		br.close();

		return monsters;
	}
	
}
