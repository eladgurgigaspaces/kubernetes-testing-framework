package main;

import iTests.framework.utils.CommandTestUtils;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.junit.Assert;

import java.io.IOException;
import java.text.MessageFormat;

import static main.HelmUtils.installChart;
import static main.KubernetesUtils.assertPodsIsRunning;
import static main.helmInstallTest.CHART_INSTANCE_NAME;

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
//            String formaterResult = new StringBuilder("\nCommand Result:\n")
//                    .append("------------\n")
//                    .append(result).append('\n')
//                    .append("------------\n").toString();
//            System.out.println(formaterResult);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        return result;
    }

}