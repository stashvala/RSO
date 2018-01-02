#!/bin/bash

# etcd

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
 
