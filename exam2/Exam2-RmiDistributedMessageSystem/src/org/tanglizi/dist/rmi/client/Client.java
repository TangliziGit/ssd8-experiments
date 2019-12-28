package org.tanglizi.dist.rmi.client;

import org.tanglizi.dist.rmi.entity.Message;
import org.tanglizi.dist.rmi.entity.User;
import org.tanglizi.dist.rmi.model.MessageModel;
import org.tanglizi.dist.rmi.model.Response;
import org.tanglizi.dist.rmi.service.RmiService;

import java.io.*;
import java.rmi.*;
import java.util.*;

/**
 * class <em>Client</em> represents a client for the SSD8 Distributed Message
 * Center.
 *
 * @author wben
 * @version 1.0
 *
 */
public class Client {

	/**
	 * main method
	 */
	public static void main(String[] args) {

		int portNumber = 1099;
		String hostname = "localhost";

		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		/**
		 * Print usage instructions if the incorrect paramteres are supplied.
		 */
		if (args.length != 2) {
			System.out.println("Usage: Client <hostname> <port>");
			System.exit(0);
		} else {
			portNumber = Integer.parseInt(args[1]);
			hostname = args[0];
		}

		int choice = 0;
		String username, password, recipientname, message;

		/**
		 * Create the connection to the message center.
		 */
		RmiService service = null;

		try {
			service = (RmiService) Naming.lookup("rmi://" + hostname + ":" + portNumber + "/SSD8Exam2");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * Loop forever while displaying the menu and performing operations for
		 * the user.
		 */
		while (true) {

			System.out.println("********** Distributed Message Center ********** ");
			System.out.println("(1) Show registered users ");
			System.out.println("(2) Register a new user");
			System.out.println("(3) Check Messages");
			System.out.println("(4) Leave a message");
			System.out.println("(5) Exit");
			System.out.println("************************************************");
			System.out.println("Enter choice: ");
			try {
				choice = Integer.parseInt(keyboard.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (choice == 1) {
				try {
					Response<List<String>> response = service.showUsers();

					if (!response.isSuccess()) {
						System.out.println(response.getMessage());
					} else for (String name: response.getPayload()) {
						System.out.println(name);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (choice == 2) {
				try {
					System.out.println("Enter username: ");
					username = keyboard.readLine();
					System.out.println("Enter password: ");
					password = keyboard.readLine();

					Response<User> response = service.register(username, password);

					if (!response.isSuccess()) {
						System.out.println("Registration failed, try another username!");
						System.out.println("Detail error information: " + response.getMessage());
					} else {
						System.out.println("Registration succeeded!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (choice == 3) {
				try {
					System.out.println("Enter username: ");
					username = keyboard.readLine();
					System.out.println("Enter password: ");
					password = keyboard.readLine();

					Response<List<MessageModel>> response = service.checkMessages(username, password);

					if (!response.isSuccess()) {
						System.out.println("Authentication failed or you have no messages!");
						System.out.println("Detail error information: " + response.getMessage());
					} else {
						System.out.println("Your messages: ");
						for (MessageModel messageModel: response.getPayload())
							System.out.println(messageModel);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (choice == 4) {
				try {
					System.out.println("Enter your username: ");
					username = keyboard.readLine();
					System.out.println("Enter your password: ");
					password = keyboard.readLine();
					System.out.println("Enter the recipient name: ");
					recipientname = keyboard.readLine();
					System.out.println("Enter the message: ");
					message = keyboard.readLine();

					Response<Message> response = service.leaveMessage(username, password, recipientname, message);

					if (!response.isSuccess()) {
						System.out.println("Trouble leaving message!");
						System.out.println("Detail error information: " + response.getMessage());
					} else {
						System.out.println("Your message: " + response.getPayload());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (choice == 5) {
				System.exit(0);
			}
		}
	}
}
