package main.utils;


import org.junit.Assert;

import java.text.MessageFormat;

import static main.utils.utils.log;
import static main.utils.utils.runBashCommand;

public class HelmUtils {

    public static void removeChartInstance(String chartName) {
        runBashCommand("helm del --purge " + chartName);
        String charts = runBashCommand("helm list --short");
        String erroMsg = MessageFormat.format("Failed to remove chart: {0} from cluster", chartName);
        Assert.assertFalse(erroMsg, charts.contains(chartName));
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
            Assert.assertTrue("Failed to remove all charts from cluster", getAllChartsInstancesNames().isEmpty());
        }
    }

    public static String getAllChartsInstancesNames() {
        return runBashCommand("helm list --short");
    }

    public static boolean isChartInstanceExist(String chartName) {
        return getAllChartsInstancesNames().contains(chartName);
    }

    public static void installChart(String repo, String chart, String instanceName) {
        installChartWithArgs(repo, chart, instanceName);
    }

    /***
     *
     * @param repo - Helm chart repo - e.g.: gigaspaces
     * @param chart - The name of the chart - e.g.: xap
     * @param instanceName - The name you would like the instance will have - e.g.: hello
     * @param args - arguments for the --set parts of the helm install - e.g.: ha=true", "partitions=2
     */
    public static void installChartWithArgs(String repo, String chart, String instanceName, String... args) {
        log("Installing chart: {0} from repo: {1}", chart, repo);

        StringBuilder sb = new StringBuilder("");

        if (args.length > 0) {
            sb.append("--set ");
            for (String arg : args) {
                sb.append(arg).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        String cmd = MessageFormat.format("helm install {0}/{1} --name {2} {3}", repo, chart, instanceName, sb.toString());
        runBashCommand(cmd);
        Assert.assertTrue(isChartInstanceExist(instanceName));
    }

}