package ssd8.socket.http.handler;

import ssd8.socket.http.entity.HttpRequest;
import ssd8.socket.http.entity.HttpResponse;
import ssd8.socket.http.enums.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * A HttpHandler for parsing HTTP requests, call a corresponding ProcessStrategy and reply using HTTP responses.
 * Note HttpHandler does not provide service logic, but ProcessStrategy do.
 *
 * @author Zhang Chunxu
 */
public class HttpHandler implements Runnable {

    private final String HTTP_VERSION = "HTTP/1.0";
    private final String ALLOWED_METHOD = "GET";
    private final String SERVER_NAME = "HttpTool/1.0";
    private final String FROM = "proxy@nwpu.edu.cn";

    private Socket client;
    private Socket remoteServer;
    private OutputStream remoteOutputStream;
    private InputStream remoteInputStream;

    /**
     * Constructor
     *
     * @param client
     * @throws SocketException
     */
    public HttpHandler(Socket client) throws SocketException {
        this.client = client;
        this.client.setSoTimeout(1000);
    }

    /**
     * Processing HTTP request.
     * Get the corresponding process strategy, using it to process request and obtain a HTTP response.
     *
     * @param request
     * @return
     */
    private HttpResponse process(HttpRequest request) throws IOException {
        HttpResponse.Builder builder = checkIsBadRequest(request);

        if (null != builder)
            return builder.build();

        request.getHeaders().put("From", FROM);
        request.getHeaders().put("User-Agent", SERVER_NAME);

        return HttpResponse.parseFrom(getResponseInputStream(request));
    }


    /**
     * Get remote server socket, outputStream and inputStream.
     * The aim of using this 3 object as handler properties it to provide a living connection.
     *
     * @param request
     * @return
     */
    private InputStream getResponseInputStream(HttpRequest request) {
        try{
            if (null == remoteServer)
                remoteServer = new Socket(request.parseHost(), request.parsePort());
            if (null == remoteOutputStream)
                remoteOutputStream = remoteServer.getOutputStream();
            if (null == remoteInputStream)
                remoteInputStream = remoteServer.getInputStream();

            remoteOutputStream.write(request.toByte());
            remoteOutputStream.write('\n');

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return remoteInputStream;
    }

    /**
     * Check if this request is bad.
     *
     * @param request
     * @return
     */
    private HttpResponse.Builder checkIsBadRequest(HttpRequest request) {
        // if you want to use only HTTP/1.0, you should uncomment this code.
        // if (!request.getVersion().equals(HTTP_VERSION))
        //   return new HttpResponse.Builder(HTTP_VERSION, HttpStatus.BAD_REQUEST);

        if (!request.getMethod().equals(ALLOWED_METHOD))
            return new HttpResponse.Builder(HTTP_VERSION, HttpStatus.BAD_REQUEST);

        return null;
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

        // try-with-resource, java lang can close those resources when try ended.
        try (InputStream inputStream = client.getInputStream();
             OutputStream outputStream = client.getOutputStream() ){

            HttpRequest request = HttpRequest.parseFrom(inputStream);
            HttpResponse response = process(request);
            System.out.println(request);
            System.out.println(response);
            sendHttpResponse(response, outputStream);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (null != client) client.close();
                if (null != remoteServer) remoteServer.close();
                if (null != remoteInputStream) remoteInputStream.close();
                if (null != remoteOutputStream) remoteOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
