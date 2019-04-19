package com.konfuse.mapmatching.domain;

/**
 * @Author: Konfuse
 * @Date: 2019/4/18 10:18
 */
public class MapPoint {
    private long id;
    private String description;
    private double speed;
    private double lat;
    private double lng;

    public MapPoint()
    {
    }

    public MapPoint(String description, double speed, double lat, double lng) {
        this.description = description;
        this.speed = speed;
        this.lat = lat;
        this.lng = lng;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", speed=" + speed +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
