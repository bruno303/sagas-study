package com.bso.order.infra.proxy

import com.bso.order.application.dto.Payment
import com.bso.order.application.proxy.PaymentServiceProxy
import com.bso.order.infra.feign.PayPayload
import com.bso.order.infra.feign.PaymentFeign
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FeignPaymentServiceProxy(
    private val paymentFeign: PaymentFeign
) : PaymentServiceProxy {
    override fun pay(orderId: UUID): Payment {
        return paymentFeign.pay(PayPayload(orderId = orderId))
    }
}
