package com.bso.order.application

interface MessagePublisher {
    fun <T : Any> publish(message: T, queue: String)
}
