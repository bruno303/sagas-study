package com.bso.order.infra.aws.sqs.listener

import com.bso.order.commons.log.logger
import org.slf4j.Logger
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

abstract class SqsListener(
    protected open val sqsClient: SqsClient,
    protected val queue: String
) {
    private val logger: Logger by logger()

    abstract fun processMessage(message: Message)

    fun listen() {
        while (true) {
            val receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queue)
                .maxNumberOfMessages(5)
                .build()

            sqsClient
                .receiveMessage(receiveMessageRequest)
                .messages()
                .let { messages ->
                    if (messages.isNotEmpty()) {
                        logger.info("Received {} messages from queue {}", messages.size, queue)
                    } else {
                        logger.trace("No messages received")
                    }
                    messages.forEach {
                        try {
                            processMessage(it)
                            this.deleteMessage(it)
                        } catch (ex: Exception) {
                            logger.error("Error when processing message from queue '$queue'.", ex)
                        }
                    }
                }
        }
    }

    private fun deleteMessage(message: Message) {
        val deleteMessageRequest = DeleteMessageRequest.builder()
            .queueUrl(queue)
            .receiptHandle(message.receiptHandle())
            .build()
        sqsClient.deleteMessage(deleteMessageRequest)
    }
}
