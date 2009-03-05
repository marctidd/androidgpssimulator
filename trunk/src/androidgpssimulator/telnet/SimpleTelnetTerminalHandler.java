/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package androidgpssimulator.telnet;

import thor.net.DefaultTelnetTerminalHandler;
import thor.net.TelnetConstants;

public class SimpleTelnetTerminalHandler extends DefaultTelnetTerminalHandler 
        implements TelnetConstants{

    @Override
    public void LineFeed() {
        /*
        System.out.print('\n');
        System.out.flush();
         */
    }

    @Override
    public void CarriageReturn() {
        /*
        System.out.print('\r');
        System.out.flush();
         */
    }

    @Override
    public void BackSpace() {
        /*
        System.out.print((char) BS);
        System.out.flush();
         */
    }

    @Override
    public void HorizontalTab() {
        /*
        System.out.print((char) HT);
        System.out.flush();
         */
    }
}
