package ssd8.socket.http.server.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP request entity, using builder design pattern to keep entity constant, and offer a flexible building process.
 *
 * @author Zhang Chunxu
 */
public class HttpRequest {
    private static final String SP = " ";
    private static final String CRLF = "\r\n";

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

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
