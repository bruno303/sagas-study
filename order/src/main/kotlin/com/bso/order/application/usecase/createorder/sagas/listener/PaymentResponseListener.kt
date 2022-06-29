package com.bso.order.application.usecase.createorder.sagas.listener

import com.bso.order.application.usecase.createorder.sagas.CreateOrderAsyncSagaManager
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.CreateTicketResponseAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.PayResponseAsyncSagasDto
import com.bso.order.commons.json.JsonUtils
import com.bso.order.infra.aws.sqs.listener.SqsListener
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message

@Component
class PaymentResponseListener(
    override val sqsClient: SqsClient,
    private val jsonUtils: JsonUtils,
    private val createOrderAsyncSagaManager: CreateOrderAsyncSagaManager
) : SqsListener(sqsClient = sqsClient, queue = "order-pay-response") {

    override fun processMessage(message: Message) {
        createOrderAsyncSagaManager.processPaymentResponse(message.toPaymentResponse())
    }

    private fun Message.toPaymentResponse(): PayResponseAsyncSagasDto = jsonUtils.fromJson(this.body())
}
