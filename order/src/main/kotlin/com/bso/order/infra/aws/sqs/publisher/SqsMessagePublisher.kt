package com.bso.order.infra.aws.sqs.publisher

import com.bso.order.application.MessagePublisher
import com.bso.order.commons.json.JsonUtils
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Component
class SqsMessagePublisher(
    private val sqsClient: SqsClient,
    private val jsonUtils: JsonUtils
) : MessagePublisher {

    override fun <T : Any> publish(message: T, queue: String) {
        val request = SendMessageRequest.builder()
            .queueUrl(queue)
            .messageBody(jsonUtils.toJson(message))
            .build()

        sqsClient.sendMessage(request)
    }
}
