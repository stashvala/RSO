#Cloud native streaming app

##Build
1. Build java with mvn clean package
2. Build docker image with docker build -t users-api:1.00 -f Dockerfile.txt .
3. Run docker-compose up