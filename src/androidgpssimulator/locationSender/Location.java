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
    private float latitude;
    private float longitude;
    private int altitude;

    public Location(float latitude, float longitude){
        this(latitude, longitude, 0);
    }

    public Location(float latitude, float longitude, int altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public float getLatitude(){
        return this.latitude;
    }

    public void setLatitude(float lat){
        this.latitude = lat;
    }

    public float getLongitude(){
        return this.longitude;
    }

    public void setLongitude(float longit){
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
