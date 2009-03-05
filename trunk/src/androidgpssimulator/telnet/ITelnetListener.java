/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package androidgpssimulator.telnet;

/**
 *
 * @author JuanmaSP
 */
public interface ITelnetListener {
    public void messageReceived(String message);

    public void stateChanged(int state);
}
