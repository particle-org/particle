package com.particle.model.network.packets.data;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class PersonaPieceTint {
    private String pieceType;
    private ImmutableList<String> colors;

    public PersonaPieceTint() {
    }

    public PersonaPieceTint(String pieceType, List<String> colorList) {
        this.pieceType = pieceType;
        this.colors = ImmutableList.copyOf(colorList);
    }

    public String getPieceType() {
        return pieceType;
    }

    public void setPieceType(String pieceType) {
        this.pieceType = pieceType;
    }

    public ImmutableList<String> getColors() {
        return colors;
    }

    public void setColors(ImmutableList<String> colors) {
        this.colors = colors;
    }
}
