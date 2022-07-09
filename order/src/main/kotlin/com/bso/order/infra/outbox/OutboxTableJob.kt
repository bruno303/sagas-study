package com.bso.order.infra.outbox

import com.bso.order.application.messaging.MessagePublisherStrategy
import com.bso.order.application.messaging.Strategy
import com.bso.order.application.outbox.OutboxTableItem
import com.bso.order.application.outbox.OutboxTableItemRepository
import com.bso.order.application.outbox.OutboxTableItemStatus
import com.bso.order.commons.log.logger
import org.slf4j.Logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class OutboxTableJob(
    private val outboxTableRepository: OutboxTableItemRepository,
    private val messagePublisherStrategy: MessagePublisherStrategy
) {
    private val logger: Logger by logger()

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    fun execute() {
        logger.debug("Executing OutboxTableJob")

        val pendingMessages: List<OutboxTableItem> = outboxTableRepository.findByStatus(OutboxTableItemStatus.PENDING)
        if (pendingMessages.isNotEmpty()) {
            logger.info("Found {} pending messages", pendingMessages.size)
        } else {
            logger.debug("No pending messages found")
        }

        pendingMessages.forEach { pendingMessage ->
            messagePublisherStrategy.publish(
                message = pendingMessage.message,
                queue = pendingMessage.queue,
                strategy = Strategy.DIRECT_SEND
            )

            pendingMessage.markAsSent().also { outboxTableRepository.save(it) }
        }

        logger.debug("Finishing OutboxTableJob")
    }
}
