package org.tanglizi.dist.rmi.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Message java bean
 *
 * @author Zhang Chunxu
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1;

    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Instant time;

    public Message(Integer id, Integer senderId, Integer receiverId, String content, Instant time) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id.equals(message.id) &&
                senderId.equals(message.senderId) &&
                receiverId.equals(message.receiverId) &&
                content.equals(message.content) &&
                time.equals(message.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderId, receiverId, content, time);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
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
