def call(Map config) {

    if (!config) {
        error "❌ Config is empty"
    }

    if (!config.stages || config.stages.size() == 0) {
        error "❌ No stages defined in config"
    }

    config.stages.each { s ->

        if (!s.name) {
            error "❌ Stage missing name"
        }

        if (!s.type) {
            error "❌ Stage '${s.name}' missing type"
        }

        if (s.enabled == null) {
            error "❌ Stage '${s.name}' missing enabled flag"
        }

        if (!(s.enabled instanceof Boolean)) {
            error "❌ Stage '${s.name}' enabled must be true/false"
        }
    }

    echo "✅ Config validation passed"
}