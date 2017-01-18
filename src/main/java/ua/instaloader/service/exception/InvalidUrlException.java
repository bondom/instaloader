package ua.instaloader.service.exception;

public class InvalidUrlException extends RuntimeException {
	
	public InvalidUrlException(){
		super();
	}
	
	public InvalidUrlException(String msg){
		super(msg);
	}
	
	public InvalidUrlException(Throwable er){
		super(er);
	}
}

