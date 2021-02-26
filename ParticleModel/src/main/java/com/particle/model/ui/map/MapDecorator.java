package com.particle.model.ui.map;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MapDecorator {
    private byte rotation;
    private Type icon = MapDecorator.Type.MARKER_WHITE;
    private byte offsetX;
    private byte offsetZ;
    private String label;
    private Color color;

    public MapDecorator() {
    }

    public MapDecorator(Type icon, byte rotation, byte offsetX, byte offsetZ, String label, Color color) {
        this.rotation = rotation;
        this.icon = icon;
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
        this.label = label;
        this.color = color;
    }

    public byte getRotation() {
        return rotation;
    }

    public void setRotation(byte rotation) {
        this.rotation = rotation;
    }

    public Type getIcon() {
        return icon;
    }

    public void setIcon(Type icon) {
        this.icon = icon;
    }

    public byte getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(byte offsetX) {
        this.offsetX = offsetX;
    }

    public byte getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(byte offsetZ) {
        this.offsetZ = offsetZ;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public enum Type {
        MARKER_WHITE((byte) 0),       //Player
        MARKER_GREEN((byte) 1),       //ItemFrame
        MARKER_RED((byte) 2),
        MARKER_BLUE((byte) 3),
        X_WHITE((byte) 4),
        TRIANGLE_RED((byte) 5),
        SQUARE_WHITE((byte) 6),       //PlayerOffMap
        MARKER_SIGN((byte) 7),
        MARKER_PINK((byte) 8),
        MARKER_ORANGE((byte) 9),
        MARKER_YELLOW((byte) 10),
        MARKER_TEAL((byte) 11),
        TRIANGLE_GREEN((byte) 12),
        SMALL_SQUARE_WHITE((byte) 13), //PlayerOffLimits
        MANSION((byte) 14),
        MONUMENT((byte) 15),
        NO_DRAW((byte) 16),
        COUNT((byte) 17);


        private static final Map<Byte, Type> TYPE_MAP = new HashMap<>();

        static {
            for (Type value : Type.values()) {
                TYPE_MAP.put(value.getData(), value);
            }
        }

        private byte data;

        Type(byte data) {
            this.data = data;
        }

        public byte getData() {
            return data;
        }

        public static Type fromData(byte data) {
            Type type = TYPE_MAP.get(data);

            return type == null ? Type.MARKER_WHITE : type;
        }
    }
}
