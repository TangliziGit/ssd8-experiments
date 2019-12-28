package ssd8.socket.http.server.strategy;

import ssd8.socket.http.server.entity.HttpRequest;
import ssd8.socket.http.server.entity.HttpResponse;

import java.io.File;

/**
 * Abstract class of HTTP processing strategy.
 */
public abstract class HttpProcessStrategy {

    protected File rootPath;

    public void setRootPath(File rootPath) {
        this.rootPath = rootPath;
    }

    public abstract HttpResponse.Builder process(HttpRequest request);
}
