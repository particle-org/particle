package com.particle.model.math;

public class Vector2d {
    public final double x;
    public final double y;

    public Vector2d() {
        this(0, 0);
    }

    public Vector2d(double x) {
        this(x, 0);
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getFloorX() {
        return (int) Math.floor(this.x);
    }

    public int getFloorY() {
        return (int) Math.floor(this.y);
    }

    public Vector2d add(double x) {
        return this.add(x, 0);
    }

    public Vector2d add(double x, double y) {
        return new Vector2d(this.x + x, this.y + y);
    }

    public Vector2d add(Vector2d x) {
        return this.add(x.getX(), x.getY());
    }

    public Vector2d subtract(double x) {
        return this.subtract(x, 0);
    }

    public Vector2d subtract(double x, double y) {
        return this.add(-x, -y);
    }

    public Vector2d subtract(Vector2d x) {
        return this.add(-x.getX(), -x.getY());
    }

    public Vector2d ceil() {
        return new Vector2d((int) (this.x + 1), (int) (this.y + 1));
    }

    public Vector2d floor() {
        return new Vector2d((int) Math.floor(this.x), (int) Math.floor(this.y));
    }

    public Vector2d round() {
        return new Vector2d(Math.round(this.x), Math.round(this.y));
    }

    public Vector2d abs() {
        return new Vector2d(Math.abs(this.x), Math.abs(this.y));
    }

    public Vector2d multiply(double number) {
        return new Vector2d(this.x * number, this.y * number);
    }

    public Vector2d divide(double number) {
        return new Vector2d(this.x / number, this.y / number);
    }

    public double distance(double x) {
        return this.distance(x, 0);
    }

    public double distance(double x, double y) {
        return Math.sqrt(this.distanceSquared(x, y));
    }

    public double distance(Vector2d vector) {
        return Math.sqrt(this.distanceSquared(vector.getX(), vector.getY()));
    }

    public double distanceSquared(double x) {
        return this.distanceSquared(x, 0);
    }

    public double distanceSquared(double x, double y) {
        return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2);
    }

    public double distanceSquared(Vector2d vector) {
        return this.distanceSquared(vector.getX(), vector.getY());
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public Vector2d normalize() {
        double len = this.lengthSquared();
        if (len != 0) {
            return this.divide(Math.sqrt(len));
        }
        return new Vector2d(0, 0);
    }

    public double dot(Vector2d v) {
        return this.x * v.x + this.y * v.y;
    }

    @Override
    public String toString() {
        return "Vector2d(x=" + this.x + ",y=" + this.y + ")";
    }

}