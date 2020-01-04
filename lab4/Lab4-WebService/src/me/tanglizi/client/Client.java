package me.tanglizi.client;

import me.tanglizi.service.TodoListService;
import me.tanglizi.client.strategy.Strategy;
import me.tanglizi.service.impl.TodoListServiceImpl;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

/**
 * Client class, provide user interface of all operations.
 *
 * @author Zhang Chunxu
 */
public class Client {

    private TodoListService service;
    private static Map<Integer, BiConsumer<Scanner, TodoListService>> strategyMap = new HashMap<>();

    static {
        strategyMap.put(1, Strategy::register);
        strategyMap.put(2, Strategy::addItem);
        strategyMap.put(3, Strategy::searchItems);
        strategyMap.put(4, Strategy::removeItem);
        strategyMap.put(5, Strategy::clearItems);
    }

    public Client(int port) throws MalformedURLException {
        URL wsdlUrl = new URL(String.format("http://localhost:8080/todo?wsdl", port));
        QName qName = new QName("http://service.tanglizi.me", "TodoListServiceImplService");
        this.service = Service.create(wsdlUrl, qName).getPort(TodoListService.class);
    }

    private void printMenu() {
        String menu = "\n" +
            "********** Todo List Service **********\n" +
            "(1) Register a new user\n" +
            "(2) Add a todo item\n" +
            "(3) Search todo items\n" +
            "(4) Remove a todo item \n" +
            "(5) Clear items\n" +
            "(6) Quit\n" +
            "***************************************\n" +
            "Please make your choice:";

        System.out.println(menu);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 6) break;
            strategyMap.getOrDefault(choice, Strategy::ignoreChoice)
                    .accept(scanner, service);
        }
        System.out.println("Have a nice day.");
        System.out.println("Bye.");
    }

    public static void main(String[] args) throws MalformedURLException {
        if (args.length != 1) {
            System.err.println("Usage: java -jar WebServiceClient.jar <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        new Client(port).run();
    }
}
