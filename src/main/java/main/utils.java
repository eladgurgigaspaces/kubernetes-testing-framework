package main;

import iTests.framework.utils.CommandTestUtils;
import org.junit.Assert;

import java.text.MessageFormat;

public class utils {

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void log(String msgPattern, Object... args) {
        String formattedMsg = MessageFormat.format(msgPattern, args);
        log(formattedMsg);
    }

    public static String runBashCommand(String cmd) {
        String result = "";
        try {
            result = CommandTestUtils.runLocalCommand(cmd, true, false);
            String formaterResult = new StringBuilder("\nCommand Result:\n")
                    .append("------------\n")
                    .append(result).append('\n')
                    .append("------------\n").toString();
            System.out.println(formaterResult);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        return result;
    }

}