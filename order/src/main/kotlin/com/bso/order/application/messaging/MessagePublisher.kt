package com.bso.order.application.messaging

interface MessagePublisher {
    val strategy: Strategy
    fun <T : Any> publish(message: T, queue: String)
}

enum class Strategy {
    DIRECT_SEND,
    OUTBOX_TABLE,
}
