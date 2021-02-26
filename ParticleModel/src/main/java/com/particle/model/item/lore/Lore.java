package com.particle.model.item.lore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Lore implements Cloneable {
    private List<String> lores = new LinkedList<>();

    public Lore() {
    }

    public Lore(String line) {
        this.lores = new LinkedList<>();
        this.lores.add(line);
    }

    public Lore(String... lines) {
        this.lores = new LinkedList<>();

        Collections.addAll(this.lores, lines);
    }

    public Lore(List<String> lores) {
        this.lores = new LinkedList<>();

        this.lores.addAll(lores);
    }

    public List<String> getLores() {
        return lores;
    }

    public void setLore(String line) {
        this.lores = new LinkedList<>();
        this.lores.add(line);
    }

    public void setLores(String... lines) {
        this.lores = new LinkedList<>();

        Collections.addAll(this.lores, lines);
    }

    public void setLores(List<String> lores) {
        this.lores = new LinkedList<>();

        this.lores.addAll(lores);
    }

    public void appendLore(String line) {
        this.lores.add(line);
    }

    public void reset() {
        this.lores = new LinkedList<>();
    }

    @Override
    public Lore clone() throws CloneNotSupportedException {
        Lore newLore = (Lore) super.clone();
        newLore.setLores(this.lores);
        return newLore;
    }
}
