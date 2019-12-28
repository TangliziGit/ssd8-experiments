package org.tanglizi.dist.strategy;

import java.io.File;
import java.util.Objects;

/**
 * CommandStrategy for `ls` command.
 * @author Zhang Chunxu
 */
public class ListCommandStrategy extends CommandStrategy {

    public ListCommandStrategy(){
        super();
    }

    /**
     * List the files and directories in current path.
     * @param argument
     * @return
     */
    @Override
    public String process(String argument) {
        StringBuilder builder = new StringBuilder("The list of file:\n");

        for (File file: Objects.requireNonNull(currentPath.listFiles())){
            if (file.isFile())
                builder.append("<file>").append(SPLITOR);
            else
                builder.append("<dir>").append(SPLITOR);

            builder.append(file.getName()).append(SPLITOR);
            builder.append(file.length()).append("B").append("\n");
        }

        return builder.toString();
    }
}
