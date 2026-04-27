def call(Map config) {

    config.stages = config.stages.collect { s ->

        def enabledRaw = s.enabled

        if (enabledRaw == null) {
            error "❌ ${s.name}: enabled is missing"
        }

        def enabled = enabledRaw.toString().toLowerCase()

        if (!(enabled in ['true', 'false'])) {
            error "❌ ${s.name}: invalid enabled value = ${enabledRaw}"
        }

        return [
            name: s.name,
            type: s.type,
            enabled: enabled == 'true'
        ]
    }

    return config
}