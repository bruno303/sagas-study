package com.bso.order.infra.repository

import com.bso.order.application.outbox.OutboxTableItem
import com.bso.order.application.outbox.OutboxTableItemRepository
import com.bso.order.application.outbox.OutboxTableItemStatus
import com.bso.order.domain.entity.Order
import com.bso.order.domain.repository.OrderRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class InMemoryOutboxTableItemRepository : OutboxTableItemRepository {
    private val items: MutableList<OutboxTableItem> = mutableListOf()

    override fun findById(id: UUID): OutboxTableItem? = items.firstOrNull { it.id == id }

    override fun save(entity: OutboxTableItem): OutboxTableItem {
        findById(entity.id)?.let {
            items.set(items.indexOf(it), entity)
        } ?: items.add(entity)
        return entity
    }

    override fun findAll(): List<OutboxTableItem> = items.toList()

    override fun findByStatus(status: OutboxTableItemStatus): List<OutboxTableItem> {
        return items.filter { it.status == status }
    }
}
