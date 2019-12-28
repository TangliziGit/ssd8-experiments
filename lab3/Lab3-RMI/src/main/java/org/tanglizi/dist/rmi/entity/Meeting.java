package org.tanglizi.dist.rmi.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Meeting entity bean, implemented Serializable, Comparable, to enable rmi transforming and sorting.
 *
 * @author Chunxu Zhang
 */
public class Meeting implements Serializable, Comparable<Meeting> {

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.CHINESE)
            .withZone(ZoneId.systemDefault());

    private Integer meetingId;
    private String title;
    private Instant startTime;
    private Instant endTime;
    private Integer creatorId;
    private Set<Integer> members;

    public Meeting(Integer meetingId, String title, Instant startTime, Instant endTime, Integer creatorId) {
        this.meetingId = meetingId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creatorId = creatorId;
        this.members = new HashSet<>();
    }

    public Set<Integer> getMembers() {
        return members;
    }

    public void setMembers(Set<Integer> members) {
        this.members = members;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public boolean isOverlap(Instant startTime, Instant endTime) {
        if (this.startTime.isBefore(startTime))
            return this.endTime.isAfter(startTime);
        else if (this.startTime.equals(startTime))
            return true;
        else return this.startTime.isBefore(endTime);
    }

    public boolean isBefore(Meeting meeting) {
        if (this.startTime.isBefore(meeting.getStartTime()))
            return true;
        if (this.startTime.equals(meeting.getStartTime()) && (
            this.endTime.isBefore(meeting.getEndTime()) || this.endTime.equals(meeting.getEndTime())))
            return true;
        return false;
    }

    @Override
    public int compareTo(Meeting meeting) {
        if (this.isBefore(meeting))
            return 1;
        else if (meeting.isBefore(this))
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meetingId=" + meetingId +
                ", title='" + title + '\'' +
                ", startTime=" + formatter.format(startTime) +
                ", endTime=" + formatter.format(endTime) +
                ", creatorId=" + creatorId +
                '}';
    }
}
