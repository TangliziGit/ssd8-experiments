package org.tanglizi.dist.rmi.client.strategy.impl;

import org.tanglizi.dist.rmi.client.strategy.CommandStrategy;
import org.tanglizi.dist.rmi.entity.User;
import org.tanglizi.dist.rmi.model.Response;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;

public class RegisterStrategy implements CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws RemoteException {
        String username = args[3];
        String password = args[4];

        Response<User> response = service.register(username, password);

        if (response.isSuccess()) {
            System.out.println("Register succeed, this is your account:");
            System.out.println(response.getPayload());
        } else {
            System.out.println("Register failed.");
            System.out.println(response.getMessage());
        }
    }
}
