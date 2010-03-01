/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amltpvclient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.tree.DefaultMutableTreeNode;
import amltpv.*;
/**
 *
 * @author pc-xp1
 */
public class ObjectClient {
    public ObjectClient(){
      ObjectOutputStream oos = null;
      ObjectInputStream ois = null;
      Socket socket = null;
      try {
        // open a socket connection
        socket = new Socket(ConectarJDialog.servidor,3000);
        // open I/O streams for objects
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        // read an object from the server
        AmltpvViewClient.productosModel = (amltpv.ProductosModel) ois.readObject();
        ProductosModel pm = AmltpvViewClient.productosModel;
        AmltpvViewClient.productosModel.activate();
        
        oos.close();
        ois.close();
      } catch(Exception e) {
        System.out.println(e.toString());
      }
   }

}
