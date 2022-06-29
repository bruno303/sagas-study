package com.bso.order.infra.proxy

import com.bso.order.application.proxy.NotificationServiceProxy
import com.bso.order.infra.feign.NotificationFeign
import com.bso.order.infra.feign.NotifyPayload
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FeignNotificationServiceProxy(
    private val notificationFeign: NotificationFeign
) : NotificationServiceProxy {
    override fun notify(orderId: UUID) {
        notificationFeign.notify(NotifyPayload(orderId = orderId))
    }
}
