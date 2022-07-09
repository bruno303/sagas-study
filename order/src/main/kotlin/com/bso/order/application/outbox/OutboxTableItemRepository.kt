package com.bso.order.application.outbox

import com.bso.order.domain.repository.BaseRepository
import java.util.UUID

interface OutboxTableItemRepository : BaseRepository<UUID, OutboxTableItem> {
    fun findByStatus(status: OutboxTableItemStatus): List<OutboxTableItem>
}
