package com.bso.order.application.usecase.createorder.sagas.listener

import com.bso.order.application.usecase.createorder.sagas.CreateOrderAsyncSagaManager
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.CreateOrderResponseAsyncSagasDto
import com.bso.order.commons.json.JsonUtils
import com.bso.order.infra.aws.sqs.listener.SqsListener
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message

@Component
class CreateOrderResponseListener(
    override val sqsClient: SqsClient,
    private val jsonUtils: JsonUtils,
    private val createOrderAsyncSagaManager: CreateOrderAsyncSagaManager
) : SqsListener(sqsClient = sqsClient, queue = "order-create-order-response") {

    override fun processMessage(message: Message) {
        createOrderAsyncSagaManager.processCreateOrderResponse(message.toOrderResponse())
    }

    private fun Message.toOrderResponse(): CreateOrderResponseAsyncSagasDto = jsonUtils.fromJson(this.body())
}