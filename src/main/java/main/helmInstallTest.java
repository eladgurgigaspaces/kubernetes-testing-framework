package main;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.MessageFormat;

import static main.HelmUtils.*;
import static main.utils.log;

public class helmInstallTest extends KubernetesAbstractTest {

    public static final String CHART_INSTANCE_NAME = "hello";

    public helmInstallTest() throws IOException {
    }

    @Test
    public void verifyHelmInstallOnChart() {
        log("Installing chart: " + this.chartName);
        installChart(this.repoName, this.chartName, CHART_INSTANCE_NAME);

        log("validating that {0} instance is up", this.chartName);
        String errorMsg = MessageFormat.format("chart instance named '{0}' is not exist", CHART_INSTANCE_NAME);
        Assert.assertTrue(isChartInstanceExist(CHART_INSTANCE_NAME), errorMsg);

        log("Remove chart named: {0}", CHART_INSTANCE_NAME);
        removeChartInstance(CHART_INSTANCE_NAME);

        Assert.assertTrue(getAllChartsInstancesNames().isEmpty(), "Failed to remove chart");
    }


}
