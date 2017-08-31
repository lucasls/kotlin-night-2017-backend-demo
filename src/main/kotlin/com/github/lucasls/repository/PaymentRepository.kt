package com.github.lucasls.repository

import com.github.lucasls.model.Payment
import com.github.lucasls.util.getZonedDateTime
import com.github.lucasls.util.insertReturningKey
import com.github.lucasls.util.toDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Created on 27/08/2017.
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */
@Repository
class PaymentRepository @Autowired constructor(
    private val jdbcTemplate: JdbcTemplate) {

    fun findAll(): List<Payment> {
        val sql = Sql.SELECT_ALL

        return jdbcTemplate.query(sql) { resultSet, _ ->
            Payment(
                id = resultSet.getInt("id"),
                createdAt = resultSet.getZonedDateTime("created_at")!!,
                customerId = resultSet.getInt("customer_id"),
                orderId = resultSet.getInt("order_id"),
                restaurantId = resultSet.getInt("restaurant_id"),
                value = resultSet.getBigDecimal("value"),
                status = Payment.Status.valueOf(resultSet.getString("status"))
            )
        }
    }

    fun create(payment: Payment): Payment {
        val generatedKey = jdbcTemplate.insertReturningKey(
            Sql.INSERT,
            payment.createdAt.toDate(),
            payment.value,
            payment.customerId,
            payment.restaurantId,
            payment.orderId,
            payment.status.name
        )

        return payment.copy(
            id = generatedKey ?: throw IllegalStateException("generatedKey returned null")
        )
    }

    object Sql {
        val INSERT = """
            insert into
                payment
            values (
                null, ?, ?, ?, ?, ?, ?
            )
            """.trimIndent()

        val SELECT_ALL = """
            select
                id,
                created_at,
                value,
                customer_id,
                restaurant_id,
                order_id,
                status
            from
                payment
            """.trimIndent()
    }
}
