package org.tanglizi.dist.rmi.client.strategy;

import org.tanglizi.dist.rmi.service.RmiService;

import java.text.SimpleDateFormat;

/**
 * abstract class for strategy design pattern.
 *
 * @author Chunxu Zhang
 */
public abstract class CommandStrategy {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");

    public void process(String[] args, RmiService service) throws Exception {
        // need to be override.
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}
