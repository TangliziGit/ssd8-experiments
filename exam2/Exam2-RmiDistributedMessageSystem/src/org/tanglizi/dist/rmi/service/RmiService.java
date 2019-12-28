package org.tanglizi.dist.rmi.service;

import org.tanglizi.dist.rmi.entity.Message;
import org.tanglizi.dist.rmi.entity.User;
import org.tanglizi.dist.rmi.model.MessageModel;
import org.tanglizi.dist.rmi.model.Response;

import javax.annotation.Resource;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.List;

/**
 * RmiService interface
 *
 * @author Zhang Chunxu
 */
public interface RmiService extends Remote {

    Response<User> register(String name, String password) throws RemoteException;
    Response<List<String>> showUsers() throws RemoteException;
    Response<List<MessageModel>> checkMessages(String name, String password) throws RemoteException;
    Response<Message> leaveMessage(String name, String password, String receiverName, String message) throws RemoteException;

}
