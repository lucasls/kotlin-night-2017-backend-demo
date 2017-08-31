package com.github.lucasls.service

import com.github.lucasls.model.Payment
import com.github.lucasls.repository.PaymentRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

/**
 * Created on 30/08/2017.
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */
@Component
class PaymentService @Autowired constructor(
    private val antiFraudService: AntiFraudService,
    private val externalPaymentService: ExternalPaymentService,
    private val paymentRepository: PaymentRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun acceptPayment(request: PaymentRequest): Payment {

        val existingOrderIds = paymentRepository.findAll()
            .map { it.orderId }
            .toHashSet()

        if (request.orderId in existingOrderIds) {
            throw IllegalArgumentException("Payment for order ${request.orderId} already exists")
        }

        val status = when {
            !antiFraudCheck(request) -> Payment.Status.BLOCKED_ANTIFRAUD
            !externalServiceAuthorize(request) -> Payment.Status.DECLINED
            else -> Payment.Status.APPROVED
        }

        val payment = paymentRepository.create(Payment(
            createdAt = ZonedDateTime.now(),
            customerId = request.customerId,
            restaurantId = request.restaurantId,
            orderId = request.orderId,
            value = request.value,
            status = status
        ))

        log.info("Payment created with id #${payment.id} and status '$status'")

        return payment
    }

    fun findAll(): List<Payment> {
        return paymentRepository.findAll()
            .sortedByDescending { it.id }
    }


    private fun antiFraudCheck(request: PaymentRequest): Boolean {
        val antiFraudRequest = AntiFraudRequest.builder()
            .orderId(request.orderId)
            .value(request.value)
            .build()

        val antiFraudStatus = antiFraudService.check(antiFraudRequest, null)

        return when (antiFraudStatus) {
            AntiFraudService.Status.SAFE -> true
            AntiFraudService.Status.UNSAFE -> false
        }
    }

    private fun externalServiceAuthorize(request: PaymentRequest): Boolean {
        val tid = externalPaymentService.authorize(request)
        log.debug("Received tid '${tid ?: "none"}'")

        return tid != null
    }

}