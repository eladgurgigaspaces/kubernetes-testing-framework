package main.tests;

import com.gigaspaces.rest.client.java.api.InformationApi;
import com.gigaspaces.rest.client.java.api.SpacesApi;
import com.gigaspaces.rest.client.java.invoker.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static main.utils.HelmUtils.installChartWithArgs;
import static main.utils.HelmUtils.removeAllChartsInstances;
import static main.utils.KubernetesUtils.assertNoRunningPods;
import static main.utils.KubernetesUtils.assertPodsIsRunning;
import static main.utils.utils.log;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KubernetesAbstractTest {

    protected CoreV1Api k8sClient;
    protected String chart;
    protected String repo;
    protected SpacesApi spaceRestApi;
    protected InformationApi informationApi;

    @BeforeAll
    public void setUp() throws IOException {
        log("Setting up tests environment");
        getEnvVars();
        setK8Api();
        setRestClient();
    }

    @BeforeEach
    @AfterEach
    public void tearDown() {
        removeAllChartsInstances();
        assertNoRunningPods(k8sClient);
    }

    private void getEnvVars() {
        chart = System.getenv("CHART");
        if (chart == null) {
            chart = "xap";
        }

        repo = System.getenv("REPO_NAME");
        if (repo == null) {
            repo = "gigaspaces";
        }
    }

    private void setK8Api() throws IOException {
        Configuration.setDefaultApiClient(Config.fromConfig("/home/eladg/.kube/config"));
        k8sClient = new CoreV1Api();
    }

    private void setRestClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://192.168.33.204:30890/v2");
        spaceRestApi = new SpacesApi(apiClient);
        informationApi = new InformationApi(apiClient);
    }

    protected void installChartAndValidate(String chart, String deploymentName, String... args) {
        installChartWithArgs(repo, chart, deploymentName, args);
        assertPodsIsRunning(k8sClient, deploymentName);
    }

}