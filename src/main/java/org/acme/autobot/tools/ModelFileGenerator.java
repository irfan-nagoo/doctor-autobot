package org.acme.autobot.tools;

import lombok.extern.jbosslog.JBossLog;
import opennlp.tools.doccat.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This Tool is used to generate model binary file from given train file. This
 * is generally useful when the train file is huge. Otherwise, we can also use
 * train file directly if required. Its takes two optional CLI arguments as input
 * which are train file path and the targeted mode file path.
 *
 * @author irfan.nagoo
 */

@JBossLog
public class ModelFileGenerator {

    private final String trainFile;
    private final String modelFile;

    public ModelFileGenerator(String trainFile, String modelFile) {
        this.trainFile = trainFile;
        this.modelFile = modelFile;
    }

    public void generateDoccatModel() throws IOException {
        // read plain text train input file
        InputStreamFactory isf = () -> new FileInputStream(trainFile);
        try (var objStream = new DocumentSampleStream(new PlainTextByLineStream(isf,
                Charset.defaultCharset()))) {

            // customize doc category factory with feature generators for
            // processing words in a given input
            var featureGenerators = new FeatureGenerator[]{new NGramFeatureGenerator(1, 1),
                    new NGramFeatureGenerator(2, 3), new BagOfWordsFeatureGenerator()};

            // Training parameters-
            //   CUTOFF_PARAM - Minimum number of words to be considered in a given category
            //   ITERATIONS_PARAM - Number of iteration to perform
            TrainingParameters trainingParameters = TrainingParameters.defaultParams();
            trainingParameters.put(TrainingParameters.CUTOFF_PARAM, 1);
            trainingParameters.put(TrainingParameters.ITERATIONS_PARAM, 10);
            DoccatModel doccat = DocumentCategorizerME.train("en", objStream, trainingParameters,
                    new DoccatFactory(featureGenerators));
            doccat.serialize(new File(modelFile));
        }
    }

    public static void main(String[] args) throws IOException {
        String trainFilePath;
        String modelFilePath;
        if (ArrayUtils.isNotEmpty(args) && args.length == 2) {
            trainFilePath = args[0];
            modelFilePath = args[1];
        } else {
            trainFilePath = "src/main/resources/opennlp/en-disease.train";
            modelFilePath = "src/main/resources/opennlp/en-disease.bin";
        }
        ModelFileGenerator modelFileGenerator = new ModelFileGenerator(trainFilePath, modelFilePath);
        modelFileGenerator.generateDoccatModel();
        log.info(String.format("Generated [%s] Successfully!", modelFilePath));
    }

}
