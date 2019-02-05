package main.tests;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static main.utils.HelmUtils.getAllChartsInstancesNames;
import static main.utils.HelmUtils.removeChartInstance;
import static main.utils.utils.log;

public class helmInstallTest extends KubernetesAbstractTest {

    public static final String CHART_INSTANCE_NAME = "hello";

    @ParameterizedTest
    @ValueSource(strings = {"xap", "insightedge"})
    public void verifyHelmInstallOnChart(String chart) {

        installChartAndValidate(chart, CHART_INSTANCE_NAME);

        log("Remove chart named: {0}", CHART_INSTANCE_NAME);
        removeChartInstance(CHART_INSTANCE_NAME);

        Assert.assertTrue("Failed to remove chart", getAllChartsInstancesNames().isEmpty());
    }

}