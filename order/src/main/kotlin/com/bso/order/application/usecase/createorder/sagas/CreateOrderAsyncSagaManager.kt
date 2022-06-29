package com.bso.order.application.usecase.createorder.sagas

import com.bso.order.application.MessagePublisher
import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
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
import com.bso.order.domain.entity.OrderStatus
import org.slf4j.Logger
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional
class CreateOrderAsyncSagaManager(
    private val orderService: OrderService,
    private val messagePublisher: MessagePublisher
) {
    private val logger: Logger by logger()

    fun execute(createCmd: CreateOrderCommand) {
        val createOrderRequest = CreateOrderAsyncSagasDto(endToEndId = UUID.randomUUID(), createOrderCommand = createCmd)
        logger.info("[EndToEnd = {}] requesting order creation", createOrderRequest.endToEndId)
        createOrder(createOrderRequest)
    }

    fun processCreateOrderResponse(orderResponse: CreateOrderResponseAsyncSagasDto) {
        logger.info("[EndToEnd = {}] [OrderId = {}] processing response for order creation",
            orderResponse.endToEndId, orderResponse.order.id)
        val order: Order = orderService.findByIdOrThrow(orderResponse.order.id)
        orderService.ticketPending(order)

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
        orderService.paymentPending(order)

        logger.info("[EndToEnd = {}] [OrderId = {}] requesting payment",
            ticketResponse.endToEndId, ticketResponse.orderId)
        pay(
            PayAsyncSagasDto(
                endToEndId = ticketResponse.endToEndId,
                orderId = ticketResponse.orderId,
                ticketId = ticketResponse.ticket.id
            )
        )
    }

    fun processPaymentResponse(paymentResponse: PayResponseAsyncSagasDto) {
        logger.info("[EndToEnd = {}] [OrderId = {}] processing response for payment",
            paymentResponse.endToEndId, paymentResponse.orderId)
        val order: Order = orderService.findByIdOrThrow(paymentResponse.orderId)
        orderService.notificationPending(order)

        logger.info("[EndToEnd = {}] [OrderId = {}] requesting notification",
            paymentResponse.endToEndId, paymentResponse.orderId)
        notify(
            NotifyAsyncSagasDto(
                endToEndId = paymentResponse.endToEndId,
                order = order,
                ticketId = paymentResponse.ticketId,
                paymentId = paymentResponse.payment.id
            )
        )
    }

    fun processNotificationResponse(notificationResponse: NotifyResponseAsyncSagasDto) {
        logger.info("[EndToEnd = {}] [OrderId = {}] requesting notification",
            notificationResponse.endToEndId, notificationResponse.order.id)
        val order: Order = orderService.findByIdOrThrow(notificationResponse.order.id)

        val hasErrors: Boolean = notificationResponse.errors != null && notificationResponse.errors.isNotEmpty()
        orderService.finishOrder(order = order, hasErrors = hasErrors).also {
            logger.info(
                "[EndToEnd = {}] [OrderId = {}] order finished with status {}",
                notificationResponse.endToEndId, notificationResponse.order.id, it.status
            )
        }
    }

    private fun createOrder(data: CreateOrderAsyncSagasDto) {
        messagePublisher.publish(message = data, queue = "order-create-order-command")
    }

    private fun createTicket(data: CreateTicketAsyncSagasDto) {
        messagePublisher.publish(message = data, queue = "restaurant-create-ticket-command")
    }

    private fun pay(data: PayAsyncSagasDto) {
        messagePublisher.publish(message = data, queue = "payment-pay-command")
    }

    private fun notify(data: NotifyAsyncSagasDto) {
        messagePublisher.publish(message = data, queue = "notification-notify-command")
    }
}
