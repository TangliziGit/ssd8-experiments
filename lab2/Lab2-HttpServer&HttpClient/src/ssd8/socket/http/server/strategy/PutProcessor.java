package ssd8.socket.http.server.strategy;

import ssd8.socket.http.server.entity.HttpRequest;
import ssd8.socket.http.server.entity.HttpResponse;
import ssd8.socket.http.server.enums.HttpStatus;

import java.io.*;

/**
 * The put processor, only process PUT method request.
 *
 */
public class PutProcessor extends HttpProcessStrategy {
    /**
     * Write the content of HTTP request to a specific file.
     * Should be written in FileUtil class as a static method.
     *
     * @param file
     * @param content
     * @throws IOException
     */
    private void writeFile(File file, byte[] content) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content);
        outputStream.close();
    }

    @Override
    public HttpResponse.Builder process(HttpRequest request) {
        if (!request.getUri().startsWith("/"))
            return new HttpResponse.Builder(request.getVersion(), HttpStatus.BAD_REQUEST);

        File file = new File(rootPath, request.getUri());

        String version = "HTTP/1.1";
        boolean isFileExists = file.exists();

        HttpResponse.Builder builder;

        if (!version.equals(request.getVersion())) {
            builder = new HttpResponse.Builder(request.getVersion(), HttpStatus.METHOD_NOT_ALLOWED);

            if (request.getVersion().equals("HTTP/1.0") || request.getVersion().equals("HTTP/0.9"))
                builder.addHeader("Allow", "GET");
            else
                builder.addHeader("Allow", "");
            return builder;
        }

        try {
            writeFile(file, request.getBody());

            /*
            If a new resource is created, the origin server MUST inform the user agent
            via the 201 (Created) response.  If an existing resource is modified,
            either the 200 (OK) or 204 (No Content) response codes SHOULD be sent
            to indicate successful completion of the request.
             */
            if (!isFileExists)
                builder = new HttpResponse.Builder(version,  HttpStatus.CREATED);
            else
                builder = new HttpResponse.Builder(version, HttpStatus.OK);
        } catch (IOException e) {
            System.err.println(e.getMessage());

            builder = new HttpResponse.Builder(version, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return builder;
    }
}
