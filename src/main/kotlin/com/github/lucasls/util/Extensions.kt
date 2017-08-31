package com.github.lucasls.util

import com.github.lucasls.repository.PaymentRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.ResultSet
import java.sql.Statement
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

/**
 * Created on 27/08/2017.
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */

inline fun <reified T> JdbcTemplate.queryForObject(
    sql: String, vararg args: Any, crossinline mapper: (ResultSet) -> T): T? {

    return this.queryForObject(sql, args, { rs, rowNum ->
        mapper(rs)
    })
}

fun JdbcTemplate.insertReturningKey(sql: String, vararg params: Any?): Int? {
    val keyHolder = GeneratedKeyHolder()
    update({ conn ->
        conn.prepareStatement(PaymentRepository.Sql.INSERT, Statement.RETURN_GENERATED_KEYS)
            .apply {
                params.forEachIndexed({ i, o -> setObject(i+1, o) })
            }
    }, keyHolder)

    return keyHolder.key as Int?
}

fun ResultSet.getZonedDateTime(columnLabel: String): ZonedDateTime? {
    return this.getTimestamp(columnLabel)
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.let { ZonedDateTime.from(it) }
}

fun ZonedDateTime.toDate(): Date {
    return Date.from(toInstant())
}
