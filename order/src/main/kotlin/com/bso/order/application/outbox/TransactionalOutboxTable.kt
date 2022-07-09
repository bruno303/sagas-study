package com.bso.order.application.outbox

interface TransactionalOutboxTable {
    fun <T : Any> publish(message: T, queue: String)
}
