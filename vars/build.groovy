def call(Map config) {

    def image = "${config.dockerImage}:${config.dockerTag ?: 'latest'}"

    echo "Building Docker image ${image}"
    sh """
        docker build -t ${image} .
    """

    echo "Logging in & pushing Docker image"

    docker.withRegistry(
        'https://registry.hub.docker.com',
        config.dockerCredentialsId
    ) {
        def dockerImage = docker.image(image)
        dockerImage.push()

        // optional: also push latest tag
        if (config.pushLatest == true) {
            dockerImage.push('latest')
        }
    }

    echo "Docker image pushed successfully"
}