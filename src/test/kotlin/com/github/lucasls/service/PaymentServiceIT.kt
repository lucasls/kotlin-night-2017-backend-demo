package com.github.lucasls.service

import com.github.lucasls.Application
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal

/**
 * Created on 31/08/2017.
 *
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class))
class PaymentServiceIT {

    @Autowired
    private lateinit var paymentService: PaymentService

    @Test
    fun acceptPayment() {
        paymentService.acceptPayment(PaymentRequest(
            value = BigDecimal(200),
            orderId = 123,
            customerId = 123,
            restaurantId = 123,
            cardInfo = PaymentRequest.CardInfo(
                number = "41111111111111",
                cvv = "123",
                expiration = "08/19"
            )
        ))
    }

}