package com.bso.order.application.proxy

import java.util.UUID

interface NotificationServiceProxy {
    fun notify(orderId: UUID)
}
