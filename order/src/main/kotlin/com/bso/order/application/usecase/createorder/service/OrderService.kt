package com.bso.order.application.usecase.createorder.service

import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import com.bso.order.commons.log.logger
import com.bso.order.domain.entity.Order
import com.bso.order.domain.entity.OrderStatus
import com.bso.order.domain.repository.OrderRepository
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
    private val logger: Logger by logger()

    @Transactional
    fun create(createCmd: CreateOrderCommand): Order {
        val id: UUID = UUID.randomUUID()

        return Order(id = id, name = createCmd.name).let { order ->
            logger.info("Creating order [id: {}]", order.id)
            orderRepository.save(order)
        }
    }

    @Transactional
    fun cancelOrder(orderId: UUID) {
        logger.info("Canceling order [id: {}]", orderId)
        orderRepository.findById(id = orderId)?.let {
            changeStatus(it, OrderStatus.CANCELED).also { order: Order ->
                orderRepository.save(order)
            }
        }
    }

    fun findById(id: UUID): Order? = orderRepository.findById(id)

    fun ticketPending(order: Order): Order = changeStatus(order, OrderStatus.TICKET_PENDING)
    fun paymentPending(order: Order): Order = changeStatus(order, OrderStatus.PAYMENT_PENDING)
    fun notificationPending(order: Order): Order = changeStatus(order, OrderStatus.NOTIFICATION_PENDING)


    private fun changeStatus(order: Order, status: OrderStatus, error: String? = null): Order {
        logger.debug("[OrderId {}] Changing status from {} to {}", order.id, order.status, status)
        return orderRepository.save(order.copy(status = status, error = error))
    }

    fun finishOrder(order: Order, hasErrors: Boolean, error: String? = null): Order {
        val status: OrderStatus = if (hasErrors) OrderStatus.FAILURE else OrderStatus.SUCCESS
        return changeStatus(order = order, status = status, error = error)
    }

    fun findByIdOrThrow(id: UUID): Order {
        return orderRepository.findById(id) ?: throw IllegalStateException("Order with id $id not found!")
    }
}
