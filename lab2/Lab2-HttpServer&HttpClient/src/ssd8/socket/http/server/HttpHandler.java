package ssd8.socket.http.server;

import ssd8.socket.http.server.entity.HttpRequest;
import ssd8.socket.http.server.entity.HttpResponse;
import ssd8.socket.http.server.strategy.DefaultProcessor;
import ssd8.socket.http.server.strategy.HttpProcessStrategy;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * A HttpHandler for parsing HTTP requests, call a corresponding ProcessStrategy and reply using HTTP responses.
 * Note HttpHandler does not provide service logic, but ProcessStrategy do.
 *
 * @author Zhang Chunxu
 */
public class HttpHandler implements Runnable {

    // Mapping a command to a specific strategy.
    private static Map<String, HttpProcessStrategy> processStrategyMap = new HashMap<>();
    // Restore list of allowed methods.
    private static List<String> methodList =
            new ArrayList<>(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE"));
    private final String SERVER_NAME = "HttpServer/1.0";

    private Socket client;
    private File rootPath;

    /**
     * Check if processing strategy map is initalized.
     *
     * @return
     */
    public static Boolean isProcessStrategyInitialized() {
        return !processStrategyMap.isEmpty();
    }

    /**
     * Add strategy to the map.
     *
     * @param method
     * @param strategy
     */
    public static void addProcessStrategy(String method, HttpProcessStrategy strategy) {
        if (methodList.contains(method))
            HttpHandler.processStrategyMap.put(method, strategy);
    }

    /**
     * Constructor
     *
     * @param client
     * @param rootPath
     * @throws SocketException
     */
    public HttpHandler(Socket client, File rootPath) throws SocketException {
        this.client = client;
        this.rootPath = rootPath;
        this.client.setSoTimeout(30000);
    }

    /**
     * Using a given input stream to get a string line.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private String getLine(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        byte[] ch = new byte[1];
        int len;
        while ((len = inputStream.read(ch)) != -1){
            if (ch[0] == '\r') ;
            else if (ch[0] == '\n') break;
            else builder.append((char)ch[0]);
        }
        if (builder.length() == 0)
            return null;
        return builder.toString();
    }

    /**
     * Getting and parsing the HTTP request from a given input stream, which got from a remote socket.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private HttpRequest parseHttpRequest(InputStream inputStream) throws IOException {
        String requestLine = getLine(inputStream);
        while (null == requestLine) return null;
        String[] requestLineItems = requestLine.split(" ");

        if (requestLineItems.length != 3)
            return new HttpRequest.Builder("DEFAULT", "", "").build();

        String method = requestLineItems[0];
        String uri = requestLineItems[1];
        String version = requestLineItems[2];

        HttpRequest.Builder builder = new HttpRequest.Builder(method, uri, version);

        String line;
        while ((line = getLine(inputStream)) != null){
            if (line.equals("")) break;
            String[] nameAndContent = line.split(": ");
            String name = nameAndContent[0];
            String content = nameAndContent[1];

            builder = builder.addHeader(name, content);
        }

        int bodyLength = Integer.parseInt(builder.getHeaders().getOrDefault("Content-Length", "0"));
        if (bodyLength == 0)
            return builder.build();

        byte[] body = new byte[bodyLength];
        byte[] buffer = new byte[8196];
        int bufferSize = 0;
        int len;
        while ((len = inputStream.read(buffer)) != -1){
            System.arraycopy(buffer, 0, body, bufferSize, len);
            bufferSize += len;
            if (bufferSize == body.length) break;
        }
        System.out.println(bufferSize);
        return builder.setBody(body).build();
    }

    /**
     * Processing HTTP request.
     * Get the corresponding process strategy, using it to process request and obtain a HTTP response.
     *
     * @param request
     * @return
     */
    private HttpResponse process(HttpRequest request) {
        HttpProcessStrategy processor = processStrategyMap.getOrDefault(request.getMethod(),
                processStrategyMap.getOrDefault("DEFAULT", new DefaultProcessor()));

        processor.setRootPath(rootPath);
        HttpResponse.Builder builder = processor.process(request);
        builder.addHeader("Server", SERVER_NAME);
        return builder.build();
    }

    /**
     * Sending the HTTP response to remote socket using a given output stream.
     *
     * @param response
     * @param outputStream
     * @throws IOException
     */
    private void sendHttpResponse(HttpResponse response, OutputStream outputStream) throws IOException {
        outputStream.write(response.toByte());
    }

    /**
     * Service logic processing skeleton.
     */
    @Override
    public void run() {
        String remoteIp = client.getRemoteSocketAddress().toString();
        int remotePort = client.getPort();
        System.err.println(remoteIp + ":" + remotePort);

        try (InputStream inputStream = client.getInputStream();
             OutputStream outputStream = client.getOutputStream() ){

            HttpRequest request = parseHttpRequest(inputStream);
            System.out.println(request);
            HttpResponse response = process(request);
            System.out.println(response);
            sendHttpResponse(response, outputStream);

        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
