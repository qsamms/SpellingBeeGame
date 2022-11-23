
public class SpellingGameGUI{
	private static SpellingGame game;
	private static SQL sql;
	
	public SpellingGameGUI(SpellingGame game, SQL sql) {
		
	}
	
	public static void main(String[] args) {
		game = new SpellingGame();
		sql = new SQL();
		new SpellingGameGUI(game,sql);
	}
}
