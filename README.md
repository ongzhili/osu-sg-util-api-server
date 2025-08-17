# Build and run

## Build the jar file locally

-> `mvn clean package`

## Build docker image locally

```
docker build -t <name> .
```

## Push image to docker hub

```shell
docker login
docker tag <name> username/<name>:latest
docker push username/<name>:latest
```

## EC2 Side

Assuming Docker is installed.

Add user to docker group (no sudo needed)

```
sudo usermod -aG docker ubuntu
```

Run the container

```
docker pull your-dockerhub-username/my-spring-app:latest
docker run -d --name spring-app -p 8080:8080 your-dockerhub-username/my-spring-app:latest
```

Check if it is running

```
docker ps
(or) curl localhost:8080/hello
```

nginx as reverse proxy (so that external parties can connect)

## Running with Docker Compose

Make sure:

1. nginx is not running (`sudo systelctl stop nginx`)
2. container is not running (`docker ps`)

Create a `docker-compose.yml` file:

```yaml
version: "3"
services:
  app:
    image: your-dockerhub-username/spring-rest-api:latest
    container_name: spring-app
    restart: always
    ports:
      - "8080:8080"

  nginx:
    image: nginx:latest
    container_name: nginx-proxy
    restart: always
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/conf.d/default.conf
```

Create an nginx.conf file in the same folder:

```
server {
    listen 80;

    location / {
        proxy_pass http://app:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

To start everything:

`docker-compose up -d`

### To check if it works:

Getting the IPv4 Address:

- Go to EC2 → Instances
- Find your instance → look at Public IPv4 address (or Public DNS if you prefer).

Access the API:

- `<public_ip>/hello`

### Teardown

`docker-compose stop`
