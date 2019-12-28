package org.tanglizi.dist.rmi.client.strategy.impl;

import org.tanglizi.dist.rmi.client.strategy.CommandStrategy;
import org.tanglizi.dist.rmi.model.Response;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;
import java.util.List;

public class ShowUsersStrategy implements CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws RemoteException {
        Response<List<String>> response = service.showUsers();

        if (response.isSuccess()) {
            System.out.println("Below is all registered user:");
            for (String username: response.getPayload())
                System.out.println(username);
        } else {
            System.out.println(response.getMessage());
        }
    }
}
