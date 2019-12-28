package org.tanglizi.dist.rmi.client.strategy.impl;

import org.tanglizi.dist.rmi.client.strategy.CommandStrategy;
import org.tanglizi.dist.rmi.model.MessageModel;
import org.tanglizi.dist.rmi.model.Response;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;
import java.util.List;

public class CheckMessagesStrategy implements CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws RemoteException {
        String username = args[3];
        String password = args[4];

        Response<List<MessageModel>> response = service.checkMessages(username, password);

        if (response.isSuccess()) {
            System.out.println("Below is all your messages:");
            for (MessageModel message: response.getPayload())
                System.out.println(message);
        } else {
            System.out.println("Check messages operation failed.");
            System.out.println(response.getMessage());
        }
    }
}
