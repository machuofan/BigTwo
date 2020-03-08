import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI
 * for the Big Two card game and handle all user actions
 * @author Ma Chuofan
 */

public class BigTwoTable implements CardGameTable {
	private BigTwoClient game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea msgArea2;
	private JTextField inputArea;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem quit;
	private JMenuItem connect;
	private JScrollPane te;
	private JScrollPane te2;
	
	/**
	 * ¨C a constructor for creating a BigTwoTable.
	 * @param game a reference to a card game associates with this table.
	 */
	public BigTwoTable(BigTwoClient game) {
		frame = new JFrame();
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setBackground(new Color(0,153,76));
		playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		
		msgArea = new JTextArea();
		msgArea.setEditable(false);
		Font font = new Font("Arial", Font.BOLD, 18);
        msgArea.setFont(font);
		te = new JScrollPane(msgArea);
		Dimension size1 = new Dimension(500, 150);
		te.setPreferredSize(size1);
		
		msgArea2 = new JTextArea();
		msgArea2.setEditable(false);
        msgArea2.setFont(font);
		te2 = new JScrollPane(msgArea2);
		Dimension size2 = new Dimension(500, 150);
		te.setPreferredSize(size2);
		
		inputArea = new JTextField(20);
		inputArea.setEditable(true);
		inputArea.addActionListener(new InputMsgListener());
		
		avatars = new Image[4];
		cardImages = new Image[13][4];
		menubar = new JMenuBar();
		menu = new JMenu("game");
		quit = new JMenuItem("Quit");
		connect = new JMenuItem("Connect");
		quit.addActionListener(new QuitMenuItemListener());
		connect.addActionListener(new ConnectMenuItemListener());
		this.game = game;
	}
	
	/**
	 * a method for setting the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
		selected = new boolean[game.getPlayerList().get(activePlayer).getNumOfCards()];
		resetSelected();
	}
	
	/**
	 * a method for getting an array of indices of the cards selected
	 */
	public int[] getSelected() {
		int count = 0;
		for (boolean card : selected) {
			if (card == true) {
				count++;
			}
		}
		int[] arr = new int[count];
		int i = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j] == true) {
				arr[i] = j;
				i++;
			}
		}
		return arr;
	}
	
	/**
	 * a method for resetting the list of selected cards
	 */
	public void resetSelected() {
		for(int i = 0; i < selected.length; i++) {
			selected[i] = false;
		}
	}
	
	/**
	 * a method for repainting the GUI
	 */
	public void repaint() {
		frame.repaint();
	}
	
	/**
	 * a method for printing the specified string to the message area of the GUI
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	/**
	 * a method for printing the specified string to the chat area of the GUI
	 */
	public void printMsg2 (String msg) {
		msgArea2.append(msg);
	}
	
	/**
	 * a method for clearing the message area of the GUI
	 */
	public void clearMsgArea() {
		msgArea.setText(null);
	}
	
	/**
	 * a method for clearing the chat area of the GUI
	 */
	public void clearChatMsg() {
		msgArea2.setText(null);
	}
	/**
	 * a method for resetting the GUI.
	 */
	public void reset() {
		this.resetSelected();
		this.clearMsgArea();
		this.clearChatMsg();
		this.enable();
	}
	
	/**
	 * a method for enabling user interactions with the GUI
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	
	/**
	 * a method for disabling user interactions with the GUI
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	
	/**
	 * a method for creating the initial GUI.
	 */
	public void go() {
		//load images
			//load front cards image
		char[] seq_1 = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		char[] seq_2 = {'d', 'c', 'h', 's'};
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 4; j++) {
				cardImages[i][j] = new ImageIcon("images/" + seq_1[i] + seq_2[j] + ".gif").getImage();
			}
		}
			//load back card image
		cardBackImage = new ImageIcon("images/b.gif").getImage();
			//load avatars' image
		for (int k = 0; k < 4; k++) {
			avatars[k] = new ImageIcon("images/" + Integer.toString(k+1) + ".png").getImage();
		}
		
		//load gui structure
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
			//paint big two panel
		c.gridx= 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 15;
		c.gridheight = 15;
		frame.add(bigTwoPanel, c);
			//paint panel holding buttons
		c.gridy = 15;
		c.gridheight = 1;
		c.gridwidth = 30;
		c.weightx = 0.05;
		c.weighty = 0.05;
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.insets = new Insets(5, 0, 5, 0);
		d.weightx = 0.8;
		d.weighty = 0.8;
		d.ipadx = 15;
		d.ipady = 5;
				//paint play button
		d.gridx = 1;
		d.gridwidth = 7;
		d.anchor = GridBagConstraints.EAST;
		buttons.add(playButton, d);
				//paint pass button
		d.gridx = 9;
		d.anchor = GridBagConstraints.WEST;
		buttons.add(passButton, d);
				//paint blank area between two buttons
		d.gridx = 8;
		d.gridwidth = 1;
		d.weightx = 0.2;
		JPanel tab1 = new JPanel();
		buttons.add(tab1, d);
		JPanel tab2 = new JPanel();
		d.gridx = 0;
		buttons.add(tab2, d);
				//paint message label
		JLabel msgInput = new JLabel("Message: ");
		msgInput.setFont(new Font("Arial", Font.BOLD, 20));
		d.gridx = 16;
		d.gridwidth = 5;
		d.anchor = GridBagConstraints.EAST;
		d.weightx = 0.8;
		buttons.add(msgInput, d);
				//paint input area
		d.gridx = 21;
		d.gridwidth = 9;
		d.anchor = GridBagConstraints.WEST;
		d.fill = GridBagConstraints.HORIZONTAL;
		buttons.add(inputArea, d);
		c.gridwidth = 30;
		frame.add(buttons, c);
			//paint message area
		JPanel text  = new JPanel();
		Dimension size = new Dimension(500, 850);
		text.setPreferredSize(size);
		text.setLayout(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.weightx = 0.5;
		e.weighty = 0.5;
		e.fill = GridBagConstraints.BOTH;
		text.add(te, e);
		e.gridy = 1;
		text.add(te2, e);
	
		c.gridx = 15;
		c.gridy = 0;
		c.weightx = 0.7;
		c.weighty = 0.7;
		c.gridheight = 15;
		c.gridwidth = 15;
		frame.add(text, c);

		
		menu.add(connect);
		menu.add(quit);
		menubar.add(menu);
		frame.setJMenuBar(menubar);
		
		disable();
		
		frame.setSize(1250, 900);
		frame.setVisible(true);
	}

	/**
	 * an inner class implementing the details of bigTwoGame table
	 * @author Ma Chuofn
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		
		/**
		 * a constructor for adding a mouse listener to the panel
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		/**
		 * override the the paintComponent method to draw the game table
		 */
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(new Color(209, 186, 116));
			g.fillRect(0, 0, getWidth(), getHeight());
			//draw horizontal lines to separate areas in panel
			g.setColor(Color.BLACK);
			for (int i = 1; i <= 4; i++) {
				g.drawLine(0, i * this.getHeight()/5, this.getWidth(), i * this.getHeight()/5);
			}
			
			int cardheight = cardBackImage.getHeight(null);
			int avataricon = avatars[0].getHeight(null);
			int rowheight = this.getHeight()/5;
			for (int i = 0; i < 4; i++) {
				//draw avatars
				g.setFont(new Font("TimesRoman", Font.BOLD, 20));
				g.drawString(game.getPlayerList().get(i).getName(), 10, i*rowheight + 20);
				g.drawImage(avatars[i], 0, i*rowheight + (rowheight - avataricon + 20)/2, this);
				//draw cards
				if (i != activePlayer) {
					int numOfCards = game.getPlayerList().get(i).getNumOfCards();
					for (int j = 0; j < numOfCards; j++) {
						g.drawImage(cardBackImage, 150 + j * 20, i*rowheight + (rowheight - cardheight)/2, this);
					}
				}
				else {
					int numOfCards = game.getPlayerList().get(i).getNumOfCards();
					CardList cardsInHand = game.getPlayerList().get(i).getCardsInHand();
					for (int j = 0; j < numOfCards; j++) {
						Card card = cardsInHand.getCard(j);
						int suit = card.getSuit();
						int rank = card.getRank();
						if (selected[j]) {
							g.drawImage(cardImages[rank][suit], 150 + j * 20, i*rowheight + (rowheight - cardheight)/2 - 15, this);
						}
						else {
							g.drawImage(cardImages[rank][suit], 150 + j * 20, i*rowheight + (rowheight - cardheight)/2, this);
						}
					}
				}
			}
			Hand prevhand = (game.getHandsOnTable().size() > 0) ? game.getHandsOnTable().get(game.getHandsOnTable().size() - 1) : null;
			if (prevhand != null) {
				g.drawString("Played by " + prevhand.getPlayer().getName(), 10, 4*rowheight + 25);
				for (int i = 0; i < prevhand.size(); i++) {
					Card card = prevhand.getCard(i);
					int suit = card.getSuit();
					int rank = card.getRank();
					g.drawImage(cardImages[rank][suit], 40 + i * 20, 4*rowheight + (rowheight - cardheight + 30)/2, this);
				}
			}
		}
		
		/**
		 * Override the mouseClicked() method to detect the mouse click event.
		 * If the player selects a specified card, the card will be raised.
		 * Or if the player clicks on a raised card, the card will be de-raised.
		 * 
		 * @param e a mouse event
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			int x, y;
			x = e.getX();
			y = e.getY();
			int cardheight = cardBackImage.getHeight(null);
			int cardwidth = cardBackImage.getWidth(null);
			int rowheight = this.getHeight()/5;
			int pos;
			if (y <= rowheight * 4) {
				if (y/rowheight == activePlayer) {
					CardGamePlayer player = game.getPlayerList().get(y/rowheight);
					int numOfCards = player.getNumOfCards();
					if (x - 150 < (numOfCards - 1) * 20 + cardwidth && x - 150 >= 0) {
						if (x - 150 < (numOfCards - 1) * 20) {
							pos = (x - 150)/20;
						}
						else {
							pos = numOfCards - 1;
						}
						if (selected[pos]) {
							if ((rowheight-cardheight)/2-15 <= (y-rowheight*activePlayer) && (y-rowheight*activePlayer) <= rowheight-(rowheight-cardheight)/2-15) {
								selected[pos] = false;
								repaint();
							}
							if ((rowheight-cardheight)/2-15+cardheight <= (y-rowheight*activePlayer) && (y-rowheight*activePlayer) <= rowheight-(rowheight-cardheight)/2) {
								if (pos != numOfCards - 1) {
									for (int k = 1; pos-k >= 0 && k <= 3; k++) {
										if (!selected[pos-k]) {
											if (k <= 2) {
												selected[pos-k] = true;
												repaint();
												break;
											}
											else {
												if (x-150-20*pos <= 13) {
													selected[pos-k] = true;
													repaint();
													break;
												}
											}
										}
									}
								}
								else {
									int distance = x - 150 - 20 * (numOfCards - 1);
									for (int k = 1; pos-k >= 0 && k <= 3; k++) {
										if (distance <= cardwidth - 20*k && !selected[pos-k]) {
											selected[pos-k] = true;
											repaint();
											break;
										}
									}
								}
							}
						}
						else {
							if ( (rowheight-cardheight)/2 <= (y-rowheight*activePlayer) && (y-rowheight*activePlayer) <= rowheight-(rowheight-cardheight)/2 ) {
								selected[pos] = true;
								repaint();
							}
						}
					}
				}
			}
		}
		
		/**
		 * Not implemented
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {

		}

		/**
		 * Not implemented
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
				
		}

		/**
		 * Not implemented
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
				
		}

		/** 
		 * Not implemented
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
				
		}
	}
	
	/**
	 * This inner class is the play button listener to handle action on play button, implementing the action listener.
	 */
	class PlayButtonListener implements ActionListener {
		/**
		 * This method handles play action from the player
		 * 
		 * @param e an action event
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(getSelected().length != 0) {
				game.makeMove(activePlayer, getSelected());
				resetSelected();
				if (game.endOfGame()) {
					passButton.setEnabled(false);
					playButton.setEnabled(false);
					bigTwoPanel.setEnabled(false);
					
				}
				repaint();
			}
		}
	}
	
	/**
	 * This inner class is the pass button listener to handle action on pass button, implementing the action listener.
	 */
	class PassButtonListener implements ActionListener {
		/**
		 * This method handles play action from the player
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			resetSelected();
			game.makeMove(activePlayer, getSelected());
			repaint();
		}
	}
	
	/**
	 * This inner class is the quit menu item listener, which performs the logic of quiting the game.
	 *
	 */
	class QuitMenuItemListener implements ActionListener {
		/**
		 * This method quits the game
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * This inner class is the connect menu item listener, which performs the logic of connecting to the server.
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {
		/**
		 * This method makes connection to the server
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed (ActionEvent e) {
			game.makeConnection();
		}
	}
	
	/**
	 * This inner class is the send message action listener, which performs the logic of sending chat message to the server.
	 *
	 */
	class InputMsgListener implements ActionListener {
		/**
		 * This method sends chat message to the server
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (!inputArea.getText().isEmpty()) {
				String msg = inputArea.getText();
				CardGameMessage chatMsg = new CardGameMessage(CardGameMessage.MSG, -1, msg);
				game.sendMessage(chatMsg);
			}
			inputArea.setText("");
		}
	}
}
