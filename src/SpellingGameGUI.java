import java.util.Scanner;

public class SpellingGameGUI{
	private static SpellingGame game;
	
	public SpellingGameGUI(SpellingGame game) {
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter(System.lineSeparator());
		game.generateHive();

		System.out.println(game.hive);
		System.out.println(game.hiveLetters.toString());
		
		System.out.println("Enter words: ");
		while(true) {
			String word = scanner.nextLine();
			
			System.out.println(game.checkLetters(word) && game.checkValid(word));
		}
	}
	
	public static void main(String[] args) {
		game = new SpellingGame();
		new SpellingGameGUI(game);
	}
}