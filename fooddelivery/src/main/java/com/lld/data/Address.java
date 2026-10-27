package com.lld.data;

public class Address {
    private String street;
    private String city;
    private String zipCode;
    private double latitude;
    private double longitude;

    public Address(String street, String city, String zipCode, double latitude, double longitude) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistanceTo(Address other){
        double dLat = this.latitude - other.latitude;
        double dLon = this.longitude - other.longitude;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }
        
    
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZipCode() { return zipCode; }
    
}
    