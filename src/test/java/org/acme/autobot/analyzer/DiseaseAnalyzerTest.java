package org.acme.autobot.analyzer;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class DiseaseAnalyzerTest {

    @InjectMock
    private DiseaseAnalyzer diseaseAnalyzer;

    @Test
    void analyze() {
        var input = List.of("dizziness", "shortness_of_breath", "pain_chest");
        var output = diseaseAnalyzer.analyze(input);
    }
}