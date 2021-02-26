package com.particle.api.proxy.economy;

import java.math.BigDecimal;

public interface Account {
    /**
     * 查询货币
     *
     * @return
     */
    BigDecimal getBalance();

    /**
     * 获取货币名称
     *
     * @return
     */
    Currency getCurrency();

    /**
     * 重置货币
     */
    void resetBalance();

    /**
     * 发放货币
     *
     * @param amount
     * @return
     */
    TransactionResult deposit(BigDecimal amount, String source, String reason);

    /**
     * 发放货币
     *
     * @param amount
     * @return
     */
    TransactionResult deposit(BigDecimal amount, String reason);

    /**
     * 扣除货币
     *
     * @param amount
     * @return
     */
    TransactionResult withdraw(BigDecimal amount, String reason);
}