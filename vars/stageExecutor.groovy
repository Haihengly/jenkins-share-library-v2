def call(String type, Map config) {

    def registry = [
        check: { cfg -> checkoutStage(cfg) },
        build: { cfg -> buildStage(cfg) },
        deploy: { cfg -> deployStage(cfg) },
        test: { cfg -> testStage(cfg) }
    ]

    def key = type?.toLowerCase()
    def executor = registry[key]

    // ✅ Unknown stage type
    if (!executor) {
        error "❌ Unknown stage type: '${type}' — available: ${registry.keySet()}"
    }

    echo "🚀 Executing stage: ${type}"

    try {
        executor(config)
        echo "✅ Stage '${type}' completed successfully"

    } catch (Exception e) {
        // ✅ Shows exactly which stage failed and why
        echo "❌ Stage '${type}' failed!"
        echo "📋 Error: ${e.getMessage()}"
        throw e  // rethrow so Jenkins marks build as failed
    }
}