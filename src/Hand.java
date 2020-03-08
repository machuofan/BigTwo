
public abstract class Hand extends CardList {
	private static final long serialVersionUID = -3711761437629470849L;
	private CardGamePlayer player;
	
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the hand played by the player
	 */
	public Hand (CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.getCard(i);
			this.addCard(card);
		}
	}
	
	/**
	 * a method for retrieving the player of this hand.
	 * 
	 * @return the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		Card topcard = this.getCard(0);
		for (int i = 1; i < this.size(); i++) {
			if (topcard.compareTo(this.getCard(i)) == -1) {
				topcard = this.getCard(i);
			}
		}
		return topcard;
	}
	
	/**
	 * a method for checking if this hand beats a specified hand
	 * 
	 * @param hand the target hand to compare to
	 * 
	 * @return true if the hand beats the target hand, else false
	 */
	public boolean beats (Hand hand) {
		Card card1 = this.getTopCard();
		Card card2 = hand.getTopCard();
		if (card1.compareTo(card2) == 1) {
			return true;
		}
		else {
			return false;
		}		
	}
	
	/**
	 * a method for checking if this is a valid hand
	 * @return true if the hand is valid, else false
	 */
	public abstract boolean isValid();
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand
	 */
	public abstract String getType();
	
}
