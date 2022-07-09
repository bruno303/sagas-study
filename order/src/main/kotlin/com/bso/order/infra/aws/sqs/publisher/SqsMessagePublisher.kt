package com.bso.order.infra.aws.sqs.publisher

import com.bso.order.application.messaging.MessagePublisher
import com.bso.order.application.messaging.MessagePublisherStrategy
import com.bso.order.application.messaging.Strategy
import com.bso.order.commons.json.JsonUtils
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Component
class SqsMessagePublisher(
    private val sqsClient: SqsClient,
    private val jsonUtils: JsonUtils
) : MessagePublisher {

    override val strategy: Strategy = Strategy.DIRECT_SEND

    override fun <T : Any> publish(message: T, queue: String) {
        val messageToSend: String = if (message is String) message else { jsonUtils.toJson(message) }

        val request = SendMessageRequest.builder()
            .queueUrl(queue)
            .messageBody(messageToSend)
            .build()

        sqsClient.sendMessage(request)
    }
}
