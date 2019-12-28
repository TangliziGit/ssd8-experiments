package org.tanglizi.dist.rmi.server;

import org.tanglizi.dist.rmi.service.impl.RmiServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RmiServer class, to start rmi registry.
 *
 * @author Chunxu Zhang
 */
public class RmiServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            Naming.rebind("//localhost:1099/service", new RmiServiceImpl());

            System.out.println("Server is running.");
        } catch (Exception e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}
