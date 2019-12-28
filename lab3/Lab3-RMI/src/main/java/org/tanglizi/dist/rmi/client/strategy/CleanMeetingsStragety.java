package org.tanglizi.dist.rmi.client.strategy;

import org.tanglizi.dist.rmi.service.RmiService;

/**
 * CleamMeetingStragety class, a command strategy implement.
 *
 * @author Chunxu Zhang
 */
public class CleanMeetingsStragety extends CommandStrategy {

    @Override
    public void process(String[] args, RmiService service) throws Exception {
        String name = args[3];
        String password = args[4];

        Boolean isCleared = service.clearMeetings(name, password);

        if (!isCleared)
            System.err.println("[ERROR] User authorizing is not passed or some other problems occurred, please check your arguments.");
        else
            System.out.println("[INFO] Cleared successfully.");
    }
}
