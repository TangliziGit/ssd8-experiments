package me.tanglizi.service;

import me.tanglizi.entity.TodoItem;
import me.tanglizi.entity.User;
import me.tanglizi.model.Response;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * TodoListService interface, provide a static interface for WebService.
 *
 * @author Zhang Chunxu
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface TodoListService {

    Response<User> register(String name, String password);
    Response<TodoItem> addItem(String username, String password, Date start, Date end, String tag);
    Response<ArrayList<TodoItem>> searchItems(String username, String password, Date start, Date end);
    Response<Serializable> removeItem(String username, String password, Integer itemId);
    Response<Serializable> clearItems(String username, String password);

}
