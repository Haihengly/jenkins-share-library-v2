// def call(Map config) {

//     echo "Building Docker image ${config.dockerImage}:${config.dockerTag}"
//     sh "docker build -t ${config.dockerImage}:${config.dockerTag} ."

//     echo "Pushing Docker image to registry"
//     docker.withRegistry('https://registry.hub.docker.com', config.dockerCredentials) {
//         docker.image("${config.dockerImage}:${config.dockerTag}").push()
//     }
    
// }


def call(Map config) {

    withCredentials([usernamePassword(
        credentialsId: config.dockerCredentialsId,
        usernameVariable: 'USER',
        passwordVariable: 'PASS'
    )]) {

        sh """
            echo \$PASS | docker login -u \$USER --password-stdin

            docker build -t ${config.dockerImage}:${config.dockerTag} .

            docker push ${config.dockerImage}:${config.dockerTag}
        """
    }
}