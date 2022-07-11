package com.bso.order.application.usecase.createorder.reversal.listener

import com.bso.order.application.usecase.createorder.reversal.dto.CreateOrderReversalAsyncSagasDto
import com.bso.order.application.usecase.createorder.service.OrderService
import com.bso.order.commons.json.JsonUtils
import com.bso.order.commons.log.logger
import com.bso.order.infra.aws.sqs.listener.AbstractSqsListener
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Component
class CreateOrderReversalListener(
    override val sqsClient: SqsClient,
    private val orderService: OrderService,
    private val jsonUtils: JsonUtils,
    @Value("\${app.order.create.create-reversal.listener.pool-size:1}")
    private val poolSize: Int
) : AbstractSqsListener(sqsClient = sqsClient, queue = "order-create-order-reversal") {
    private val logger: Logger by logger()

    override val executor: Executor = Executors.newFixedThreadPool(poolSize)

    override fun processMessage(message: Message) {
        with(message.toReversalMessage()) {
            logger.info("[EndToEndId = {}] [OrderId = {}] Message received to revert order creation", endToEndId, orderId)
            orderService.cancelOrder(orderId)
        }
    }

    private fun Message.toReversalMessage(): CreateOrderReversalAsyncSagasDto = jsonUtils.fromJson(this.body())
}
