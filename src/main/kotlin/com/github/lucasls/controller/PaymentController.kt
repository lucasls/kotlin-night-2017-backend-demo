package com.github.lucasls.controller

import com.github.lucasls.model.Payment
import com.github.lucasls.service.PaymentRequest
import com.github.lucasls.service.PaymentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created on 31/08/2017.
 * @author Lucas Laurindo dos Santos (lucas.santos@ifood.com.br)
 */
@RestController
class PaymentController @Autowired constructor(
    private val paymentService: PaymentService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/payments")
    fun create(@RequestBody paymentRequest: PaymentRequest): ResponseEntity<ResultRepresentation> {
        return try {
            val payment = paymentService.acceptPayment(paymentRequest)

            val entityBuider = when (payment.status) {
                Payment.Status.APPROVED -> ResponseEntity.status(200)
                else -> ResponseEntity.status(400)
            }

            entityBuider
                .body(ResultRepresentation(data = payment))

        } catch (e: Exception) {
            ResponseEntity
                .status(500)
                .body(ResultRepresentation(errorMessage = e.message))
        }
    }

    @GetMapping("/payments")
    fun list(): ResponseEntity<ListResultRepresentation> {
        return try {
            val payments = paymentService.findAll()

            ResponseEntity
                .status(200)
                .body(ListResultRepresentation(data = payments))

        } catch (e: Exception) {
            ResponseEntity
                .status(500)
                .body(ListResultRepresentation(errorMessage = e.message))
        }
    }


    data class ResultRepresentation(
        val data: Payment? = null,
        val errorMessage: String? = null
    )

    data class ListResultRepresentation(
        val data: List<Payment>? = null,
        val errorMessage: String? = null
    ) {

        val currentPage: Int? = data?.let { 0 }
        val itemsPerPage = 10
    }
}