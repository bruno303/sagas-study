package com.bso.order.infra.outbox

import com.bso.order.application.outbox.OutboxTableItem
import com.bso.order.application.outbox.OutboxTableItemRepository
import com.bso.order.application.outbox.TransactionalOutboxTable
import com.bso.order.commons.json.JsonUtils
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DatabaseTransactionalOutboxTableImpl(
    private val outboxTableRepository: OutboxTableItemRepository,
    private val jsonUtils: JsonUtils
) : TransactionalOutboxTable {
    override fun <T : Any> publish(message: T, queue: String) {
        outboxTableRepository.save(OutboxTableItem(
            id = UUID.randomUUID(),
            message = jsonUtils.toJson(message),
            queue = queue
        ))
    }
}
