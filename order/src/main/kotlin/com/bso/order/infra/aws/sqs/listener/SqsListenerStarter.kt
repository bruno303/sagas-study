package com.bso.order.infra.aws.sqs.listener

import com.bso.order.commons.log.logger
import org.slf4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import java.util.concurrent.CompletableFuture

@Configuration
class SqsListenerStarter(
    private val listeners: List<SqsListener>
) : ApplicationListener<ApplicationReadyEvent> {
    private val logger: Logger by logger()

    private fun start() {
        listeners.forEach { listener ->
            logger.info("Starting ${listener::class.simpleName}...")
            CompletableFuture.runAsync({ listener.listen() }, listener.executor)
        }
        logger.info("All sqs listeners started")
    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        start()
    }

}
