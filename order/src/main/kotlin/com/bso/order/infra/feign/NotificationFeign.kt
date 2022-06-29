package com.bso.order.infra.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import java.util.UUID

@FeignClient(name = "notificationFeign", url = "http://localhost:3002")
interface NotificationFeign {

    @PostMapping("notification", produces = ["application/json"], consumes = ["application/json"])
    fun notify(payload: NotifyPayload)

}

data class NotifyPayload(
    val orderId: UUID
)
