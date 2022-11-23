import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SpellingGame{
	private static String username;
	private static StringBuilder hive; 
	private static Set<Character> hiveLetters;
	private static char[] upperConstanants = {'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 
			'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X','Z'};
	private static char[] lowerConstanants = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 
			'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x','z'};
	private static char[] upperVowels = {'A', 'E', 'I', 'O', 'U', 'Y'};
	private static char[] lowerVowels = {'a', 'e', 'i', 'o', 'u', 'y'};
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter(System.lineSeparator());
		generateHive();

		System.out.println(hive);
		System.out.println(hiveLetters.toString());
		
		System.out.println("Enter words: ");
		while(true) {
			String word = scanner.nextLine();
			
			System.out.println(checkLetters(word) && checkValid(word));
		}
	}
	
	public static void generateHive() {
		hiveLetters = new HashSet<Character>();
		hive = new StringBuilder();
		Random rand = new Random();
		int int_random;
		
		for(int i = 0;i<2;i++) {
			int_random = rand.nextInt(20);
			hive.append(upperConstanants[int_random]);
			hiveLetters.add(upperConstanants[int_random]);
			hiveLetters.add(lowerConstanants[int_random]);
		}
		
		for(int i = 0;i< 2;i++) {
			int_random = rand.nextInt(6);
			hive.append(upperVowels[int_random]);
			hiveLetters.add(upperVowels[int_random]);
			hiveLetters.add(lowerVowels[int_random]);
		}
		
		for(int i = 0;i<2;i++) {
			int_random = rand.nextInt(20);
			hive.append(upperConstanants[int_random]);
			hiveLetters.add(upperConstanants[int_random]);
			hiveLetters.add(lowerConstanants[int_random]);
		}
		
		
	}
	
	public static Boolean checkValid(String word) {
		String createString = "https://wordsapiv1.p.rapidapi.com/words/" + word + "/definitions";
		
		//create request
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(createString))
				.header("X-RapidAPI-Key", "3a08e177d0msh59e222ec3e593ccp1a24cdjsnb90f6604288a")
				.header("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		
		//get HttpResponse
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//get response as string
		String stringResp = response.body();
		
		//parse the response and get as JSONObject
		Object obj = null;
		try {
			obj = new JSONParser().parse(stringResp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject jo = (JSONObject) obj;
		
		//return whether the word is valid or not
		if(jo.get("word") == null) {
			return false;
		} else return true;
	}
	
	public static Boolean checkLetters(String word) {
		for(int i = 0;i<word.length();i++) {
			if(!(hiveLetters.contains(word.charAt(i)))){
				return false;
			}
		}
		return true;
	}
}
