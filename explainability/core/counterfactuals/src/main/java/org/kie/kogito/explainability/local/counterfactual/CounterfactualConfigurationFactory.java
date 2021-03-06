package org.kie.kogito.explainability.local.counterfactual;

import org.kie.kogito.explainability.local.counterfactual.entities.BooleanEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CategoricalEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.DoubleEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.IntegerEntity;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.decider.acceptor.LocalSearchAcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import java.util.ArrayList;
import java.util.List;

public class CounterfactualConfigurationFactory {

    private static final long DEFAULT_TIME_LIMIT = 30;
    private static final int DEFAULT_TABU_SIZE = 70;
    private static final int DEFAULT_ACCEPTED_COUNT = 5000;

    private CounterfactualConfigurationFactory() {

    }

    public static CounterfactualConfigurationFactory.Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private TerminationConfig terminationConfig = null;
        private int tabuSize = DEFAULT_TABU_SIZE;
        private int acceptedCount = DEFAULT_ACCEPTED_COUNT;

        private Builder() {

        }

        public SolverConfig build() {
            // create a default termination config if none supplied
            if (terminationConfig == null) {
                terminationConfig = new TerminationConfig();
                terminationConfig.setSecondsSpentLimit(DEFAULT_TIME_LIMIT);
            }

            SolverConfig solverConfig = new SolverConfig();

            solverConfig.withEntityClasses(IntegerEntity.class, DoubleEntity.class, BooleanEntity.class, CategoricalEntity.class);
            solverConfig.setSolutionClass(CounterfactualSolution.class);

            ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
            scoreDirectorFactoryConfig.setEasyScoreCalculatorClass(CounterFactualScoreCalculator.class);
            solverConfig.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);

            solverConfig.setTerminationConfig(terminationConfig);

            LocalSearchAcceptorConfig acceptorConfig = new LocalSearchAcceptorConfig();
            acceptorConfig.setEntityTabuSize(tabuSize);

            LocalSearchForagerConfig localSearchForagerConfig = new LocalSearchForagerConfig();
            localSearchForagerConfig.setAcceptedCountLimit(acceptedCount);

            LocalSearchPhaseConfig localSearchPhaseConfig = new LocalSearchPhaseConfig();
            localSearchPhaseConfig.setAcceptorConfig(acceptorConfig);
            localSearchPhaseConfig.setForagerConfig(localSearchForagerConfig);

            List<PhaseConfig> phaseConfigs = new ArrayList<>();
            phaseConfigs.add(localSearchPhaseConfig);

            solverConfig.setPhaseConfigList(phaseConfigs);
            return solverConfig;
        }

        public Builder withTabuSize(int size) {
            this.tabuSize = size;
            return this;
        }

        public Builder withAcceptedCount(int count) {
            this.acceptedCount = count;
            return this;
        }

        public Builder withTerminationConfig(TerminationConfig terminationConfig) {
            this.terminationConfig = terminationConfig;
            return this;
        }

        public Builder withScoreCalculationCountLimit(long scoreCalculationCountLimit) {
            if (this.terminationConfig == null) {
                this.terminationConfig = new TerminationConfig();
            }
            this.terminationConfig.setScoreCalculationCountLimit(scoreCalculationCountLimit);
            return this;
        }

    }

}
