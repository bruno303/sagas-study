#!/bin/bash

#QUEUE_URL="http://localstack:4566/000000000000/order-notify-response"
#echo "Waiting for SQS at address $QUEUE_URL, attempting every 5s"
#until $(curl --silent --fail $QUEUE_URL | grep "<QueueUrl>$QUEUE_URL</QueueUrl>" > /dev/null); do
#    printf '.'
#    sleep 5
#done
#echo ' Success: Reached SQS'

java -jar app.jar
