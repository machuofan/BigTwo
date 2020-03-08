
public class Flush extends Hand {
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor for building a flush hand with the specified player and list of cards.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the hand played by the player
	 */
	public Flush (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand, which is "Flush"
	 */
	public String getType() {
		String type = "Flush";
		return type;
	}
	
	/**
	 * a method for checking if this is a valid hand
	 * @return true if the hand is valid, else false
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			int suit = this.getCard(0).getSuit();
			for (int i = 1; i < this.size(); i++) {
				if (this.getCard(i).getSuit() != suit) {
					return false;
				}
			}
			return true;
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
		if (hand.size() == 5) {
			String type = hand.getType();
			if (type == "FullHouse" || type == "Quad" || type == "StraightFlush") {
				return false;
			}
			else if (type == "Straight") {
				return true;
			}
			else {
				return super.beats(hand);
			}
		}
		else {
			return false;
		}
	}
}
