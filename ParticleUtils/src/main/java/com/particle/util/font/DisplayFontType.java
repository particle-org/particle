package com.particle.util.font;

public enum DisplayFontType {
    ScoreboardType("黑体", 12);

    /**
     * 字体类型
     */
    private String familyName;

    /**
     * 字体大小
     */
    private int size;

    DisplayFontType(String familyName, int size) {
        this.familyName = familyName;
        this.size = size;
    }

    public String getFamilyName() {
        return familyName;
    }

    public int getSize() {
        return size;
    }
}
