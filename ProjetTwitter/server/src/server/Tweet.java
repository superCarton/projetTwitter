package server;

public class Tweet {

	private String author;
	private String message;
	
	public Tweet(String author, String message){
		this.author = author;
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public String getAuthor(){
		return author;
	}
	
}
