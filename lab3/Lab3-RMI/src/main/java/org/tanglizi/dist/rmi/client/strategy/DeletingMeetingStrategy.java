package org.tanglizi.dist.rmi.client.strategy;

import org.tanglizi.dist.rmi.service.RmiService;

/**
 * DeletingMeetingStrategy class, a command strategy implement.
 *
 * @author Chunxu Zhang
 */
public class DeletingMeetingStrategy extends CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws Exception {
        String name = args[3];
        String password = args[4];
        Integer meetingId = Integer.parseInt(args[5]);

        Boolean isDeleted = service.deleteMeeting(name, password, meetingId);

        if (!isDeleted)
            System.err.println("[ERROR] Such meeting is not existing or some other problems occurred, please check your arguments.");
        else
            System.out.println("[INFO] Deleted successfully.");
    }
}
