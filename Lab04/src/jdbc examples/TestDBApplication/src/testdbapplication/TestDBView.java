/*
 * TestDBView.java
 */

package testdbapplication;

import java.awt.Component;
import java.awt.TrayIcon.MessageType;
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
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.TableColumnModel;
import org.sqlite.SQLite;
import org.sqlite.JDBC;

/**
 * The application's main frame.
 */
public class TestDBView extends FrameView 
{
    private Connection bd;
    private Statement st;
    private ResultSet rs;
    
    public TestDBView(SingleFrameApplication app) 
    {
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
        init();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = TestDBApplication.getApplication().getMainFrame();
            aboutBox = new TestDBAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        TestDBApplication.getApplication().show(aboutBox);
    }

    private void init()
    {
        try
        {
            TableColumnModel c = jTableGoods.getColumnModel();
            for(int i = 0; i < c.getColumnCount(); i++)
            {
                c.getColumn(i).setResizable(false);
            }
            jTableGoods.setColumnModel(c);
            
            Class.forName("org.sqlite.JDBC");
            bd = DriverManager.getConnection("jdbc:sqlite:Products.db");
            st = bd.createStatement();
            st.execute("create table if not exists 'Goods' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'price' text, 'count' INTEGER);");

            rs = st.executeQuery("select * from Goods");
            int i = 0;
            while (rs.next())
            {
                jTableGoods.setValueAt(rs.getString(1), i, 0);
                jTableGoods.setValueAt(rs.getString(2), i, 1);
                jTableGoods.setValueAt(rs.getString(3), i, 2);
                jTableGoods.setValueAt(rs.getString(4), i, 3);
                i++;
            }
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this.mainPanel, "Error: " + ex.getMessage());}
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableGoods = new javax.swing.JTable();
        jButtonInsert = new javax.swing.JButton();
        jTextFieldPrice = new javax.swing.JTextField();
        jTextFieldNameProduct = new javax.swing.JTextField();
        jTextFieldCount = new javax.swing.JTextField();
        jButtonUpdate = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonReload = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableGoods.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Наименование", "Цена", "Количество"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableGoods.setName("jTableGoods"); // NOI18N
        jTableGoods.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTableGoods);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(testdbapplication.TestDBApplication.class).getContext().getResourceMap(TestDBView.class);
        jButtonInsert.setText(resourceMap.getString("jButtonInsert.text")); // NOI18N
        jButtonInsert.setName("jButtonInsert"); // NOI18N
        jButtonInsert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonInsertMouseClicked(evt);
            }
        });

        jTextFieldPrice.setText(resourceMap.getString("jTextFieldPrice.text")); // NOI18N
        jTextFieldPrice.setName("jTextFieldPrice"); // NOI18N

        jTextFieldNameProduct.setText(resourceMap.getString("jTextFieldName.text")); // NOI18N
        jTextFieldNameProduct.setName("jTextFieldName"); // NOI18N

        jTextFieldCount.setText(resourceMap.getString("jTextFieldCount.text")); // NOI18N
        jTextFieldCount.setName("jTextFieldCount"); // NOI18N

        jButtonUpdate.setText(resourceMap.getString("jButtonUpdate.text")); // NOI18N
        jButtonUpdate.setName("jButtonUpdate"); // NOI18N
        jButtonUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonUpdateMouseClicked(evt);
            }
        });

        jButtonDelete.setText(resourceMap.getString("jButtonDelete.text")); // NOI18N
        jButtonDelete.setName("jButtonDelete"); // NOI18N
        jButtonDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonDeleteMouseClicked(evt);
            }
        });

        jButtonReload.setText(resourceMap.getString("jButtonReload.text")); // NOI18N
        jButtonReload.setName("jButtonReload"); // NOI18N
        jButtonReload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonReloadMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonReload, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(jButtonDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(jButtonUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(jButtonInsert, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jTextFieldNameProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jTextFieldCount, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jButtonInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonReload, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNameProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCount, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(testdbapplication.TestDBApplication.class).getContext().getActionMap(TestDBView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
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
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 386, Short.MAX_VALUE)
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

    private void jButtonInsertMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonInsertMouseClicked
        try
        {
            st.execute("insert into 'Goods' ('name', 'price', 'count') values ('" + jTextFieldNameProduct.getText() 
                    + "', '" + jTextFieldPrice.getText() + "' , " + jTextFieldCount.getText() + "); ");
            jTextFieldNameProduct.setText("");
            jTextFieldPrice.setText("");
            jTextFieldCount.setText("");
            jButtonReloadMouseClicked(evt);
            JOptionPane.showMessageDialog(this.mainPanel, "Data was added!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this.mainPanel, "Error: " + ex.getMessage());}
    }//GEN-LAST:event_jButtonInsertMouseClicked

    private void jButtonReloadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonReloadMouseClicked
        try
        {
            for(int i = 0; i < jTableGoods.getRowCount(); i++)
            {
                jTableGoods.setValueAt("", i, 0);
                jTableGoods.setValueAt("", i, 1);
                jTableGoods.setValueAt("", i, 2);
                jTableGoods.setValueAt("", i, 3);
            }
            rs = st.executeQuery("select * from Goods");
            int i = 0;
            while (rs.next())
            {
                jTableGoods.setValueAt(rs.getString(1), i, 0);
                jTableGoods.setValueAt(rs.getString(2), i, 1);
                jTableGoods.setValueAt(rs.getString(3), i, 2);
                jTableGoods.setValueAt(rs.getString(4), i, 3);
                i++;
            }
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this.mainPanel, "Error: " + ex.getMessage());}
    }//GEN-LAST:event_jButtonReloadMouseClicked

    private void jButtonDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDeleteMouseClicked
        try
        {
            String id = jTableGoods.getValueAt(jTableGoods.getSelectedRow(), 0).toString();
            st.execute("delete from 'Goods' where id = " + id + ";");
            JOptionPane.showMessageDialog(this.mainPanel, "Data was deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
            jButtonReloadMouseClicked(evt);
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this.mainPanel, "Error: " + ex.getMessage());}
    }//GEN-LAST:event_jButtonDeleteMouseClicked

    private void jButtonUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUpdateMouseClicked
        try
        {
            String id = jTableGoods.getValueAt(jTableGoods.getSelectedRow(), 0).toString();
            st.execute("update 'Goods' set name='" + jTextFieldNameProduct.getText() + "', price='" + jTextFieldPrice.getText()
                    + "', count='" + jTextFieldCount.getText() + "' where id = " + id + ";");
            JOptionPane.showMessageDialog(this.mainPanel, "Data was updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            jButtonReloadMouseClicked(evt);
        }
        catch(Exception ex) { JOptionPane.showMessageDialog(this.mainPanel, "Error: " + ex.getMessage());}
    }//GEN-LAST:event_jButtonUpdateMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonInsert;
    private javax.swing.JButton jButtonReload;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableGoods;
    private javax.swing.JTextField jTextFieldCount;
    private javax.swing.JTextField jTextFieldNameProduct;
    private javax.swing.JTextField jTextFieldPrice;
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
}
