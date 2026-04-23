def call(Map config) {
    pipeline {
        agent {
            docker {
                image 'haihengly/docker-agent:1.2'
                label 'agent-01'
                args '''
                    -v /tmp/npm-cache:/home/jenkins/.npm
                    -v /var/run/docker.sock:/var/run/docker.sock
                    -v /ansible:/ansible
                    --memory=4g
                    --cpus=3
                    --group-add 988
                '''
            }
        }

        environment {
            DOCKER_IMAGE = "${config.dockerImage}"
            DOCKER_TAG = "${env.BUILD_NUMBER}"
            DOCKER_CREDENTIALS = 'dockerhub-credentials-id'
            ANSIBLE_CREDENTIALS = 'ansible-ssh-key'
        }

        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Build Docker Image') {
                steps {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }

            stage('Push to Docker Hub') {
                steps {
                    script {
                        docker.withRegistry('https://registry.hub.docker.com', "${DOCKER_CREDENTIALS}") {
                            docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        }
                    }
                }
            }

            stage('Deploy via Ansible') {
                steps {
                    withCredentials([sshUserPrivateKey(
                        credentialsId: "${ANSIBLE_CREDENTIALS}",
                        keyFileVariable: 'SSH_KEY',
                        usernameVariable: 'SSH_USER'
                    )]) {
                        sh """
                            cd /ansible
                            ansible-playbook playbook/deploy.yml \
                            --private-key \$SSH_KEY \
                            --user \$SSH_USER \
                            -e "image_tag=${DOCKER_TAG}"
                        """
                    }
                }
            }
        }

        post {
            success {
                echo "Deployment successful! Image: ${DOCKER_IMAGE}:${DOCKER_TAG}"
            }
            failure {
                echo "Pipeline failed!"
            }
        }
    }
}