package org.acme.autobot.analyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiseaseAnalyzerTest {

    DiseaseAnalyzer diseaseAnalyzer;

    @BeforeEach
    void setup() {
        diseaseAnalyzer = new DiseaseAnalyzer();
    }


    @Test
    void analyze() {
        var input = List.of("C0008031", "C0392680", "C0012833");
        var output = diseaseAnalyzer.analyze(input);
        assertFalse(output.isEmpty());
        assertEquals("C0020538", output.get(0));
    }

    @Test
    void analyze_NoMatch() {
        var input = List.of("C000001", "C000002", "C000003");
        var output = diseaseAnalyzer.analyze(input);
        assertTrue(output.isEmpty());
    }

}