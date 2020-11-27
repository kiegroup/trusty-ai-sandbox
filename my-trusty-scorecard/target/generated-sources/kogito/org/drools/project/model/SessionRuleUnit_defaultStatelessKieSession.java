package org.drools.project.model;

import org.kie.kogito.rules.KieRuntimeBuilder;
import org.kie.kogito.rules.units.SessionData;
import org.kie.kogito.rules.units.SessionRuleUnitInstance;
import org.kie.kogito.rules.units.SessionUnit;

@javax.inject.Singleton()
@javax.inject.Named("defaultStatelessKieSession")
public class SessionRuleUnit_defaultStatelessKieSession extends SessionUnit {

    @javax.inject.Inject()
    KieRuntimeBuilder runtimeBuilder;

    @Override
    public String id() {
        return "defaultStatelessKieSession";
    }

    @Override
    public SessionRuleUnitInstance createInstance(SessionData memory, String name) {
        return new SessionRuleUnitInstance(this, memory, runtimeBuilder.newKieSession("defaultStatelessKieSession"));
    }
}
