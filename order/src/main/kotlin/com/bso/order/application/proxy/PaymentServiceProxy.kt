package com.bso.order.application.proxy

import com.bso.order.application.dto.Payment
import java.util.UUID

interface PaymentServiceProxy {
    fun pay(orderId: UUID): Payment
}
