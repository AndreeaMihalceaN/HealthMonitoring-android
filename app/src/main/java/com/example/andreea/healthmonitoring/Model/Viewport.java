package com.example.andreea.healthmonitoring.Model;

/**
 * Created by Andreea on 14.02.2018.
 */

public class Viewport {
    private Southwest southwest;

    private Northeast northeast;

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    @Override
    public String toString() {
        return "ClassPojo [southwest = " + southwest + ", northeast = " + northeast + "]";
    }
}
