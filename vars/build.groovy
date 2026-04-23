def call(Map config) {

    echo "Building Docker image ${config.dockerImage}:${config.dockerTag}"
    sh "docker build -t ${config.dockerImage}:${config.dockerTag} ."

    echo "Pushing Docker image to registry"
    docker.withRegistry('https://registry.hub.docker.com', config.dockerCredentials) {
        docker.image("${config.dockerImage}:${config.dockerTag}").push()
    }
    
}