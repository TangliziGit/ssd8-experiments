package me.tanglizi.service.impl;

import me.tanglizi.entity.TodoItem;
import me.tanglizi.entity.User;
import me.tanglizi.model.Response;
import me.tanglizi.service.TodoListService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TodoListServiceImpl implements TodoListService, the service provider.
 *
 * @author Zhang Chunxu
 */
@WebService(name = "TodoService", targetNamespace = "http://service.tanglizi.me", endpointInterface = "me.tanglizi.service.TodoListService")
public class TodoListServiceImpl implements TodoListService {

    private static final String FAIL_TO_AUTHORIZATION =
            "Authorization not passed, please enter a correct name and password.";
    private static final String FAIL_TO_CHECK_DATE_AVAILABLE =
            "Date availability checking not passed, please enter a correct date duration.";

    private Set<User> userSet = new HashSet<>();

    private static Integer nextTodoId = 1;

    @WebMethod
    public Response<User> register(String name, String password) {
        boolean isDuplicatedUserExists = userSet.stream()
                .anyMatch(x -> x.getName().equals(name));

        if (isDuplicatedUserExists)
            return Response.fail("This username is duplicated, please enter another one.");

        User user = new User(name, password);
        userSet.add(user);
        return Response.ok(user);
    }

    @WebMethod
    public Response<TodoItem> addItem(String username, String password, Date start, Date end, String tag) {
        User user = authorize(username, password);

        if (validateDuration(start, end))
            return Response.fail(FAIL_TO_CHECK_DATE_AVAILABLE);

        if (null == user)
            return Response.fail(FAIL_TO_AUTHORIZATION);

        TodoItem todoItem = new TodoItem(nextTodoId++, start.toInstant(), end.toInstant(), tag);
        user.getTodoItems().add(todoItem);

        return Response.ok(todoItem);
    }

    @WebMethod
    public Response<ArrayList<TodoItem>> searchItems(String username, String password, Date start, Date end) {
        User user = authorize(username, password);

        if (validateDuration(start, end))
            return Response.fail(FAIL_TO_CHECK_DATE_AVAILABLE);

        if (null == user)
            return Response.fail(FAIL_TO_AUTHORIZATION);

        ArrayList<TodoItem> items = user.getTodoItems().stream()
                .filter(x -> x.during(start.toInstant(), end.toInstant()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (items.isEmpty())
            return Response.fail("Your search result is empty.", items);
        return Response.ok(items);
    }

    @WebMethod
    public Response<Serializable> removeItem(String username, String password, Integer itemId) {
        User user = authorize(username, password);

        if (null == user)
            return Response.fail(FAIL_TO_AUTHORIZATION);

        boolean isRemoved = user.getTodoItems().removeIf(x -> x.getId().equals(itemId));

        if (!isRemoved)
            return Response.fail("Such item not exists.");
        return Response.ok(null);
    }

    @WebMethod
    public Response<Serializable> clearItems(String username, String password) {
        User user = authorize(username, password);

        if (null == user)
            return Response.fail(FAIL_TO_AUTHORIZATION);

        user.setTodoItems(new HashSet<>());

        return Response.ok(null);
    }

    private User authorize(String username, String password) {
        return userSet.stream()
                .filter(x -> x.authorize(username, password))
                .findAny().orElse(null);
    }

    private boolean validateDuration(Date start, Date end) {
        return start.after(end) && !start.equals(end);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar WebServiceServer.jar <port>");
            System.exit(1);
        }

        Integer port = Integer.parseInt(args[0]);
        Endpoint.publish(String.format("http://localhost:%d/todo", port), new TodoListServiceImpl());
        System.out.println("Service has been run.");
    }

}
