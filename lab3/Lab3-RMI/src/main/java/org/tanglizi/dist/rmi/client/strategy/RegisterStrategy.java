package org.tanglizi.dist.rmi.client.strategy;

import org.tanglizi.dist.rmi.entity.User;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;

/**
 * RegisterStragety class, a command strategy implement.
 *
 * @author Chunxu Zhang
 */
public class RegisterStrategy extends CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws RemoteException {
        String name = args[3];
        String password = args[4];
        User user = service.register(name, password);

        if (null == user)
            System.err.println("[ERROR] Such user is existing, please enter another name to register.");
        else
            System.out.println("[INFO] Registered successfully.");
    }
}
