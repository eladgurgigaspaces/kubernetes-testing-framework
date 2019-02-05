package main.tests;

import com.gigaspaces.rest.client.java.invoker.ApiException;
import com.gigaspaces.rest.client.java.model.Space;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static java.lang.Thread.sleep;
import static main.utils.utils.log;

public class AdminRestApiTest extends KubernetesAbstractTest {

    public static final String CHART_INSTANCE_NAME = "hello";

    @ParameterizedTest
    @ValueSource(strings = {"xap", "insightedge"})
    public void getSpacesTest(String chart) throws ApiException, InterruptedException {
        installChartAndValidate(chart, CHART_INSTANCE_NAME);
        sleep(10000);

        log("Asserting that there is a spaces through the RestAPI");
        List<Space> spaces = spaceRestApi.spacesGet();
        Assert.assertFalse("Can't find any space using Rest API", spaces.isEmpty());

        log("Asserting that there is a space named '{0}' through the RestAPI", CHART_INSTANCE_NAME);
        String spaceName = spaces.get(0).getName();
        Assert.assertEquals("Can't assert space is deployed using Rest API", "hello", spaceName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"xap", "insightedge"})
    public void getManagersTest(String chart) throws ApiException, InterruptedException {
        installChartAndValidate(chart, CHART_INSTANCE_NAME);
        sleep(10000);

        List<String> managers = informationApi.infoGet().getManagers();
        Assert.assertFalse("Can't find any manager using Rest API", managers.isEmpty());

        String managerName = managers.get(0);
        Assert.assertTrue("Can't assert space is deployed using Rest API", managerName.startsWith(CHART_INSTANCE_NAME));
    }

    private void printSpacesByRest() throws ApiException {
        log("\n-------------------------");
        log("Printing spaces by Rest API:");
        spaceRestApi.spacesGet().forEach(space -> System.out.println(space.getName()));
        log("-------------------------\n");
    }

    private void printManagersByRest() throws ApiException {
        log("\n-------------------------");
        log("Printing managers by Rest API:");
        informationApi.infoGet().getManagers().forEach(System.out::println);
        log("-------------------------\n");
    }

}