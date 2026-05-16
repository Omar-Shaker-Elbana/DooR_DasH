package game.engine.cells;

import game.engine.Board;
import game.engine.cards.Card;
import game.engine.monsters.Monster;

public class CardCell extends Cell {
    
    private Card lastDrawnCard;
    
    public CardCell(String name) {
        super(name);
    }
    
    public Card getLastDrawnCard() {
        return lastDrawnCard;
    }
    
    public void clearLastDrawnCard() {
        this.lastDrawnCard = null;
    }
    
    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        
        lastDrawnCard = Board.drawCard();
        lastDrawnCard.performAction(landingMonster, opponentMonster);
    }
}