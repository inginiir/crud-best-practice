docker build -t drone .

docker run -p 8080:8080 --name drone --rm -it drone:latest