/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.developer.xai.lime.pmml;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.pmml.evaluator.api.executor.PMMLRuntime;

public abstract class AbstractPMMLTest {

    public static final String GROUP_ID = "org.kie";
    public static final String ARTIFACT_ID = "kie-maven-plugin-test-kjar-12";
    public static final String VERSION = "1.0.0.Final";

    public static KieContainer kieContainer;

    static {
        final KieServices kieServices = KieServices.get();
        ReleaseId releaseId = new ReleaseIdImpl(GROUP_ID, ARTIFACT_ID, VERSION);
        kieContainer = kieServices.newKieClasspathContainer();
    }

    protected AbstractPMMLExecutor abstractPMMLExecutor;
    protected PMMLRuntime pmmlRuntime;

    public AbstractPMMLTest(PMMLRuntime pmmlRuntime) {
        this.pmmlRuntime = pmmlRuntime;
    }

    public static PMMLRuntime getPMMLRuntime(String kbaseName) {
        KieBase kieBase = kieContainer.getKieBase(kbaseName);
        final KieRuntimeFactory kieRuntimeFactory = KieRuntimeFactory.of(kieBase);
        return kieRuntimeFactory.get(PMMLRuntime.class);
    }

}
