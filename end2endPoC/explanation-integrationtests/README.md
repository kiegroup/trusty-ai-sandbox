# Explainability Integration Tests

A test suite for explainability on different types of models.

* [DMN ITs](https://github.com/kiegroup/trusty-ai-sandbox/tree/master/end2endPoC/explanation-integrationtests/explanation-integrationtests-dmn) provides functional tests for extracting explanations for different DMN models. 
* [PMML ITs](https://github.com/kiegroup/trusty-ai-sandbox/tree/master/end2endPoC/explanation-integrationtests/explanation-integrationtests-pmml) provides functional tests for extracting explanations for different PMML models.
* [OpenNLP ITs](https://github.com/kiegroup/trusty-ai-sandbox/tree/master/end2endPoC/explanation-integrationtests/explanation-integrationtests-opennlp) provides functional tests for extracting explanations for different OpenNLP models.

To execute the tests `cd` into the desired directory and simply run `mvn clean install`.
e.g.
```$bash
cd explanation-integrationtests-dmn
mvn clean install

[INFO] 
[INFO] -------< com.redhat.developer:explanation-integrationtests-dmn >--------
[INFO] Building explanation-integrationtests-dmn 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ explanation-integrationtests-dmn ---
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.redhat.developer.TrafficViolationDmnLimeExplainerTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.04 s - in com.redhat.developer.TrafficViolationDmnLimeExplainerTest
[INFO] Running com.redhat.developer.LoanEligibilityDmnLimeExplainerTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.271 s - in com.redhat.developer.LoanEligibilityDmnLimeExplainerTest
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 16.398 s
[INFO] Finished at: 2020-07-01T14:39:25+02:00
[INFO] ------------------------------------------------------------------------

```