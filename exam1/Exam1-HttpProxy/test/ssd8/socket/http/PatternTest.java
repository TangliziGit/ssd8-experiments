package ssd8.socket.http;

import org.junit.Test;
import ssd8.socket.http.entity.HttpRequest;

import java.util.regex.Matcher;

import static org.junit.Assert.*;


public class PatternTest {

    @Test
    public void testUriPattern() {
        String uri = "http://www.somesite.com:8080/index.html";

        Matcher matcher = HttpRequest.uriWithPortPattern.matcher(uri);
        assertTrue(matcher.find());
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));

        matcher = HttpRequest.uriPattern.matcher(uri);
        assertFalse(matcher.find());
    }
}
