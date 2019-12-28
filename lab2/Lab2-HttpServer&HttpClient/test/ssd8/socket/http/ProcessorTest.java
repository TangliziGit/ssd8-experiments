package ssd8.socket.http;

import org.junit.BeforeClass;
import org.junit.Test;
import ssd8.socket.http.server.entity.HttpRequest;
import ssd8.socket.http.server.entity.HttpResponse;
import ssd8.socket.http.server.enums.HttpStatus;
import ssd8.socket.http.server.strategy.DefaultProcessor;
import ssd8.socket.http.server.strategy.GetProcessor;
import ssd8.socket.http.server.strategy.PutProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ProcessorTest {
    private HttpRequest.Builder builder;
    private HttpResponse response;

    private static GetProcessor getProcessor = new GetProcessor();
    private static PutProcessor putProcessor = new PutProcessor();
    private static DefaultProcessor defaultProcessor = new DefaultProcessor();

    private static byte[] fileContent;

    @BeforeClass
    public static void setup() throws IOException {
        File root = new File("/home/tanglizi/tmp");
        getProcessor.setRootPath(root);
        putProcessor.setRootPath(root);
        defaultProcessor.setRootPath(root);

        File file = new File("/home/tanglizi/tmp/test");
        FileInputStream inputStream = new FileInputStream(file);
        fileContent = new byte[(int) file.length()];
        inputStream.read(fileContent);
    }

    @Test
    public void testGet() {
        builder = new HttpRequest.Builder("GET", "/test", "HTTP/1.1");
        response = getProcessor.process(builder.build()).build();

        assertEquals(HttpStatus.OK.toString(), response.getReason());
        assertArrayEquals(fileContent, response.getBody());
        System.out.println(response.getReason());
    }

    @Test
    public void testGetError() {
        builder = new HttpRequest.Builder("GET", "/test", "HTTP/1.1");
        response = getProcessor.process(builder.build()).build();
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getReason());
        System.out.println(response.getReason());

        builder = new HttpRequest.Builder("GET", "/test", "HTTP/1.1");
        response = getProcessor.process(builder.build()).build();
        assertEquals(HttpStatus.NOT_FOUND.toString(), response.getReason());
        System.out.println(response.getReason());
    }

    @Test
    public void testPut() {
        builder = new HttpRequest.Builder("PUT", "/test2", "HTTP/1.1");
        builder.setBody(fileContent);
        response = putProcessor.process(builder.build()).build();
        assertEquals(HttpStatus.CREATED.toString(), response.getReason());
    }

    @Test
    public void testPutError() {
        builder = new HttpRequest.Builder("PUT", "/test2", "HTTP/1.1");
        builder.setBody(fileContent);
        response = putProcessor.process(builder.build()).build();
        assertEquals(HttpStatus.OK.toString(), response.getReason());

        builder = new HttpRequest.Builder("PUT", "test2", "HTTP/1.1");
        builder.setBody(fileContent);
        response = putProcessor.process(builder.build()).build();
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getReason());

        builder = new HttpRequest.Builder("PUT", "/test2", "HTTP/1.0");
        builder.setBody(fileContent);
        response = putProcessor.process(builder.build()).build();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.toString(), response.getReason());
    }

    @Test
    public void testDefault() {
        builder = new HttpRequest.Builder("HEAD", "/test", "HTTP/1.1");
        response = defaultProcessor.process(builder.build()).build();

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.toString(), response.getReason());
        assertEquals("GET, PUT", response.getHeaders().get("Allow"));
    }

}
