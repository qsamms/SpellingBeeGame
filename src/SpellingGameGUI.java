import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class SpellingGameGUI{
	private static SpellingGame game;
	private static SQL sql;
	private String username;
	private int score;
	private JFrame window,leaderboardwindow;
	private JPanel menutop, menutitle, menubuttons, gametop, gamehelper, hivePanel, rulesPanel;
	private JButton playButton,viewScores;
	private JLabel titleLabel, description, timer,textarea, yourhive, hive, errorLabel, letters, rules;
	private JTextField input;
	private JTextArea scores;
	private JScrollPane scoresholder;
	private Timer counter;
	private JTable currentTable;
	private StringBuilder playerwords = new StringBuilder();
	private Set<String> usedwords = new LinkedHashSet<String>();
	private Color color = new Color(242,219,179);
	private int rowSelected;
	private int idSelected;
	ArrayList<String> queryWords = new ArrayList<String>();
	
	private ListSelectionListener tableListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel selectModel = (ListSelectionModel) e.getSource();
			rowSelected = selectModel.getMinSelectionIndex();
			idSelected = Integer.parseInt((String) currentTable.getValueAt(rowSelected, 0));
		}
	};
		
	public SpellingGameGUI() {
		createWindow();
		createMenuPage();
		createGamePage();
		createLeaderboard();
		window.setVisible(true);
	}
	
	public static void main(String[] args) {
		game = new SpellingGame();
		sql = new SQL();
		new SpellingGameGUI();
	}
	
	public void createWindow() {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(600,600);
		window.setTitle("Spelling Bee Game");
	}
	
	public void createMenuPage() {
		menutop = new JPanel();
		menutop.setBackground(color);
		menutop.setLayout(new BoxLayout(menutop, BoxLayout.Y_AXIS));
		menutitle = new JPanel();
		menutitle.setBackground(color);
		menutitle.setLayout(new BoxLayout(menutitle, BoxLayout.Y_AXIS));
		menubuttons = new JPanel(new FlowLayout());
		menubuttons.setBackground(color);
		rulesPanel = new JPanel();
		rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
		rules = new JLabel("Rules: ");
		rules.setFont(new Font("TimesRoman", Font.BOLD, 16));
		JLabel words = new JLabel("Words must be at least 3 characters long ");
		words.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		JLabel start = new JLabel("Words must start with one of the two given letters");
		start.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		rulesPanel.setBackground(color);
		
		playButton = new JButton("Let's Play!");
		viewScores = new JButton("Leaderboard");
		
		titleLabel = new JLabel("Spelling Bee Game!");
		description = new JLabel("Create as many words as you can from the given hive");
		titleLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
		description.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		
		menubuttons.add(playButton);
		menubuttons.add(viewScores);

		menutitle.add(titleLabel,BorderLayout.CENTER);
		menutitle.add(Box.createVerticalStrut(20));
		menutitle.add(description,BorderLayout.SOUTH);
		
		menutop.add(Box.createVerticalGlue());
		menutop.add(menutitle);
		
		menutop.add(Box.createVerticalStrut(30));
		rulesPanel.add(rules);
		rulesPanel.add(words);
		rulesPanel.add(start);
		menutop.add(rulesPanel);
		
		
		menutop.add(Box.createVerticalStrut(50));
		menutop.add(menubuttons);
		
		window.setContentPane(menutop);
		
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username = JOptionPane.showInputDialog(window, "Enter username", "Username", JOptionPane.PLAIN_MESSAGE);
				window.setContentPane(gametop);
				window.validate();
				startNewGame();
			}
		});
		
		viewScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leaderboardwindow.setVisible(true);
			}
		});
	}
	
	public void createLeaderboard() {
		ResultSet result = null;
		try {
			result = sql.selectAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		TableModel tablemodel = new TableModel(result);
		JTable table = new JTable(tablemodel);
		currentTable = table;
		JScrollPane scrollpane = new JScrollPane(table);
		leaderboardwindow = new JFrame("Leaderboard");
		JPanel helper = new JPanel();
		helper.setBackground(color);
		JPanel toppanel = new JPanel();
		toppanel.setBackground(color);
		JPanel buttonpanel = new JPanel(new FlowLayout());
		buttonpanel.setBackground(color);
		JPanel enterPanel = new JPanel(new FlowLayout());
		enterPanel.setBackground(color);
		JLabel enter = new JLabel("Sort by username: ");
		JTextField input = new JTextField(10);
		JButton menu = new JButton("Back to menu");
		JButton seeall = new JButton("See all");
		JTextArea words = new JTextArea(2,30);
		JButton seeWords = new JButton("See Words");
		JPanel seeAllPanel = new JPanel();
		seeAllPanel.setBackground(color);
		JPanel wordsPanel = new JPanel();
		wordsPanel.setBackground(color);
				
		table.getSelectionModel().addListSelectionListener(tableListener);
		
		toppanel.setLayout(new BoxLayout(toppanel,BoxLayout.X_AXIS));
		helper.setLayout(new BoxLayout(helper,BoxLayout.Y_AXIS));
		
		toppanel.add(Box.createHorizontalStrut(50));
		toppanel.add(Box.createHorizontalGlue());
		
		helper.add(Box.createVerticalGlue());
		
		helper.add(Box.createVerticalStrut(30));
		
		enterPanel.add(enter);
		enterPanel.add(input);
		seeAllPanel.add(seeall);
		
		helper.add(enterPanel);
		
		helper.add(seeAllPanel);
		
		helper.add(Box.createVerticalStrut(30));
		
		helper.add(scrollpane);
		
		helper.add(Box.createVerticalStrut(30));
		
		buttonpanel.add(menu);
		buttonpanel.add(seeWords);
		helper.add(buttonpanel);
		
		helper.add(Box.createVerticalStrut(30));
		
		JScrollPane scrollPane = new JScrollPane(words);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Words Used:"));
		wordsPanel.add(scrollPane);
		helper.add(wordsPanel);
		
		helper.add(Box.createVerticalGlue());
		
		toppanel.add(helper);
		toppanel.add(Box.createHorizontalGlue());
		toppanel.add(Box.createHorizontalStrut(50));
		
		leaderboardwindow.setSize(500,400);
		leaderboardwindow.setContentPane(toppanel);
		leaderboardwindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		leaderboardwindow.setVisible(false);
		
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leaderboardwindow.setVisible(false);
			}
		});
		
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResultSet results = null;
				try {
					results = sql.usernameQuery(input.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				TableModel tablemodel = new TableModel(results);
				JTable table = new JTable(tablemodel);
				currentTable = table;
				table.getSelectionModel().addListSelectionListener(tableListener);
				table.getColumnModel().getColumn(0).setMaxWidth(50);
				table.getColumnModel().getColumn(1).setMaxWidth(100);
				table.getColumnModel().getColumn(2).setMaxWidth(90);
				table.getColumnModel().getColumn(3).setMaxWidth(110);
				JScrollPane scrollpane2 = new JScrollPane(table);
				scrollpane.setViewportView(scrollpane2);
			}
		});
		
		seeall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResultSet results = null;
				try {
					results = sql.selectAll();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				TableModel tablemodel = new TableModel(results);
				JTable table = new JTable(tablemodel);
				currentTable = table;
				table.getSelectionModel().addListSelectionListener(tableListener);
				table.getColumnModel().getColumn(0).setMaxWidth(50);
				table.getColumnModel().getColumn(1).setMaxWidth(100);
				table.getColumnModel().getColumn(2).setMaxWidth(90);
				table.getColumnModel().getColumn(3).setMaxWidth(110);
				JScrollPane scrollpane2 = new JScrollPane(table);
				scrollpane.setViewportView(scrollpane2);
			}
		});
		
		seeWords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryWords.clear();
				ResultSet results = null;
				try {
					results = sql.wordsQuery(idSelected);
					while(results.next()) {
						queryWords.add(results.getString("word"));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			
				words.setText(queryWords.toString());
			}
		});
	}
	
	public void startNewGame() {
		score = 0;
		usedwords.clear();
		playerwords.setLength(0);
		scores.setText("");
		input.setText("");
		timer.setText("1:00");
		timer.setForeground(Color.BLACK);
		textarea.setText("Your score: " + score);
		String hiveletters = game.generateHive();
		hive.setText(hiveletters);
		String startingletters = game.startLetters.toString();
		StringBuilder s = new StringBuilder();
		s.append(startingletters.charAt(1) + ", ");
		s.append(startingletters.charAt(7));
		letters.setText("Starting Letters: " + s);
		
		int delay = 1000;
		ActionListener taskPerformer = new ActionListener() { 
			int time = 60;
			public void actionPerformed(ActionEvent e) {
				if(time == 60) {
					timer.setText("1:00");
				}else if(time < 10 && time > 0){
					if(time % 2 == 0) {
						timer.setForeground(Color.BLACK);
						timer.setText("0:0" + Integer.toString(time));
					}else {
						timer.setForeground(Color.RED);
						timer.setText("0:0" + Integer.toString(time));
					}
				}else if(time <= 0){
					endgame();
				}else {
					timer.setText("0:" + Integer.toString(time));
				}
				time--;
			}
		};
		counter = new Timer(delay,taskPerformer);
		counter.start();
	}

	public void createGamePage() {
		JPanel timerPanel = new JPanel();
		JPanel textareaPanel = new JPanel();
		textareaPanel.setLayout(new BoxLayout(textareaPanel, BoxLayout.Y_AXIS));
		textareaPanel.setBackground(color);
		JPanel startLetters = new JPanel(new FlowLayout());
		JPanel errorholder = new JPanel();
		errorholder.setBackground(color);
		startLetters.setBackground(color);
		letters = new JLabel();
		letters.setFont(new Font("TimesRoman",Font.BOLD, 20));

		gametop = new JPanel();
		gametop.setBackground(color);
		gamehelper = new JPanel();
		gamehelper.setBackground(color);
		timer = new JLabel();
		hive = new JLabel();
		hive.setFont(new Font("TimesRoman",Font.BOLD, 30));
		input = new JTextField(10);
		textarea = new JLabel("Your score: " + score);
		scores = new JTextArea();
		errorLabel = new JLabel();
		hivePanel = new JPanel(new FlowLayout());
		
		yourhive = new JLabel("Hive:");
		gamehelper.setLayout(new BoxLayout(gamehelper, BoxLayout.Y_AXIS));
		
		startLetters.add(letters);
		gamehelper.add(Box.createVerticalStrut(20));
		timer.setFont(new Font("TimesRoman", Font.BOLD, 30));
		timerPanel.add(timer);
		timerPanel.setBackground(color);
		gamehelper.add(timerPanel);
		
		gamehelper.add(Box.createVerticalStrut(10));
		
		yourhive.setFont(new Font("TimesRoman",Font.BOLD, 30));
		gamehelper.add(yourhive);
		
		gamehelper.add(Box.createVerticalStrut(20));
		
		hivePanel.add(yourhive);
		hivePanel.add(hive);
		hivePanel.setBackground(color);
		gamehelper.add(hivePanel);
		
		gamehelper.add(Box.createVerticalStrut(20));
		
		gamehelper.add(startLetters);
		gamehelper.add(Box.createVerticalStrut(20));
		errorholder.add(errorLabel);
		gamehelper.add(errorholder);
		
		input.setSize(new Dimension(50,50));
		input.setPreferredSize(new Dimension(50,50));
		
		gamehelper.add(Box.createVerticalStrut(20));
	
		gamehelper.add(input);
		
		gamehelper.add(Box.createVerticalStrut(20));
		
		textareaPanel.add(textarea);
		textareaPanel.add(Box.createVerticalStrut(10));
		scores.setRows(10);
		scoresholder = new JScrollPane(scores);
		textareaPanel.add(scoresholder);
		gamehelper.add(textareaPanel);
		
		gamehelper.add(Box.createVerticalGlue());
		
		gametop.add(gamehelper);
		
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = input.getText();
				String lowercaseS = s.toLowerCase();
				try {
					game.checkValid(lowercaseS, usedwords);
					usedwords.add(lowercaseS);
					int wordscore = game.getScore(s);
					score += wordscore;
					textarea.setText("Your score: " + score);
					playerwords.append(s + ": " + wordscore + "\n");
					String area = playerwords.toString();
					scores.setText(area);
					errorLabel.setText("Nice!");
					errorLabel.setForeground(Color.GREEN);
				}catch(NotaWordException e1) {
					errorLabel.setText(e1.getMessage());
					errorLabel.setForeground(Color.RED);
				}catch(LettersException e2) {
					errorLabel.setText(e2.getMessage());
					errorLabel.setForeground(Color.RED);
				}catch(WordUsedException e3){
					errorLabel.setText(e3.getMessage());
					errorLabel.setForeground(Color.RED);
				} catch (TooShortException e4) {
					errorLabel.setText(e4.getMessage());
					errorLabel.setForeground(Color.RED);
				} catch (StartLetterException e5) {
					errorLabel.setText(e5.getMessage());
					errorLabel.setForeground(Color.RED);
				}
				input.setText("");
			}
		});
	}
	
	public void endgame() {
		counter.stop();
		JOptionPane.showMessageDialog(window, "Game Over! User: " + username + " Score: "
				+ score + " Nice Job!", "Game over :(", JOptionPane.PLAIN_MESSAGE);
		
		try {
			String[] words = convertSetToArr();
			sql.insert(username, score, "scores");
			sql.insertWords(sql.getMaxID(), words);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		window.setContentPane(menutop);
		window.validate();
	}
	
	public String[] convertSetToArr() {
		String[] strings = new String[usedwords.size()];
		
		int idx = 0;
		
		for(String s : usedwords) {
			strings[idx] = s;
			idx++;
		}
		
		return strings;
	}
}
