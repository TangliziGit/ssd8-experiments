package org.tanglizi.dist;

import org.tanglizi.dist.strategy.CommandStrategy;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * A handler provides execute command.
 * @author Zhang Chunxu
 */
public class CommandHandler implements Runnable {

    private static final String END_STRING = "";
    private static Map<String, CommandStrategy> router = new HashMap<>();

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private File currentPath;
    private File rootPath;

    /**
     * Check if some CommandStrategy is included in router map.
     * @return
     */
    public static Boolean isInitialized() {
        return !router.isEmpty();
    }

    /**
     * Add a command strategy.
     * @param command
     * @param strategy
     */
    public static void addCommandStrategy(String command, CommandStrategy strategy) {
        router.put(command, strategy);
    }

    /**
     * Constructor for CommandHandler, initialize socket and current file path.
     * @param socket
     * @param rootPath
     */
    public CommandHandler(Socket socket, File rootPath) {
        this.socket = socket;
        this.currentPath = rootPath;
        this.rootPath = rootPath;
    }

    /**
     * Initialize IO reading / writing objects.
     * @throws IOException
     */
    private void initializeIO() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream())), true);
    }

    /**
     * Dispatch command to specific CommandStrategy.
     * @param command
     * @param argument
     * @return
     */
    private String dispatch(String command, String argument) {
        if (!router.containsKey(command))
            return "no such command\n";

        CommandStrategy strategy = router.get(command);
        strategy.setState(currentPath, rootPath);
        String reply = strategy.process(argument);
        currentPath = strategy.getCurrentPath();
        return reply;
    }

    /**
     * start handling the requests.
     */
    @Override
    public void run() {
        String remoteIP = socket.getInetAddress().toString();
        int remotePort = socket.getPort();
        String remote = remoteIP + ":" + remotePort;

        System.out.println("New connect: " + remote);

        try {
            initializeIO();
            writer.println(remote + "> Connected");

            String line = null, command = null, argument = null;
            String[] commandAndArgument = null;
            // get client requests as command line.
            while (null != (line = reader.readLine())) {
                System.out.println("Request (from " + remote + ") : " + line);

                commandAndArgument = CommandParserUtil.parseCommandLine(line);
                command = commandAndArgument[0];
                argument = commandAndArgument[1];

                if (null == command)
                    continue;

                // get reply from specific CommandStrategy, and send the reply to client.
                String reply = dispatch(command, argument);
                writer.print(reply);
                writer.println(END_STRING);

                if (command.equals("bye"))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // remember close socket.
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
