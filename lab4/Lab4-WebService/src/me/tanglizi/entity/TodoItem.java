package me.tanglizi.entity;

import me.tanglizi.adapter.InstantAdapter;
import me.tanglizi.util.TimeUtil;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * TodoItem java bean, contains id, startTime, endTime and tag information.
 *
 * @author Zhang Chunxu
 */
public class TodoItem implements Serializable {

    private static final long serialVersionUID = 1;

    private Integer id;
    private Instant startTime;
    private Instant endTime;
    private String tag;

    public TodoItem() {}

    public TodoItem(Integer id, Instant startTime, Instant endTime, String tag) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tag = tag;
    }

    public boolean during(Instant start, Instant end) {
        return (start.equals(startTime) || start.isBefore(startTime)) &&
                (end.equals(endTime) || end.isAfter(endTime));
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", startTime=" + TimeUtil.format(startTime) +
                ", endTime=" + TimeUtil.format(endTime) +
                ", tag='" + tag + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return id.equals(todoItem.id) &&
                startTime.equals(todoItem.startTime) &&
                endTime.equals(todoItem.endTime) &&
                tag.equals(todoItem.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, tag);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(InstantAdapter.class)
    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    @XmlJavaTypeAdapter(InstantAdapter.class)
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
