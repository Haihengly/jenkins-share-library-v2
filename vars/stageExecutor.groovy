def call(String type, Map config) {

    def registry = [
        check: { cfg -> checkoutStage(cfg) },
        build: { cfg -> buildStage(cfg) },
        deploy: { cfg -> deployStage(cfg) },
        test: { cfg -> testStage(cfg) }
    ]

    def stageCfg = config.stages.find { it.type == type }

    def executor = registry[type]

    if (!executor) {
        error "❌ Unknown executor: ${type}"
    }

    if (stageCfg?.enabled == false) {

        echo "⏭ Skipping stage: ${type}"

    } else {

        echo "🚀 Executing stage: ${type}"
        executor(config)

    }
}
