import { sqs } from "../../libs/aws/sqs.js";
import { Consumer } from 'sqs-consumer'
import { CreateTicketReversalHandler } from './createTicketReversalHandler.js'

const localstackUrl = process.env["AWS_ENDPOINT"]

const consumer = Consumer.create({
  region: 'us-east-1',
  attributeNames: ['SentTimestamp'],
  messageAttributeNames: ['All'],
  queueUrl: `${localstackUrl}/000000000000/restaurant-create-ticket-reversal`,
  handleMessage: new CreateTicketReversalHandler().handle,
  sqs: sqs
});

consumer.on('error', (err) => {
  console.error(err.message);
});

consumer.on('processing_error', (err) => {
  console.error(err.message);
});

export { consumer }
