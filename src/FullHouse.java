
public class FullHouse extends Hand {
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor for building a fllhouse hand with the specified player and list of cards.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the hand played by the player
	 */
	public FullHouse (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand, which is "Fullhouse"
	 */
	public String getType() {
		String type = "FullHouse";
		return type;
	}
	
	/**
	 * a method for checking if this is a valid hand
	 * @return true if the hand is valid, else false
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			int rank1 = this.getCard(0).getRank();
			int rank2 = 0;
			int count1 = 1;
			int count2 = 0;
			for (int i = 1; i < this.size(); i++) {
				if (this.getCard(i).getRank() == rank1) {
					count1++;
				}
				else {
					rank2 = this.getCard(i).getRank();
				}
			}
			if (count1 == 3) {
				for (int i = 0; i < this.size(); i++) {
					if (this.getCard(i).getRank() == rank2) {
						count2++;
					}
				}
				if (count2 == 2) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (count1 == 2) {
				for (int i = 0; i < this.size(); i++) {
					if (this.getCard(i).getRank() == rank2) {
						count2++;
					}
				}
				if (count2 == 3) {
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
		else {
			return false;
		}
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		int count = 1;
		Card topcard = this.getCard(0);
		for (int j = 0; j < this.size(); j++) {
			topcard = this.getCard(j);
			count = 1;
			for (int i = j + 1; i < this.size(); i++) {
				if (topcard.getRank() == this.getCard(i).getRank()) {
					count++;
				}
			}
			if (count == 3) {
				break;
			}
		}
		for (int k = 0; k < this.size(); k++) {
			if (this.getCard(k).getRank() == topcard.getRank()) {
				if (this.getCard(k).getSuit() > topcard.getSuit()) {
					topcard = this.getCard(k);
				}
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
			if (type == "Quad" || type == "StraightFlush") {
				return false;
			}
			else if (type == "Straight" || type == "Flush") {
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
