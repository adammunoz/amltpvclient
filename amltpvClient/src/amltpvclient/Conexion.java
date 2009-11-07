/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amltpvclient;
import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;



/**
 *
 * @author adam
 */
public class Conexion {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String answerToQuery = null;
    private String serverName;
    private boolean canIsend = true;
    static Vector<String> mesasOcupadas = new Vector();

    Conexion(String serverName) throws UnknownHostException, IOException {
        this.serverName = serverName;
        start(7);
        System.out.println("Conexión con servidor temporal abierta");
    }

    Conexion(String serverName, int port) throws UnknownHostException, IOException {
        this.serverName = serverName;
        start(port);
        System.out.println("Conexión con servidor principal abierta");
    }

    void reset() {
        System.out.println("Reset:" + answerToQuery);
        answerToQuery = null;
        youCanSend();
    }

    String waitForMsgs(boolean decode) throws IOException{
        if (decode){
            System.out.println("Waiting for answer from main server");
        }
        else{
            System.out.println("Waiting for answer from temporary server");
        }

        String msg=null;
        while ((msg = in.readLine())!=null){
            System.out.println("Answere received:"+msg);
            if (decode){
                return decode(msg);
            }
            else{
                close();
                return msg;
            }
        }
        return null;
    }

    private void start(int port) throws UnknownHostException, IOException{
        socket = new Socket(serverName, port);
        out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
        
    }

    private void close(){
        try {
            socket.close();
            out.close();
            in.close();
            System.out.println("Conexion closed");
        } catch (IOException ex) {
            AmltpvViewClient.util.log(ex.toString());
        }
    }
    void youCanSend(){
        canIsend = true;
    }
    String decode(String msg) {
        System.out.println("Decoding server message: " + msg);
        String operation = "";
        String operand = "";

        try{
            String[] a = msg.split("@");
            operation = a[0];
            operand = a[1];
        }
        catch (java.lang.ArrayIndexOutOfBoundsException ex){
            System.out.println("No operation identified");
        }
        
        if (operation.equals("mesaOcupada")){
            System.out.println("Decoded:mesa ocupada");
            mesasOcupadas.add(operand);
            return "done";
        }
        else if (operation.equals("mesaCerrada")){
            System.out.println("Decoded:mesa cerrada");
            mesasOcupadas.remove(operand);
            return "done";
        }
        else if (operation.equals("mesaCambiada")){
            System.out.println("Decoded:mesa cambiada");
            AmltpvViewClient.changeMesasIcon(operand, new ImageIcon("imgs/mesaBusy.jpg"));
            return "done";
        }
        else if (operation.equals("mesaServida")){
            System.out.println("Decoded:mesa servida");
            AmltpvViewClient.changeMesasIcon(operand, new ImageIcon("imgs/mesaBusyServida.jpg"));
            return "done";
        }
        else if (operation.equals("liberate")){
            System.out.println("Decoded:liberar mesa");
            AmltpvViewClient.changeMesasIcon(operand, new ImageIcon("imgs/mesa.jpg"));
            return "done";
        }
        else if (operation.equals("kill")){
            System.out.println("Identified remote kill!");
            return "auto-destroy";
        }
        else{
            return "nothind done";
        }

    }
    
    void sendMsg(String msg){
        System.out.println("sending " + msg);
        out.println(msg);
    }
    
    void sendQuery(String query) throws IOException{
        sendMsg("dbQuery@"+query);
        while (answerToQuery==null){
            try {
                System.out.println("Sleeping, waiting for query answer");
                Thread.sleep(10);
                System.out.println("Query woke up");
            } catch (InterruptedException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    String getProductosString(){
        try {
            trySendAction("productosTree@no_operand");
        } catch (IOException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (answerToQuery==null){
            try {
                System.out.println("Sleeping, waiting for productos answer");
                Thread.sleep(10);
                System.out.println("Woke up, looking for productos answer");
            } catch (InterruptedException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String result = answerToQuery;
        reset();
        return result;
    }
    void trySendQuery(String query) throws IOException{
        if (canIsend){
            System.out.println("I can send now");
            sendQuery(query);
        }
        else{
            try {
                System.out.println("Sleeping, trySendQuery");
                Thread.sleep(10);
                System.out.println("trySendQuery woke up");
                trySendQuery(query);
            } catch (InterruptedException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void trySendAction(String action) throws IOException{
        if (canIsend){
            System.out.println("I can send now");
            sendMsg(action);
        }
        else{
            try {
                System.out.println("Sleeping, trySendAction");
                Thread.sleep(10);
                System.out.println("trySendAction woke up");
                trySendAction(action);
            } catch (InterruptedException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        reset();
    }
    
    void mesaOcupada(int i){
        try {
            trySendAction("mesaOcupada@" + i);
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void mesaCerrada(int i){
        try {
            trySendAction("mesaCerrada@" + i);
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String returnAnswerToQuery() {
        String s = answerToQuery;
        reset();
        return s;
    }


}
