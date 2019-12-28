package org.tanglizi.dist;

import org.tanglizi.dist.strategy.ByeCommandStrategy;
import org.tanglizi.dist.strategy.ChangeDirectoryCommandStrategy;
import org.tanglizi.dist.strategy.GetFileCommandStrategy;
import org.tanglizi.dist.strategy.ListCommandStrategy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A file server providing file control operations on TCP,
 *  and file transferring on UDP.
 * @author Zhang Chunxu
 */
public class FileServer {

    public final static Integer TCP_PORT = 2021;
    public final static Integer UDP_PORT = 2020;

    private ServerSocket serverSocket;
    private File rootPath;

    /**
     * Constructor for FileServer, initialize serverSocket, rootPath and thread count of the thread pool.
     * And call initializeHandler method to initialize the request (command) handler.
     * @param rootPath
     * @throws IOException
     */
    public FileServer(String rootPath) throws IOException {
        this.serverSocket = new ServerSocket(TCP_PORT);
        this.rootPath = new File(rootPath);
        initializeHandler();
        ThreadPoolUtil.resetThreadCountPerProcessor(4);
    }

    /**
     * Initialize CommandHandler, register some strategy for command routing.
     */
    private void initializeHandler() {
        if (CommandHandler.isInitialized()) return;
        CommandHandler.addCommandStrategy("ls", new ListCommandStrategy());
        CommandHandler.addCommandStrategy("cd", new ChangeDirectoryCommandStrategy());
        CommandHandler.addCommandStrategy("get", new GetFileCommandStrategy());
        CommandHandler.addCommandStrategy("bye", new ByeCommandStrategy());
    }

    /**
     * Start service, create a new thread to handle a new TCP connection on a thread pool.
     */
    public void service(){
        Socket clientSocket;
        while (true){
            try {
                clientSocket = serverSocket.accept();
                ThreadPoolUtil.run(new CommandHandler(clientSocket, rootPath));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * The running entry for server, the root file path is included in 'args'.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length <= 0)
            System.err.println("Usage: java FileServer <dir>");
        else
            new FileServer(args[0]).service();
    }
}
