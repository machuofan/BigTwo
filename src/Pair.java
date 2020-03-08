
public class Pair extends Hand {
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor for building a pair hand with the specified player and list of cards.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the hand played by the player
	 */
	public Pair (CardGamePlayer player, CardList cards) {
		super (player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand, which is "Pair"
	 */
	public String getType() {
		String type = "Pair";
		return type;
	}
	
	/**
	 * a method for checking if this is a valid pair hand
	 * @return true if the hand is valid, else false
	 */
	public boolean isValid() {
		if (this.size() == 2) {
			if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * a method for checking if this hand beats a specified hand
	 * 
	 * @param hand the target hand to compare to
	 * 
	 * @return true if the hand beats the target hand, else false
	 */
	public boolean beats (Hand hand) {
		if (hand.size() == 2) {
			return super.beats(hand);
		}
		else {
			return false;
		}
	}
}
