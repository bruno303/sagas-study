package com.bso.order.application.usecase.createorder.sagas

import com.bso.order.application.dto.Payment
import com.bso.order.application.dto.Ticket
import com.bso.order.application.proxy.NotificationServiceProxy
import com.bso.order.application.proxy.OrderServiceProxy
import com.bso.order.application.proxy.PaymentServiceProxy
import com.bso.order.application.proxy.RestaurantServiceProxy
import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import com.bso.order.domain.entity.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import java.util.UUID

private const val LOG_PREFIX: String = "[Order {}]"

@Component
class CreateOrderSagaManager(
    private val orderServiceProxy: OrderServiceProxy,
    private val paymentServiceProxy: PaymentServiceProxy,
    private val restaurantServiceProxy: RestaurantServiceProxy,
    private val notificationServiceProxy: NotificationServiceProxy
) {
    private val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

    fun execute(createCmd: CreateOrderCommand) {
        val orderCreatedResult: SagaStepResult<Order> = createOrder(createCmd)

        if (!orderCreatedResult.successful) {
            throw Exception("Error when creating order in Order Service")
        }

        with(orderCreatedResult.result!!) {
            var result: SagaStepResult<*> = createTicket(orderId = id)
            if (!result.successful) {
                throw Exception("[Order $id] Error when creating ticket in Restaurant Service")
            }

            result = pay(orderId = id)
            if (!result.successful) {
                throw Exception("[Order $id] Error when paying in Payment Service")
            }

            result = notify(orderId = id)
            if (!result.successful) {
                throw Exception("[Order $id] Error when notifying in Notification Service")
            }
        }
    }

    private fun createOrder(createCmd: CreateOrderCommand): SagaStepResult<Order> {
        orderServiceProxy.create(createCmd = createCmd).also { order ->
            logger.info("$LOG_PREFIX Order created", order.id)
        }.also { return SagaStepResult.success(it) }
    }

    private fun pay(orderId: UUID): SagaStepResult<Payment> {
        paymentServiceProxy.pay(orderId = orderId).also {
            logger.info("$LOG_PREFIX Payment received. PaymentId: {}", orderId, it.id)
        }.let { return SagaStepResult.success(it) }
    }

    private fun createTicket(orderId: UUID): SagaStepResult<Ticket> {
        try {
            restaurantServiceProxy.createTicket(orderId = orderId).also {
                logger.info("$LOG_PREFIX Ticket created. TicketId: {}", orderId, it.id)
            }.let {
                return SagaStepResult.success(it)
            }
        } catch (ex: Exception) {
            cancelOrder(orderId)
            return SagaStepResult.error()
        }
    }

    private fun notify(orderId: UUID): SagaStepResult<Unit> {
        notificationServiceProxy.notify(orderId = orderId)
        logger.info("$LOG_PREFIX Notification sent", orderId)
        return SagaStepResult.success()
    }

    private fun cancelOrder(orderId: UUID) {
        orderServiceProxy.cancelOrder(orderId)
        logger.info("[Order $orderId] Order canceled")
    }
}
