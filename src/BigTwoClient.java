import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *  This class is used to model a Big Two card game that supports 4 players playing over the internet.
 * @author Ma Chuofan
 *
 */

public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	
	private boolean gameStart = false;
	private static String IP = "127.0.0.1";
	private static int PORT = 2396;
	
	/**
	 * constructor for creating a Big Two client
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		handsOnTable = new ArrayList<Hand>();
		for (int i = 0; i < 4; i++) {
			playerList.add(new CardGamePlayer());
		}
		table = new BigTwoTable(this);
		table.go();
		String name = JOptionPane.showInputDialog("Player name:");
		setPlayerName(name);
		setServerIP(IP);
		setServerPort(PORT);
		makeConnection();
	}
	
	/**
	 * a method for getting the number of players
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	
	/**
	 * a method for getting the deck of cards being used
	 */
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * a method for getting the list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}
	
	/**
	 * a method for getting the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}
	
	/**
	 * a method for getting the index of the player for the current turn
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	/**
	 * a method for setting the status of the game (started or not)
	 * @param status the status of the game
	 */
	public void setGameStatus(boolean status) {
		this.gameStart = status;
	}
	
	/**
	 * a method for getting the status of the game (started or not)
	 * @return the status of the game
	 */
	public boolean getGameStatus() {
		return gameStart;
	}
	
	/**
	 * a method for starting/restarting the game with a given shuffled deck of cards
	 */
	public void start(Deck deck) {
		//initialization
		for (int i = 0; i < 4; i++) {
			playerList.get(i).removeAllCards();
		}
		handsOnTable.clear();
		table.clearChatMsg();
		table.clearMsgArea();
		gameStart = true;
		//distribute cards to players
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				Card card = deck.getCard(13 * i + j);
				playerList.get(i).addCard(card);
			}
			playerList.get(i).sortCardsInHand();
		}
		table.setActivePlayer(this.playerID);
		//decide which player plays first
		Card initial = new Card(0, 2);
		for (int i = 0; i < 4; i++) {
			if (playerList.get(i).getCardsInHand().contains(initial)) {
				currentIdx = i;
				table.printMsg("Game starts!\n");
				table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn: \n");
				break;
			}
		}
		if (playerID == this.getCurrentIdx()) {
			table.enable();
		}
	}
	
	/**
	 * a method for making a move by a player with the specified playerID using the cards specified by the list of indices
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
		sendMessage(move);
	}
	
	/**
	 * a method for checking a move made by a player (is a valid move or not)
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		CardGamePlayer currplayer = playerList.get(playerID);
		CardList raw = currplayer.play(cardIdx);
		Hand prevhand = (handsOnTable.size() > 0) ? handsOnTable.get(handsOnTable.size() - 1) : null;
		if (raw == null) {
			if (handsOnTable.size() == 0) {
				table.printMsg("Not a legal move!!!\n");
				table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn:\n");
			}
			else if (prevhand.getPlayer().getName() == currplayer.getName()) {
				table.printMsg("Not a legal move!!!\n");
				table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn:\n");
			}
			else {
				table.printMsg("{pass}\n");
				if (!endOfGame()) {
					currentIdx = (currentIdx + 1) % 4;
					if (this.getPlayerID() == this.getCurrentIdx()) {
						table.enable();
					}
					else {
						table.disable();
					}
				}
				table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn:\n");
			}
		}
		//check if input is valid
		else {
			Hand currhand = composeHand(currplayer, raw);
			if (currhand == null) {
				table.printMsg("Not a legal move!!!\n");
				table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn:\n");
			}
			else {
				if (prevhand != null && !currhand.beats(prevhand) && prevhand.getPlayer().getName() != currplayer.getName()){
					table.printMsg("Not a legal move!!!\n");
					table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn:\n");
				}
				else {
					handsOnTable.add(currhand);
					currplayer.removeCards(currhand);
					if (!endOfGame()) {
						currentIdx = (currentIdx + 1) % 4;
						if (this.getPlayerID() == this.getCurrentIdx()) {
							table.enable();
						}
						else {
							table.disable();
						}
					}
					table.printMsg("{" + currhand.getType() + "} ");
					for (int i = 0; i < currhand.size(); i++) {
						Card card = currhand.getCard(i);
						table.printMsg("[" + card.toString() + "]");
					}
					table.printMsg("\n");
					table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn:\n");
				}
			}
		}
		table.repaint();
		if (endOfGame()) {
			String gameEndMsg = "Game ends\n";
			for (int i = 0; i < 4; i++) {
				if (i != getCurrentIdx()) {
					String player = getPlayerList().get(i).getName();
					int remaincards = getPlayerList().get(i).getNumOfCards();
					gameEndMsg = gameEndMsg + player + " has " + remaincards + " cards in hand.\n";
				}
				else {
					gameEndMsg = gameEndMsg + getPlayerList().get(i).getName() + " wins the game\n";
				}
			}
			table.printMsg(gameEndMsg);
			if (getGameStatus()) {
				JOptionPane.showMessageDialog(table.getFrame(), gameEndMsg);
				setGameStatus(false);
			}
			CardGameMessage readyMsg = new CardGameMessage(CardGameMessage.READY, -1, null);
			sendMessage(readyMsg);
		}
	}
	
	/**
	 * a method for checking if the game ends
	 * @return a boolean value specifying whether the game ends
	 */
	public boolean endOfGame() {
		if (gameStart) {
			if (playerList.get(currentIdx).getNumOfCards() <= 0) {
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
	 * ¨C a method for creating an instance of BigTwoClient
	 * @param args arguments for starting the game
	 */
	public static void main(String[] args) {
		new BigTwoClient();
	}
	
	/**
	 * a method for composing a hand
	 * @param player the index of the player
	 * @param cards the index of specified cards
	 * @return A valid hand from the specified list of cards of the player or null if no valid hand can be composed from the specified list of cards
	 */
	public static Hand composeHand (CardGamePlayer player, CardList cards) {
		Hand rawhand;
		if (cards.size() == 1) {
			rawhand = new Single(player, cards);
		}
		else if (cards.size() == 2) {
			rawhand = new Pair(player, cards);
		}
		else if (cards.size() == 3) {
			rawhand = new Triple(player, cards);
		}
		else {
			rawhand = new Straight(player, cards);
			if (!rawhand.isValid()) {
				rawhand = new Flush(player, cards);
			}
			if (!rawhand.isValid()) {
				rawhand = new FullHouse(player, cards);
			}
			if (!rawhand.isValid()) {
				rawhand = new Quad(player, cards);
			}
			if (!rawhand.isValid()) {
				rawhand = new StraightFlush(player, cards);
			}
		}
		
		if (!rawhand.isValid()) {
			return null;
		}
		else {
			return rawhand;
		}
	}
	
	//implement NetworkGame interface methods
	/**
	 * a method for getting the playerID (i.e., index) of the local player
	 */
	public int getPlayerID() {
		return playerID;
	}
	
	/**
	 * a method for setting the playerID (i.e., index) of the local player
	 * @param playerID the new ID of the local player
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * a method for getting the name of the local player
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * a method for setting the name of the local player
	 * @param playerName the new name of local player
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * a method for getting the IP address of the game server
	 */
	public String getServerIP() {
		return serverIP;
	}
	
	/**
	 * a method for setting the IP address of the game server
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	/**
	 * a method for getting the TCP port of the game server
	 */
	public int getServerPort() {
		return serverPort;
	}
	
	/**
	 * a method for setting the TCP port of the game server
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * ¨C a method for making a socket connection with the game server
	 */
	public void makeConnection() {
		try {
			sock = new Socket(getServerIP(), getServerPort());
			oos = new ObjectOutputStream(sock.getOutputStream());
			Runnable handler = new ServerHandler();
			Thread myThread = new Thread(handler);
			myThread.start();
			
			CardGameMessage joinGame = new CardGameMessage(CardGameMessage.JOIN, -1, playerName);
			sendMessage(joinGame);
			CardGameMessage readyGame = new CardGameMessage(CardGameMessage.READY, -1, null);
			sendMessage(readyGame);
			
		} catch (IOException e) {
			table.printMsg("Error in making connection with the game server \n");
			e.printStackTrace();
		}
	}
	
	/**
	 * a method for parsing the messages received from the game server
	 */
	public void parseMessage(GameMessage message) {
		if (message.getType() == CardGameMessage.PLAYER_LIST) {
			setPlayerID(message.getPlayerID());
			String[] names = (String[]) message.getData();
			for (int i = 0; i < names.length; i++) {
				if (names[i] != null) {
					getPlayerList().get(i).setName(names[i]);
				}
				else {
					getPlayerList().get(i).setName("");
				}
			}
		}
		else if (message.getType() == CardGameMessage.JOIN) {
			String name = (String) message.getData();
			getPlayerList().get(message.getPlayerID()).setName(name);
		}
		else if (message.getType() == CardGameMessage.FULL) {
			table.printMsg("The game is full now. Cannot join.\n");
			
		}
		else if (message.getType() == CardGameMessage.QUIT) {
			String address = (String) message.getData();
			table.printMsg("Player " + address + getPlayerList().get(message.getPlayerID()).getName() + " quits the game.\n");
			getPlayerList().get(message.getPlayerID()).setName("");
			if (gameStart) {
				table.reset();
				table.disable();
				CardGameMessage readyNow = new CardGameMessage (CardGameMessage.READY, -1, null);
				sendMessage(readyNow);
			}
		}
		else if (message.getType() == CardGameMessage.READY) {
			table.printMsg("Player " + getPlayerList().get(message.getPlayerID()).getName() + " is ready.\n");
		}
		else if (message.getType() == CardGameMessage.START) {
			start((BigTwoDeck) message.getData());
		}
		else if (message.getType() == CardGameMessage.MOVE) {
			int player = message.getPlayerID();
			int[] cardIdx = (int[]) message.getData();
			checkMove(player, cardIdx);
		} else if (message.getType() == CardGameMessage.MSG) {
			table.printMsg2((String) message.getData() + "\n");
		}
		table.repaint();
	}
	
	/**
	 * a method for sending the specified message to the game server
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * an inner class that implements the Runnable interface
	 * @author Ma Chuofan
	 *
	 */
	class ServerHandler implements Runnable {
		public void run() {
			try {
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				while (true) {
					CardGameMessage message = (CardGameMessage) ois.readObject();
					parseMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				
			} catch (Exception e) {
				table.printMsg("Error! Cannot receive messages from the game server \n");
				e.printStackTrace();
			} 
		}
	}
	
	
	
	
	
}
