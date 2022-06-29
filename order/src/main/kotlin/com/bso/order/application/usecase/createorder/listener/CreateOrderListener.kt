package com.bso.order.application.usecase.createorder.listener

import com.bso.order.application.MessagePublisher
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.CreateOrderResponseAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.send.CreateOrderAsyncSagasDto
import com.bso.order.application.usecase.createorder.service.OrderService
import com.bso.order.commons.json.JsonUtils
import com.bso.order.commons.log.logger
import com.bso.order.infra.aws.sqs.listener.SqsListener
import org.slf4j.Logger
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message

@Component
class CreateOrderListener(
    override val sqsClient: SqsClient,
    private val orderService: OrderService,
    private val jsonUtils: JsonUtils,
    private val messagePublisher: MessagePublisher
) : SqsListener(sqsClient = sqsClient, queue = "order-create-order-command") {
    private val logger: Logger by logger()

    override fun processMessage(message: Message) {
        with(message.toCreateOrderAsyncSagasDto()) {
            logger.info("Message received to create order")
            orderService.create(this.createOrderCommand).let {
                messagePublisher.publish(
                    message = CreateOrderResponseAsyncSagasDto(endToEndId = this.endToEndId, order = it, errors = null),
                    queue = "order-create-order-response"
                )
            }
        }
    }

    private fun Message.toCreateOrderAsyncSagasDto(): CreateOrderAsyncSagasDto = jsonUtils.fromJson(this.body())
}
