package org.tanglizi.dist.rmi.client;

import org.tanglizi.dist.rmi.client.strategy.*;
import org.tanglizi.dist.rmi.client.strategy.impl.CheckMessagesStrategy;
import org.tanglizi.dist.rmi.client.strategy.impl.LeaveMessagesStrategy;
import org.tanglizi.dist.rmi.client.strategy.impl.RegisterStrategy;
import org.tanglizi.dist.rmi.client.strategy.impl.ShowUsersStrategy;
import org.tanglizi.dist.rmi.service.RmiService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * RmiClient class, provide client operations on terminal.
 *
 * @author Zhang Chunxu
 */
public class RmiClient {

    private RmiService service;
    private static Map<String, CommandStrategy> strategyMap = new HashMap<>();

    public RmiClient(String host, int port) throws RemoteException, NotBoundException, MalformedURLException {
        this.service = (RmiService) Naming.lookup(String.format("//%s:%d/service", host, port));
        initializeStrategyMap();
    }

    private static void initializeStrategyMap(){
        strategyMap.put("register", new RegisterStrategy());
        strategyMap.put("showUsers", new ShowUsersStrategy());
        strategyMap.put("checkMessages", new CheckMessagesStrategy());
        strategyMap.put("leaveMessage", new LeaveMessagesStrategy());
    }

    private static void printHelp() {
        String information = "Usage: <serverName> <portNumber> <command> [options...]\n" +
                "Commands:\n" +
                "\t" + "register <username> <password>\n" +
                "\t" + "showUsers\n" +
                "\t" + "checkMessages <username> <password>\n" +
                "\t" + "leaveMessage <username> <password> <receiverName> <messageText>\n";

        System.out.println(information);
    }

    private void process(String[] args) throws Exception {
        CommandStrategy strategy = strategyMap.get(args[2]);

        if (null == strategy)
            printHelp();
        else {
            strategy.process(args, service);
            System.out.println("");
        }
    }

    public static void main(String[] args) {

        if (args.length < 3){
            RmiClient.printHelp();
            System.exit(0);
        }

        try {
            RmiClient client = new RmiClient(args[0], Integer.parseInt(args[1]));
            client.process(args);

        } catch (Exception e) {
            System.err.println("org.tanglizi.dist.rmi.client.Client Error: " + e.getMessage());
            printHelp();
        }
    }
}
