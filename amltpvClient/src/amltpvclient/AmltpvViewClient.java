/*
 * AmltpvViewClient.java
 */

package amltpvclient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import persistance.Persister;

/**
 * The application's main frame.
 */
public class AmltpvViewClient extends FrameView {
    CobradasDialog cobradasDialog;
    static AmltpvViewClient self;
    static Persister persister = new Persister();
    public AmltpvViewClient(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        //SEVERRAL INITIALIZATIONS
        util = new amltpv.Utils(statusMessageLabel);
        util.setMainFrame(AmltpvClientApp.getApplication().getMainFrame());
        db = new DataBase();
        super.getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //Thread t = new Thread(new ThreadTest());
        //t.start();
        cobradasDialog = new CobradasDialog(super.getFrame(), true);
        cobradasDialog.setLocationRelativeTo(super.getComponent());
        self = this;

}
    static void changeMesasIcon(String mesa,ImageIcon icon){
        mesasArray[Integer.parseInt(mesa)].setIcon(icon);
    }

    void pintarMesas(){
        int numMesas = Byte.parseByte(db.queryValor("numMesas"));
        System.out.println("NumMesas to be painted:"+numMesas);
        numMesasArray = new String[numMesas];
        for (int j=0;j<numMesas;j++){
            numMesasArray[j] = Integer.toString(j+1);
        }
        mesasArray = new JButton[numMesas+1];
        for (int i=1;i<=numMesas;i++){
           mesasArray[i] = new JButton(String.valueOf(i),new ImageIcon("imgs/mesa.jpg"));
           mesasArray[i].setFont(new Font("sansserif",Font.BOLD,18));
           mainPanel.add(mesasArray[i]);
           mesasArray[i].addActionListener(new ButtonHandler());
        }
        mainPanel.revalidate();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = AmltpvClientApp.getApplication().getMainFrame();
            aboutBox = new AmltpvAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        AmltpvClientApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        conectarItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        cobradasMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(amltpvclient.AmltpvClientApp.class).getContext().getResourceMap(AmltpvViewClient.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setFont(resourceMap.getFont("fileMenu.font")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        conectarItem.setFont(resourceMap.getFont("conectarItem.font")); // NOI18N
        conectarItem.setForeground(resourceMap.getColor("conectarItem.foreground")); // NOI18N
        conectarItem.setText(resourceMap.getString("conectarItem.text")); // NOI18N
        conectarItem.setName("conectarItem"); // NOI18N
        conectarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conectarItemActionPerformed(evt);
            }
        });
        fileMenu.add(conectarItem);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(amltpvclient.AmltpvClientApp.class).getContext().getActionMap(AmltpvViewClient.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setFont(resourceMap.getFont("exitMenuItem.font")); // NOI18N
        exitMenuItem.setForeground(resourceMap.getColor("exitMenuItem.foreground")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setFont(resourceMap.getFont("jMenu1.font")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        cobradasMenuItem.setFont(resourceMap.getFont("cobradasMenuItem.font")); // NOI18N
        cobradasMenuItem.setForeground(resourceMap.getColor("cobradasMenuItem.foreground")); // NOI18N
        cobradasMenuItem.setText(resourceMap.getString("cobradasMenuItem.text")); // NOI18N
        cobradasMenuItem.setName("cobradasMenuItem"); // NOI18N
        cobradasMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cobradasMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(cobradasMenuItem);

        menuBar.add(jMenu1);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setFont(resourceMap.getFont("helpMenu.font")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setFont(resourceMap.getFont("aboutMenuItem.font")); // NOI18N
        aboutMenuItem.setForeground(resourceMap.getColor("aboutMenuItem.foreground")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void conectarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conectarItemActionPerformed
        ConectarJDialog conectarJDialog = new ConectarJDialog(this.getFrame(),true,this);
        conectarJDialog.setLocationRelativeTo(this.getFrame());
        conectarJDialog.setVisible(true);
    }//GEN-LAST:event_conectarItemActionPerformed

    private void cobradasMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cobradasMenuItemActionPerformed
        cobradasDialog.setVisible(true);
    }//GEN-LAST:event_cobradasMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        persister.shutdown();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem cobradasMenuItem;
    private javax.swing.JMenuItem conectarItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    static DataBase db;
    static amltpv.Utils util;
    static amltpv.ProductosModel productosModel;
    static Conexion conexion;
    static JButton[] mesasArray;
    static String[] numMesasArray;
    static int remoteProcessing;
}
class ButtonHandler implements ActionListener{
    public void actionPerformed(java.awt.event.ActionEvent e){
        JButton sourceButton = (JButton) e.getSource();
        int numMesa = Integer.parseInt(sourceButton.getText());
        if (Conexion.mesasOcupadas.contains(Integer.toString(numMesa))){
           JOptionPane.showMessageDialog(AmltpvViewClient.util.getMainFrame(),"Esta mesa esta siendo procesada en otra ordenador");
        }
        else{

            try {
                Conexion conexion = new Conexion(ConectarJDialog.servidor);
                conexion.sendMsg("mesaOcupada@"+numMesa);
                conexion.waitForMsgs(false);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ButtonHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ButtonHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            VentasDialogScreen ventasScreen = new VentasDialogScreen(AmltpvViewClient.util.getMainFrame(),true,numMesa);
            ventasScreen.setTitle("Mesa "+numMesa);
            ventasScreen.setLocationRelativeTo(AmltpvViewClient.util.getMainFrame());
            ventasScreen.setVisible(true);
        }
    }
}