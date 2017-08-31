package com.github.lucasls.service;

import java.math.BigDecimal;

/**
 * Created on 30/08/2017.
 *
 * @author Lucas Laurindo dos Santos (lls.lucas@gmail.com)
 */
public class AntiFraudRequest {
    private BigDecimal value;
    private Integer orderId;

    AntiFraudRequest(BigDecimal value, Integer orderId) {
        this.value = value;
        this.orderId = orderId;
    }

    public static AntiFraudRequestBuilder builder() {
        return new AntiFraudRequestBuilder();
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public static class AntiFraudRequestBuilder {
        private BigDecimal value;
        private Integer orderId;

        AntiFraudRequestBuilder() {
        }

        public AntiFraudRequest.AntiFraudRequestBuilder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        public AntiFraudRequest.AntiFraudRequestBuilder orderId(Integer orderId) {
            this.orderId = orderId;
            return this;
        }

        public AntiFraudRequest build() {
            return new AntiFraudRequest(value, orderId);
        }
    }
}
