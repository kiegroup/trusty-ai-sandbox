package org.kie.trusty.v1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DummyModelRegistry {

    private static Map<UUID, Model> models = new HashMap<>();

    public static Model getModel(UUID uuid) {
        return models.get(uuid);
    }

    public static void registerModel(UUID uuid, Model model) {
        models.put(uuid, model);
    }

}
