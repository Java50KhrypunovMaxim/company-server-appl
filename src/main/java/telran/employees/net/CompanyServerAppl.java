package telran.employees.net;

import java.util.Scanner;

import telran.employees.*;
import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.TcpServer;

public class CompanyServerAppl {

	private static final String FILE_NAME = "employeesTest.data";
	private static final int PORT = 5000;

	public static void main(String[] args) {

		Company company = new CompanyMapsImpl();
		try {
			((Persistable) company).restore(FILE_NAME);
		} catch (Exception e) {
			System.out.println("Failed to restore company data.");
		}
		Protocol protocol = new CompanyProtocol(company);
		TcpServer tcpServer = new TcpServer(protocol, PORT);
		Thread newServer = new Thread(() -> tcpServer.run());
		newServer.start();
		Scanner lineScanner = new Scanner(System.in);
		boolean serverRunning = true;
		while (serverRunning) {
			System.out.println("Enter 'shutdown' to stop the server:");
			String answer = lineScanner.nextLine();
			if ("shutdown".equalsIgnoreCase(answer)) {
				tcpServer.shutdown();
				serverRunning = false;
				System.out.println("Server was stop");
				break;
			} else {
				System.out.println("Unknown command. Please enter 'shutdown' to stop the server.");
			}
		}

		lineScanner.close();

		try {
			((Persistable) company).save(FILE_NAME);
		} catch (Exception e) {
			System.out.println("Failed to save company data.");
		}
	}

}
