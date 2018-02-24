#!/usr/bin/env bash
curl -H "Content-Type: application/json" -X POST -d \
'{"text":"Message","address":"1"}' \
http://localhost:8080/sendMessage

curl http://localhost:8080/getHistory

kubectl apply -f kubernetes-chat.yaml