#!/bin/bash

# etcd

kubectl create -f etcd-service.yaml
kubectl create -f etcd.yaml  

# user

kubectl create -f postgres-user-service.yaml  
kubectl create -f postgres-user.yaml
kubectl create -f user-service.yaml
kubectl create -f user-deployment.yaml

# video

kubectl create -f postgres-video-service.yaml
kubectl create -f postgres-video.yaml
kubectl create -f video-service.yaml
kubectl create -f video-deployment.yaml

# comment

kubectl create -f postgres-comment-service.yaml  
kubectl create -f postgres-comment.yaml
kubectl create -f comment-service.yaml
kubectl create -f comment-deployment.yaml

# player

kubectl create -f player-service.yaml
kubectl create -f player-deployment.yaml

# rating

kubectl create -f postgres-rating-service.yaml  
kubectl create -f postgres-rating.yaml
kubectl create -f rating-service.yaml
kubectl create -f rating-deployment.yaml

# recommendation

kubectl create -f recommendation-service.yaml
kubectl create -f recommendation-deployment.yaml

# subscription

kubectl create -f postgres-subscription-service.yaml
kubectl create -f postgres-subscription.yaml
kubectl create -f subscription-service.yaml
kubectl create -f subscription-deployment.yaml

# uploader

kubectl create -f postgres-uploader-service.yaml
kubectl create -f postgres-uploader.yaml
kubectl create -f uploader-service.yaml
kubectl create -f uploader-deployment.yaml

# info

kubectl get pods
kubectl get deployment
kubectl get service
