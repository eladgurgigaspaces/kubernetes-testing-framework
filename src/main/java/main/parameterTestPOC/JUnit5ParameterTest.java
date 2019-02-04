package main.parameterTestPOC;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static main.utils.log;

public class JUnit5ParameterTest {

    @ParameterizedTest
    @ValueSource(strings = {"xap", "insightedge"})
    public void printWord(String chart) {
        log("chart: " + chart);
    }

}