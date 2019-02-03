package main;

import org.testng.Assert;

import java.text.MessageFormat;

import static main.utils.log;
import static main.utils.runBashCommand;

public class HelmUtils {

    public static void removeChartInstance(String chartName) {
        runBashCommand("helm del --purge " + chartName);
        String charts = runBashCommand("helm list --short");
        String erroMsg = MessageFormat.format("Failed to remove chart: {0} from cluster", chartName);
        Assert.assertFalse(charts.contains(chartName), erroMsg);
    }

    public static void removeAllChartsInstances() {
        log("Removing all charts from cluster");

        String charts = getAllChartsInstancesNames();
        if (!charts.isEmpty()) {
            charts = charts.replace("\n", " ");
            log("Charts to be removed from cluster: {0}", charts);
            for (String chart : charts.split(" ")) {
                removeChartInstance(chart);
            }
            Assert.assertTrue(getAllChartsInstancesNames().isEmpty(), "Failed to remove all charts from cluster");
        }
    }

    public static String getAllChartsInstancesNames() {
        return runBashCommand("helm list --short");
    }

    public static boolean isChartInstanceExist(String chartName) {
        return getAllChartsInstancesNames().contains(chartName);
    }

    public static void installChart(String repoName, String chartName, String instanceName) {
        String cmd = MessageFormat.format("helm install {0}/{1} --name {2}", repoName, chartName, instanceName);
        runBashCommand(cmd);
    }

    public static void installChartWithArgs(String repoName, String chartName, String instanceName, String... args) {
        StringBuilder sb = new StringBuilder("");
        for (String arg : args) {
            sb.append(arg).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        String cmd = MessageFormat.format("helm install {0}/{1} --name {2} --set {3}", repoName, chartName, instanceName, sb.toString());
        runBashCommand(cmd);
    }

}