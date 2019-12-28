package ssd8.socket.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP request entity, using builder design pattern to keep entity constant, and offer a flexible building process.
 *
 * @author Zhang Chunxu
 */
public class HttpRequest {

    public static final Pattern uriWithPortPattern = Pattern.compile("http://(.*):([0-9]*)/.*");
    public static final Pattern uriPattern = Pattern.compile("http://([^:/]*)/.*");
    private static final String SP = " ";
    private static final String CRLF = "\r\n";
    private static final int BUFFER_SIZE = 8192;

    private final String method;
    private final String uri;
    private final String version;
    private final Map<String, String> headers;
    private final byte[] body;

    /**
     * Private constructor.
     * Note user can not construct a HTTP request via this way directly.
     * You should use HttpRequest.Builder to build new one.
     *
     * @param builder
     */
    private HttpRequest(Builder builder){
        this.method = builder.method;
        this.uri = builder.uri;
        this.version = builder.version;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    /**
     * Getting and parsing the HTTP request from a given input stream, which got from a remote socket.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static HttpRequest parseFrom(InputStream inputStream) throws IOException {
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
        byte[] buffer = new byte[BUFFER_SIZE];
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
     * Getting header content.
     *
     * @return string
     */
    private String getHeaderString() {
        StringBuilder builder = new StringBuilder();
        builder.append(method).append(SP)
                .append(uri).append(SP)
                .append(version).append(CRLF);

        for (Map.Entry<String, String> entry: headers.entrySet()) {
            builder.append(entry.getKey()).append(": ")
                    .append(entry.getValue()).append(CRLF);
        }

        builder.append(CRLF);
        return builder.toString();
    }

    @Override
    public String toString() {
        return new String(toByte());
    }

    /**
     * Getting the byte array, can be sent to remote socket via output stream.
     *
     * @return byte array
     */
    public byte[] toByte() {
        byte[] header = getHeaderString().getBytes();

        if (null == body)
            return header;

        byte[] result = new byte[header.length + body.length];
        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(body, 0, result, header.length, body.length);
        return result;
    }

    /**
     * Static builder of HttpRequest.
     *
     */
    public static class Builder {
        private final String method;
        private final String uri;
        private final String version;

        private Map<String, String> headers = new HashMap<>();
        private byte[] body;

        public Builder(String method, String uri, String version) {
            this.method = method;
            this.uri = uri;
            this.version = version;
        }

        public Builder addHeader(String name, String content) {
            this.headers.put(name, content);
            return this;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }

        public Map<String, String> getHeaders() {
            return headers;
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String parseHost() {
        Matcher matcher = uriWithPortPattern.matcher(getUri());
        if (matcher.find()) return matcher.group(1);

        matcher = uriPattern.matcher(getUri());
        if (matcher.find()) return matcher.group(1);

        return null;
    }

    public int parsePort() {
        Matcher matcher = uriWithPortPattern.matcher(getUri());
        if (matcher.find()) return Integer.parseInt(matcher.group(2));
        return 80;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    /**
     * Using a given input stream to get a string line.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String getLine(InputStream inputStream) throws IOException {
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
}
