package me.tanglizi.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User java bean, contains username, password and todoItemsList information.
 *
 * @author Zhang Chunxu
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private String password;
    private Set<TodoItem> todoItems = new HashSet<>();

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean authorize(String name, String password) {
        return getName().equals(name) && getPassword().equals(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                password.equals(user.password) &&
                todoItems.equals(user.todoItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, todoItems);
    }

    public Set<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(Set<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
