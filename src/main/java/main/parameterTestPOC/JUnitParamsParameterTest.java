package main.parameterTestPOC;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static main.utils.log;

@RunWith(JUnitParamsRunner.class)
public class JUnitParamsParameterTest {

    @Test
    @Parameters({ "gigaspaces, xap", "gigaspaces, insightedge" })
    public void printWord(String repo, String chart) {
        log("repo: " + repo);
        log("chart: " + chart);
    }

}
