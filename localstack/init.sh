echo "########### Creating SQS ###########"
awslocal sqs create-queue --queue-name order-create-order-command &
awslocal sqs create-queue --queue-name order-create-order-response &

awslocal sqs create-queue --queue-name restaurant-create-ticket-command &
awslocal sqs create-queue --queue-name order-create-ticket-response &

awslocal sqs create-queue --queue-name payment-pay-command &
awslocal sqs create-queue --queue-name order-pay-response &

awslocal sqs create-queue --queue-name notification-notify-command &
awslocal sqs create-queue --queue-name order-notify-response &
wait

echo "########### Listing SQS ###########"
awslocal sqs list-queues
wait

echo "########### Environment ready to use ###########"
