package org.acme.autobot.tools;

import lombok.extern.jbosslog.JBossLog;
import org.acme.autobot.constants.AutoBotConstants;
import org.acme.autobot.constants.StatusType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.acme.autobot.constants.AutoBotConstants.CSV_DATA_LOADER;

/**
 * This CSV Loader tool is used for loading data from CSV file to the targeted database (e.g. PostGres). We
 * just need to provide the env CLI argument [dev, prod etc.] and it will load data to that env database.
 * If we do not pass any env argument then default application.properties will be used for target database
 * config
 *
 * @author irfan.nagoo
 */

@JBossLog
public class CSVDataLoader {

    private static final String INSERT_SYMPTOM_SQL = "INSERT INTO umls_symptom(cui, name, status, version, " +
            "create_date, created_by, update_date, updated_by) VALUES(?,?,?,?,?,?,?,?)";
    private static final String INSERT_DISEASE_SQL = "INSERT INTO umls_disease(cui, name, sem_type1, status, version, " +
            "create_date, created_by, update_date, updated_by) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_SYMPTOM__DISEASE_SQL = "INSERT INTO umls_symptom_disease(sym_cui, dis_cui) VALUES(?,?)";
    private static final String INSERT_MEDICINE_SQL = "INSERT INTO umls_medicine(cui, name, source, sem_type1, sem_type2, sem_type3, status, version, " +
            "create_date, created_by, update_date, updated_by) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_DISEASE_MEDICINE_SQL = "INSERT INTO umls_disease_medicine(dis_cui, med_cui) VALUES(?,?)";

    private final String env;
    private Properties properties;
    private Connection connection;

    public CSVDataLoader(String env) {
        this.env = env;
    }

    private void loadProperties() throws IOException {
        if (properties == null) {
            String propFile = (env == null) ? "src/main/resources/application.properties" :
                    "src/main/resources/application-" + env + ".properties";
            Properties properties = new Properties();
            properties.load(new FileInputStream(propFile));
            this.properties = properties;
        }
    }

    private void createDBConnection() throws ClassNotFoundException, SQLException, IOException {
        loadProperties();
        Class.forName(properties.getProperty("quarkus.datasource.jdbc.driver"));
        this.connection = DriverManager.getConnection(properties.getProperty("quarkus.datasource.jdbc.url"),
                properties.getProperty("quarkus.datasource.username"), properties.getProperty("quarkus.datasource.password"));
    }

    private PreparedStatement createPreparedStatement(String sql) throws ClassNotFoundException, SQLException, IOException {
        if (connection == null || !connection.isValid(3000)) {
            createDBConnection();
        }
        return connection.prepareStatement(sql);
    }

    private void loadSymptomTable() throws IOException, SQLException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("data/symptom.csv")) {
            CSVParser parser = CSVFormat.Builder.create(CSVFormat.EXCEL)
                    .setHeader().build()
                    .parse(new InputStreamReader(fis));

            try (PreparedStatement statement = createPreparedStatement(INSERT_SYMPTOM_SQL)) {
                for (CSVRecord record : parser) {
                    statement.setString(1, record.get("cui"));
                    statement.setString(2, record.get("name"));
                    statement.setString(3, StatusType.ACTIVE.toString());
                    statement.setInt(4, 1);
                    statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    statement.setString(6, CSV_DATA_LOADER);
                    statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                    statement.setString(8, CSV_DATA_LOADER);
                    statement.addBatch();
                }
                statement.executeLargeBatch();
            }
        }
        log.info("Completed data load for Symptom table!");
    }

    private void loadDiseaseTable() throws IOException, SQLException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("data/disease.csv")) {
            CSVParser parser = CSVFormat.Builder.create(CSVFormat.EXCEL)
                    .setHeader().build()
                    .parse(new InputStreamReader(fis));

            try (PreparedStatement statement = createPreparedStatement(INSERT_DISEASE_SQL)) {
                for (CSVRecord record : parser) {
                    statement.setString(1, record.get("und_umls_cuii"));
                    statement.setString(2, record.get("ind_umls_pt"));
                    statement.setString(3, record.get("ind_umls_sem_typ1"));
                    statement.setString(4, StatusType.ACTIVE.toString());
                    statement.setInt(5, 1);
                    statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    statement.setString(7, AutoBotConstants.CSV_DATA_LOADER);
                    statement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                    statement.setString(9, CSV_DATA_LOADER);
                    statement.addBatch();
                }
                statement.executeLargeBatch();
            }
        }
        log.info("Completed data load for Disease table!");
    }

    private void loadSymptomDiseaseTable() throws IOException, SQLException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("data/symptom_disease.csv")) {
            CSVParser parser = CSVFormat.Builder.create(CSVFormat.EXCEL)
                    .setHeader().build()
                    .parse(new InputStreamReader(fis));

            try (PreparedStatement statement = createPreparedStatement(INSERT_SYMPTOM__DISEASE_SQL)) {
                for (CSVRecord record : parser) {
                    statement.setString(1, record.get("sym_cui"));
                    statement.setString(2, record.get("dis_cui"));
                    statement.addBatch();
                }
                statement.executeLargeBatch();
            }
        }
        log.info("Completed data load for Symptom Disease Mapping table!");
    }

    private void loadMedicineTable() throws IOException, SQLException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("data/medicine.csv")) {
            CSVParser parser = CSVFormat.Builder.create(CSVFormat.EXCEL)
                    .setHeader().build()
                    .parse(new InputStreamReader(fis));

            try (PreparedStatement statement = createPreparedStatement(INSERT_MEDICINE_SQL)) {
                for (CSVRecord record : parser) {
                    statement.setString(1, record.get("umls_cui"));
                    statement.setString(2, record.get("drug_Raw_Name"));
                    statement.setString(3, record.get("src_nm"));
                    statement.setString(4, record.get("umls_sem_typ1"));
                    statement.setString(5, record.get("umls_sem_typ2"));
                    statement.setString(6, record.get("umls_sem_typ3"));
                    statement.setString(7, StatusType.ACTIVE.toString());
                    statement.setInt(8, 1);
                    statement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                    statement.setString(10, CSV_DATA_LOADER);
                    statement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
                    statement.setString(12, CSV_DATA_LOADER);
                    statement.addBatch();
                }
                statement.executeLargeBatch();
            }
        }
        log.info("Completed data load for Medicine table!");
    }

    private void loadDiseaseMedicineTable() throws IOException, SQLException, ClassNotFoundException {
        int counter = 10000;
        try (FileInputStream fis = new FileInputStream("data/disease_medicine.csv")) {
            CSVParser parser = CSVFormat.Builder.create(CSVFormat.EXCEL)
                    .setHeader().build()
                    .parse(new InputStreamReader(fis));

            try (PreparedStatement statement = createPreparedStatement(INSERT_DISEASE_MEDICINE_SQL)) {
                for (CSVRecord record : parser) {
                    statement.setString(1, Optional.ofNullable(record.get("und_umls_cuii"))
                            .orElse("NA" + counter++));
                    statement.setString(2, Optional.ofNullable(record.get("umls_cui"))
                            .orElse("NA" + counter++));
                    statement.addBatch();
                }
                statement.executeLargeBatch();
            }
        }
        log.info("Completed data load for Disease Medicine Mapping table!");
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        String env = null;
        if (ArrayUtils.isNotEmpty(args) && args.length == 2) {
            env = args[0];
        }
        CSVDataLoader csvDataLoader = new CSVDataLoader(env);
        StopWatch stopWatch = StopWatch.create();
        stopWatch.start();
        csvDataLoader.loadSymptomTable();
        csvDataLoader.loadDiseaseTable();
        csvDataLoader.loadSymptomDiseaseTable();
        csvDataLoader.loadMedicineTable();
        csvDataLoader.loadDiseaseMedicineTable();
        stopWatch.stop();
        log.info(String.format("Successfully Completed data load in %d minutes !",
                stopWatch.getTime(TimeUnit.MINUTES)));
    }

}
