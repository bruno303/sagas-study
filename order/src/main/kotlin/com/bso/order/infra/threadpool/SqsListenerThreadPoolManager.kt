package com.bso.order.infra.threadpool

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Component
class SqsListenerThreadPoolManager(
    @Value("\${app.order.create.listener.pool-size:1}")
    private val createOrderListenerPoolSize: Int,
    @Value("\${app.order.create.listener.pool-size:1}")
    private val createOrderResponseListenerPoolSize: Int,
    @Value("\${app.order.create.listener.pool-size:1}")
    private val createTicketResponseListenerPoolSize: Int,
    @Value("\${app.order.create.listener.pool-size:1}")
    private val paymentResponseListenerPoolSize: Int,
    @Value("\${app.order.create.listener.pool-size:1}")
    private val notificationResponseListenerPoolSize: Int
) {
    final val notificationResponseListenerExecutor: Executor
    final val createOrderListenerExecutor: Executor
    final val createOrderResponseListenerExecutor: Executor
    final val createTicketResponseListenerExecutor: Executor
    final val paymentResponseListenerExecutor: Executor

    init {
        createOrderListenerExecutor = Executors.newFixedThreadPool(createOrderListenerPoolSize)
        createOrderResponseListenerExecutor = Executors.newFixedThreadPool(createOrderResponseListenerPoolSize)
        createTicketResponseListenerExecutor = Executors.newFixedThreadPool(createTicketResponseListenerPoolSize)
        paymentResponseListenerExecutor = Executors.newFixedThreadPool(paymentResponseListenerPoolSize)
        notificationResponseListenerExecutor = Executors.newFixedThreadPool(notificationResponseListenerPoolSize)
    }
}
