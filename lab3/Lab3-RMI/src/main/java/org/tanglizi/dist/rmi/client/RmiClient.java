package org.tanglizi.dist.rmi.client;

import org.tanglizi.dist.rmi.client.strategy.*;
import org.tanglizi.dist.rmi.service.RmiService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * RmiClient class, provides user interface using strategy design pattern.
 *
 * @author Chunxu Zhang
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
        strategyMap.put("add", new AddMeetingStrategy());
        strategyMap.put("query", new QueryMeetingStrategy());
        strategyMap.put("delete", new DeletingMeetingStrategy());
        strategyMap.put("clear", new CleanMeetingsStragety());
    }

    private static void printHelp() {
        StringBuilder builder = new StringBuilder();

        builder.append("Usage: <serverName> <portNumber> <command> [options...]\n")
                .append("Commands:\n")
                .append("\t").append("register [username] [password]\n")
                .append("\t").append("add [username] [password] [otherUserName] [start] [end] [title]\n")
                .append("\t").append("query [username] [password] [start] [end]\n")
                .append("\t").append("delete [username] [password] [meetingID]\n")
                .append("\t").append("clear [username] [password]\n");

        System.out.println(builder.toString());
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

        if (args.length < 4){
            RmiClient.printHelp();
            System.exit(0);
        }

        try {
            RmiClient client = new RmiClient(args[0], Integer.parseInt(args[1]));
            client.process(args);

        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
            printHelp();
        }
    }
}
