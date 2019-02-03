package main.parameterTestPOC;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static main.utils.log;

public class TestNGParameterTest {

    @Test
    @Parameters({ "repo", "chart" })
    public void printWord(String repo, String chart) {
        log("repo: " + repo);
        log("chart: " + chart);
    }

}
