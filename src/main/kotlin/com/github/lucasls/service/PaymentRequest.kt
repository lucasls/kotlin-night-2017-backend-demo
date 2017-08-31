package com.github.lucasls.service

import java.math.BigDecimal

data class PaymentRequest(val value: BigDecimal,
                          val customerId: Int,
                          val restaurantId: Int,
                          val orderId: Int,
                          val cardInfo: CardInfo) {

    data class CardInfo(val number: String,
                        val cvv: String,
                        val expiration: String)
}