package org.acme.autobot.analyzer;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.TokenizerME;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This analyzer returns the UMLS CUI list of the probable major sickness/disease
 * as per the given symptoms.
 *
 * @author irfan.nagoo
 */

@ApplicationScoped
@Slf4j
public class DiseaseAnalyzer implements Analyzer<List<String>, List<String>> {

    private final TokenizerME tokenizerME;
    private final DoccatModel doccatModel;

    public DiseaseAnalyzer() {
        this("opennlp/en-token.bin", "opennlp/en-disease.bin");
    }

    public DiseaseAnalyzer(String tokenFile, String trainFile) {
        try {
            this.tokenizerME = Analyzer.getTokenizerME(tokenFile);
            this.doccatModel = Analyzer.getDoccatModel(trainFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid tokenizer or train file path", e);
        }
    }

    /**
     * Returns the UMLS CUI list of probable diseases matching given symptoms.
     * The list is sorted by the probability score with the most probable disease as first element.
     *
     * <br><br>
     * UMLS: Unified Medical Language System<br>
     * CUI: Concept unique identifier
     *
     * @param input List of symptoms
     * @return Sorted list of disease CUIs starting with most probable disease first
     */
    @Override
    public List<String> analyze(List<String> input) {
        return findDiseases(input);
    }

    private List<String> findDiseases(List<String> input) {
        log.info("Analyzing the input Symptoms {}", input);
        // find the diseases as per given input symptom using Apache OpenNLP ML
        // (Artificial Intelligence) library
        var tokens = tokenizerME.tokenize(String.join(" ", input));
        var documentCategorizerME = new DocumentCategorizerME(doccatModel);

        // return empty list if the probability of all the outcome elements is same
        // which means the given symptom does not have matching disease in records
        if (Arrays.stream(documentCategorizerME.categorize(tokens)).distinct().count() == 1) {
            return List.of();
        }

        // score and sort (desc) the diseases
        var map = documentCategorizerME.sortedScoreMap(tokens);
        var sortedCategoryList = new ArrayList<>(map.values());
        Collections.reverse(sortedCategoryList);
        return sortedCategoryList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
