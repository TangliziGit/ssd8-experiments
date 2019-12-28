package ssd8.socket.http.server.strategy;

import ssd8.socket.http.server.entity.HttpRequest;
import ssd8.socket.http.server.entity.HttpResponse;
import ssd8.socket.http.server.enums.HttpStatus;

/**
 * The default processor, which process disallowed methods and some warning requests.
 *
 * @author Zhang Chunxu
 */
public class DefaultProcessor extends HttpProcessStrategy {
    @Override
    public HttpResponse.Builder process(HttpRequest request) {
        HttpResponse.Builder builder = new HttpResponse.Builder(request.getVersion(), HttpStatus.METHOD_NOT_ALLOWED);

        builder.addHeader("Allow", "GET, PUT");
        return builder;
    }
}
