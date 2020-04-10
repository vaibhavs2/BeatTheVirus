package com.example.beatthevirus;

public class list_Info {
   private double distance;
   private String bluetooth;


    public list_Info(String bluetooth, double distance){
        this.bluetooth=bluetooth;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getBlutooth() {
        return bluetooth;
    }

    public void setBlutooth(String blutooth) {
        this.bluetooth = blutooth;
    }



}
