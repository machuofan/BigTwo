
public class Straight extends Hand {
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor for building a straight hand with the specified player and list of cards.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the hand played by the player
	 */
	public Straight (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand, which is "Straight"
	 */
	public String getType() {
		String type = "Straight";
		return type;
	}
	
	/**
	 * a method for checking if this is a valid hand
	 * @return true if the hand is valid, else false
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			this.sort();
			int rank = this.getCard(0).getRank();
			if (rank == 0 || rank == 1) {
				rank += 13;
			}
			for (int i = 1; i < this.size(); i++) {
				int rank2 = this.getCard(i).getRank();
				if (rank2 == 0 || rank2 == 1) {
					rank2 += 13;
				}
				if (rank2 != (rank + i)) {
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
			if (hand.getType() != "Straight") {
				return false;
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
