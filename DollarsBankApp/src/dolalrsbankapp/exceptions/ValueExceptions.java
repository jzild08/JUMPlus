package dolalrsbankapp.exceptions;

public class ValueExceptions extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ValueExceptions() {}
	
	public void valueIsBelowZero() {
		
		System.out.println("\nThe value you entered is below Zero. Please enter a value that is above zero\n");
	}
	
	public void valueIsAString() {
		
		System.out.println("\nValue is a String\n");
	}
	
	public void wrongUserNameOrPassword() {
		
		System.out.println("\nWrong username or password\n");
	}
}
