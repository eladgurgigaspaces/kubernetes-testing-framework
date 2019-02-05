package main.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static main.utils.utils.log;

public class SpaceHATest extends KubernetesAbstractTest {

    public static final String MANAGER_CHART = "xap-manager";
    public static final String SPACE_CHART_NAME = "xap-pu";
    public static final String MANAGER_NAME = "hello";
    public static final String SPACE_NAME = "world";

    @ParameterizedTest
    @ValueSource(strings = {"xap", "insightedge"})
    public void verifyHelmInstallOnChart() {
        installChartAndValidate(MANAGER_CHART, MANAGER_NAME);

        log("Installing space and connect it to the existing manager");
        installChartAndValidate(SPACE_CHART_NAME, SPACE_NAME, "manager.name=hello", "ha=true", "partitions=2");
    }

}