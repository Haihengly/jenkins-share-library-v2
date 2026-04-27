def call(String type, Map config) {

    def registry = [
        check: { cfg -> checkoutStage(cfg) },
        build: { cfg -> buildStage(cfg) },
        deploy: { cfg -> deployStage(cfg) },
        test: { cfg -> testStage(cfg) }
    ]

    def stageCfg = config.stages.find { it.type == type }
    echo "DEBUG => type: ${type}, enabled: ${stageCfg?.enabled}"

    if (stageCfg?.enabled == false) {
        echo "⏭ Skipping stage: ${type}"
        return
    }

    def executor = registry[type]

    if (!executor) {
        error "❌ Unknown executor: ${type}"
    }

    echo "🚀 Executing stage: ${type}"

    executor(config)
}