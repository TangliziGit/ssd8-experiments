package ssd8.socket.http.server;

import ssd8.socket.http.server.entity.HttpResponse;
import ssd8.socket.http.server.strategy.DefaultProcessor;
import ssd8.socket.http.server.strategy.GetProcessor;
import ssd8.socket.http.server.strategy.PutProcessor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A single thread HTTP server.
 *
 * @author Zhang Chunxu
 */
public class HttpServer {

    protected static final Integer PORT = 8080;

    ServerSocket serverSocket;
    File rootPath;

    /**
     * Constructor, initialize serverSocket and HttpHandler process strategies.
     *
     * @param rootPath
     * @throws IOException
     */
    public HttpServer(String rootPath, int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.rootPath = new File(rootPath);
        initializeProcessStrategy();
    }

    public HttpServer(String rootPath) throws IOException {
        this(rootPath, PORT);
    }

    /**
     * Initialize process strategy for HttpHandler
     *
     */
    private void initializeProcessStrategy() {
        if (HttpHandler.isProcessStrategyInitialized())
            return;
        HttpHandler.addProcessStrategy("GET", new GetProcessor());
        HttpHandler.addProcessStrategy("PUT", new PutProcessor());
        HttpHandler.addProcessStrategy("DEFAULT", new DefaultProcessor());
    }

    /**
     * Start servicing.
     *
     */
    public void service() {
        while (true){
            try (Socket client = serverSocket.accept()) {
                HttpHandler handler = new HttpHandler(client, rootPath);
                handler.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
