package org.tanglizi.dist.rmi.client.strategy;

import org.tanglizi.dist.rmi.service.RmiService;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;

/**
 * CommandStrategy, strategy design pattern.
 *
 * @author Zhang Chunxu
 */
public interface CommandStrategy {

    void process(String[] args, RmiService service) throws RemoteException;
}
