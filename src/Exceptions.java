class NotaWordException extends Exception{
	public NotaWordException() {
		this("Not a Valid Word");
	}

	public NotaWordException(String string) {
		super(string);
	}
}

class LettersException extends Exception{
	public LettersException() {
		this("One or more letters not in hive");
	}
	
	public LettersException(String string) {
		super(string);
	}
}

class WordUsedException extends Exception{
	public WordUsedException() {
		this("Word already used");
	}
	
	public WordUsedException(String string) {
		super(string);
	}
}

class TooShortException extends Exception{
	public TooShortException() {
		this("Word length must at least 3 letters");
	}
	
	public TooShortException(String string) {
		super(string);
	}
}

class StartLetterException extends Exception{
	public StartLetterException() {
		this("Must start with one of the given letters");
	}
	
	public StartLetterException(String string) {
		super(string);
	}
}