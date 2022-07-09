package com.bso.order.application.usecase.createorder.sagas.listener

import com.bso.order.application.usecase.createorder.sagas.CreateOrderAsyncSagaManager
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.CreateOrderResponseAsyncSagasDto
import com.bso.order.commons.json.JsonUtils
import com.bso.order.infra.aws.sqs.listener.AbstractSqsListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Component
class CreateOrderResponseListener(
    override val sqsClient: SqsClient,
    private val jsonUtils: JsonUtils,
    private val createOrderAsyncSagaManager: CreateOrderAsyncSagaManager,
    @Value("\${app.order.create.order-response.listener.pool-size:1}")
    private val poolSize: Int
) : AbstractSqsListener(sqsClient = sqsClient, queue = "order-create-order-response") {

    override val executor: Executor = Executors.newFixedThreadPool(poolSize)

    override fun processMessage(message: Message) {
        createOrderAsyncSagaManager.processCreateOrderResponse(message.toOrderResponse())
    }

    private fun Message.toOrderResponse(): CreateOrderResponseAsyncSagasDto = jsonUtils.fromJson(this.body())
}
