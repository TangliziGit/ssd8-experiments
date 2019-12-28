package org.tanglizi.dist.strategy;

import java.io.File;

/**
 * Abstract class for CommandStrategy, using strategy design pattern.
 * @author Zhang Chunxu
 */
public abstract class CommandStrategy {
    final String SPLITOR = "\t\t";
    final Integer PACKET_SIZE = 512;

    File currentPath, rootPath;

    /**
     * Set server state. Specifically, the current path and root path.
     * @param currentPath
     * @param rootPath
     */
    public void setState(File currentPath, File rootPath){
        this.currentPath = currentPath;
        this.rootPath = rootPath;
    }

    /**
     * Get current path, which maybe changed from original state.
     * @return
     */
    public File getCurrentPath(){
        return this.currentPath;
    }

    /**
     * Process the command, need to be implemented.
     * @param argument
     * @return
     */
    public String process(String argument){
        return null;
    }
}
