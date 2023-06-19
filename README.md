# doctor-autobot

![doctor-autobot](https://github.com/irfan-nagoo/doctor-autobot/assets/96521607/33fd02f3-e31b-4f02-948b-2c65cd8832f7)


The project doctor-auto offers to perform preliminary medical diagnosis of the given symptoms and provides a diagnosis report which includes the probable minor/major diseases and a list of medicines that is used to treat those diseases. This application use Artificial Intelligence (ML) to detect major diseases and uses medicine database to offer cure. If you don’t get full diagnosis response that is probably because data is not available in the database. More **related** symptoms as input provide more accurate disease diagnosis. The diagnosis is purely based on symptoms and doesn't consider other parameters like medical test reports, allergies, past history, age etc. because that data is not available. At least 3 symptoms as input are required to indicate a major disease which is configurable though. This application runs on real clinical data extracted from credible sources. The symptom to disease mapping clinical data is extracted from [New York Presbyterian Hospital clinical data](https://people.dbmi.columbia.edu/~friedma/Projects/DiseaseSymptomKB/index.html). The disease to medicine/drug mapping clinical data is motivated by [Journal of Biomedical Semantics](https://jbiomedsem.biomedcentral.com/articles/10.1186/s13326-016-0110-0) scientific journal.


This project follows [UMLS](https://www.nlm.nih.gov/research/umls/index.html)  (Unified Medical Language System) standards. Under UMLS, each entity (Symptom, Disease, Medicine) is a concept and each concept has a CUI (Concept Unique Identifier) associated with it. Therefore, each component of response you get from this application will have CUI field in it. The advantage of having CUI is that you can just Google it and you will get more information about it. You can also use various flavors of get/search REST API of this application to get more information about the entity.

This project includes the sql component to create required PostGres database schema and tables. This application can work with any relational database technology. Once the application and DB is setup, you need to run **CSVDataLoader.java** which will load the required clinical data to the database. This tools should run smoothly and load about 220,000 records to the database in just about 1 minute (which is really good). For training/retraining AI component, you can use **ModelFileGenerator.java** tool. It takes training file as input and, constructs and generates binary model file to be used by this application. Read java docs for more details. Here is the summary of this project:

    1. CSVDataLoader Tool       - This loads valid clinical data to the database. It is required to be executed before running this application.
    2. ModelFileGenerator Tool  - This tool trains the AI component and generates the binary model file. Every time a new disease is onboarded into this application with symptoms and medicines, we also need to make entries in disease training file and use this tool to generate new disease model.
    3. Diagnosis API            - This API takes set of symptoms as input and returns the diagnosis report which is the purpose of this application.
    4. Inventory/Catalog APIs   - There are about 21 REST APIs provided to onboard/query Symptoms, Diseases and Medicines. The APIs support pagination and sorting as required.

**Tech Stack:** Java 11, Quarkus, Quarkus REST Reactive, Quarkus Panache Reactive, Hibernate Reactive, Vertx, Mutiny, Lombok, MapStruct, Apache Open NLP, Apache Common CSV, PostGres DB, Quarkus Test, Junit 5

This application is fully developed using Reactive programming style for better code readability and high performance. This application is ready to deploy on Kubernetes and required docker files are also included in this project. This is my first application in Quarkus technology and I had a positive experience with it. I'm likely to use Quarkus again in my future initiatives.

Here is the Swagger URL for various REST endpoints on local: http://localhost:8080/q/swagger-ui


