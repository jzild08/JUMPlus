package ColorDesign;

public class ColorPrompts {
	
	final static String RESET = "\\033[0m";
	public static void errorMessage (String message){
		
		final String RED = "\033[0;31m";
		System.out.println(RED + message);
	}
	
	public static void menu (String message){
		
		final String PURPLE = "\033[0;35m";
		System.out.println(PURPLE + message);
	}

	public static void selection (String message){
		
		final String YELLOW = "\033[0;33m";
		System.out.println(YELLOW + message);
	}

	public static void input (String message){
		
		final String CYAN = "\033[0;36m";
		System.out.println(CYAN + message);
	}

	public static void greenPrompt (String message){
		
		final String GREEN = "\033[0;32m";
		System.out.println(GREEN + message);
	}
	
	public static void titles (String message) {
		final String YELLOW_BOLD = "\033[1;33m";
		System.out.println(YELLOW_BOLD + message);
	}
}
