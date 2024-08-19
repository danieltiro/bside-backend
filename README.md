aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 381491984567.dkr.ecr.us-east-1.amazonaws.com
docker build -t bside-backend -f ./Dockerfile .
docker tag bside-backend 381491984567.dkr.ecr.us-east-1.amazonaws.com/bside-backend:10
docker push 381491984567.dkr.ecr.us-east-1.amazonaws.com/bside-backend:10