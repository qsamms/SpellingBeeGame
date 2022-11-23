public class SpellingGameGUI{
	private static SpellingGame game;
	
	public SpellingGameGUI(SpellingGame game) {
		
	}
	
	public static void main(String[] args) {
		game = new SpellingGame();
		new SpellingGameGUI(game);
	}
}