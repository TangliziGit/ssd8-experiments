package org.tanglizi.dist.rmi.server;

import org.tanglizi.dist.rmi.service.impl.RmiServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RmiServer, simply run RmiRegistry.
 *
 * @author Zhang Chunxu
 */
public class RmiServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            Naming.rebind("//localhost:1099/SSD8Exam2", new RmiServiceImpl());

            System.out.println("Server is running.");
        } catch (Exception e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}
