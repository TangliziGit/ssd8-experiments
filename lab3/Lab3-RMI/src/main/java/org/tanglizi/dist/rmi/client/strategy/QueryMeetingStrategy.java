package org.tanglizi.dist.rmi.client.strategy;

import org.tanglizi.dist.rmi.entity.Meeting;
import org.tanglizi.dist.rmi.service.RmiService;

import java.time.Instant;
import java.util.List;

/**
 * QueryMeetingStrategy class, a command strategy implement.
 *
 * @author Chunxu Zhang
 */
public class QueryMeetingStrategy extends CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws Exception {
        String name = args[3];
        String password = args[4];
        Instant startInstant = dateFormat.parse(args[5]).toInstant();
        Instant endInstant = dateFormat.parse(args[6]).toInstant();

        List<Meeting> meetings = service.queryMeetings(name, password, startInstant, endInstant);

        System.out.println("[INFO] Query successfully, below is the result:");
        for (Meeting meeting: meetings)
            System.out.println("\t" + meeting);
    }
}
