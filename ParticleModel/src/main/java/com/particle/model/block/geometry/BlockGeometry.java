package com.particle.model.block.geometry;

public enum BlockGeometry {
    SOLID(false),
    MODEL(true),
    EMPTY(true);

    private boolean canPassThrow;

    BlockGeometry(boolean canPassThrow) {
        this.canPassThrow = canPassThrow;
    }

    public boolean canPassThrow() {
        return canPassThrow;
    }
}
