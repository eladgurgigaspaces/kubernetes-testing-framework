package main;

import org.testng.annotations.Test;

import static main.utils.log;

public class parameterTest {

    @Test
    public void printWord(String word) {
        log(word);
    }

}
