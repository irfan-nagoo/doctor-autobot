package org.acme.autobot.analyzer;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author irfan.nagoo
 */
public interface Analyzer<I, O> {

    O analyze(I input);

    static TokenizerME getTokenizerME(String tokenFilePath) throws IOException {
        try (InputStream is = getInputStream(tokenFilePath)) {
            return new TokenizerME(new TokenizerModel(is));
        }
    }

    static DoccatModel getDoccatModel(String modelFilePath) throws IOException {
        try (InputStream is = getInputStream(modelFilePath)) {
            return new DoccatModel(is);
        }
    }

    static InputStream getInputStream(String modelFilePath) {
        return Analyzer.class.getClassLoader().getResourceAsStream(modelFilePath);
    }
}
