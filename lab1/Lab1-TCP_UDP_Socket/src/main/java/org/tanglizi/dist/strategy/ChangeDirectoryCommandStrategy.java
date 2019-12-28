package org.tanglizi.dist.strategy;

import java.io.File;
import java.io.IOException;

/**
 * CommandStrategy for `cd` command.
 * @author Zhang Chunxu
 */
public class ChangeDirectoryCommandStrategy extends CommandStrategy {

    public ChangeDirectoryCommandStrategy(){
        super();
    }

    /**
     * Concat the new path for an absolute path on root path,
     *  or concat relative path with current path.
     * @param pathArgument
     * @return
     */
    private File getFileWithPathArgument(String pathArgument){
        if (pathArgument.length() == 0)
            return rootPath;

        File file;
        // If path does not start with a '/', it is a relative path.
        if (!pathArgument.startsWith("/"))
            file =  new File(currentPath, pathArgument);
        else {
            StringBuilder builder = new StringBuilder(pathArgument);
            builder.setCharAt(0, ' ');
            pathArgument = builder.toString();
            file = new File(rootPath, pathArgument);
        }

        // check if the file path is correct
        try {
            if (!file.getCanonicalFile().getPath().startsWith(rootPath.getPath()))
                file = rootPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Check the new path if it's a existed directory, and change current path into the new path,
     *  then say `OK`
     * @param argument
     * @return
     */
    @Override
    public String process(String argument) {
        File directory = getFileWithPathArgument(argument);
        if (!directory.exists() || !directory.isDirectory())
            return "no such directory\n";

        this.currentPath = directory;
        return "OK\n";
    }
}
