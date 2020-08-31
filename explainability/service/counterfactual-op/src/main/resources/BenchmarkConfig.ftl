<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
    <benchmarkDirectory>local/data/approval</benchmarkDirectory>
    <parallelBenchmarkCount>AUTO</parallelBenchmarkCount>
    <inheritedSolverBenchmark>
        <solver>
            <solutionClass>com.redhat.developer.counterfactual.solutions.Approval</solutionClass>
            <entityClass>com.redhat.developer.counterfactual.entities.CreditCardApprovalEntity</entityClass>
            <scoreDirectorFactory>
                <constraintProviderClass>com.redhat.developer.counterfactual.constraints.ApprovalContraintsProvider</constraintProviderClass>
                <initializingScoreTrend>ONLY_DOWN</initializingScoreTrend>
            </scoreDirectorFactory>
        </solver>
    </inheritedSolverBenchmark>

    <#list [10, 100] as entityTabuSize>
    <#list ["FIRST_FIT", "CHEAPEST_INSERTION", "ALLOCATE_FROM_POOL"] as heuristic>
    <#list [10, 1000] as acceptedCountLimit>
        <solverBenchmark>
            <name>T${heuristic} S${entityTabuSize} AC${acceptedCountLimit}</name>
            <solver>
                <constructionHeuristic>
                    <constructionHeuristicType>${heuristic}</constructionHeuristicType>
                </constructionHeuristic>
                <localSearch>
                    <termination>
                        <secondsSpentLimit>30</secondsSpentLimit>
                    </termination>
                    <acceptor>
                        <entityTabuSize>${entityTabuSize}</entityTabuSize>
                    </acceptor>
                    <forager>
                        <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
                    </forager>
                </localSearch>
            </solver>
        </solverBenchmark>
    </#list>
    </#list>
    </#list>
</plannerBenchmark>