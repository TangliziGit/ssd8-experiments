package org.tanglizi.dist.rmi.model;

import org.tanglizi.dist.rmi.entity.Message;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * MessageModel can be built from Message class, provide message information which client need.
 *
 * @author Zhang Chunxu
 */
public class MessageModel implements Serializable {

    private static final long serialVersionUID = 1;

    private String senderName;
    private String content;
    private Instant time;

    private MessageModel(String senderName, String content, Instant time) {
        this.senderName = senderName;
        this.content = content;
        this.time = time;
    }

    public static MessageModel buildFrom(Message message, String senderName) {
        return new MessageModel(senderName, message.getContent(), message.getTime());
    }

    @Override
    public String toString() {
        return "{" +
                "senderName='" + senderName + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageModel that = (MessageModel) o;
        return senderName.equals(that.senderName) &&
                content.equals(that.content) &&
                time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderName, content, time);
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
