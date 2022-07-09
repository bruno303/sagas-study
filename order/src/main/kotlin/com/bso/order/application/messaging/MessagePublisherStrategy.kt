package com.bso.order.application.messaging

import com.bso.order.commons.log.logger
import org.slf4j.Logger
import org.springframework.stereotype.Component

@Component
class MessagePublisherStrategy(
    strategies: List<MessagePublisher>
) {
    private val logger: Logger by logger()
    private val strategiesMap: MutableMap<Strategy, MessagePublisher> = mutableMapOf()

    init {
        strategies.forEach { strategiesMap[it.strategy] = it }
    }

    fun <T : Any> publish(message: T, queue: String, strategy: Strategy = Strategy.DIRECT_SEND) {
        val publisher: MessagePublisher? = strategiesMap[strategy]
        if (publisher != null) {
            logger.debug("Sending message to queue [{}] using strategy [{}]", queue, strategy)
            publisher.publish(message, queue)
            return
        }

        logger.warn("Message publisher for strategy [{}] not found! Message will not be sent!", strategy)
    }
}
