package org.tanglizi.dist.rmi.service.impl;

import org.tanglizi.dist.rmi.entity.Message;
import org.tanglizi.dist.rmi.entity.User;
import org.tanglizi.dist.rmi.model.MessageModel;
import org.tanglizi.dist.rmi.model.Response;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * RmiServiceImpl class, implemented RmiService interface, provide all rmi service methods.
 *
 * @author Zhang Chunxu
 */
public class RmiServiceImpl extends UnicastRemoteObject implements RmiService {

    private static Map<Integer, User> userMap = new HashMap<>();
    private static Map<Integer, Message> messageMap = new HashMap<>();

    private static Integer nextUserId = 1;
    private static Integer nextMessageId = 1;

    public RmiServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public Response<User> register(String name, String password) throws RemoteException {
        Optional<User> anyUser = userMap.values().stream()
                .filter(x -> x.getName().equals(name))
                .findAny();

        if (anyUser.isPresent())
            return Response.fail("This username is duplicated, please enter another one.", null);

        User user = new User(nextUserId++, name, password);
        userMap.put(user.getId(), user);

        return Response.ok(user);
    }

    @Override
    public Response<List<String>> showUsers() throws RemoteException {
        List<String> usernameList = userMap.values().stream()
                .map(User::getName)
                .collect(Collectors.toList());

        if (usernameList.size() == 0)
            return Response.fail("There is no user.", usernameList);

        return Response.ok(usernameList);
    }

    @Override
    public Response<List<MessageModel>> checkMessages(String name, String password) throws RemoteException {
        Optional<User> anyUser = userMap.values().stream()
                .filter(x -> x.authorize(name, password))
                .findAny();

        if (!anyUser.isPresent())
            return Response.fail("Authorization not passed, please check your name and password.", null);

        User user = anyUser.get();
        List<MessageModel> messageModelList = messageMap.values().stream()
                .filter(x -> x.getReceiverId().equals(user.getId()))
                .map(x -> MessageModel.buildFrom(x, userMap.get(x.getSenderId()).getName()) )
                .collect(Collectors.toList());

        if (messageModelList.size() == 0)
            return Response.fail("There is no message.", messageModelList);

        return Response.ok(messageModelList);
    }

    @Override
    public Response<Message> leaveMessage(String name, String password, String receiverName, String messageContent) throws RemoteException {
        Optional<User> anySender = userMap.values().stream()
                .filter(x -> x.authorize(name, password))
                .findAny();

        if (!anySender.isPresent())
            return Response.fail("Authorization not passed, please check your name and password.", null);

        Optional<User> anyReceiver = userMap.values().stream()
                .filter(x -> x.getName().equals(receiverName))
                .findAny();

        if (!anyReceiver.isPresent())
            return Response.fail("This receiver does not exist.", null);

        User sender = anySender.get();
        User receiver = anyReceiver.get();
        Message message = new Message(nextMessageId++, sender.getId(), receiver.getId(), messageContent, Instant.now());
        messageMap.put(message.getId(), message);

        return Response.ok(message);
    }
}
