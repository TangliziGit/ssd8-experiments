package org.tanglizi.dist.rmi.client.strategy;

import org.tanglizi.dist.rmi.entity.Meeting;
import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.time.Instant;

/**
 * AddMeetingStragety class, a command strategy implement.
 *
 * @author Chunxu Zhang
 */
public class AddMeetingStrategy extends CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws RemoteException, ParseException {
        String name = args[3];
        String password = args[4];
        String otherName = args[5];
        Instant startInstant = dateFormat.parse(args[6]).toInstant();
        Instant endInstant = dateFormat.parse(args[7]).toInstant();
        String title = args[8];

        Meeting meeting = service.addMeeting(name, password, otherName, startInstant, endInstant, title);

        if (null == meeting)
            System.err.println("[ERROR] Such meeting is existing, or user not find, please check your arguments.");
        else
            System.out.println("[INFO] Added successfully.");
    }
}
