def call(Map config) {
    // def listStage = allstage(config)

    pipeline {
        agent {
            docker  {
                image 'haihengly/docker-agent:1.2'
                label 'agent-01'
                args '''
                    -v /tmp/npm-cache:/home/jenkins/.npm
                    -v /var/run/docker.sock:/var/run/docker.sock
                    -v /ansible:/ansible
                    --memory=4g
                    --cpus=2
                    --group-add 988
                '''
            }
        }
        stages {
            stage('Checkout') {
                steps {
                    echo "Checking out branch ${config.branch}"
                    checkoutStage(config)
                }
            }

            if (config.build) {
                stage('Build') {
                    steps {
                        echo "Building version ${config.version}"
                    build(config)
                    }
                }
            }

            if (config.deploy) {
                stage('Deploy') {
                    steps {
                        echo "Deploying version ${config.version}"
                        deploy(config)
                    }
                }
            }
        }
        post {
            success { 
                echo "Pipeline succeeded!"
            }
            failure { 
                echo "Pipeline failed!"
            }
            unstable { 
                echo "Pipeline is unstable!"
            }
        }
    }
}