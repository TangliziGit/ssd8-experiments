package ssd8.socket.http.server.enums;

/**
 * HTTP status enums, defined the status name and corresponding code.
 *
 */
public enum HttpStatus {

    OK("OK", 200), CREATED(201),
    BAD_REQUEST(400), FORBIDDEN(403), NOT_FOUND(404), METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500);

    private String name;
    private Integer statusCode;

    /**
     * Constructor, only define the state code.
     *
     * @param statusCode
     */
    HttpStatus(Integer statusCode) {
        this(null, statusCode);
    }

    /**
     * Constructor.
     *
     * @param name
     * @param statusCode
     */
    HttpStatus(String name, Integer statusCode) {
        this.name = name;
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    /**
     * Getting the HTTP status name in real world.
     *
     * @return string
     */
    @Override
    public String toString() {
        if (null != name)
            return name;

        StringBuilder builder = new StringBuilder();
        for (String string: this.name().toLowerCase().split("_"))
            builder.append(string.substring(0, 1).toUpperCase())
                    .append(string.substring(1))
                    .append(" ");

        return builder.toString();
    }
}
