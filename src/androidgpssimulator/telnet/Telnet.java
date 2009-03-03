/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package androidgpssimulator.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import thor.net.TelnetURLConnection;
import thor.net.URLStreamHandler;

/**
 * Clase encargada de hacer de intermediaria entre la librería JTelnet y el
 * resto del programa.
 * @TODO recepción de mensajes
 * @TODO recepción de mensajes generen eventos.
 *
 * @author JuanmaSP
 * @date 2009-03-03
 */
public class Telnet {
    private static final String protocol = "telnet";

    private ConfigTelnet config;
    
    private List<String> messages;

    private boolean transmitting = false;

    private URLConnection connection;
    private OutputStream out;
    private InputStream in;

    public Telnet(ConfigTelnet config){
        this.config = config;
        this.messages = new LinkedList<String>();
    }

    /**
     * Realiza la conexión con el telnet.
     * @return
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    public boolean connect() throws MalformedURLException, IOException{
        //Establecemos la conexión
        URL url = new URL(protocol, config.getHost(), config.getPort(), "", new URLStreamHandler());
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        
        if (urlConnection instanceof TelnetURLConnection) {
            ((TelnetURLConnection) urlConnection).setTelnetTerminalHandler(new SimpleTelnetTerminalHandler());
		}

        //Abrimos los canales de comunicación
        out = urlConnection.getOutputStream();
		in = urlConnection.getInputStream();

        return true;
    }

    /**
     * Desconecta la conexión.
     *
     * @return
     * @throws java.io.IOException
     */
    public boolean disconnect() throws IOException{
        out.close();
        in.close();
        ((TelnetURLConnection)connection).disconnect();

        return true;
    }

    /**
     * Transmite el comando pasado como argumento por telnet
     * @param comando
     * @throws java.io.IOException
     * @throws androidgpssimulator.telnet.TelnetDisconnectException si no está
     *      establecida la conexión.
     */
    public void sendComand(String text) throws IOException, TelnetDisconnectException{
        if(!isConnected())
            throw new TelnetDisconnectException("No se puede transmitir un comando sin conexión");

        transmitting = true;

        for(int i = 0; i < text.length(); i++){
            try {
                out.write(text.charAt(i));
            } catch (IOException ex) {
                transmitting = false;
                throw ex;
            }
        }

        transmitting = false;
    }

    /**
     * Indica si está establecida la conexión
     *
     * @return true si está establecida la conexión o false en caso contrario.
     */
    public boolean isConnected(){
        return ((TelnetURLConnection)connection).connected();
    }
}
