import awsSdkPkg from 'aws-sdk';
const { config, SQS } = awsSdkPkg;

config.update({ region: 'us-east-1' })

const sqs = new SQS();

export { sqs }
