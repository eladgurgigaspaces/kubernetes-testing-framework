package main;

import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.testng.annotations.AfterTest;

import java.io.IOException;

import static main.HelmUtils.removeAllChartsInstances;

public class KubernetesAbstractTest {

    protected CoreV1Api api;
    protected String chart;
    protected String repo;

    public KubernetesAbstractTest() throws IOException {
        getEnvVars();
        setK8Api();
    }

    private void getEnvVars() {
        this.chart = System.getenv("CHART");
        if (chart == null) {
            this.chart = "xap";
        }

        this.repo = System.getenv("REPO_NAME");
        if (this.repo == null) {
            this.repo = "gigaspaces";
        }
    }

    private void setK8Api() throws IOException {
        Configuration.setDefaultApiClient(Config.fromConfig("/home/eladg/.kube/config"));
        api = new CoreV1Api();
    }

    @AfterTest
    public void testDown() {
        removeAllChartsInstances();
    }

}