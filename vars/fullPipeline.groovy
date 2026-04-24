def call(Map config) {

    pipeline {
        agent {
            dockerAgent()
        }

        stages {
            stage('Pipeline') {
                steps {
                    script {

                        config.stages.each { s ->

                            stage(s.name) {
                                stageExecutor(s.type, config)
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