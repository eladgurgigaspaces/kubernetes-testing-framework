package main;

import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

import java.io.IOException;

import static main.HelmUtils.removeAllChartsInstances;

public class KubernetesAbstractTest {

    protected CoreV1Api api;
    protected boolean isI9E;
    protected String chartName;
    protected String repoName;

    public KubernetesAbstractTest() throws IOException {
        getEnvVars();
        setTypeOfTestFromArgs();
        setK8Api();
    }

    private void getEnvVars() {
        this.chartName = System.getenv("CHART");
        if (chartName == null) {
            this.chartName = "xap";
        }

        this.repoName = System.getenv("REPO_NAME");
        if (this.repoName == null) {
            this.repoName = "gigaspaces";
        }
    }

    private void setTypeOfTestFromArgs() {
        String envVar = System.getenv("IS_I9E");

        if (envVar != null) {
            this.isI9E = envVar.equalsIgnoreCase("true");
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