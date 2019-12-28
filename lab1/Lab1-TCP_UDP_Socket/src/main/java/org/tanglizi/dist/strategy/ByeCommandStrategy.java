package org.tanglizi.dist.strategy;

/**
 * CommandStrategy for `bye` command.
 * @author Zhang Chunxu
 */
public class ByeCommandStrategy extends CommandStrategy {

    public ByeCommandStrategy() {
        super();
    }

    /**
     * Just say bye.
     * @param argument
     * @return
     */
    @Override
    public String process(String argument) {
        return "bye\n";
    }
}
