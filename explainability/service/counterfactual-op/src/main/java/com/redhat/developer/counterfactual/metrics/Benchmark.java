package com.redhat.developer.counterfactual.metrics;

import com.redhat.developer.counterfactual.entities.CreditCardApprovalEntity;
import com.redhat.developer.counterfactual.solutions.Approval;
import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;

import javax.xml.bind.JAXBException;

public class Benchmark {

  public static void main(String[] args) throws JAXBException {

    // create dataset
    Approval solution = new Approval();

    Facts.input = new CreditCardApprovalEntity(30, 10000, 0, 100, false, false, false);

    PlannerBenchmarkFactory benchmarkFactory =
        PlannerBenchmarkFactory.createFromFreemarkerXmlResource("BenchmarkConfig.ftl");

    PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark(solution);
    benchmark.benchmarkAndShowReportInBrowser();
  }
}
