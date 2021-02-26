package com.particle.util.loader.model;

public class Schema {
    private String key;
    private String alias;
    private String type;
    private int minItems = 0;
    private int maxItems = 2147483647;
    private int maxLength = 2147483647;
    private int minLength = -2147483648;
    private String pattern;
    private long minimum = -9223372036854775808L;
    private long maximum = 9223372036854775807L;

    public Schema() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinItems() {
        return this.minItems;
    }

    public void setMinItems(int minItems) {
        this.minItems = minItems;
    }

    public int getMaxItems() {
        return this.maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return this.minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public long getMinimum() {
        return this.minimum;
    }

    public void setMinimum(long minimum) {
        this.minimum = minimum;
    }

    public long getMaximum() {
        return this.maximum;
    }

    public void setMaximum(long maximum) {
        this.maximum = maximum;
    }

    public Schema copy() {
        Schema schema = new Schema();
        schema.key = this.key;
        schema.alias = this.alias;
        schema.type = this.type;
        schema.minItems = this.minItems;
        schema.maxItems = this.maxItems;
        schema.maxLength = this.maxLength;
        schema.minLength = this.minLength;
        schema.pattern = this.pattern;
        schema.minimum = this.minimum;
        schema.maximum = this.maximum;
        return schema;
    }
}
