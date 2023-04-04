package dolalrsbankapp.exceptions;

public class ConnectionExceptions extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void ConnectionTimeOutException() {
		
		System.out.println("/nConnection timed out or not established/n");
	}
}
