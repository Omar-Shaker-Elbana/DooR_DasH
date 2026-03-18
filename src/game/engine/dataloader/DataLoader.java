package game.engine.dataloader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import game.engine.Role;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.monsters.*;
public class DataLoader {
	
	public static final String CARDS_FILE_NAME = "cards.csv";
	public static final String CELLS_FILE_NAME = "cells.csv";
	public static final String MONSTERS_FILE_NAME = "monsters.csv";
	
	
	public static ArrayList<Card> readCards() throws IOException{
		ArrayList<Card> cardsList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE_NAME));
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(","); 
            String type = data[0];
            String name = data[1];
            String desc = data[2];
            int rarity = Integer.parseInt(data[3]);

            switch (type) {
                case "SwapperCard":
                    cardsList.add(new SwapperCard(name, desc, rarity)); 
                    break;
                case "ShieldCard":
                    cardsList.add(new ShieldCard(name, desc, rarity));
                    break;
                case "EnergyStealCard":
                    int energy = Integer.parseInt(data[4]);
                    cardsList.add(new EnergyStealCard(name, desc, rarity, energy)); 
                    break;
                case "StartOverCard":
                    boolean lucky = Boolean.parseBoolean(data[4]);
                    cardsList.add(new StartOverCard(name, desc, rarity, lucky)); 
                    break;
                case "ConfusionCard":
                    int duration = Integer.parseInt(data[4]);
                    cardsList.add(new ConfusionCard(name, desc, rarity, duration)); 
                    break;
            }
        }
        br.close();
        return cardsList;
	}
	public static ArrayList<Cell> readCells() throws IOException { 
	        ArrayList<Cell> cellsList = new ArrayList<>();
	        BufferedReader br = new BufferedReader(new FileReader(CELLS_FILE_NAME));
	        String line;

	        while ((line = br.readLine()) != null) {
	            String[] data = line.split(","); 
	            String name = data[0];

	            if (data.length == 3) { // DoorCell (name, role, energy) 
	                Role role = Role.valueOf(data[1]);
	                int energy = Integer.parseInt(data[2]);
	                cellsList.add(new DoorCell(name, role, energy));
	            } else if (data.length == 2) { // TransportCells 
	                int effect = Integer.parseInt(data[1]);
	                if (effect > 0) {
	                    cellsList.add(new ConveyorBelt(name, effect)); 
	                } else {
	                    cellsList.add(new ContaminationSock(name, effect)); 
	                }
	            }
	        }
	        br.close();
	        return cellsList;
	    }
	public static ArrayList<Monster> readMonsters() throws IOException{
	        ArrayList<Monster> monstersList = new ArrayList<>();
	        BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE_NAME));
	        String line;

	        while ((line = br.readLine()) != null) {
	            String[] data = line.split(","); 
	            String type = data[0];
	            String name = data[1];
	            String desc = data[2];
	            Role role = Role.valueOf(data[3]);
	            int energy = Integer.parseInt(data[4]);

	            switch (type) {
	                case "Dasher":
	                    monstersList.add(new Dasher(name, desc, role, energy)); 
	                    break;
	                case "Dynamo":
	                    monstersList.add(new Dynamo(name, desc, role, energy));
	                    break;
	                case "MultiTasker":
	                    monstersList.add(new MultiTasker(name, desc, role, energy)); 
	                    break;
	                case "Schemer":
	                    monstersList.add(new Schemer(name, desc, role, energy)); 
	                    break;
	            }
	        }
	        br.close();
	        return monstersList;
	    }
}
