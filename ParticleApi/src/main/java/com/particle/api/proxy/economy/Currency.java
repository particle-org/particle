package com.particle.api.proxy.economy;

import java.math.BigDecimal;

public interface Currency {

    /**
     * 货币名称
     *
     * @return
     */
    String getDisplayName();

    /**
     * 货币标识符
     *
     * @return
     */
    String getIdentifiedID();

    /**
     * 格式化货币
     *
     * @param amount
     * @param numFractionDigits
     * @return
     */
    String format(BigDecimal amount, int numFractionDigits);

    /**
     * 小数点保留位数
     *
     * @return
     */
    int getFractionDigits();

    default String format(BigDecimal amount) {
        return this.format(amount, this.getFractionDigits());
    }
}