package com.particle.model.permission;

public enum Tristate {
    TRUE(true) {
        @Override
        public Tristate and(Tristate other) {
            return other == TRUE || other == UNDEFINED ? TRUE : FALSE;
        }

        @Override
        public Tristate or(Tristate other) {
            return TRUE;
        }
    },
    FALSE(false) {
        @Override
        public Tristate and(Tristate other) {
            return FALSE;
        }

        @Override
        public Tristate or(Tristate other) {
            return other == TRUE ? TRUE : FALSE;
        }
    },
    UNDEFINED(false) {
        @Override
        public Tristate and(Tristate other) {
            return other;
        }

        @Override
        public Tristate or(Tristate other) {
            return other;
        }
    };

    private final boolean val;

    Tristate(boolean val) {
        this.val = val;
    }

    /**
     * Return the appropriate tristate for a given boolean value.
     *
     * @param val The boolean value
     * @return The appropriate tristate
     */
    public static Tristate fromBoolean(boolean val) {
        return val ? TRUE : FALSE;
    }

    /**
     * ANDs this tristate with another tristate.
     *
     * @param other The tristate to AND with
     * @return The result
     */
    public abstract Tristate and(Tristate other);

    /**
     * ORs this tristate with another tristate.
     *
     * @param other The tristate to OR with
     * @return The result
     */
    public abstract Tristate or(Tristate other);

    /**
     * Returns the boolean representation of this tristate.
     *
     * @return The boolean tristate representation
     */
    public boolean asBoolean() {
        return this.val;
    }
}
