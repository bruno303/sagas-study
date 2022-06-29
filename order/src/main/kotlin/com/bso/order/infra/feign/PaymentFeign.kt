package com.bso.order.infra.feign

import com.bso.order.application.dto.Payment
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import java.util.UUID

@FeignClient(name = "paymentFeign", url = "http://localhost:3001")
interface PaymentFeign {

    @PostMapping("payment", produces = ["application/json"], consumes = ["application/json"])
    fun pay(payload: PayPayload): Payment

}

data class PayPayload(
    val orderId: UUID
)
