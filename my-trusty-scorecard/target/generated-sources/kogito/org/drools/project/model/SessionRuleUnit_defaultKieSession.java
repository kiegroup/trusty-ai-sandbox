package org.drools.project.model;

import org.kie.kogito.rules.KieRuntimeBuilder;
import org.kie.kogito.rules.units.SessionData;
import org.kie.kogito.rules.units.SessionRuleUnitInstance;
import org.kie.kogito.rules.units.SessionUnit;

@javax.inject.Singleton()
@javax.inject.Named("defaultKieSession")
public class SessionRuleUnit_defaultKieSession extends SessionUnit {

    @javax.inject.Inject()
    KieRuntimeBuilder runtimeBuilder;

    @Override
    public String id() {
        return "defaultKieSession";
    }

    @Override
    public SessionRuleUnitInstance createInstance(SessionData memory, String name) {
        return new SessionRuleUnitInstance(this, memory, runtimeBuilder.newKieSession("defaultKieSession"));
    }
}
