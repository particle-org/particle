package com.particle.model.item.enchantment;

public class Enchantment implements Cloneable {
    private IEnchantmentType type;

    private short level;


    public Enchantment(IEnchantmentType type, short level) {
        this.type = type;
        this.setLevel(level);
    }

    public IEnchantmentType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(short level) {
        if (level < this.type.getMinimumLevel()) {
            level = this.type.getMinimumLevel();
        } else if (level > this.type.getMaximumLevel()) {
            level = this.type.getMaximumLevel();
        }

        this.level = level;
    }

    @Override
    public Enchantment clone() throws CloneNotSupportedException {
        Enchantment clone = (Enchantment) super.clone();
        clone.level = this.level;
        clone.type = this.type;
        return clone;
    }
}
