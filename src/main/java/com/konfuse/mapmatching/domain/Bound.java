package com.konfuse.mapmatching.domain;

import java.util.Arrays;

/**
 * @Author: Konfuse
 * @Date: 2019/4/19 11:56
 */
public class Bound {
    private long id;
    private String color;
    private double x1, x2, y1, y2;

    public Bound() {
    }

    public Bound(double x1, double x2, double y1, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return "Bound{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                '}';
    }

    public boolean contains(double x, double y) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2;
    }

    public String coordinates() {
        return Arrays.toString(new double[]{x1, y1});
    }
}
