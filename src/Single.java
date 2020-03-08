
public class Single extends Hand {
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor for creating a single hand with the specified player and list of cards.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the hand played by the player
	 */
	public Single (CardGamePlayer player, CardList cards) {
		super (player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand, which is "Single"
	 */
	public String getType() {
		String type = "Single";
		return type;
	}

	/**
	 * a method for checking if this is a valid single hand
	 * @return true if the hand is valid, else false
	 */
	public boolean isValid() {
		if (this.size() == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * a method for retrieving the top card of this single hand.
	 * 
	 * @return the top card of this single hand
	 */
	public Card getTopCard() {
		return this.getCard(0);
	}
	
	/**
	 * a method for checking if this single hand beats a specified hand
	 * 
	 * @param hand the target hand to compare to
	 * 
	 * @return true if the single hand beats the target hand, else false
	 */
	public boolean beats (Hand hand) {
		if (hand.size() == 1) {
			return super.beats(hand);
		}
		else {
			return false;
		}
	}
}
