def call(Map config) {

    node('agent-01') {

        docker.image('haihengly/docker-agent:1.2')
            .inside("""
                -v /tmp/npm-cache:/home/jenkins/.npm
                -v /var/run/docker.sock:/var/run/docker.sock
                -v /ansible:/ansible
                --memory=4g
                --cpus=2
                --group-add 988
            """) {

            stage('Pipeline Execution') {

                config.stages.each { s ->

                    stage(s.name) {

                        echo "🚀 Running stage: ${s.name} (${s.type})"

                        stageExecutor(s.type, config)
                    }
                }
            }
        }
    }

    post {
        always {
            echo "📦 Pipeline finished"
        }
    }
}