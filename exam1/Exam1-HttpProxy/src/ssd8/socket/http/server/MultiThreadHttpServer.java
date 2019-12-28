package ssd8.socket.http.server;

import ssd8.socket.http.handler.HttpHandler;
import ssd8.socket.http.server.HttpServer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A multiple threads HTTP server.
 * Extended from HttpServer, which is a single thread HttpServer.
 *
 * @author Zhang Chunxu
 */
public class MultiThreadHttpServer extends HttpServer {

    private final Integer THREAD_NUMBER_PER_PROCESSOR = 4;
    private ExecutorService executorService;

    /**
     * Constructor, initialize a thread pool.
     *
     * @throws IOException
     */
    public MultiThreadHttpServer(int port) throws IOException {
        super(port);
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() *
                THREAD_NUMBER_PER_PROCESSOR);
    }

    public MultiThreadHttpServer() throws IOException {
        this(PORT);
    }

    /**
     * Start servicing, override HttpServer's service method.
     * Using thread pool.
     *
     */
    @Override
    public void service() {
        while (true){
            try {
                Socket client = serverSocket.accept();
                executorService.execute(new HttpHandler(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
