package com.bso.order.application.usecase.createorder.sagas

import com.bso.order.application.messaging.MessagePublisherStrategy
import com.bso.order.application.messaging.Strategy
import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import com.bso.order.application.usecase.createorder.reversal.dto.CreateOrderReversalAsyncSagasDto
import com.bso.order.application.usecase.createorder.reversal.dto.CreateTicketReversalAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.CreateOrderResponseAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.CreateTicketResponseAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.NotifyResponseAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.receive.PayResponseAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.send.CreateOrderAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.send.CreateTicketAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.send.NotifyAsyncSagasDto
import com.bso.order.application.usecase.createorder.sagas.listener.dto.send.PayAsyncSagasDto
import com.bso.order.application.usecase.createorder.service.OrderService
import com.bso.order.commons.log.logger
import com.bso.order.domain.entity.Order
import org.slf4j.Logger
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional
class CreateOrderAsyncSagaManager(
    private val orderService: OrderService,
    private val messagePublisherStrategy: MessagePublisherStrategy
) {
    private val logger: Logger by logger()

    fun execute(createCmd: CreateOrderCommand) {
        val createOrderRequest = CreateOrderAsyncSagasDto(endToEndId = UUID.randomUUID(), createOrderCommand = createCmd)
        logger.info("[EndToEnd = {}] requesting order creation", createOrderRequest.endToEndId)
        createOrder(createOrderRequest)
    }

    fun processCreateOrderResponse(orderResponse: CreateOrderResponseAsyncSagasDto) {
        logger.info("[EndToEnd = {}] [OrderId = {}] processing response for order creation",
            orderResponse.endToEndId, orderResponse.order?.id)

        if (orderResponse.errors != null) {
            logger.warn("[EndToEnd = {}] [OrderId = {}] order creation failed",
                orderResponse.endToEndId, orderResponse.order?.id)
            return
        }

        val orderFound: Order = orderService.findByIdOrThrow(orderResponse.order!!.id)
        orderService.ticketPending(orderFound)
        logger.info("[EndToEnd = {}] [OrderId = {}] requesting ticket creation",
            orderResponse.endToEndId, orderResponse.order.id)
        createTicket(
            CreateTicketAsyncSagasDto(
                endToEndId = orderResponse.endToEndId,
                orderId = orderResponse.order.id
            )
        )
    }

    fun processCreateTicketResponse(ticketResponse: CreateTicketResponseAsyncSagasDto) {
        logger.info("[EndToEnd = {}] [OrderId = {}] processing response for ticket creation",
            ticketResponse.endToEndId, ticketResponse.orderId)
        val order: Order = orderService.findByIdOrThrow(ticketResponse.orderId)

        if (ticketResponse.errors != null) {
            logger.warn("[EndToEnd = {}] [OrderId = {}] ticket creation failed",
                ticketResponse.endToEndId, ticketResponse.orderId)
            undoOrderCreation(ticketResponse.endToEndId, ticketResponse.orderId)
            orderService.finishOrder(order, true, ticketResponse.errors.joinToString())
            return
        }

        orderService.paymentPending(order)
        logger.info("[EndToEnd = {}] [OrderId = {}] requesting payment",
            ticketResponse.endToEndId, ticketResponse.orderId)
        pay(
            PayAsyncSagasDto(
                endToEndId = ticketResponse.endToEndId,
                orderId = ticketResponse.orderId,
                ticketId = ticketResponse.ticket!!.id
            )
        )
    }

    fun processPaymentResponse(paymentResponse: PayResponseAsyncSagasDto) {
        logger.info("[EndToEnd = {}] [OrderId = {}] processing response for payment",
            paymentResponse.endToEndId, paymentResponse.orderId)
        val order: Order = orderService.findByIdOrThrow(paymentResponse.orderId)

        if (paymentResponse.errors != null) {
            logger.warn("[EndToEnd = {}] [OrderId = {}] payment failed",
                paymentResponse.endToEndId, paymentResponse.orderId)
            undoTicketCreation(paymentResponse.endToEndId, paymentResponse.orderId, paymentResponse.ticketId)
            undoOrderCreation(paymentResponse.endToEndId, paymentResponse.orderId)
            orderService.finishOrder(order, true, paymentResponse.errors.joinToString())
            return
        }

        orderService.notificationPending(order)
        logger.info("[EndToEnd = {}] [OrderId = {}] requesting notification",
            paymentResponse.endToEndId, paymentResponse.orderId)
        notify(
            NotifyAsyncSagasDto(
                endToEndId = paymentResponse.endToEndId,
                order = order,
                ticketId = paymentResponse.ticketId,
                paymentId = paymentResponse.payment!!.id
            )
        )
    }

    fun processNotificationResponse(notificationResponse: NotifyResponseAsyncSagasDto) {
        logger.info("[EndToEnd = {}] [OrderId = {}] processing notification response",
            notificationResponse.endToEndId, notificationResponse.order.id)
        val order: Order = orderService.findByIdOrThrow(notificationResponse.order.id)

        val orderUpdated: Order = if (notificationResponse.errors != null) {
            orderService.finishOrder(order = order, hasErrors = true, notificationResponse.errors.joinToString())
        } else {
            orderService.finishOrder(order = order, hasErrors = false)
        }

        logger.info(
            "[EndToEnd = {}] [OrderId = {}] order finished with status {}",
            notificationResponse.endToEndId, notificationResponse.order.id, orderUpdated.status
        )
    }

    private fun createOrder(data: CreateOrderAsyncSagasDto) {
        messagePublisherStrategy.publish(
            message = data,
            queue = "order-create-order-command",
            strategy = Strategy.OUTBOX_TABLE
        )
    }

    private fun createTicket(data: CreateTicketAsyncSagasDto) {
        messagePublisherStrategy.publish(
            message = data,
            queue = "restaurant-create-ticket-command",
            strategy = Strategy.OUTBOX_TABLE
        )
    }

    private fun pay(data: PayAsyncSagasDto) {
        messagePublisherStrategy.publish(
            message = data,
            queue = "payment-pay-command",
            strategy = Strategy.OUTBOX_TABLE
        )
    }

    private fun notify(data: NotifyAsyncSagasDto) {
        messagePublisherStrategy.publish(
            message = data,
            queue = "notification-notify-command",
            strategy = Strategy.OUTBOX_TABLE
        )
    }

    private fun undoOrderCreation(endToEndId: UUID, orderId: UUID) {
        messagePublisherStrategy.publish(
            message = CreateOrderReversalAsyncSagasDto(
                endToEndId = endToEndId,
                orderId = orderId
            ),
            queue = "order-create-order-reversal",
            strategy = Strategy.OUTBOX_TABLE
        )
    }

    private fun undoTicketCreation(endToEndId: UUID, orderId: UUID, ticketId: UUID) {
        messagePublisherStrategy.publish(
            message = CreateTicketReversalAsyncSagasDto(
                endToEndId = endToEndId,
                orderId = orderId,
                ticketId = ticketId
            ),
            queue = "restaurant-create-ticket-reversal",
            strategy = Strategy.OUTBOX_TABLE
        )
    }
}
