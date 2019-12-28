package ssd8.socket.http.entity;

import ssd8.socket.http.enums.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP response entity, using builder design pattern to keep entity constant, and offer a flexible building process.
 *
 * @author Zhang Chunxu
 */
public class HttpResponse {

    private static final String SP = " ";
    private static final String CRLF = "\r\n";
    private static final int BUFFER_SIZE = 8192;

    private final String version;
    private final Integer statusCode;
    private final String reason;
    private final Map<String, String> headers;
    private final byte[] body;

    /**
     * Private constructor.
     * Note user can not construct a HTTP response via this way directly.
     * You should use HttpResponse.Builder to build new one.
     *
     * @param builder
     */
    private HttpResponse(Builder builder){
        this.version = builder.version;
        this.statusCode = builder.statusCode;
        this.reason = builder.reason;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    /**
     * Getting and parsing the HTTP response from a given input stream, which got from a remote socket.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static HttpResponse parseFrom(InputStream inputStream) throws IOException {
        String responseLine = getLine(inputStream);
        while (null == responseLine) return null;
        String[] responseLineItems = responseLine.split(" ");

        String version = responseLineItems[0];
        String statusCode = responseLineItems[1];
        String statusDescript = responseLineItems[2];

        HttpResponse.Builder builder = new HttpResponse.Builder(version, statusCode, statusDescript);

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
        builder.append(version).append(SP)
                .append(statusCode).append(SP)
                .append(reason).append(CRLF);

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
     * Static builder of HttpResponse.
     *
     */
    public static class Builder {
        private final String version;
        private final Integer statusCode;
        private final String reason;

        private Map<String, String> headers = new HashMap<>();
        private byte[] body;

        public Builder(String version, HttpStatus status) {
            this(version, status.getStatusCode(), status.toString());
        }

        public Builder(String version, String statusCode, String reason) {
            this(version, Integer.parseInt(statusCode), reason);
        }

        public Builder(String version, int statusCode, String reason) {
            this.version = version;
            this.statusCode = statusCode;
            this.reason = reason;
        }

        public Builder addHeader(String name, String content) {
            this.headers.put(name, content);
            return this;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }

        public Map<String, String> getHeaders() {
            return headers;
        }
    }

    public String getVersion() {
        return version;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
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
