#!/bin/bash
set -eux

# move into the app dir
cd /home/ec2-user/app

# build a fresh image
docker build -t blog .

# run the container in detached mode
docker run -d \
  --name blog \
  -p 80:8080 \
  -e PROJECT_NAME="${PROJECT_NAME}" \
  -e DB_NAME="${DB_NAME}" \
  -e DB_URL="${DB_URL}" \
  -e DB_USERNAME="${DB_USERNAME}" \
  -e DB_PASSWORD="${DB_PASSWORD}" \
  --restart unless-stopped \
  blog

echo "✅ blog is up and running"