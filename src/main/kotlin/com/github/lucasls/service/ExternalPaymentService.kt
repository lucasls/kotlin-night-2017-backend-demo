package com.github.lucasls.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Created on 30/08/2017.
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */
@Component
class ExternalPaymentService {
    val log = LoggerFactory.getLogger(javaClass)

    fun authorize(paymentRequest: PaymentRequest): String? {
        return if (paymentRequest.cardInfo.cvv in sequenceOf("123","321")) {
            UUID.randomUUID().toString()
                .also { log.info("Payment authorized with TID '$it'") }
        } else {
            log.warn("Payment declined, wrong CVV")
            null
        }
    }
}