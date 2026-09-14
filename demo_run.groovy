pipeline {
    agent {
        label 'test1' // Allocates the slave1 node first
    }
    stages {
        stage('Run Docker Images') {
            agent {
                docker {
                    image 'docker:latest'
                    reuseNode true
                    args '-u 1000:1000 --group-add 984 -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                withDockerRegistry([credentialsId: 'demo-run-key', url: '']) {
                    sh '''
                        # 1. Clean up existing containers and networks if they exist
                        docker rm -f docker-network-demo-frontend docker-network-demo-api docker-network-demo-db || true
                        docker network rm backend-net frontend-net || true

                        # 2. Create networks
                        docker network create backend-net
                        docker network create frontend-net
                        
                        # 3. Run Database Container
                        docker run -d --name docker-network-demo-db \
                            --network backend-net \
                            -e POSTGRES_DB=demo \
                            -e POSTGRES_USER=demo \
                            -e POSTGRES_PASSWORD=secret \
                            shaharcpp/docker-network-demo-db:latest
                        
                        # 4. Run API Container
                        docker run -d --name api \
                            --network backend-net \
                            -e DB_HOST=docker-network-demo-db \
                            -e DB_USER=demo \
                            -e DB_PASSWORD=secret \
                            shaharcpp/docker-network-demo-api:latest
                        
                        # 5. Connect API to Frontend network
                        docker network connect frontend-net api
                        
                        # 6. Run Frontend Container
                        docker run -d --name docker-network-demo-frontend \
                            --network frontend-net \
                            -p 8080:80 \
                            shaharcpp/docker-network-demo-frontend:latest
                    '''
                }
            }
        }
    } 
    
    post { 
        always {
            echo 'Pipeline has finished executing.'
        }
    }
}
