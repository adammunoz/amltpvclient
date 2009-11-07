/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amltpvclient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adam
 */
public class ThreadNetworking implements Runnable {
    public void run(){
        try {
            System.out.println("Intentando abrir conexion con servidor principal en puerto 8");
            Conexion conexion = new Conexion(ConectarJDialog.servidor,8);
            String returnedMsgFromDecode;
            while (true){
                returnedMsgFromDecode = conexion.waitForMsgs(true);
                System.out.println("Decode returned " + returnedMsgFromDecode);
                if (returnedMsgFromDecode.equals("auto-destroy")){
                    System.out.println("We will auto-destroy");
                    System.exit(0);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadNetworking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
