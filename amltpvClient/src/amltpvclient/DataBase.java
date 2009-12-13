package amltpvclient;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;


import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
/**
 *
 * @author Adam
 */
public class DataBase{

    
    /*This class is a man in the middle class to communicate with the database
     It translates sql queries to queries the server can understantnd*/
    

    void addProductoToMesasPool(String mesa,String producto,boolean cocina){
        String s = "";
        if (cocina){
            s = "clientMesaInsert@"+mesa+":"+producto;
            AmltpvViewClient.changeMesasIcon(mesa, new ImageIcon("imgs/mesaBusy.jpg"));
        }
        else{
            s = "clientMesaInsertNoCocina@"+mesa+":"+producto;
        }
        System.out.println(s);
        
            
        try {
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void moverMesaInPool(String source,String target){
        String s = "";
        s = "moverMesa@"+source+":"+target;
        System.out.println(s);
        try {
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void deleteFromMesasPool(String mesa, String producto) {
        int cant = queryCantOfProducto(mesa,producto);
        String s = "clientMesaDelete@"+mesa+":"+producto;
        System.out.println(s);
        
        try {
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i=0;i<cant-1;i++){
                addProductoToMesasPool(mesa,producto,false);
        }
    }


    Enumeration queryMesaContents(String mesa){
        try {
            String s = "dbQuery@mesaContents"+":"+mesa;
            System.out.println("Server query:" + s);
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
            Enumeration resultEnum = AmltpvViewClient.util.stringToEnum(result);
            return resultEnum;
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
 
    }
    int queryCantOfProducto(String mesa,String producto){
        try{
            String s = "dbQuery@cantOfProducto"+":"+mesa+":"+producto;
            System.out.println("Server query:" + s);
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
            return Integer.parseInt(result);
        }
        catch (IOException ex){
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    String queryValor(String campo){
        try {
            String s = "dbQuery@queryValor:"+campo;
            System.out.println("Server query:" + s);
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    void fromPoolToCobradas(String mesa) {
        String s = "fromPoolToCobradas@"+mesa;
        System.out.println(s);

        try {
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void emptyPool(String mesa) {
        String s = "emptyPool@"+mesa;
        System.out.println(s);

        try {
            Conexion conexion = new Conexion(ConectarJDialog.servidor);
            conexion.sendMsg(s);
            String result = conexion.waitForMsgs(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
