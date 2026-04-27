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
        """ws
    }
}