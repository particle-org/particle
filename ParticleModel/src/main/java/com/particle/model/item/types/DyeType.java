package com.particle.model.item.types;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public enum DyeType {
    inkSac(0, 0x1D1D21),
    roseRed(1, 0xB02E26),
    cocoa(2, 0x5E7C16),
    lapis(3, 0x835432),
    purple(4, 0x3C44AA),
    cyan(5, 0x8932B8),
    lightGray(6, 0x169C9C),
    gray(7, 0x9D9D97),
    pink(8, 0x474F52),
    lime(9, 0xF38BAA),
    yellow(10, 0x80C71F),
    lightBlue(12, 0x3AB3DA),
    megenta(13, 0xC74EBD),
    oriange(14, 0xF9801D),
    bone(15, 0xF9FFFE),
    black(16, 0x1D1D21),
    brown(17, 0x835432),
    blue(18, 0x3C44AA),
    white(19, 0xF9FFFE);

    private int meta;

    private int colorHex;

    private Color color;

    private static final Map<Integer, DyeType> allDyes = new HashMap<>();

    static {
        for (DyeType dyeType : DyeType.values()) {
            allDyes.put(dyeType.getMeta(), dyeType);
        }
    }

    /**
     * 根据meta值获取数据
     *
     * @param meta
     * @return
     */
    public static DyeType from(int meta) {
        return allDyes.get(meta);
    }

    DyeType(int meta, int colorHex) {
        this.meta = meta;
        this.colorHex = colorHex;
        this.color = new Color(colorHex);
    }

    public int getMeta() {
        return meta;
    }

    public int getColorHex() {
        return colorHex;
    }

    public Color getColor() {
        return color;
    }
}
