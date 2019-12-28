package org.tanglizi.dist.rmi.service;

import org.tanglizi.dist.rmi.entity.Meeting;
import org.tanglizi.dist.rmi.entity.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.List;

/**
 * RmiSerivce interface, defined rmi services necessary.
 *
 * @author Chunxu Zhang
 */
public interface RmiService extends Remote {

    User register(String name, String password) throws RemoteException;

    Meeting addMeeting(String username, String password, String otherUserName, Instant startInstant,
                       Instant endInstant, String title) throws RemoteException;

    List<Meeting> queryMeetings(String name, String password, Instant startInstant,
                                Instant endInstant) throws RemoteException;

    Boolean deleteMeeting(String name, String password, Integer meetingId) throws RemoteException;

    Boolean clearMeetings(String name, String password) throws RemoteException;

    void cleanData() throws RemoteException;
}
