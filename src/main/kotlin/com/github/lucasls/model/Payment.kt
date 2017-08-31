package com.github.lucasls.model

import java.math.BigDecimal
import java.time.ZonedDateTime

/**
 * Created on 17/08/2017.
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */
data class Payment(val id: Int = 0,
                   val createdAt: ZonedDateTime,
                   val value: BigDecimal,
                   val customerId: Int,
                   val restaurantId: Int,
                   val orderId: Int,
                   val status: Status) {

    enum class Status {
        APPROVED, BLOCKED_ANTIFRAUD, DECLINED
    }
}