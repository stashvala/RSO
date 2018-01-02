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

# info

kubectl get pods
kubectl get deployment
kubectl get service
