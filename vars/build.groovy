def call(Map config) {

    def image = "${config.dockerImage}:${config.dockerTag ?: 'latest'}"

    echo "Building Docker image ${image}"
    sh "docker build -t ${image} ."

    echo "Pushing Docker image"

    docker.withRegistry('https://index.docker.io/v1/', config.dockerCredentialsId) {
        def img = docker.image(image)
        img.push()
    }

    echo "Push completed"
}