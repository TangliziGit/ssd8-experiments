package org.tanglizi.dist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CommandParserUtil provides parse function a static utility.
 */
public class CommandParserUtil {

    private static final Pattern commandPattern = Pattern.compile("(\\w+) *(.*)");

    /**
     * Parse command using regex.
     * Return a string array, include command as first element, argument as second element.
     * @param line
     * @return
     */
    public static String[] parseCommandLine(String line) {
        String[] result = new String[2];
        Matcher matcher = CommandParserUtil.commandPattern.matcher(line);

        if (!matcher.find())
            return result;

        String command = matcher.group(1);
        String argument = matcher.group(2);
        result[0] = command;
        result[1] = argument;

        return result;
    }
}
