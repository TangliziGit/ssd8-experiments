package ssd8.socket.http.server;

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
     * @param rootPath
     * @throws IOException
     */
    public MultiThreadHttpServer(String rootPath, int port) throws IOException {
        super(rootPath, port);
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() *
                THREAD_NUMBER_PER_PROCESSOR);
    }

    public MultiThreadHttpServer(String rootPath) throws IOException {
        this(rootPath, PORT);
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
                executorService.execute(new HttpHandler(client, rootPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
