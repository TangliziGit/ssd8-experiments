package org.tanglizi.dist.rmi.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity bean, implements Serializable, to enable rmi transforming.
 *
 * @author Chunxu Zhang
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1;

    private Integer userId;
    private String name;
    private String password;
    private Set<Integer> agenda;

    public User(Integer userId, String name, String password) {
        super();
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.agenda = new HashSet<>();
    }

    public Set<Integer> getAgenda() {
        return agenda;
    }

    public void setAgenda(Set<Integer> agenda) {
        this.agenda = agenda;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public boolean authorize(String name, String password) {
        return this.name.equals(name) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
