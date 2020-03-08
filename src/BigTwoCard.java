
public class BigTwoCard extends Card{
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * constructor for building a card with the specified suit and rank
	 * @param suit an integer between 0 and 3
	 * @param rank an integer between 0 and 12
	 */
	public BigTwoCard (int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * a method for comparing the order of this card with the specified card. 
	 * 
	 * @return Returns a negative integer, zero, or a positive integer as this card is less than, 
	 * equal to, or greater than the specified card
	 */
	public int compareTo (Card card) {
		int r1 = this.getRank();
		int r2 = card.getRank();
		int s1 = this.getSuit();
		int s2 = card.getSuit();
		if (r1 == 0 || r1 == 1) {
			r1 += 13;
		}
		if (r2 == 0 || r2 == 1) {
			r2 += 13;
		}
		if (r1 > r2) {
			return 1;
		} else if (r1 < r2) {
			return -1;
		} else if (s1 > s2) {
			return 1;
		} else if (s1 < s2) {
			return -1;
		} else {
			return 0;
		}
	}
}
