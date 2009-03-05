/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package androidgpssimulator.locationSender;

/**
 *
 * @author JuanmaSP
 */
public class Location {
    private double latitude;
    private double longitude;
    private int altitude;

    public Location(double latitude, double longitude){
        this(latitude, longitude, 0);
    }

    public Location(double latitude, double longitude, int altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(double lat){
        this.latitude = lat;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setLongitude(double longit){
        this.longitude = longit;
    }

    public int getAltitude(){
        return this.altitude;
    }

    public void setAltitude(int alt){
        this.altitude = alt;
    }

    @Override
    public String toString(){
        return "Location (lat: " + this.latitude
                + ", long: " + this.longitude
                + ", alt: " + this.altitude + "m.)";
    }
}
