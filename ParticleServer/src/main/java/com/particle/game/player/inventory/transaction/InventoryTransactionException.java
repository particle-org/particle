package com.particle.game.player.inventory.transaction;

import com.particle.model.exception.BaseException;

public class InventoryTransactionException extends BaseException {

    public InventoryTransactionException() {
        super();
    }

    public InventoryTransactionException(int code, String errorMsg) {
        super(code, errorMsg);
    }

    public InventoryTransactionException(int code, String errorMsg, Throwable throwable) {
        super(code, errorMsg, throwable);
    }
}
