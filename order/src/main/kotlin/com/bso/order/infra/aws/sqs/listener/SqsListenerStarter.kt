package com.bso.order.infra.aws.sqs.listener

import com.bso.order.application.usecase.createorder.listener.CreateOrderListener
import com.bso.order.application.usecase.createorder.sagas.listener.CreateOrderResponseListener
import com.bso.order.application.usecase.createorder.sagas.listener.CreateTicketResponseListener
import com.bso.order.application.usecase.createorder.sagas.listener.NotificationResponseListener
import com.bso.order.application.usecase.createorder.sagas.listener.PaymentResponseListener
import com.bso.order.commons.log.logger
import com.bso.order.infra.threadpool.SqsListenerThreadPoolManager
import org.slf4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import java.util.concurrent.CompletableFuture

@Configuration
class SqsListenerStarter(
    private val createOrderListener: CreateOrderListener,
    private val sqsListenerThreadPoolManager: SqsListenerThreadPoolManager,
    private val createOrderResponseListener: CreateOrderResponseListener,
    private val createTicketResponseListener: CreateTicketResponseListener,
    private val paymentResponseListener: PaymentResponseListener,
    private val notificationResponseListener: NotificationResponseListener
) : ApplicationListener<ApplicationReadyEvent> {
    private val logger: Logger by logger()

    private fun start() {
        logger.info("Starting CreateOrderListener...")
        CompletableFuture.runAsync({ createOrderListener.listen() }, sqsListenerThreadPoolManager.createOrderListenerExecutor)
        logger.info("Starting CreateOrderResponseListener...")
        CompletableFuture.runAsync({ createOrderResponseListener.listen() }, sqsListenerThreadPoolManager.createOrderResponseListenerExecutor)
        logger.info("Starting CreateTicketResponseListener...")
        CompletableFuture.runAsync({ createTicketResponseListener.listen() }, sqsListenerThreadPoolManager.createTicketResponseListenerExecutor)
        logger.info("Starting PaymentResponseListener...")
        CompletableFuture.runAsync({ paymentResponseListener.listen() }, sqsListenerThreadPoolManager.paymentResponseListenerExecutor)
        logger.info("Starting NotificationResponseListener...")
        CompletableFuture.runAsync({ notificationResponseListener.listen() }, sqsListenerThreadPoolManager.notificationResponseListenerExecutor)
        logger.info("All sqs listeners started")
    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        start()
    }

}
