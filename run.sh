#!/bin/bash

docker build -t drone .

docker run -p 8080:8080 --rm -it drone:latest