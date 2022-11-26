import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class SpellingGame{
	StringBuilder hive = new StringBuilder(); 
	String lowercaseHive;
	Set<Character> hiveLetters = new HashSet<Character>();
	Set<Character> startLetters = new HashSet<Character>();
	Random rand = new Random();
	private static char[] upperConstanants = {'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 
			'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X','Z'};
	private static char[] lowerConstanants = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 
			'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x','z'};
	private static char[] upperVowels = {'A', 'E', 'I', 'O', 'U'};
	private static char[] lowerVowels = {'a', 'e', 'i', 'o', 'u'};
	
	public String generateHive() {
		startLetters.clear();
		hiveLetters.clear();
		hive.setLength(0);
		int int_random;
		
		//two constanants
		for(int i = 0;i<3;i++) {
			int_random = rand.nextInt(20);
			while(hiveLetters.contains(upperConstanants[int_random])) {
				int_random = rand.nextInt(20);
			}
			hive.append(upperConstanants[int_random]);
			hiveLetters.add(upperConstanants[int_random]);
			hiveLetters.add(lowerConstanants[int_random]);
		}
		
		//two vowels
		for(int i = 0;i< 2;i++) {
			int_random = rand.nextInt(5);
			while(hiveLetters.contains(upperVowels[int_random])) {
				int_random = rand.nextInt(5);
			}
			hive.append(upperVowels[int_random]);
			hiveLetters.add(upperVowels[int_random]);
			hiveLetters.add(lowerVowels[int_random]);
		}
		
		//two constanants
		for(int i = 0;i<3;i++) {
			int_random = rand.nextInt(20);
			while(hiveLetters.contains(upperConstanants[int_random])) {
				int_random = rand.nextInt(20);
			}
			hive.append(upperConstanants[int_random]);
			hiveLetters.add(upperConstanants[int_random]);
			hiveLetters.add(lowerConstanants[int_random]);
		}
		
		shuffle(hive.toString());
		String s = hive.toString();
		lowercaseHive = s.toLowerCase();
		getStartLetterIndexes();
		return hive.toString();
	}
	
	public void shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        
        hive = output;
    }
	
	public void getStartLetterIndexes() {
		int max = hive.length();
		Set<Integer> startLetterIdx = new HashSet<Integer>();
		int int_random;
		
		while(startLetters.size() < 4) {
			int_random = rand.nextInt(max);
			if(!startLetterIdx.contains(int_random)) {
				startLetters.add(hive.charAt(int_random));
				startLetters.add(lowercaseHive.charAt(int_random));
				startLetterIdx.add(rand.nextInt(max));
			}
		}
		System.out.println(startLetters.toString());
	}
	
	public void checkValid(String word, Set<String> usedwords) throws NotaWordException, LettersException, WordUsedException, 
	TooShortException, StartLetterException{
		
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
			throw new NotaWordException();
		} 
		
		if(checkLetters(word) == false) {
			throw new LettersException();
		}
		
		if(usedwords.contains(word)) {
			throw new WordUsedException();
		}
		
		if(checkLength(word) == false) {
			throw new TooShortException();
		}
		
		if(checkStartletter(word) == false) {
			throw new StartLetterException();
		}
	}
	
	public Boolean checkLetters(String word) {
		for(int i = 0;i<word.length();i++) {
			if(!(hiveLetters.contains(word.charAt(i)))){
				return false;
			}
		}
		return true;
	}
	
	public Boolean checkLength(String word) {
		if(word.length() < 3) {
			return false;
		}
		return true;
	}
	
	public Boolean checkStartletter(String word) {
		if(startLetters.contains(word.charAt(0))) {
			return true;
		}
		return false;
	}
	
	public int getScore(String word) {
		int length = word.length();
		
		if(length == 1) {
			return 25;
		}else if(length == 2) {
			return 50;
		}else if(length == 3) {
			return 100;
		}else if(length == 4) {
			return 200;
		}else if(length == 5) {
			return 300;
		}else{
			return 500;
		}
		
	}
}
