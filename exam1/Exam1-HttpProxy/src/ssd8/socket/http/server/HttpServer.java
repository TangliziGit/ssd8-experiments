package ssd8.socket.http.server;

import ssd8.socket.http.handler.HttpHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A single thread HTTP server.
 *
 * @author Zhang Chunxu
 */
public class HttpServer {

    public static final Integer PORT = 8000;

    ServerSocket serverSocket;

    /**
     * Constructor, initialize serverSocket and HttpHandler process strategies.
     *
     * @throws IOException
     */
    public HttpServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public HttpServer() throws IOException {
        this(PORT);
    }

    /**
     * Start servicing.
     *
     */
    public void service() {
        while (true){
            try (Socket client = serverSocket.accept()) {
                HttpHandler handler = new HttpHandler(client);
                handler.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
