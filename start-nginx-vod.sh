#!/bin/bash

docker run --name nginx-vod  -p 127.0.0.1:8082:80 -v /Users/sai/docker/videos:/opt/static/videos -v /Users/sai/docker/config/nginx.conf:/usr/local/nginx/conf/nginx.conf -d nytimes/nginx-vod-module
