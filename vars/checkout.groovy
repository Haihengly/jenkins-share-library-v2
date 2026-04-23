def call(Map config) {
    checkout([
        $class: 'GitSCM',
        branches: [[name: "*/${config.branch}"]],
        userRemoteConfigs: [[
            url: config.repoUrl,
            // credentialsId: 'your-credentials-id'
        ]]
    ])
}