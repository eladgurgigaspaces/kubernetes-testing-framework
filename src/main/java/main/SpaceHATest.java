package main;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.MessageFormat;

import static main.HelmUtils.*;
import static main.helmInstallTest.CHART_INSTANCE_NAME;
import static main.utils.log;

public class SpaceHATest extends KubernetesAbstractTest {

    public static final String MANAGER_CHART_NAME = "xap-manager";
    public static final String SPACE_CHART_NAME = "xap-pu";

    public static final String MANAGER_NAME = "hello";
    public static final String SPACE_NAME = "world";

    public SpaceHATest() throws IOException {
    }

    @Test
    public void verifyHelmInstallOnChart() {
        log("Installing manager chart");
        installChart(this.repoName, MANAGER_CHART_NAME, MANAGER_NAME);
        Assert.assertTrue(isChartInstanceExist(MANAGER_NAME));

        log("Installing space and connect it to the existing manager");
        installChartWithArgs(this.repoName, SPACE_CHART_NAME, SPACE_NAME, "manager.name=hello", "ha=true", "partitions=2");
        Assert.assertTrue(isChartInstanceExist(SPACE_NAME));

        // TODO: assert that the charts are running (using the k8 api)

        log("validating that {0} instance is up", MANAGER_CHART_NAME);
        Assert.assertTrue(isChartInstanceExist(MANAGER_NAME), MessageFormat.format("chart instance named '{0}' is not exist", MANAGER_NAME));

        log("Removing charts");
        removeAllChartsInstances();

        log("Remove chart named: {0}", CHART_INSTANCE_NAME);
        Assert.assertTrue(getAllChartsInstancesNames().isEmpty(), "Failed to remove chart");
    }

}
