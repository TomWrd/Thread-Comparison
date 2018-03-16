import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Client class serves as the client, creates threads, accepts user input, and print results.
 * @author Peter Nguyen
 * @version 9/30/2017
 */
public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);
		int choice;
		int cliCount;
		long totalTime=0;
		// Create array of threads
		ArrayList<ClientThread> clientThread = new ArrayList<ClientThread>();
		// Menu code
		while(true) {
			System.out.println();
			System.out.println("1. Host current date and time\n2. Host uptime\n3. Host memory use\n4. Host netstat\n5. Host current users\n6. Host running processes\n7. Quit");
			System.out.print("Please enter an integer from the menu: ");
			try {
				choice = Integer.parseInt(sc.next());
			}
			catch(NumberFormatException e) {
				choice = 0;
			}
			if (choice<1 || choice>7) {
				System.out.println("Invalid input. Please enter an integer from 1-7");
				continue;
			}
			else if (choice == 7) {
				clientThread.add(new ClientThread(choice));
				clientThread.get(0).start();
				System.out.println("Goodbye!");
				break;
			}
			// Client Thread code
			while (true) {
				System.out.print("How many clients between 1-75? ");
				try {
					cliCount = Integer.parseInt(sc.next());
				}
				catch (NumberFormatException e){
					cliCount = 0;
				}
				if (cliCount < 1 || cliCount > 75) {
					System.out.println("Invalid input. Please enter an integer from 1-75");
					continue;
				}
				else {		
					// Create, run, and join threads based on client count
					for (int i =0; i<cliCount;i++) {
						clientThread.add(new ClientThread(choice));
					}
					for (int i =0; i<cliCount;i++) {
						clientThread.get(i).start();
					}
					for (int i =0; i<cliCount;i++) {
						clientThread.get(i).join();
					}
					// Add all thread times
					for (int i =0; i<cliCount;i++) {
						totalTime+=clientThread.get(i).getTotal();
					}
					// Print response time
					System.out.println("Response time = " + totalTime + " millis");
					totalTime=0;
					// Clear array for more commands
					clientThread.clear();
					break;
				}
			}
		}
		sc.close();

	}
}
/**
 * ClientThread class creates threads and runs them
 * @author Peter Nguyen
 *
 */
class ClientThread extends Thread {
	String command;
	long total;
	// Constructor
	public ClientThread (int choice){
		// change choice to command
		if (choice == 1)
			command = "date";
		else if (choice ==2)
			command = "uptime";
		else if (choice ==3)
			command = "free";
		else if (choice ==4)
			command = "netstat";
		else if (choice ==5)
			command = "who";
		else if (choice ==6)
			command = "process";
		else
			command = "exit";
	}
	/**
	 * Method that sends user input to server
	 */
	public void run() {
		try {
			// Create socket
			Socket socket = new Socket("192.168.100.120", 4444);
			// Send command to server
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			// Get info from server and print results
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			// Record start time
			long end;
			long start = System.currentTimeMillis();
			printWriter.println(command);
			while ((line = bufferedReader.readLine()) != null) {
				end = System.currentTimeMillis()-start;
				total +=end;
				System.out.println(line);
				start = System.currentTimeMillis();
			}
			socket.close();
		}
		catch (IOException e){
			
		}		
	}
	/**
	 * Method that returns total thread time
	 * @return total time to run thread
	 */
	public long getTotal(){
		return total;
	}
}
