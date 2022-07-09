package com.bso.order.domain.repository

import com.bso.order.domain.entity.Order
import java.util.UUID

interface OrderRepository : BaseRepository<UUID, Order>
