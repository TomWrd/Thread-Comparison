import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * The Server class serves as the server, creates server socket, and starts server commands.
 * @author Peter Nguyen
 * @version 9/30/2017
 */
public class ServerP2 {
		
	public static void main(String[] args) throws IOException, InterruptedException {
		// Create socket to connect
		ServerSocket serverSocket = new ServerSocket(4444);
		System.out.println("Server Waiting for Connection...");
		// Create server threads
		while(true){
            new MultiThread(serverSocket.accept()).start();
        }
	}
}
/**
 * The ServerThread class runs user commands in the form of threads
 * @author Peter Nguyen
 *
 */
	class MultiThread extends Thread{
		Socket clientSocket = null;
		// Constructor
	    public MultiThread(Socket clientSocket){
	        this.clientSocket = clientSocket;
	    }
		/**
		 * Method that accepts user input, perform commands, and send output to user
		 */
	    public void run(){
		try {
			while(true) {
				// Create socket to handle request
				String choice;
				System.out.println("Connection Established on Port 4444");
				// Accept input and perform command
				BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);		
				while (!clientSocket.isClosed() && ((choice = input.readLine()) != null)) {
					Process cmdProc = null;		
					// Command 1 date and time request
					if(choice.equals("date")){
						System.out.println("Responding to date and time request from the client ");
						String[] cmd = {"bash", "-c", "date"};
						cmdProc = Runtime.getRuntime().exec(cmd);
					}
					// Command 2 uptime request
					else if(choice.equals("uptime")){
						System.out.println("Responding to uptime request from the client ");
						String[] cmd = {"bash", "-c", "uptime"};
						cmdProc = Runtime.getRuntime().exec(cmd);
					}
					// Command 3 memory use request
					else if(choice.equals("free")){
						System.out.println("Responding to memory use request from the client ");
						String[] cmd = {"bash", "-c", "free"};
						cmdProc = Runtime.getRuntime().exec(cmd);
					}
					//Command 4 netstat request
					else if(choice.equals("netstat")){
						System.out.println("Responding to netstat request from the client ");
		            	String[] cmd = {"bash", "-c", "netstat"};
		            	cmdProc = Runtime.getRuntime().exec(cmd);
					}	
					// Command 5 list of current users request
					else if(choice.equals("who")){
						System.out.println("Responding to list of current users request from the client");
						String[] cmd = {"bash", "-c", "who"};
						cmdProc = Runtime.getRuntime().exec(cmd);
					}
					// Command 6 running process request
					else if(choice.equals("process")){
						System.out.println("Responding to running process request from the client");
						String[] cmd = {"bash", "-c", "ps -e"};
						cmdProc = Runtime.getRuntime().exec(cmd);
					}
					// Command 7 exit request
					else if(choice.equals("exit")){
						System.out.println("Responding to exit request ");
						clientSocket.close();		           
						String[] cmd = {"bash", "-c", "exit"};
						cmdProc = Runtime.getRuntime().exec(cmd);
						System.out.println("Server Closed");
						System.exit(0);
					}
					// Send output to client
					BufferedReader cmdin = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
					String cmdout = null;		            
					while((cmdout = cmdin.readLine()) != null){
						output.println(cmdout);
					}
					// Tell client to stop receiving
					output.close();
				}
		}
			}
			catch (IOException e){
			
			}
	}
	}
	
		
		
		
	
	

