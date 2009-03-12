/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package androidgpssimulator.locationSender;

import androidgpssimulator.telnet.ConfigTelnet;
import androidgpssimulator.telnet.Telnet;
import androidgpssimulator.telnet.TelnetDisconnectException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author JuanmaSP
 */
public class LocationSender {
    private Telnet telnetConnection;

    public LocationSender(ConfigTelnet conf){
        telnetConnection = new Telnet(conf);
    }

    public boolean connect() throws MalformedURLException, LocationSenderUnreachableEmulator{
        try {
            telnetConnection.connect();
        } catch (IOException ex) {
            throw new LocationSenderUnreachableEmulator(ex.getMessage());
        }

        return true;
    }

    public boolean disconnect() throws LocationSenderUnknowException{
        if(isConnected())
            try {
            telnetConnection.disconnect();
        } catch (IOException ex) {
            throw new LocationSenderUnknowException(ex.getMessage());
        }
        return true;
    }

    public boolean isConnected(){
        return telnetConnection.isConnected();
    }

    public boolean send(Location loc) throws LocationSenderUnknowException, LocationSenderDisconnectedException{
        boolean sent = false;

        String command = getCommand(loc.getLongitude(), loc.getLatitude(), loc.getAltitude());

        System.out.println(command);
        
        try {
            telnetConnection.sendComand(command);
        } catch (IOException ex) {
            throw new LocationSenderUnknowException(ex.getMessage());
        } catch (TelnetDisconnectException ex) {
            throw new LocationSenderDisconnectedException(ex.getMessage());
        }
        //@TODO Realizar comprobación de mensaje recibido de vuelta.
        sent = true;

        return sent;
    }

    private String getCommand(double longitude, double latitude, double elevation){
        Calendar c = Calendar.getInstance();
        String format = "geo nmea $GPGGA,%1$02d%2$02d%3$02d.%4$03d,%5$03d%6$09.6f,%7$c,%8$03d%9$09.6f,%10$c,1,10,0.0,0.0,0,0.0,0,0.0,0000";

        double absLong = Math.abs(longitude);
        int longDegree = (int)Math.floor(absLong);
        char longDireccion = 'E';
        if (longitude < 0D) {
          longDireccion = 'W';
        }
        double longMinute = (absLong - Math.floor(absLong)) * 60.0D;

        double absLat = Math.abs(latitude);
        int latDegree = (int)Math.floor(absLat);
        char latDireccion = 'N';
        if (latitude < 0D) {
          latDireccion = 'S';
        }
        double latMinute = (absLat - Math.floor(absLat)) * 60.0D;

        String command = String.format(Locale.US, format,
                new Object[] {
                    Integer.valueOf(c.get(Calendar.HOUR_OF_DAY)),
                    Integer.valueOf(c.get(Calendar.MINUTE)),
                    Integer.valueOf(c.get(Calendar.SECOND)),
                    Integer.valueOf(c.get(Calendar.MILLISECOND)),
                    Integer.valueOf(latDegree),
                    Double.valueOf(latMinute),
                    Character.valueOf(latDireccion),
                    Integer.valueOf(longDegree),
                    Double.valueOf(longMinute),
                    Character.valueOf(longDireccion) });

        return command;
    }
}
