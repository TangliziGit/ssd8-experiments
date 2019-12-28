package ssd8.socket.http.server.entity;

import ssd8.socket.http.server.enums.HttpStatus;

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
}
