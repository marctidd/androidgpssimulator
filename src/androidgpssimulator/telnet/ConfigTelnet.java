/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package androidgpssimulator.telnet;

/**
 *
 * @author JuanmaSP
 */
public class ConfigTelnet {
    private String host;
    private int port;

    private static final String hostDefault = "localhost";
    private static final int portDefault = 5554;
    
    public ConfigTelnet(){
        this.host = hostDefault;
        this.port = portDefault;
    }

    public ConfigTelnet(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static ConfigTelnet getDefault(){
        return new ConfigTelnet();
    }

    public void setHost(String host) throws IllegalArgumentException{
        if(host == null || host.length() == 0)
            throw new IllegalArgumentException("Debe pasarse un host");

        this.host = host;
    }

    public String getHost(){
        return this.host;
    }

    public void setPort(int port) throws IllegalArgumentException{
        if(port <= 0)
            throw new IllegalArgumentException("El puerto debe ser mayor que cero.");
        this.port = port;
    }

    public void setPort(String port) throws NumberFormatException, IllegalArgumentException{
        setPort(Integer.parseInt(host));
    }

    public int getPort(){
        return this.port;
    }
    
}
