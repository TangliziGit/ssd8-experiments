package org.tanglizi.dist.rmi.client.strategy.impl;

import org.tanglizi.dist.rmi.client.strategy.CommandStrategy;
import org.tanglizi.dist.rmi.entity.Message;
import org.tanglizi.dist.rmi.model.Response;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;

public class LeaveMessagesStrategy implements CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String receiverName = args[5];
        String messageText = args[6];

        Response<Message> response = service.leaveMessage(username, password, receiverName, messageText);

        if (response.isSuccess()) {
            System.out.println("Message has been leaved succeed, this is your message:");
            System.out.println(response.getPayload());
        } else {
            System.out.println("Leaving message failed.");
            System.out.println(response.getMessage());
        }
    }
}
