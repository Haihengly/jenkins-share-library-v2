def call(String type, Map config) {

    def registry = [
        build: 'buildPlugin',
        test: 'testPlugin',
        deploy: 'deployPlugin'
    ]

    def pluginName = registry[type]

    if (!pluginName) {
        error "Unknown plugin type: ${type}"
    }

    def path = "plugins/${pluginName}.groovy"

    if (!fileExists(path)) {
        error "Plugin not found: ${path}"
    }

    def plugin = load path

    plugin.call(config)
}