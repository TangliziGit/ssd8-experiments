package org.tanglizi.dist.rmi.service.impl;

import org.tanglizi.dist.rmi.entity.Meeting;
import org.tanglizi.dist.rmi.entity.User;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * RmiServiceImpl class, a RmiService implement, extends UnicastRemoteObject.
 *
 * @author Chunxu Zhang
 */
public class RmiServiceImpl extends UnicastRemoteObject implements RmiService {

    private static Set<User> userList = new HashSet<>();
    private static Set<Meeting> meetingList = new HashSet<>();

    private static Integer nextUserId = 1;
    private static Integer nextMeetingId = 1;

    public RmiServiceImpl() throws RemoteException {
        super();
    }

    public User register(String name, String password) {
        Optional<User> anyUser = userList.stream()
                .filter(x -> x.getName().equals(name)).findAny();

        // if this username is duplicated.
        if (anyUser.isPresent()) return null;

        User newUser = new User(nextUserId++, name, password);
        userList.add(newUser);

        return newUser;
    }

    public Meeting addMeeting(String username, String password, String otherUserName,
                              Instant startInstant, Instant endInstant, String title) {
        if (startInstant.isAfter(endInstant))
            return null;

        Optional<User> anyCreator = userList.stream()
                .filter(x -> x.authorize(username, password)).findAny();
        Optional<User> anyOtherUser = userList.stream()
                .filter(x -> x.getName().equals(otherUserName)).findAny();

        // if such user does not exist or the authorizing did not passed.
        if (!anyCreator.isPresent() || !anyOtherUser.isPresent())
            return null;

        boolean isDuplicatedTitle = meetingList.stream()
                .anyMatch(x -> x.getTitle().equals(title));

        // if this title is duplicated.
        if (isDuplicatedTitle)
            return null;

        User creator = anyCreator.get();
        User otherUser = anyOtherUser.get();
        Set<Integer> agendaSet = new HashSet<>(creator.getAgenda());
        agendaSet.addAll(otherUser.getAgenda());

        // check if any meeting is overlap.
        boolean isAnyOverlapped = meetingList.stream()
                .filter(x -> agendaSet.contains(x.getMeetingId()))
                .anyMatch(x -> x.isOverlap(startInstant, endInstant));

        if (isAnyOverlapped)
            return null;

        Meeting meeting = new Meeting(nextMeetingId++, title, startInstant, endInstant, creator.getUserId());

        // maintaining.
        meeting.getMembers().add(otherUser.getUserId());
        creator.getAgenda().add(meeting.getMeetingId());
        otherUser.getAgenda().add(meeting.getMeetingId());
        meetingList.add(meeting);

        return meeting;
    }

    public List<Meeting> queryMeetings(String name, String password, Instant startInstant, Instant endInstant) {
        Optional<User> anyUser = userList.stream()
                .filter(x -> x.authorize(name, password)).findAny();

        // if such user does not exist or the authorizing did not passed.
        if (!anyUser.isPresent())
            return null;

        User user = anyUser.get();

        return meetingList.stream()
                .filter(x -> user.getAgenda().contains(x.getMeetingId()))
                .filter(x -> x.isOverlap(startInstant, endInstant))
                .sorted()
                .collect(Collectors.toList());
    }

    public Boolean deleteMeeting(String name, String password, Integer meetingId) {
        Optional<User> anyCreator = userList.stream()
                .filter(x -> x.authorize(name, password)).findAny();

        // if such user does not exist or the authorizing did not passed.
        if (!anyCreator.isPresent())
            return false;

        User creator = anyCreator.get();
        boolean isExisting = creator.getAgenda().contains(meetingId);

        // if the meeting is not existing in creator's agenda.
        if (!isExisting)
            return false;

        creator.getAgenda().remove(meetingId);
        for (User user: userList)
            user.getAgenda().remove(meetingId);
        meetingList.removeIf(x -> x.getMeetingId().equals(meetingId));

        return true;
    }

    public Boolean clearMeetings(String name, String password) {
        Optional<User> anyCreator = userList.stream()
                .filter(x -> x.authorize(name, password)).findAny();

        // if such user does not exist or the authorizing did not passed.
        if (!anyCreator.isPresent())
            return false;

        User creator = anyCreator.get();
        Set<Integer> meetingsSet = meetingList.stream()
                .filter(x -> x.getCreatorId().equals(creator.getUserId()))
                .map(Meeting::getMeetingId)
                .collect(Collectors.toSet());

        for (Integer meetingId: meetingsSet) {
            for (User user: userList)
                user.getAgenda().remove(meetingId);

            meetingList.removeIf(x -> x.getMeetingId().equals(meetingId));
        }

        return true;
    }

    public void cleanData() {
        userList = new HashSet<>();
        meetingList = new HashSet<>();
    }

}
