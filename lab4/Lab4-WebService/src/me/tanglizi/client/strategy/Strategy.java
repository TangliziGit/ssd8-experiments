package me.tanglizi.client.strategy;

import me.tanglizi.entity.TodoItem;
import me.tanglizi.entity.User;
import me.tanglizi.model.Response;
import me.tanglizi.service.TodoListService;
import me.tanglizi.util.TimeUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Strategies of operations in client menu.
 * Too lazy to write in one and one class.
 *
 * @author Zhang Chunxu
 */
public class Strategy {

    public static void ignoreChoice(Scanner scanner, TodoListService service) {
        System.err.println("No such choice, please enter a correct number.");
    }

    public static void register(Scanner scanner, TodoListService service) {
        Pair<String, String> userInformation = getUsernameAndPassword(scanner);
        String username = userInformation.getFirst();
        String password = userInformation.getSecond();

        Response<User> response = service.register(username, password);

        if (!response.isSuccess()) {
            System.err.println("Registration failed, try another username!");
            System.err.println("Detail error information: " + response.getMessage());
        } else {
            System.out.println("Registration succeeded.");
        }
    }

    public static void addItem(Scanner scanner, TodoListService service) {
        Pair<String, String> userInformation = getUsernameAndPassword(scanner);
        String username = userInformation.getFirst();
        String password = userInformation.getSecond();

        Pair<Date, Date> startAndEndDate = getStartAndEndTime(scanner);
        Date start = startAndEndDate.getFirst();
        Date end = startAndEndDate.getSecond();

        System.err.println("Enter todo item tag: ");
        String tag = scanner.nextLine();

        Response<TodoItem> response = service.addItem(username, password, start, end, tag);

        if (!response.isSuccess()) {
            System.err.println("AddItem operation failed.");
            System.err.println("Detail error information: " + response.getMessage());
        } else {
            System.out.println("Add succeeded.");
            System.out.println("Here is your new todo item: " + response.getPayload());
        }
    }

    public static void searchItems(Scanner scanner, TodoListService service) {
        Pair<String, String> userInformation = getUsernameAndPassword(scanner);
        String username = userInformation.getFirst();
        String password = userInformation.getSecond();

        Pair<Date, Date> startAndEndDate = getStartAndEndTime(scanner);
        Date start = startAndEndDate.getFirst();
        Date end = startAndEndDate.getSecond();

        Response<ArrayList<TodoItem>> response = service.searchItems(username, password, start, end);

        if (!response.isSuccess()) {
            System.err.println("SearchItems operation failed.");
            System.err.println("Detail error information: " + response.getMessage());
        } else {
            System.out.println("Search succeeded.");
            System.out.println("Below is your results:");
            for (TodoItem item: response.getPayload())
                System.out.println(item);
        }
    }

    public static void removeItem(Scanner scanner, TodoListService service) {
        Pair<String, String> userInformation = getUsernameAndPassword(scanner);
        String username = userInformation.getFirst();
        String password = userInformation.getSecond();

        System.err.println("Enter the item id: ");
        Integer itemId = Integer.parseInt(scanner.nextLine());

        Response<Serializable> response = service.removeItem(username, password, itemId);

        if (!response.isSuccess()) {
            System.err.println("Removing an item operation failed.");
            System.err.println("Detail error information: " + response.getMessage());
        } else {
            System.out.println("The item has been removed succeeded.");
        }
    }

    public static void clearItems(Scanner scanner, TodoListService service) {
        Pair<String, String> userInformation = getUsernameAndPassword(scanner);
        String username = userInformation.getFirst();
        String password = userInformation.getSecond();

        System.err.println("Are you sure to clear your todo item list? [y/n]");
        String confirm = scanner.nextLine();

        if (!"y".equals(confirm)) {
            System.err.println("You have given up this operation.");
            return;
        }

        Response<Serializable> response = service.clearItems(username, password);

        if (!response.isSuccess()) {
            System.err.println("CleanItems operation failed.");
            System.err.println("Detail error information: " + response.getMessage());
        } else {
            System.out.println("Your item list has been cleared succeeded.");
        }
    }

    private static Pair<String, String> getUsernameAndPassword(Scanner scanner) {
        String username, password;
        System.err.println("Enter username: ");
        username = scanner.nextLine();

        System.err.println("Enter password: ");
        password = scanner.nextLine();

        return new Pair<>(username, password);
    }

    private static Pair<Date, Date> getStartAndEndTime(Scanner scanner) {
        Date start, end;
        try {
            System.err.println("Enter start date: ");
            start = TimeUtil.parse(scanner.nextLine());

            System.err.println("Enter end date: ");
            end = TimeUtil.parse(scanner.nextLine());
        } catch (ParseException e) {
            System.err.println("please enter a correct date value (yyyy-MM-dd HH:MM).");
            System.err.println("example: `2020-1-1 00:00`");

            return null;
        }

        return new Pair<>(start, end);
    }

    static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }
}
