def call(Map config) {

    echo "Deploying application using Ansible"
    withCredentials([sshUserPrivateKey(
        credentialsId: config.ansibleCredentialID,
        keyFileVariable: 'SSH_KEY',
        usernameVariable: 'SSH_USER'
    )]) {
        sh """
            cd /ansible
            ansible-playbook playbook/deploy.yml \
            -e "image_tag=${config.dockerTag}" \
            --private-key \$SSH_KEY \
            --user \$SSH_USER 
        """
    }
}