import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;


public class SpellingGameGUI{
	private static SpellingGame game;
	private static SQL sql;
	private String username;
	private int score;
	private JFrame window,leaderboardwindow;
	private JPanel menutop, menutitle, menubuttons, gametop, gamehelper;
	private JButton playButton,viewScores;
	private JLabel titleLabel, description, timer, enter,textarea, yourhive, hive;
	private JTextField input;
	private JTextArea scores;
	private JScrollPane scoresholder;
	private Timer counter;
	
	public SpellingGameGUI() {
		createWindow();
		createMenuPage();
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
		menutop.setBackground(Color.LIGHT_GRAY);
		menutop.setLayout(new BoxLayout(menutop, BoxLayout.Y_AXIS));
		menutitle = new JPanel();
		menutitle.setBackground(Color.LIGHT_GRAY);
		menutitle.setLayout(new BoxLayout(menutitle, BoxLayout.Y_AXIS));
		menubuttons = new JPanel(new FlowLayout());
		menubuttons.setBackground(Color.LIGHT_GRAY);
		
		playButton = new JButton("Let's Play!");
		viewScores = new JButton("Leaderboard");
		
		titleLabel = new JLabel("Spelling Bee Game!");
		description = new JLabel("Create as many words as you can from the given hive");
		titleLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
		description.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		
		menubuttons.add(playButton);
		menubuttons.add(viewScores);

		menutitle.add(titleLabel,BorderLayout.CENTER);
		menutitle.add(Box.createVerticalStrut(50));
		menutitle.add(description,BorderLayout.SOUTH);
		
		menutop.add(Box.createVerticalGlue());
		menutop.add(menutitle);
		menutop.add(Box.createVerticalStrut(50));
		menutop.add(menubuttons);
		
		window.setContentPane(menutop);
		
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username = JOptionPane.showInputDialog(window, "Enter username", "Username", JOptionPane.PLAIN_MESSAGE);
				createGamePage();
				window.setContentPane(gametop);
				window.validate();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TableModel tablemodel = new TableModel(result);
		JTable table = new JTable(tablemodel);
		JScrollPane scrollpane = new JScrollPane(table);
		leaderboardwindow = new JFrame("Leaderboard");
		JPanel helper = new JPanel();
		JPanel toppanel = new JPanel();
		JPanel buttonpanel = new JPanel(new FlowLayout());
		JLabel enter = new JLabel("Sort by username: ");
		JTextField input = new JTextField(10);
		JButton menu = new JButton("Back to menu");
		JButton seeall = new JButton("See all");
		
		toppanel.setLayout(new BoxLayout(toppanel,BoxLayout.X_AXIS));
		helper.setLayout(new BoxLayout(helper,BoxLayout.Y_AXIS));
		
		toppanel.add(Box.createHorizontalStrut(50));
		toppanel.add(Box.createHorizontalGlue());
		
		helper.add(Box.createVerticalGlue());
		
		helper.add(enter);
		helper.add(input);
		helper.add(seeall);
		
		helper.add(Box.createVerticalStrut(30));
		
		helper.add(scrollpane);
		
		helper.add(Box.createVerticalStrut(30));
		
		buttonpanel.add(menu);
		helper.add(buttonpanel);
		
		helper.add(Box.createVerticalGlue());
		
		toppanel.add(helper);
		toppanel.add(Box.createHorizontalGlue());
		toppanel.add(Box.createHorizontalStrut(50));
		
		leaderboardwindow.setSize(400,300);
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				TableModel tablemodel = new TableModel(results);
				JTable table = new JTable(tablemodel);
				table.getColumnModel().getColumn(0).setMaxWidth(100);
				table.getColumnModel().getColumn(1).setMaxWidth(90);
				table.getColumnModel().getColumn(2).setMaxWidth(110);
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				TableModel tablemodel = new TableModel(results);
				JTable table = new JTable(tablemodel);
				table.getColumnModel().getColumn(0).setMaxWidth(100);
				table.getColumnModel().getColumn(1).setMaxWidth(90);
				table.getColumnModel().getColumn(2).setMaxWidth(110);
				JScrollPane scrollpane2 = new JScrollPane(table);
				scrollpane.setViewportView(scrollpane2);
			}
		});
	}

	public void createGamePage() {
		StringBuilder playerwords = new StringBuilder();
		Set<String> usedwords = new HashSet<String>();
		
		score = 0;
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
		
		gametop = new JPanel();
		gamehelper = new JPanel();
		timer = new JLabel();
		hive = new JLabel();
		hive.setFont(new Font("TimesRoman",Font.BOLD, 30));
		input = new JTextField(10);
		enter = new JLabel("Enter words: ");
		textarea = new JLabel("Your score: " + score);
		scores = new JTextArea();
		
		yourhive = new JLabel("Hive:");
		gamehelper.setLayout(new BoxLayout(gamehelper, BoxLayout.Y_AXIS));
		
		gamehelper.add(Box.createVerticalStrut(30));
		timer.setFont(new Font("TimesRoman", Font.BOLD, 30));
		gamehelper.add(timer);
		
		gamehelper.add(Box.createVerticalStrut(30));
		
		yourhive.setFont(new Font("TimesRoman",Font.BOLD, 30));
		gamehelper.add(yourhive);
		
		gamehelper.add(Box.createVerticalStrut(30));
		
		String hiveletters = game.generateHive();
		hive.setText(hiveletters);
		gamehelper.add(hive);

		
		input.setSize(new Dimension(50,50));
		input.setPreferredSize(new Dimension(50,50));
		gamehelper.add(Box.createVerticalStrut(50));
		
		gamehelper.add(enter);
		gamehelper.add(input);
		
		gamehelper.add(Box.createVerticalStrut(30));
		
		gamehelper.add(textarea);
		scoresholder = new JScrollPane(scores);
		gamehelper.add(scoresholder);
		
		gamehelper.add(Box.createVerticalGlue());
		
		gametop.add(gamehelper);
		
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = input.getText();
				Boolean check = game.checkValid(s) && game.checkLetters(s) && !usedwords.contains(s);
				if(check) {
					usedwords.add(s);
					int wordscore = game.getScore(s);
					score += wordscore;
					textarea.setText("Your score: " + score);
					playerwords.append(s + ": " + wordscore + "\n");
					String area = playerwords.toString();
					scores.setText(area);
				}else {
					input.setText("invalid input!");
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
			sql.insert(username, score, "scores");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		window.setContentPane(menutop);
		window.validate();
	}
}
