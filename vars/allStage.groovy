def call(Map config) {
    def buildFlag  = config.get('build', true)
    def deployFlag = config.get('deploy', true)

    return [
        [
            name: 'Checkout',
            action: { ->
                echo "Checking out branch ${config.branch}"
                checkout(config)
            }
        ],
        [
            name: 'Build',
            action: { ->
                if (buildFlag) {
                    echo "Building version ${config.version}"
                    build(config)
                } else {
                    echo "Skipping build stage"
                }
            }
        ],
        [
            name: 'Deploy',
            action: { ->
                if (deployFlag) {
                    echo "Deploying version ${config.version} to ${config.envName}"
                    deploy(config)
                } else {
                    echo "Skipping deploy stage"
                }
            }
        ]
    ]
}
