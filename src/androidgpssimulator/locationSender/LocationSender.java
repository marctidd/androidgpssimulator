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
        String command = "geo fix " + loc.getLongitude() + " " + loc.getLatitude() + " " + loc.getAltitude();

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
}
