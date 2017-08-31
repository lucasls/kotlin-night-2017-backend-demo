package com.github.lucasls.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;

/**
 * Created on 30/08/2017.
 *
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */
@Component
public class AntiFraudService {
    private static Logger log = LoggerFactory.getLogger(AntiFraudService.class);

    @Nonnull
    public Status check(@Nonnull AntiFraudRequest request, @Nullable String reqId) {
        if (reqId != null) {
            log.info("Request Id: {}", reqId);
        }

        log.info("Checking anti fraud for order {}" , request.getOrderId());

        BigDecimal maxValue = BigDecimal.valueOf(150);

        if (request.getValue().compareTo(maxValue) > 0) {
            log.warn("Order #{} blocked", request.getOrderId());
            return Status.UNSAFE;
        }

        return Status.SAFE;
    }

    public enum Status {
        SAFE, UNSAFE
    }
}
