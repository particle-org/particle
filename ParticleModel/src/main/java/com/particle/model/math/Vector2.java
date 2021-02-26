package com.particle.model.math;

public class Vector2 {
    private int x;
    private int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2f vector2f) {
        this(vector2f.getFloorX(), vector2f.getFloorY());
    }

    public Vector2 add(Vector2 position) {
        return new Vector2(this.x + position.getX(), this.y + position.getY());
    }

    public Vector2f add(Vector2f position) {
        return new Vector2f(this.x + position.getX(), this.y + position.getY());
    }

    public Vector2 add(int x, int y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 subtract(Vector2 position) {
        return new Vector2(this.x - position.getX(), this.y - position.getY());
    }

    public Vector2 subtract(int x, int y) {
        return new Vector2(this.x - x, this.y - y);
    }

    public Vector2 multiply(int number) {
        return new Vector2(this.x * number, this.y * number);
    }

    public Vector2 multiply(double number) {
        return new Vector2((int) (this.x * number), (int) (this.y * number));
    }

    public Vector2 divide(int number) {
        return new Vector2(this.x / number, this.y / number);
    }

    public boolean isAllZero() {
        return this.x == 0 && this.y == 0;
    }

    public boolean AllLengthLess(int length) {
        return this.x < length && this.x > -length && this.y < length && this.y > -length;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double lengthSquare() {
        return this.x * this.x + this.y * this.y;
    }


    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object ob) {
        if (ob instanceof Vector2) {
            Vector2 from = (Vector2) ob;
            return from.getX() == this.x && from.getY() == this.y;
        }

        return false;
    }
}
