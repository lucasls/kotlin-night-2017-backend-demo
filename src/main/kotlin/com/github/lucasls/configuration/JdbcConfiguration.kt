package com.github.lucasls.configuration

import com.github.lucasls.model.Payment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal
import java.util.Date
import java.util.Random
import javax.annotation.PostConstruct

/**
 * Created on 27/08/2017.
 * @author Lucas Laurindo dos Santos (lucas.santos@ifood.com.br)
 */
@Configuration
class JdbcConfiguration @Autowired constructor(
    private val jdbcTemplate: JdbcTemplate) {

    @PostConstruct
    fun setUp() {
        jdbcTemplate.execute(
            "create table payment (id int identity primary key, created_at datetime, value decimal, customer_id int, restaurant_id int, order_id int, status varchar(20))"
        )

        val arguments = (1..3)
            .map {
                arrayOf(Date(),
                    BigDecimal.valueOf(Random().nextInt(10001).toLong(), 2),
                    it,
                    Payment.Status.values()
                        .map { it.name }
                        .let { it[Random().nextInt(it.size)] }
                )
            }
            .toList()

        jdbcTemplate.batchUpdate("insert into payment values (null, ?, ?, 2222, 3333, ?, ?)", arguments)
    }
}