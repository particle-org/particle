package com.particle.util.compress;

public enum CompressType {
    NONE(-1),
    DEFLATE(0),
    LZ4(10),
    LZ4JNI(11),
    SNAPPY(20);


    private int type;

    private CompressType(int type) {
        this.type = type;
    }

    public static CompressType valueOf(int value) {
        switch (value) {
            case 0:
                return DEFLATE;
            case 10:
                return LZ4;
            case 11:
                return LZ4JNI;
            case 20:
                return SNAPPY;
        }

        return NONE;
    }

    public int value() {
        return this.type;
    }
}