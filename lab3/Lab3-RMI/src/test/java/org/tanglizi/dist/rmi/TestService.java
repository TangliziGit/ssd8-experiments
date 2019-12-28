package org.tanglizi.dist.rmi;

import org.junit.Before;
import org.junit.Test;
import org.tanglizi.dist.rmi.service.RmiService;
import org.tanglizi.dist.rmi.service.impl.RmiServiceImpl;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    RmiService service;

    public TestService() throws RemoteException {
        this.service = new RmiServiceImpl();
    }

    @Before
    public void setup() throws RemoteException, ParseException {
        service.cleanData();
        service.register("x", "x");
        service.register("y", "y");
        service.addMeeting("x", "x", "y",
                dateFormat.parse("2019-10-01 01:00").toInstant(),
                dateFormat.parse("2019-10-01 03:00").toInstant(),
                "title title"
        );
    }

    @Test
    public void testRegister() throws RemoteException {
        assertNotNull(service.register("xx", "xx"));

        assertNull(service.register("xx", "xx"));
    }

    @Test
    public void testAddMeeting() throws RemoteException, ParseException {
        assertNotNull(service.addMeeting("x", "x", "y",
                dateFormat.parse("2019-12-01 01:00").toInstant(),
                dateFormat.parse("2019-12-01 03:00").toInstant(),
                "title"
        ));

        // duplicated name
        assertNull(service.addMeeting("x", "x", "y",
                dateFormat.parse("2019-12-01 01:00").toInstant(),
                dateFormat.parse("2019-12-01 03:00").toInstant(),
           "title"
        ));

        // invalid instant
        assertNull(service.addMeeting("x", "x", "y",
                dateFormat.parse("2019-12-01 01:00").toInstant(),
                dateFormat.parse("2019-12-01 00:00").toInstant(),
                "title2"
        ));

        // authorizing can not pass
        assertNull(service.addMeeting("x", "x2", "y",
                dateFormat.parse("2019-12-01 01:00").toInstant(),
                dateFormat.parse("2019-12-01 03:00").toInstant(),
                "title2"
        ));

        // overlap
        assertNull(service.addMeeting("x", "x", "y",
                dateFormat.parse("2019-12-01 01:10").toInstant(),
                dateFormat.parse("2019-12-01 02:00").toInstant(),
                "title2"
        ));
    }

    @Test
    public void testQueryMeeting() throws ParseException, RemoteException {
        assertThat(service.queryMeetings("x", "x",
                dateFormat.parse("2019-09-01 01:00").toInstant(),
                dateFormat.parse("2019-12-12 00:00").toInstant()
        ).size(), is(1));
    }

    @Test
    public void testDeleteMeeting() throws RemoteException {
        assertTrue(service.deleteMeeting("x", "x", 1));
        assertFalse(service.deleteMeeting("x", "x", 2));
    }

    @Test
    public void testClearMeetings() throws RemoteException {
        assertTrue(service.clearMeetings("x", "x"));
    }

}
