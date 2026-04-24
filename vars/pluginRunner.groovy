def call(String type, Map config) {

    def registry = [
        check: { cfg -> checkoutPlugin(cfg) },
        build: { cfg -> buildPlugin(cfg) },
        deploy: { cfg -> deployPlugin(cfg) },
        test: { cfg -> testPlugin(cfg) }
    ]

    def plugin = registry[type]

    if (!plugin) {
        error "❌ Unknown plugin: ${type}"
    }

    echo "🚀 Running plugin: ${type}"

    plugin(config)
}