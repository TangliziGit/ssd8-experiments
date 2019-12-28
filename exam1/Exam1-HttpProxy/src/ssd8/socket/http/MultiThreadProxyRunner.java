package ssd8.socket.http;

import ssd8.socket.http.server.HttpServer;
import ssd8.socket.http.server.MultiThreadHttpServer;

import java.io.IOException;

/**
 * A multiple thread HTTP server entry point.
 *
 * @author Zhang Chunxu
 */
public class MultiThreadProxyRunner {

    public static void main(String[] args) throws IOException {
        int port = (args.length >= 1)?Integer.parseInt(args[0]): HttpServer.PORT;

        HttpServer multiThreadServer = new MultiThreadHttpServer(port);
        multiThreadServer.service();
    }
}
