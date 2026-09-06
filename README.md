# devops_network_demo

docker build -t demo-frontend ./frontend
docker build -t demo-api ./api
docker build -t demo-db ./db

docker network create backend-net
 docker network create frontend-net

docker run -d --name db --network backend-net -e POSTGRES_DB=demo -e POSTGRES_USER=demo -e POSTGRES_PASSWORD=secret devops-network-demo-db:latest
docker run -d --name api --network backend-net -e DB_HOST=db -e DB_USER=demo -e DB_PASSWORD=secret devops-network-demo-api:latest
docker network connect frontend-net api
docker run -d --name frontend --network frontend-net -p 8080:80 devops-network-demo-frontend:latest
