/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConectarJDialog.java
 *
 * Created on 04-jul-2009, 17:20:07
 */

package amltpvclient;



/**
 *
 * @author adam
 */
public class ConectarJDialog extends javax.swing.JDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private AmltpvViewClient amltpvView;
    static String servidor;
    /** Creates new form ConectarJDialog */
    public ConectarJDialog(java.awt.Frame parent, boolean modal,AmltpvViewClient view) {
        super(parent, modal);
        initComponents();
        amltpvView = view;
        String persistedServer = AmltpvViewClient.persister.operator.retrieveValue("servidor");
        if (persistedServer.equals("error")){
            AmltpvViewClient.persister.operator.newSetting("servidor", "Escriba");
        }
        servidorTextField.setText(AmltpvViewClient.persister.operator.retrieveValue("servidor"));
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        servidorTextField = new javax.swing.JTextField();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(amltpvclient.AmltpvClientApp.class).getContext().getResourceMap(ConectarJDialog.class);
        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setText(resourceMap.getString("jTextArea1.text")); // NOI18N
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        servidorTextField.setText(resourceMap.getString("servidorTextField.text")); // NOI18N
        servidorTextField.setName("servidorTextField"); // NOI18N
        servidorTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servidorTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(servidorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(servidorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
            servidor = servidorTextField.getText();
            amltpvView.pintarMesas();
            AmltpvViewClient.util.persistSetting("servidor", servidor);
            Thread servidorThread = new Thread(new ThreadNetworking());
            servidorThread.start();
            ObjectClient objClient = new ObjectClient();
            AmltpvViewClient.persister.operator.updateSetting("servidor", servidor);
            doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void servidorTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servidorTextFieldActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_servidorTextFieldActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField servidorTextField;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}