import { sqs } from "../libs/aws/sqs.js";

const localstackUrl = process.env["AWS_ENDPOINT"]
const QUEUE_DESTINATION = `${localstackUrl}/000000000000/order-create-ticket-response`

function publish(json) {
  const params = {
    DelaySeconds: 0,
    MessageBody: json,
    QueueUrl: QUEUE_DESTINATION
  }

  sqs.sendMessage(params, function(err, data) {
    if (err) {
      console.error("Erro sending message", err);
    } else {
      console.log("Message sent with success", data.MessageId);
    }
  })
}

export { publish }
