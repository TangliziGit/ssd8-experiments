package ssd8.socket.http.server.strategy;

import ssd8.socket.http.server.entity.HttpRequest;
import ssd8.socket.http.server.entity.HttpResponse;
import ssd8.socket.http.server.enums.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The get processor, process only GET method requests.
 *
 * @author Zhang Chunxu
 */
public class GetProcessor extends HttpProcessStrategy {

    /**
     * Add Content type header line to a HttpResponse.Builder.
     * Such as `text/html`, `image/jpeg`...
     *
     * @param builder
     * @param file
     */
    private void addContentTypeHeader(HttpResponse.Builder builder, File file) {
        String path = file.getAbsolutePath();
        if (path.lastIndexOf(".") == -1){
            String contentType = "application/octet-stream";
            builder.addHeader("Content-Type", contentType);
            return;
        }
        String extension = path.substring(path.lastIndexOf("."));
        String contentType = "application/octet-stream";
        switch (extension) {
            case ".html":
                contentType = "text/html; charset=utf-8";
                break;
            case ".jpg":
                contentType = "image/jpeg";
                break;
        }
        builder.addHeader("Content-Type", contentType);
    }

    /**
     * Reading a file, should be written in FileUtil class as a static method.
     *
     * @param file
     * @return
     * @throws IOException
     */
    private byte[] readFile(File file) throws IOException {
        byte[] content = new byte[(int) file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(content);
        inputStream.close();
        return content;
    }

    @Override
    public HttpResponse.Builder process(HttpRequest request) {
        if (!request.getUri().startsWith("/"))
            return new HttpResponse.Builder(request.getVersion(), HttpStatus.BAD_REQUEST);

        File file = new File(rootPath, request.getUri());

        HttpResponse.Builder builder;
        try {
            byte[] content;

            if (!file.exists() || !file.isFile()){
                content = readFile(new File(rootPath, "404.html"));
                return new HttpResponse.Builder(request.getVersion(), HttpStatus.NOT_FOUND).setBody(content);
            }

            if (!file.canRead()) {
                content = readFile(new File(rootPath, "403.html"));
                return new HttpResponse.Builder(request.getVersion(), HttpStatus.FORBIDDEN).setBody(content);
            }

            builder = new HttpResponse.Builder(request.getVersion(), HttpStatus.OK);
            addContentTypeHeader(builder, file);
            if ("keep-alive".equals(request.getHeaders().getOrDefault("Connection", "")))
                builder.addHeader("Connection", "keep-alive");
            content = readFile(file);
            builder.setBody(content);
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());

            builder = new HttpResponse.Builder(request.getVersion(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return builder;
    }
}
