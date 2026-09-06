pipeline {
    agent { label 'test' }
    environment {
        REGISTRY_CREDS = "github-key"
        SERVICE1 = "frontend"
        SERVICE2 = "api"
        SERVICE3 = "db"
        APP_NAME =  "docker-network-demo"
        REGISTRY_URL   = 'https://docker.io'
        USERNAME = credentials('DOCKERHUB_USER')
    }
    stages {
        stage('Clean Workspace') {
            steps {
                echo "---START CLEAN---"
                cleanWs()
                echo "---FINISH CLEAN---"
            }
        }
        stage('Checkout repository') {
            steps {
                // Pulls code using a stored Jenkins credential ID
                git branch: 'master', 
                    credentialsId: 'git-ssh-key', 
                    url: 'git@github.com:shaklevi/devops_network_demo.git'
                }
        }
        stage('build demo-frontend image') {
            steps {
                script {
                        def imageName ="${USERNAME}/${APP_NAME}-${SERVICE1}"
                        def customImage = docker.build("${imageName}:${BUILD_NUMBER}", "./frontend/")
                         docker.withRegistry(REGISTRY_URL, REGISTRY_CREDS) {
                            customImage.push()
                            customImage.push('latest') // Optional: Also tag and push as latest
                        }
                }
            }
        }   
        stage('build demo-api image') {
            steps {
                script {
                    def imageName = "${USERNAME}/${APP_NAME}-${SERVICE2}"
                    def customImage = docker.build("${imageName}:${BUILD_NUMBER}", "./api/")
                    docker.withRegistry(REGISTRY_URL, REGISTRY_CREDS) {
                        customImage.push()
                        customImage.push('latest') // Optional: Also tag and push as latest
                    }
                }
            }
        }
        stage('build demo-db image') {
            steps {
                script {
                        def imageName = "${USERNAME}/${APP_NAME}-${SERVICE3}"
                        def customImage = docker.build("${imageName}:${BUILD_NUMBER}", "./db/")
                        docker.withRegistry(REGISTRY_URL, REGISTRY_CREDS) {
                            customImage.push()
                            customImage.push('latest') // Optional: Also tag and push as latest
                        }
                }
            }
        }
    }
}
