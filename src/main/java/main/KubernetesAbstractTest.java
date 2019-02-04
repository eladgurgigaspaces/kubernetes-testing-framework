package main;

import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static main.HelmUtils.installChartWithArgs;
import static main.HelmUtils.removeAllChartsInstances;
import static main.KubernetesUtils.assertNoRunningPods;
import static main.KubernetesUtils.assertPodsIsRunning;

public class KubernetesAbstractTest {

    protected CoreV1Api k8sClient;
    protected String chart;
    protected String repo;

    public KubernetesAbstractTest() throws IOException {
        getEnvVars();
        setK8Api();
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

    @BeforeEach
    @AfterEach
    public void cleanCluster() {
        removeAllChartsInstances();
        assertNoRunningPods(k8sClient);
    }

    protected void installChartAndValidate(String chart, String deploymentName, String... args) {
        installChartWithArgs(repo, chart, deploymentName, args);
        assertPodsIsRunning(k8sClient, deploymentName);
    }

}