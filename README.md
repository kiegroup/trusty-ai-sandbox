# trusty-ai-sandbox
![Trusty CI](https://github.com/kiegroup/trusty-ai-sandbox/workflows/Trusty%20CI/badge.svg)

A sandbox repository for the Trusty AI team

## Content

#### Counterfactual explanation with OptaPlanner

Code of the demo we presented at OptaPlanner week about the usages of OptaPlanner to produce CF explaination:
[video](https://www.youtube.com/watch?v=4H3U6xyCgMI) + [code](counterfactual-op)

#### End to end POC: tracing + monitoring + explainability

POC to demonstrate how to use Kogito to execute a DMN and add tracing, monitoring and explainability features

[End to end POC instructions](TrustyAI%20POC%20instructions.md)

Note: the whole POC has been implemented and merged with Kogito codebase so you can use [this guide](https://github.com/kiegroup/kogito-examples/tree/master/trusty-demonstration) to Kogito and reproduce the same features

#### Explainability research

[explainability](explainability) folder contains initial experiments of different approaches to apply local explainability to a decision service

#### Front-end POC

Playground used to design and implement initial version of Audit UI. Code has been merged on Kogito codebase and can be found [here](https://github.com/kiegroup/kogito-apps/tree/master/ui-packages/packages/trusty)

#### PMML notebook

Example of a Jupyter notebook that shows how to train a model and export it as PMML to be integrated then to DMN ([link](pmml-notebook)). The notebook has been created starting from [risk-pmml-builder example](https://github.com/ruivieira/risk-pmml-builder/).

#### StackOverflow POC

POC that applies multiple algorithms to classify StackOverflow questions ([link](stackoverflow_explainabilty))