/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jclient;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author LS_Fisso
 */
public class ClientFrame extends javax.swing.JFrame {

    /**
     * Creates new form ClientFrame
     */
    public ClientFrame() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPaneContact = new javax.swing.JScrollPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPaneMessage = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanelMessageSend = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaMessageSend = new javax.swing.JTextArea();
        jButtonSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(230);
        jSplitPane1.setDividerSize(4);

        jScrollPaneContact.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jSplitPane1.setLeftComponent(jScrollPaneContact);

        jSplitPane3.setDividerLocation(410);
        jSplitPane3.setDividerSize(3);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPaneMessage.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPaneMessage.setToolTipText("");
        jScrollPaneMessage.setMinimumSize(new java.awt.Dimension(23, 100));
        jSplitPane3.setTopComponent(jScrollPaneMessage);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jSplitPane3.setRightComponent(jScrollPane2);

        jTextAreaMessageSend.setColumns(20);
        jTextAreaMessageSend.setRows(1);
        jScrollPane4.setViewportView(jTextAreaMessageSend);

        jButtonSend.setText("Send");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMessageSendLayout = new javax.swing.GroupLayout(jPanelMessageSend);
        jPanelMessageSend.setLayout(jPanelMessageSendLayout);
        jPanelMessageSendLayout.setHorizontalGroup(
            jPanelMessageSendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMessageSendLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSend)
                .addContainerGap())
        );
        jPanelMessageSendLayout.setVerticalGroup(
            jPanelMessageSendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMessageSendLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButtonSend)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanelMessageSendLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );

        jSplitPane3.setRightComponent(jPanelMessageSend);

        jSplitPane1.setBottomComponent(jSplitPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed
        // TODO add your handling code here:
        addContact();
    }//GEN-LAST:event_jButtonSendActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }

    private void addContact() {
        paneContact = new javax.swing.JPanel();
        jLabelContact = new javax.swing.JLabel();
        jLabelContactBroadcast = new javax.swing.JLabel();
        jPanelContact = new javax.swing.JPanel();
        jPanelContactBroadcast = new javax.swing.JPanel();
        
        JPanel jPanelContact2 = new javax.swing.JPanel();
        JLabel jLabelContact2 = new javax.swing.JLabel();

        paneContact.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        
        
        jPanelContactBroadcast.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelContactBroadcast.setFont(new java.awt.Font("Yu Gothic Medium", 0, 24)); // NOI18N
        jLabelContactBroadcast.setText("Test 1");

        javax.swing.GroupLayout jPanelContactBroadcastLayout = new javax.swing.GroupLayout(jPanelContactBroadcast);
        jPanelContactBroadcast.setLayout(jPanelContactBroadcastLayout);
        jPanelContactBroadcastLayout.setHorizontalGroup(
                jPanelContactBroadcastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelContactBroadcastLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelContactBroadcast, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelContactBroadcastLayout.setVerticalGroup(
                jPanelContactBroadcastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelContactBroadcastLayout.createSequentialGroup()
                                .addComponent(jLabelContactBroadcast)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelContact.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelContact.setFont(new java.awt.Font("Yu Gothic Medium", 0, 24)); // NOI18N
        jLabelContact.setText("Cont1");

        javax.swing.GroupLayout jPanelContactLayout = new javax.swing.GroupLayout(jPanelContact);
        jPanelContact.setLayout(jPanelContactLayout);
        jPanelContactLayout.setHorizontalGroup(
                jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelContactLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelContact, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanelContactLayout.setVerticalGroup(
                jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelContactLayout.createSequentialGroup()
                                .addComponent(jLabelContact)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        
        
        
        jPanelContact2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelContact2.setFont(new java.awt.Font("Yu Gothic Medium", 0, 24)); // NOI18N
        jLabelContact2.setText("Cont2");

        javax.swing.GroupLayout jPanelContact2Layout = new javax.swing.GroupLayout(jPanelContact2);
        jPanelContact2.setLayout(jPanelContact2Layout);
        jPanelContact2Layout.setHorizontalGroup(
                jPanelContact2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelContact2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelContact2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanelContact2Layout.setVerticalGroup(
                jPanelContact2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelContact2Layout.createSequentialGroup()
                                .addComponent(jLabelContact2)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        

        javax.swing.GroupLayout paneContactLayout = new javax.swing.GroupLayout(paneContact);
        
        paneContact.setLayout(paneContactLayout);
        
        GroupLayout.ParallelGroup HParallelGroup = paneContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        
        HParallelGroup.addGroup(paneContactLayout.createSequentialGroup()
                                .addComponent(jPanelContact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
        );
        
        HParallelGroup.addGroup(paneContactLayout.createSequentialGroup()
                                .addComponent(jPanelContact2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
        );
        
        
        HParallelGroup.addComponent(jPanelContactBroadcast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        paneContactLayout.setHorizontalGroup(HParallelGroup);
        
        
        GroupLayout.SequentialGroup vSequentialGroup = paneContactLayout.createSequentialGroup();
        vSequentialGroup.addComponent(jPanelContactBroadcast, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE);
        vSequentialGroup.addGap(0, 0, 0);
        vSequentialGroup.addComponent(jPanelContact, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE);
         vSequentialGroup.addComponent(jPanelContact2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE);
        vSequentialGroup.addContainerGap(389, Short.MAX_VALUE);
        
        paneContactLayout.setVerticalGroup(
                paneContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(vSequentialGroup)
        );
        
        

        jScrollPaneContact.setViewportView(paneContact);

    }

    private javax.swing.JLabel jLabelContact;
    private javax.swing.JLabel jLabelContactBroadcast;
    private javax.swing.JPanel jPanelContact;
    private javax.swing.JPanel jPanelContactBroadcast;
    private javax.swing.JPanel paneContact;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSend;
    private javax.swing.JPanel jPanelMessageSend;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPaneContact;
    private javax.swing.JScrollPane jScrollPaneMessage;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaMessageSend;
    // End of variables declaration//GEN-END:variables
}
