package ssd8.socket.http;

import ssd8.socket.http.server.HttpServer;

import java.io.IOException;

/**
 * A single thread HTTP server entry point.
 *
 * @author Zhang Chunxu
 */
public class Server {

    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            System.err.println("Usage: java -jar HttpServer.jar ROOT_PATH [PORT]");
            System.exit(1);
        }
        String path = args[0];
        int port = (args.length >= 2)?Integer.parseInt(args[1]):80;

        HttpServer server = new HttpServer(path, port);
        server.service();
    }
}
