package main.utils;

import iTests.framework.utils.AssertUtils;
import iTests.framework.utils.AssertUtils.RepetitiveConditionProvider;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import org.junit.Assert;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static main.utils.utils.log;

public class KubernetesUtils {

    public static void assertPodsIsRunning(CoreV1Api api, String deploymentName) {
        log("Assert that pods of deployments named: {0} is running", deploymentName);
        try {
            V1PodList pods = api.listNamespacedPod("default", null, null, null, null, "release=" + deploymentName, null, null, null, null);
            pods.getItems().forEach(pod -> assertPodIsRunning(api, pod));
        } catch (ApiException e) {
            Assert.fail(e.getMessage());
        }
    }

    public static void assertNoRunningPods(CoreV1Api api) {
        AssertUtils.repetitiveAssertTrue("Failed assert that no pods is running", () -> {
            try {
                return getAllPodsInNamespace(api, "default").getItems().isEmpty();
            } catch (ApiException e) {
                return false;
            }
        }, MINUTES.toMillis(5));
    }

    private static V1PodList getAllPodsInNamespace(CoreV1Api api, String namespace) throws ApiException {
        return api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null);
    }

    private static void assertPodIsRunning(CoreV1Api api, V1Pod pod) {
        final String errorMsg = "Failed to wait for chart to be running";

        RepetitiveConditionProvider conditionProvider = () -> {
            try {
                return getPodByName(api, pod).getStatus().getPhase().equalsIgnoreCase("running");
            } catch (ApiException e) {
                return false;
            }
        };

        AssertUtils.repetitiveAssertTrue(errorMsg, conditionProvider, SECONDS.toMillis(30), 2000);
    }

    private static V1Pod getPodByName(CoreV1Api api, V1Pod pod) throws ApiException {
        return api.readNamespacedPod(pod.getMetadata().getName(), "default", null, null, null);
    }

}
