package ssd8.socket.http;

import ssd8.socket.http.server.HttpServer;
import ssd8.socket.http.server.MultiThreadHttpServer;

import java.io.IOException;

/**
 * A multiple thread HTTP server entry point.
 *
 * @author Zhang Chunxu
 */
public class MultiServer {

    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            System.err.println("Usage: java -jar MultiThreadHttpServer.jar ROOT_PATH [PORT]");
            System.exit(1);
        }
        String path = args[0];
        int port = (args.length >= 2)?Integer.parseInt(args[1]):80;

        HttpServer multiThreadServer = new MultiThreadHttpServer(path, port);
        multiThreadServer.service();
    }
}
