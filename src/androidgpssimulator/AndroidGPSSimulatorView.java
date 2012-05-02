/*
 * AndroidGPSSimulatorView.java
 */

package androidgpssimulator;

import androidgpssimulator.gui.ConfigDialog;
import androidgpssimulator.gui.ErrorDialog;
import androidgpssimulator.kml.KMLToRute;
import androidgpssimulator.locationSender.Location;
import androidgpssimulator.locationSender.LocationSender;
import androidgpssimulator.locationSender.LocationSenderDisconnectedException;
import androidgpssimulator.locationSender.LocationSenderUnknowException;
import androidgpssimulator.telnet.ConfigTelnet;
import java.io.IOException;
import org.jdesktop.application.Task;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 * The application's main frame.
 */
public class AndroidGPSSimulatorView extends FrameView {

    public AndroidGPSSimulatorView(SingleFrameApplication app) {
        super(app);

        initComponents();

        //Redirijimos "System.out.println()" a la consola.
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                taConsola.append(new String(new char[]{(char)b}));
                taConsola.setCaretPosition(taConsola.getDocument().getLength());
            }
        }));

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
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = AndroidGPSSimulatorApp.getApplication().getMainFrame();
            aboutBox = new AndroidGPSSimulatorAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        AndroidGPSSimulatorApp.getApplication().show(aboutBox);
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
        taConsola = new javax.swing.JTextArea();
        tbBarraHerramientas = new javax.swing.JToolBar();
        btAbrir = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        tbtConectar = new javax.swing.JToggleButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pManual = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfLatitud = new javax.swing.JTextField();
        tfLongitud = new javax.swing.JTextField();
        btEnviar = new javax.swing.JButton();
        rbDecimal = new javax.swing.JRadioButton();
        rbSexagesimal = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        tfAltitud = new javax.swing.JTextField();
        pKML = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableLocations = new javax.swing.JTable();
        btCargar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btBack = new javax.swing.JButton();
        btPlay = new javax.swing.JButton();
        btNext = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tfTiempo = new javax.swing.JTextField();
        tbtRepetir = new javax.swing.JToggleButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        miAbrir = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        menuEditar = new javax.swing.JMenu();
        miPreferencias = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        bgFormatoLatitud = new javax.swing.ButtonGroup();

        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(androidgpssimulator.AndroidGPSSimulatorApp.class).getContext().getResourceMap(AndroidGPSSimulatorView.class);
        taConsola.setBackground(resourceMap.getColor("taConsola.background")); // NOI18N
        taConsola.setColumns(20);
        taConsola.setEditable(false);
        taConsola.setForeground(resourceMap.getColor("taConsola.foreground")); // NOI18N
        taConsola.setLineWrap(true);
        taConsola.setRows(5);
        taConsola.setText(resourceMap.getString("taConsola.text")); // NOI18N
        taConsola.setName("taConsola"); // NOI18N
        jScrollPane1.setViewportView(taConsola);

        tbBarraHerramientas.setFloatable(false);
        tbBarraHerramientas.setRollover(true);
        tbBarraHerramientas.setName("tbBarraHerramientas"); // NOI18N

        btAbrir.setIcon(resourceMap.getIcon("btAbrir.icon")); // NOI18N
        btAbrir.setText(resourceMap.getString("btAbrir.text")); // NOI18N
        btAbrir.setFocusable(false);
        btAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAbrir.setName("btAbrir"); // NOI18N
        btAbrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbBarraHerramientas.add(btAbrir);

        jSeparator1.setName("jSeparator1"); // NOI18N
        tbBarraHerramientas.add(jSeparator1);

        tbtConectar.setIcon(resourceMap.getIcon("tbtConectar.icon")); // NOI18N
        tbtConectar.setText(resourceMap.getString("tbtConectar.text")); // NOI18N
        tbtConectar.setFocusable(false);
        tbtConectar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tbtConectar.setName("tbtConectar"); // NOI18N
        tbtConectar.setPressedIcon(resourceMap.getIcon("tbtConectar.pressedIcon")); // NOI18N
        tbtConectar.setSelectedIcon(resourceMap.getIcon("tbtConectar.selectedIcon")); // NOI18N
        tbtConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtConectarActionPerformed(evt);
            }
        });
        tbBarraHerramientas.add(tbtConectar);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        pManual.setName("pManual"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        tfLatitud.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfLatitud.setText(resourceMap.getString("tfLatitud.text")); // NOI18N
        tfLatitud.setMaximumSize(new java.awt.Dimension(6, 200));
        tfLatitud.setName("tfLatitud"); // NOI18N

        tfLongitud.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfLongitud.setText(resourceMap.getString("tfLongitud.text")); // NOI18N
        tfLongitud.setName("tfLongitud"); // NOI18N

        btEnviar.setText(resourceMap.getString("btEnviar.text")); // NOI18N
        btEnviar.setName("btEnviar"); // NOI18N
        btEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEnviarActionPerformed(evt);
            }
        });

        bgFormatoLatitud.add(rbDecimal);
        rbDecimal.setSelected(true);
        rbDecimal.setText(resourceMap.getString("rbDecimal.text")); // NOI18N
        rbDecimal.setName("rbDecimal"); // NOI18N

        bgFormatoLatitud.add(rbSexagesimal);
        rbSexagesimal.setText(resourceMap.getString("rbSexagesimal.text")); // NOI18N
        rbSexagesimal.setEnabled(false);
        rbSexagesimal.setName("rbSexagesimal"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        tfAltitud.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfAltitud.setText(resourceMap.getString("tfAltitud.text")); // NOI18N
        tfAltitud.setName("tfAltitud"); // NOI18N

        javax.swing.GroupLayout pManualLayout = new javax.swing.GroupLayout(pManual);
        pManual.setLayout(pManualLayout);
        pManualLayout.setHorizontalGroup(
            pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pManualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pManualLayout.createSequentialGroup()
                        .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pManualLayout.createSequentialGroup()
                                .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfLatitud, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                    .addComponent(tfLongitud)
                                    .addComponent(tfAltitud))
                                .addGap(87, 87, 87))
                            .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(rbDecimal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rbSexagesimal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(371, Short.MAX_VALUE))
                    .addGroup(pManualLayout.createSequentialGroup()
                        .addComponent(btEnviar)
                        .addContainerGap(645, Short.MAX_VALUE))))
        );
        pManualLayout.setVerticalGroup(
            pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pManualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfLatitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pManualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfAltitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(rbDecimal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSexagesimal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btEnviar)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("pManual.TabConstraints.tabTitle"), pManual); // NOI18N

        pKML.setName("pKML"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableLocations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Latitud", "Longitud", "Elevación"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableLocations.setName("tableLocations"); // NOI18N
        jScrollPane2.setViewportView(tableLocations);

        btCargar.setText(resourceMap.getString("btCargar.text")); // NOI18N
        btCargar.setName("btCargar"); // NOI18N
        btCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCargarActionPerformed(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        btBack.setIcon(resourceMap.getIcon("btBack.icon")); // NOI18N
        btBack.setText(resourceMap.getString("btBack.text")); // NOI18N
        btBack.setName("btBack"); // NOI18N

        btPlay.setIcon(resourceMap.getIcon("btPlay.icon")); // NOI18N
        btPlay.setText(resourceMap.getString("btPlay.text")); // NOI18N
        btPlay.setName("btPlay"); // NOI18N
        btPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPlayActionPerformed(evt);
            }
        });

        btNext.setIcon(resourceMap.getIcon("btNext.icon")); // NOI18N
        btNext.setText(resourceMap.getString("btNext.text")); // NOI18N
        btNext.setName("btNext"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        tfTiempo.setText(resourceMap.getString("tfTiempo.text")); // NOI18N
        tfTiempo.setName("tfTiempo"); // NOI18N

        tbtRepetir.setText(resourceMap.getString("tbtRepetir.text")); // NOI18N
        tbtRepetir.setName("tbtRepetir"); // NOI18N

        javax.swing.GroupLayout pKMLLayout = new javax.swing.GroupLayout(pKML);
        pKML.setLayout(pKMLLayout);
        pKMLLayout.setHorizontalGroup(
            pKMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pKMLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pKMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pKMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btCargar)
                        .addGroup(pKMLLayout.createSequentialGroup()
                            .addGroup(pKMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btBack, 0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btPlay)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btNext, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel4)
                        .addComponent(tfTiempo))
                    .addComponent(tbtRepetir))
                .addGap(92, 92, 92))
        );
        pKMLLayout.setVerticalGroup(
            pKMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pKMLLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pKMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addGroup(pKMLLayout.createSequentialGroup()
                        .addComponent(btCargar)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pKMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btBack)
                            .addComponent(btPlay)
                            .addComponent(btNext))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbtRepetir)))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("pKML.TabConstraints.tabTitle"), pKML); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(tbBarraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(tbBarraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        miAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        miAbrir.setText(resourceMap.getString("miAbrir.text")); // NOI18N
        miAbrir.setName("miAbrir"); // NOI18N
        fileMenu.add(miAbrir);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(androidgpssimulator.AndroidGPSSimulatorApp.class).getContext().getActionMap(AndroidGPSSimulatorView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        menuEditar.setText(resourceMap.getString("menuEditar.text")); // NOI18N
        menuEditar.setName("menuEditar"); // NOI18N

        miPreferencias.setText(resourceMap.getString("miPreferencias.text")); // NOI18N
        miPreferencias.setName("miPreferencias"); // NOI18N
        miPreferencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPreferenciasActionPerformed(evt);
            }
        });
        menuEditar.add(miPreferencias);

        menuBar.add(menuEditar);

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
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 573, Short.MAX_VALUE)
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

    private void miPreferenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPreferenciasActionPerformed
        editarPreferencias();
    }//GEN-LAST:event_miPreferenciasActionPerformed

    private void tbtConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtConectarActionPerformed
        conectar_desconectar();
    }//GEN-LAST:event_tbtConectarActionPerformed

    private void btEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEnviarActionPerformed
        enviarLocalizacion();
    }//GEN-LAST:event_btEnviarActionPerformed

    private void btCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCargarActionPerformed
        abrirLocalizaciones();
    }//GEN-LAST:event_btCargarActionPerformed

    private void btPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPlayActionPerformed
        System.out.println("Pulsado play");
        enviarLocalizaciones();
    }//GEN-LAST:event_btPlayActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgFormatoLatitud;
    private javax.swing.JButton btAbrir;
    private javax.swing.JButton btBack;
    private javax.swing.JButton btCargar;
    private javax.swing.JButton btEnviar;
    private javax.swing.JButton btNext;
    private javax.swing.JButton btPlay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JMenuItem miAbrir;
    private javax.swing.JMenuItem miPreferencias;
    private javax.swing.JPanel pKML;
    private javax.swing.JPanel pManual;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JRadioButton rbDecimal;
    private javax.swing.JRadioButton rbSexagesimal;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextArea taConsola;
    private javax.swing.JTable tableLocations;
    private javax.swing.JToolBar tbBarraHerramientas;
    private javax.swing.JToggleButton tbtConectar;
    private javax.swing.JToggleButton tbtRepetir;
    private javax.swing.JTextField tfAltitud;
    private javax.swing.JTextField tfLatitud;
    private javax.swing.JTextField tfLongitud;
    private javax.swing.JTextField tfTiempo;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    /* ######################### MIS VARIABLES ########################### */
    private ConfigTelnet configuracion = ConfigTelnet.getDefault();
    private LocationSender transmisor = new LocationSender(configuracion);
    private Location localizacion;
    private List<Location> localizaciones = null;
    /* ######################### MIS FUNCIONES ########################### */
    public void editarPreferencias(){
        if(configuracion == null)
            configuracion = ConfigTelnet.getDefault();

        ConfigDialog dialog = new ConfigDialog(this.getFrame(), configuracion);
        if(dialog.mostrar(true) == ConfigDialog.APROVE){
            agregarInfConsola("Configuración cambiada a: \nDirección = "
                    + configuracion.getHost()
                    + "\nPuerto = " + configuracion.getPort()
                    + "\n");

            //Conectamos y reconectamos para que cargue la nueva configuración
            if(transmisor.isConnected()){
                conectar_desconectar();
                conectar_desconectar();
            }
        }
    }

    /**
     * Añade el texto pasado como argumento a la "consola".
     * 
     * @param text
     */
    private void agregarInfConsola(String text){
        taConsola.append(text);
    }

    private void conectar_desconectar(){
        Task t = null;
        if(transmisor.isConnected()){
            //Desconectamos
            t = new Task(this.getApplication()) {

                @Override
                protected Object doInBackground() throws Exception {
                    this.setMessage("Desconectado...");
                    boolean desconectado = false;
                    try{
                        desconectado = transmisor.disconnect();

                        this.setMessage("Desconectado");
                        tbtConectar.setSelected(!desconectado);
                        tbtConectar.setText("Conectar");
                    }catch(Exception e){
                        tbtConectar.setSelected(!desconectado);
                        tbtConectar.setText("Desconectar");

                        mostrarDialogoError(e);
                    }

                    return null;
                }
            };
        }
        else{
            //Conectamos
            t = new Task(this.getApplication()) {

                @Override
                protected Object doInBackground() throws Exception {
                    boolean conectado = false;
                    this.setMessage("Conectando a " + configuracion.getHost() + ":" + configuracion.getPort() + " ...");
                    try{
                        conectado = transmisor.connect();

                        this.setMessage("Conectado a " + configuracion.getHost() + ":" + configuracion.getPort());
                        tbtConectar.setSelected(conectado);
                        tbtConectar.setText("Desconectar");
                    }catch(Exception e){
                        tbtConectar.setSelected(conectado);
                        tbtConectar.setText("Conectar");
                        
                        mostrarDialogoError(e);
                    }

                    return null;
                }
            };
        }

        //Ejecutamos la acción
        ejecutarTarea(t);
    }

    private void enviarLocalizacion(){
        try{
            double lat = Double.parseDouble(tfLatitud.getText());
            double longit = Double.parseDouble(tfLongitud.getText());
            int altitud = Integer.parseInt(tfAltitud.getText());

            if(localizacion == null)
                localizacion = new Location(lat, longit, 0);
            else{
                localizacion.setLatitude(lat);
                localizacion.setLongitude(longit);
                localizacion.setAltitude(altitud);
            }

            Task t = new Task(this.getApplication()) {
                @Override
                protected Object doInBackground() throws Exception {
                    this.setMessage("Transmitiendo localización...");
                    try{
                        transmisor.send(localizacion);
                        this.setMessage("Localización transmitida");
                    }catch(LocationSenderDisconnectedException e){
                        mostrarDialogoError(e);
                    }catch(LocationSenderUnknowException e){
                        mostrarDialogoError(e);
                    }
                    return null;
                }
            };
            ejecutarTarea(t);

        }catch(NumberFormatException e){
            mostrarDialogoError(e);
        }
    }

    private void abrirLocalizaciones(){
        JFileChooser dialog = new JFileChooser();
        dialog.setDialogType(JFileChooser.OPEN_DIALOG);
        dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        dialog.setMultiSelectionEnabled(false);

        if(dialog.showOpenDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION){
            try {
                localizaciones = KMLToRute.KMLToRute(dialog.getSelectedFile());

                //Borramos las antiguas filas.
                DefaultTableModel model = (DefaultTableModel) tableLocations.getModel();
                for(int i=0; i < model.getRowCount(); i++)
                    model.removeRow(i);


                Task t = new Task(this.getApplication()) {

                    @Override
                    protected Object doInBackground() throws Exception {
                        this.setMessage("Abriendo KML...");
                        Iterator<Location> it = localizaciones.iterator();
                        DefaultTableModel model = (DefaultTableModel) tableLocations.getModel();
                        while (it.hasNext()) {
                            Location loc = it.next();
                            model.addRow(new Object[]{loc.getLatitude(), loc.getLongitude(), loc.getAltitude()});
                        }
                        this.setMessage("Localizaciones cargadas");
                        return null;
                    }
                };
                ejecutarTarea(t);
            } catch (Exception ex) {
                mostrarDialogoError(ex);
            }
        }
    }

    private void enviarLocalizaciones(){
        Task t = new Task(this.getApplication()) {

            @Override
            protected Object doInBackground() throws Exception {

                float total = localizaciones.size();
                int espera = Integer.parseInt(tfTiempo.getText());
                System.out.println("Transmitiendo localizaciones");
                try{
                    do{
                        Iterator<Location> it = localizaciones.iterator();
                        float actual = 0.0f;
                        while(it.hasNext()){
                            Location loc = it.next();

                            this.setProgress(actual / total);
                            this.setMessage("Transmitiendo: " + loc);

                            System.out.println("Transmitiendo: " + loc);

                            try{
                                transmisor.send(loc);
                                //tableLocations.changeSelection((int)actual, 1, false, false);
                            }
                            catch(LocationSenderUnknowException e){
                                this.setMessage("Fallo al transmitir " + loc);
                            }
                            this.getTaskService().awaitTermination(espera, TimeUnit.MILLISECONDS);
                            actual += 1.0;
                        }
                        this.setMessage("Transmitidos todas las localizaciones");
                        this.setProgress(1.0f);
                    }while(tbtRepetir.isSelected());
                }
                catch(LocationSenderDisconnectedException e){
                    mostrarDialogoError(e);
                }

                return null;
            }
        };
        ejecutarTarea(t);
    }

    private void ejecutarTarea(Task t){
        this.getApplication().getContext().getTaskService().execute(t);
    }

    private void mostrarDialogoError(Exception e){
        System.out.println("Error: " + e.getMessage());
        JDialog dialog = new ErrorDialog(this.getFrame(), e);
        dialog.setVisible(true);
    }
}
