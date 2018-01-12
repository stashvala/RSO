#!/bin/bash

# etcd

kubectl delete -f etcd-service.yaml
kubectl delete -f etcd.yaml

# user

kubectl delete -f postgres-user-service.yaml  
kubectl delete -f postgres-user.yaml
kubectl delete -f user-service.yaml
kubectl delete -f user-deployment.yaml

# video

kubectl delete -f postgres-video-service.yaml
kubectl delete -f postgres-video.yaml
kubectl delete -f video-service.yaml
kubectl delete -f video-deployment.yaml

# comment

kubectl delete -f postgres-comment-service.yaml
kubectl delete -f postgres-comment.yaml
kubectl delete -f comment-service.yaml
kubectl delete -f comment-deployment.yaml

# player

kubectl delete -f player-service.yaml
kubectl delete -f player-deployment.yaml

# rating

kubectl delete -f postgres-rating-service.yaml
kubectl delete -f postgres-rating.yaml
kubectl delete -f rating-service.yaml
kubectl delete -f rating-deployment.yaml

# recommendation

kubectl delete -f recommendation-service.yaml
kubectl delete -f recommendation-deployment.yaml

# subscription

kubectl delete -f postgres-subscription-service.yaml
kubectl delete -f postgres-subscription.yaml
kubectl delete -f subscription-service.yaml
kubectl delete -f subscription-deployment.yaml

# uploader

kubectl delete -f postgres-uploader-service.yaml
kubectl delete -f postgres-uploader.yaml
kubectl delete -f uploader-service.yaml
kubectl delete -f uploader-deployment.yaml

