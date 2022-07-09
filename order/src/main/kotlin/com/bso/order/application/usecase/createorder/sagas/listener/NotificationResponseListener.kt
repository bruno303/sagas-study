package com.bso.order.application.usecase.createorder.sagas.listener

import com.bso.order.application.usecase.createorder.sagas.CreateOrderAsyncSagaManager
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.NotifyResponseAsyncSagasDto
import com.bso.order.commons.json.JsonUtils
import com.bso.order.infra.aws.sqs.listener.AbstractSqsListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Component
class NotificationResponseListener(
    override val sqsClient: SqsClient,
    private val jsonUtils: JsonUtils,
    private val createOrderAsyncSagaManager: CreateOrderAsyncSagaManager,
    @Value("\${app.order.create.notify-response.listener.pool-size:1}")
    private val poolSize: Int
) : AbstractSqsListener(sqsClient = sqsClient, queue = "order-notify-response") {
    override val executor: Executor = Executors.newFixedThreadPool(poolSize)

    override fun processMessage(message: Message) {
        createOrderAsyncSagaManager.processNotificationResponse(message.toNotificationResponse())
    }

    private fun Message.toNotificationResponse(): NotifyResponseAsyncSagasDto = jsonUtils.fromJson(this.body())
}
