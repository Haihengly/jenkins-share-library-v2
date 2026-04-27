def call(Map config) {

    def repo = config.dockerImage

        echo "🧹 Cleaning old images for ${repo}"

        sh """
            docker images ${repo} --format "{{.Repository}}:{{.Tag}}" \
            | sort -r \
            | tail -n +3 \
            | xargs -r docker rmi -f
        """

        echo "✅ Kept latest 2 images only"

}