package com.example.address.model;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {
    @SerializedName("place_id")
    private String placeId;

    @SerializedName("display_name")
    private String display_name;

    private String lat;
    private String lon;

    // Getters / Setters
    public String getPlaceId() { return placeId; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public String getDisplay_name() { return display_name; }
    public void setDisplay_name(String display_name) { this.display_name = display_name; }

    public String getLat() { return lat; }
    public void setLat(String lat) { this.lat = lat; }

    public String getLon() { return lon; }
    public void setLon(String lon) { this.lon = lon; }
}
