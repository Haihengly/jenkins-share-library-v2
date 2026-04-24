def call(Map config) {
    def listStage = allstage(config)

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
        environment {
            DOCKER_IMAGE = "${config.dockerImage}"
            DOCKER_TAG = "${config.dockerTag}"
            DOCKER_CREDENTIALS = 'dockerhub-credentials-id'
            
            ANSIBLE_CREDENTIALS = 'ansible-ssh-key'
            
        }
        stages {
            // Use a single stage to wrap dynamic stages in scripted block
            stage('Run Dynamic Stages') {
                steps {
                    script {
                        for (s in listStage) {
                            echo "Running : ${s.name}"
                            // Wrap each dynamic stage in a 'stage' method (scripted)
                            stage(s.name) {
                                s.action()
                            }
                        }
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