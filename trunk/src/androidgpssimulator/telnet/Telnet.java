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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static final int STATE_CONNECTED = 1;
    public static final int STATE_DISCONNECTED = 2;
    public static final int STATE_TRASNMITTING = 3;

    /** Configuración */
    private ConfigTelnet config;

    /** Lista de mensajes entrantes */
    private List<String> messages;

    private int state;

    private List<ITelnetListener> eventListeners;

    /** Elementos necesarios para la conexión */
    private URLConnection connection;
    private OutputStream out;
    private InputStream in;

    public Telnet(ConfigTelnet config){
        this.config = config;
        this.messages = new LinkedList<String>();
        this.eventListeners = new LinkedList<ITelnetListener>();
        this.state = STATE_DISCONNECTED;
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
        connection = url.openConnection();
        connection.connect();
        
        if (connection instanceof TelnetURLConnection) {
            ((TelnetURLConnection) connection).setTelnetTerminalHandler(new SimpleTelnetTerminalHandler());
		}

        //Abrimos los canales de comunicación
        out = connection.getOutputStream();
		in = connection.getInputStream();

        changeState(STATE_CONNECTED);

        MessageReceiver receptor = new MessageReceiver();
        receptor.start();

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

        changeState(STATE_DISCONNECTED);

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

        changeState(STATE_TRASNMITTING);

        try {
            for(int i = 0; i < text.length(); i++){
                out.write(text.charAt(i));
            }
            out.write("\n".charAt(0));
        } catch (IOException ex) {
            if(isConnected())
                changeState(STATE_CONNECTED);
            else
                changeState(STATE_DISCONNECTED);
            throw ex;
        }

        changeState(STATE_CONNECTED);
    }

    /**
     * Indica si está establecida la conexión
     *
     * @return true si está establecida la conexión o false en caso contrario.
     */
    public boolean isConnected(){
        return connection != null && ((TelnetURLConnection)connection).connected();
    }


    public void addEventListener(ITelnetListener listener){
        eventListeners.add(listener);
    }

    public void removeEventListener(ITelnetListener listener){
        eventListeners.remove(listener);
    }

    public void clearEventListeners(){
        eventListeners.clear();
    }

    protected void addNewMessage(String message){
        this.messages.add(message);

        System.out.println("Mensaje recibido: " + message);

        Iterator<ITelnetListener> it = eventListeners.iterator();
        while(it.hasNext()){
            it.next().messageReceived(message);
        }
    }

    private void changeState(int state){
        this.state = state;

        Iterator<ITelnetListener> it = eventListeners.iterator();
        while(it.hasNext()){
            it.next().stateChanged(state);
        }
    }

    /**
     *  Clase encargada de ir reciviendo los mensajes entrantes desde telnet.
     */
    private class MessageReceiver extends Thread{

        @Override
        public void run() {
            while(state == STATE_CONNECTED || state == STATE_TRASNMITTING){
                int disp;

                try {
                    disp = in.available();
                } catch (IOException ex) {
                    disp = 0;
                    Logger.getLogger(Telnet.class.getName()).log(Level.SEVERE, null, ex);
                }

                if(disp > 0){
                    try{
                        //Hay información para leer
                        //La cargamos en un buffer
                        StringBuffer message = new StringBuffer();
                        for(int i=0; i < disp; i++)
                            message.append((char)in.read());

                        //Y la guardamos como mensaje recibido
                        addNewMessage(message.toString());
                    }
                    catch(IOException e){
                        if(state != STATE_DISCONNECTED)
                        addNewMessage("Telnet error. Fallo al recibir el mensaje. " + e.getMessage());
                    }
                }
                try {
                    /*
                     *  Dormiremos el proceso de recepción durante 10 ms para no
                     * sobrecargar el sistema.
                     */
                    sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Telnet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
