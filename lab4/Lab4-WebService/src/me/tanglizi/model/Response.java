package me.tanglizi.model;

import me.tanglizi.adapter.InstantAdapter;
import me.tanglizi.adapter.SerializableObjectAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Response generic class, provide better error information notice, using two static method as constructor.
 *
 * @author Zhang Chunxu
 * @param <T>
 */
public class Response<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1;

    private boolean success;
    private String message;
    private T payload;
    private Instant time;

    public Response() {}

    public Response(Boolean success, String message, T payload, Instant time) {
        this.success = success;
        this.message = message;
        this.payload = payload;
        this.time = time;
    }

    public static <T extends Serializable> Response<T> ok(T payload) {
        return new Response<T>(true, "Success", payload, Instant.now());
    }

    public static <T extends Serializable> Response<T> fail(String message, T payload) {
        if (null == message) message = "Failure";
        return new Response<T>(false, message, payload, Instant.now());
    }

    public static <T extends Serializable> Response<T> fail(String message) {
        return Response.fail(message, null);
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response<?> response = (Response<?>) o;
        return success == response.success &&
                message.equals(response.message) &&
                Objects.equals(payload, response.payload) &&
                time.equals(response.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, payload, time);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlJavaTypeAdapter(SerializableObjectAdapter.class)
    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @XmlJavaTypeAdapter(InstantAdapter.class)
    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
