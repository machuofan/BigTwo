
public class Quad extends Hand {
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor for building a quad hand with the specified player and list of cards.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the hand played by the player
	 */
	public Quad (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand, which is "Quad"
	 */
	public String getType() {
		String type = "Quad";
		return type;
	}
	
	/**
	 * a method for checking if this is a valid hand
	 * @return true if the hand is valid, else false
	 */
	public boolean isValid() {
		int count1 = 0;
		int count2 = 0;
		int rank = this.getCard(0).getRank();
		for (int i = 0; i < this.size(); i++) {
			if (this.getCard(i).getRank() == rank) {
				count1++;
			}
		}
		if (count1 == 4) {
			return true;
		}
		else {
			rank = this.getCard(1).getRank();
			for (int i = 1; i < this.size(); i++) {
				if (this.getCard(i).getRank() == rank) {
					count2++;
				}
			}
			if (count2 == 4) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		Card topcard = this.getCard(0);
		int toprank;
		if (topcard.getRank() == this.getCard(1).getRank()) {
			toprank = topcard.getRank();
		}
		else {
			if (this.getCard(1).getRank() == this.getCard(2).getRank()) {
				toprank = this.getCard(1).getRank();
			}
			else {
				toprank = topcard.getRank();
			}
		}
		for (int i = 0; i <this.size(); i++) {
			if (this.getCard(i).getRank() == toprank && this.getCard(i).getSuit() == 3) {
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
		if (hand.size() == 5) {
			String type = hand.getType();
			if (type == "StraightFlush") {
				return false;
			}
			else if (type == "Straight" || type == "Flush" || type == "FullHouse") {
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
