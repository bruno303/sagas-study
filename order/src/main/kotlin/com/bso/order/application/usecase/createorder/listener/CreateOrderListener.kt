package com.bso.order.application.usecase.createorder.listener

import com.bso.order.application.messaging.MessagePublisherStrategy
import com.bso.order.application.messaging.Strategy
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.CreateOrderResponseAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.send.CreateOrderAsyncSagasDto
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
class CreateOrderListener(
    override val sqsClient: SqsClient,
    private val orderService: OrderService,
    private val jsonUtils: JsonUtils,
    private val messagePublisherStrategy: MessagePublisherStrategy,
    @Value("\${app.order.create.create-command.listener.pool-size:1}")
    private val poolSize: Int
) : AbstractSqsListener(sqsClient = sqsClient, queue = "order-create-order-command") {
    private val logger: Logger by logger()
    override val executor: Executor = Executors.newFixedThreadPool(poolSize)

    override fun processMessage(message: Message) {
        with(message.toCreateOrderAsyncSagasDto()) {
            logger.info("[EndToEndId = {}] Message received to create order", endToEndId)
            orderService.create(this.createOrderCommand).also {
                messagePublisherStrategy.publish(
                    message = CreateOrderResponseAsyncSagasDto(endToEndId = this.endToEndId, order = it, errors = null),
                    queue = "order-create-order-response",
                    strategy = Strategy.OUTBOX_TABLE
                )
            }
        }
    }

    private fun Message.toCreateOrderAsyncSagasDto(): CreateOrderAsyncSagasDto = jsonUtils.fromJson(this.body())
}
