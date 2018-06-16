/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import static project_aed2_lp2.GestorMain.AddEquipamentoCasa;
import static project_aed2_lp2.GestorMain.ConnectVertices;
import static project_aed2_lp2.GestorMain.DeleteEquipamentoCasa;
import static project_aed2_lp2.GestorMain.LoadFromFileCasa;
import static project_aed2_lp2.GestorMain.LoadFromFileCentralE;
import static project_aed2_lp2.GestorMain.LoadFromFileEquipamentos;
import static project_aed2_lp2.GestorMain.LoadFromFileFonteE;
import static project_aed2_lp2.GestorMain.LoadGraph;
import static project_aed2_lp2.GestorMain.LoadGraphFile;
import static project_aed2_lp2.GestorMain.LoadToFileCasasBin;
import static project_aed2_lp2.GestorMain.LoadToFileEquipamentosBin;
import static project_aed2_lp2.GestorMain.casasST;
import static project_aed2_lp2.GestorMain.centralST;
import static project_aed2_lp2.GestorMain.equipamentosST;
import static project_aed2_lp2.GestorMain.fonteST;
import static project_aed2_lp2.GestorMain.SaveToFileBin;
import static project_aed2_lp2.GestorMain.dg;
import static project_aed2_lp2.GestorMain.SaveToFileAll;
import static project_aed2_lp2.GestorMain.casasST;
import static project_aed2_lp2.GestorMain.centralST;
import static project_aed2_lp2.GestorMain.desligarEquipamentoCasa;
import static project_aed2_lp2.GestorMain.dg;
import static project_aed2_lp2.GestorMain.equipamentosST;
import static project_aed2_lp2.GestorMain.fonteST;
import static project_aed2_lp2.GestorMain.getCentralPercentage;
import static project_aed2_lp2.GestorMain.getCentralPower;
import static project_aed2_lp2.GestorMain.getFontePercentage;
import static project_aed2_lp2.GestorMain.getFontePower;
import static project_aed2_lp2.GestorMain.ligarEquipamentoCasa;
import static project_aed2_lp2.GestorMain.printCasaAllInfo;
import static project_aed2_lp2.GestorMain.removeFlowEdge;
import static project_aed2_lp2.GestorMain.removeFlowEdgeLigacao;

/**
 *
 * @author pedro
 */
public class GUI extends javax.swing.JFrame {

    boolean nodo2 = false;
    Vertice origem;
    Vertice destino;
    public List<Vertice> vertices;
    public List<Aresta> arestas;

    public List<Vertice> getNodos() {
        return vertices;
    }

    public List<Aresta> getArcos() {
        return arestas;
    }

    public void setNodos(List<Vertice> vertices) {
        this.vertices = vertices;
    }

    public void setArcos(List<Aresta> arestas) {
        this.arestas = arestas;
    }

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
    }

    //@Override
    public void printGraph(Graphics g) {
        this.jPanel1.paint(g);

        //PARA DESENHAR VERTICES
        for (int i = 2; i < vertices.size(); i++) {

            if (casasST.contains(i)) {

                Image theImage = null;
                try {
                    theImage = ImageIO.read(new File(System.getProperty("user.dir") + "//images//house.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.drawImage(theImage, vertices.get(i).getPonto().x, vertices.get(i).getPonto().y, 50, 50, rootPane);

            } else if (fonteST.contains(i)) {

                Image theImage = null;
                try {
                    theImage = ImageIO.read(new File(System.getProperty("user.dir") + "//images//fonte.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.drawImage(theImage, vertices.get(i).getPonto().x, vertices.get(i).getPonto().y, 50, 50, rootPane);

            } else if (centralST.contains(i)) {

                Image theImage = null;
                try {
                    theImage = ImageIO.read(new File(System.getProperty("user.dir") + "//images//central.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.drawImage(theImage, vertices.get(i).getPonto().x, vertices.get(i).getPonto().y, 50, 50, rootPane);

            }
            //g.fillRect(vertices.get(i).getPonto().x, vertices.get(i).getPonto().y, 50, 50);
            g.setColor(Color.RED);
            g.setFont(new Font("Serif", Font.BOLD, 16));
            g.drawString(vertices.get(i).getId().toString(), vertices.get(i).getPonto().x + 22, vertices.get(i).getPonto().y - 5);

            g.setColor(Color.white);
            //g.drawOval(vertices.get(i).getPonto().x, vertices.get(i).getPonto().y, 50, 50);

        }

        //PARA DESENHAR ARESTAS
        Point p1 = new Point();
        Point p2 = new Point();

        for (int i = 0; i < arestas.size(); i++) {
            //System.out.println("" + arestas.get(i).getDestino().getId());
            p1.x = arestas.get(i).getDestino().getPonto().x;
            p1.y = arestas.get(i).getDestino().getPonto().y;
            p2.x = arestas.get(i).getOrigem().getPonto().x;
            p2.y = arestas.get(i).getOrigem().getPonto().y;

            g.setColor(Color.BLUE);
            g.drawString(String.valueOf(arestas.get(i).getPeso()), (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);

            //g.setColor(Color.GREEN);

            double ang = 0.0, angSep = 0.0;
            double tx, ty;
            int dist = 0;
            Point ponto1 = null, ponto2 = null;

            ponto1 = new Point(p2.x + 25, p2.y + 25);
            ponto2 = new Point(p1.x + 15, p1.y + 15);

            dist = 10;

            ty = -(ponto1.y - ponto2.y) * 1.0;
            tx = (ponto1.x - ponto2.x) * 1.0;
            //angulo
            ang = Math.atan(ty / tx);

            if (tx < 0) {
                ang += Math.PI;
            }

            Point ponto = ponto2;

            angSep = 25.0;

            p1.x = (int) (ponto.x + dist * Math.cos(ang - Math.toRadians(angSep)));
            p1.y = (int) (ponto.y - dist * Math.sin(ang - Math.toRadians(angSep)));
            p2.x = (int) (ponto.x + dist * Math.cos(ang + Math.toRadians(angSep)));
            p2.y = (int) (ponto.y - dist * Math.sin(ang + Math.toRadians(angSep)));

            Graphics2D g2D = (Graphics2D) g;

            g.setColor(Color.BLACK);

            g2D.setStroke(new BasicStroke(1.8f));

            g.drawLine(ponto1.x, ponto1.y, ponto.x+14, ponto.y+32);

            g.drawLine(p1.x+14, p1.y+32, ponto.x+14, ponto.y+32);
            g.drawLine(p2.x+14, p2.y+32, ponto.x+14, ponto.y+32);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelCasas = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButtonOpenFromFile = new javax.swing.JButton();
        jButtonOpenFromBin = new javax.swing.JButton();
        jButtonCriarCasa = new javax.swing.JButton();
        jButtonListarCasas = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonConsumoAtual = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListCasasPesquisas = new javax.swing.JList<>();
        jButtonConsumoEntre = new javax.swing.JButton();
        jButtonBiggestConsumer = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListCasasEquipamentos = new javax.swing.JList<>();
        jToggleOnOff = new javax.swing.JToggleButton();
        jButtonListarEquip = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListCasasCasasEquipamentos = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jDateInicio = new com.toedter.calendar.JDateChooser();
        jDateFim = new com.toedter.calendar.JDateChooser();
        jButtonRemoveEquip = new javax.swing.JButton();
        jPanelEquipamentos = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListEquipamentos = new javax.swing.JList<>();
        jButtonCriarEquip = new javax.swing.JButton();
        jButtonOpenEquipsFile = new javax.swing.JButton();
        jButtonOpenEquipsBin = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jButtonListarEquipamentos = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListAssociarEquip = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jListEquipamentosCasas = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jButtonAssociarEquip = new javax.swing.JButton();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanelCentrais = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jButtonListarCentrais = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jListCentraisEletricas = new javax.swing.JList<>();
        jButtonCapacidadeCentral = new javax.swing.JButton();
        jButtonConsumoWattsCentral = new javax.swing.JButton();
        jButtonConsumoPercentCentral = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jListSelectCentral = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jListSelectFonte = new javax.swing.JList<>();
        jButton9 = new javax.swing.JButton();
        jButtonDesconectar = new javax.swing.JButton();
        jPanelPostos = new javax.swing.JPanel();
        jSeparator9 = new javax.swing.JSeparator();
        jButtonCriarFonte = new javax.swing.JButton();
        jButtonOpenFromFileFonte = new javax.swing.JButton();
        jButtonListarFontes = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jListFontes = new javax.swing.JList<>();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jScrollPane12 = new javax.swing.JScrollPane();
        jListFonteAFonte1 = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        jListFonteAFonte2 = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jListFonteACasa = new javax.swing.JList<>();
        jScrollPane15 = new javax.swing.JScrollPane();
        jListFonteCasas = new javax.swing.JList<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanelGrafo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButtonImprimir = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemSaveAll = new javax.swing.JMenuItem();
        jMenuItemSaveALlBin = new javax.swing.JMenuItem();
        jMenuItemOpenAll = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("GUARDAR TUDO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButtonOpenFromFile.setText("OPEN FROM FILE");
        jButtonOpenFromFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenFromFileActionPerformed(evt);
            }
        });

        jButtonOpenFromBin.setText("OPEN FROM BIN FILE");
        jButtonOpenFromBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenFromBinActionPerformed(evt);
            }
        });

        jButtonCriarCasa.setText("CRIAR CASA");
        jButtonCriarCasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCriarCasaActionPerformed(evt);
            }
        });

        jButtonListarCasas.setText("LISTAR CASAS");
        jButtonListarCasas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListarCasasActionPerformed(evt);
            }
        });

        jButtonConsumoAtual.setText("CONSUMO ATUAL");
        jButtonConsumoAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsumoAtualActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jListCasasPesquisas);

        jButtonConsumoEntre.setText("CONSUMO ENTRE");
        jButtonConsumoEntre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsumoEntreActionPerformed(evt);
            }
        });

        jButtonBiggestConsumer.setText("MAIOR CONSUMIDOR");
        jButtonBiggestConsumer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBiggestConsumerActionPerformed(evt);
            }
        });

        jListCasasEquipamentos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCasasEquipamentosValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(jListCasasEquipamentos);

        jToggleOnOff.setText("-");
        jToggleOnOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleOnOffActionPerformed(evt);
            }
        });

        jButtonListarEquip.setText("LISTAR EQUIPAMENTOS");
        jButtonListarEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListarEquipActionPerformed(evt);
            }
        });

        jScrollPane5.setViewportView(jListCasasCasasEquipamentos);

        jLabel3.setText("SELECIONE A CASA:");

        jLabel4.setText("SELECIONE O EQUIPAMENTO:");

        jLabel5.setText("SELECIONE A CASA:");

        jButtonRemoveEquip.setText("REMOVER EQUIPAMENTO");
        jButtonRemoveEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveEquipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCasasLayout = new javax.swing.GroupLayout(jPanelCasas);
        jPanelCasas.setLayout(jPanelCasasLayout);
        jPanelCasasLayout.setHorizontalGroup(
            jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCasasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCasasLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonBiggestConsumer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonConsumoEntre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonConsumoAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jDateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDateFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(286, 286, 286))
                    .addGroup(jPanelCasasLayout.createSequentialGroup()
                        .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator6)
                            .addGroup(jPanelCasasLayout.createSequentialGroup()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelCasasLayout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanelCasasLayout.createSequentialGroup()
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jToggleOnOff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButtonListarEquip, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButtonRemoveEquip, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1)))))
                        .addContainerGap())
                    .addGroup(jPanelCasasLayout.createSequentialGroup()
                        .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCasasLayout.createSequentialGroup()
                                .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonCriarCasa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonOpenFromFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonOpenFromBin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonListarCasas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSeparator3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanelCasasLayout.setVerticalGroup(
            jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCasasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelCasasLayout.createSequentialGroup()
                        .addComponent(jButtonCriarCasa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpenFromFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpenFromBin)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonListarCasas))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCasasLayout.createSequentialGroup()
                        .addComponent(jButtonConsumoAtual)
                        .addGap(7, 7, 7)
                        .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonConsumoEntre)
                            .addComponent(jDateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonBiggestConsumer))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCasasLayout.createSequentialGroup()
                        .addComponent(jButtonListarEquip)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRemoveEquip)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCasasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jToggleOnOff, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );

        jTabbedPane.addTab("CASAS", jPanelCasas);

        jScrollPane3.setViewportView(jListEquipamentos);

        jButtonCriarEquip.setText("CRIAR EQUIPAMENTO");
        jButtonCriarEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCriarEquipActionPerformed(evt);
            }
        });

        jButtonOpenEquipsFile.setText("OPEN FROM FILE");
        jButtonOpenEquipsFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenEquipsFileActionPerformed(evt);
            }
        });

        jButtonOpenEquipsBin.setText("OPEN FROM BIN FILE");
        jButtonOpenEquipsBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenEquipsBinActionPerformed(evt);
            }
        });

        jButtonListarEquipamentos.setText("LISTAR EQUIPAMENTOS");
        jButtonListarEquipamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListarEquipamentosActionPerformed(evt);
            }
        });

        jScrollPane6.setViewportView(jListAssociarEquip);

        jLabel1.setText("SELECIONE O EQUIPAMENTO:");

        jScrollPane7.setViewportView(jListEquipamentosCasas);

        jLabel2.setText("SELECIONE A CASA:");

        jButtonAssociarEquip.setText("ASSOCIAR EQUIPAMENTO");
        jButtonAssociarEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAssociarEquipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEquipamentosLayout = new javax.swing.GroupLayout(jPanelEquipamentos);
        jPanelEquipamentos.setLayout(jPanelEquipamentosLayout);
        jPanelEquipamentosLayout.setHorizontalGroup(
            jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEquipamentosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4)
                    .addGroup(jPanelEquipamentosLayout.createSequentialGroup()
                        .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonListarEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonOpenEquipsBin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonOpenEquipsFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonCriarEquip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator5))
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator7)
                    .addGroup(jPanelEquipamentosLayout.createSequentialGroup()
                        .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelEquipamentosLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 430, Short.MAX_VALUE))
                            .addGroup(jPanelEquipamentosLayout.createSequentialGroup()
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonAssociarEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))))
                .addContainerGap())
        );
        jPanelEquipamentosLayout.setVerticalGroup(
            jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEquipamentosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelEquipamentosLayout.createSequentialGroup()
                        .addComponent(jButtonCriarEquip)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpenEquipsFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpenEquipsBin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonListarEquipamentos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3))
                .addGap(18, 18, 18)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                        .addComponent(jScrollPane6))
                    .addComponent(jButtonAssociarEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(280, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("EQUIPAMENTOS", jPanelEquipamentos);

        jButton2.setText("CRIAR CENTRAL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("OPEN FROM FILE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButtonListarCentrais.setText("LISTAR CENTRAIS");
        jButtonListarCentrais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListarCentraisActionPerformed(evt);
            }
        });

        jScrollPane8.setViewportView(jListCentraisEletricas);

        jButtonCapacidadeCentral.setText("CAPACIDADE");
        jButtonCapacidadeCentral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCapacidadeCentralActionPerformed(evt);
            }
        });

        jButtonConsumoWattsCentral.setText("CONSUMO (Watts)");
        jButtonConsumoWattsCentral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsumoWattsCentralActionPerformed(evt);
            }
        });

        jButtonConsumoPercentCentral.setText("CONSUMO (%)");
        jButtonConsumoPercentCentral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsumoPercentCentralActionPerformed(evt);
            }
        });

        jLabel6.setText("SELECIONE A CENTRAL:");

        jScrollPane10.setViewportView(jListSelectCentral);

        jLabel7.setText("SELECIONE A FONTE:");

        jScrollPane11.setViewportView(jListSelectFonte);

        jButton9.setText("CONECTAR");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButtonDesconectar.setText("DESCONECTAR");
        jButtonDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDesconectarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCentraisLayout = new javax.swing.GroupLayout(jPanelCentrais);
        jPanelCentrais.setLayout(jPanelCentraisLayout);
        jPanelCentraisLayout.setHorizontalGroup(
            jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCentraisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator10)
                    .addComponent(jSeparator8)
                    .addGroup(jPanelCentraisLayout.createSequentialGroup()
                        .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonListarCentrais, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCentraisLayout.createSequentialGroup()
                                .addComponent(jButtonCapacidadeCentral, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonConsumoWattsCentral)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonConsumoPercentCentral)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)))
                    .addGroup(jPanelCentraisLayout.createSequentialGroup()
                        .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanelCentraisLayout.createSequentialGroup()
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonDesconectar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCentraisLayout.setVerticalGroup(
            jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCentraisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCentraisLayout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonListarCentrais))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonConsumoWattsCentral)
                        .addComponent(jButtonConsumoPercentCentral))
                    .addComponent(jButtonCapacidadeCentral))
                .addGap(16, 16, 16)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCentraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                        .addComponent(jScrollPane11))
                    .addGroup(jPanelCentraisLayout.createSequentialGroup()
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDesconectar)))
                .addContainerGap(214, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("CENTRAIS", jPanelCentrais);

        jButtonCriarFonte.setText("CRIAR FONTE");
        jButtonCriarFonte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCriarFonteActionPerformed(evt);
            }
        });

        jButtonOpenFromFileFonte.setText("OPEN FROM FILE");
        jButtonOpenFromFileFonte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenFromFileFonteActionPerformed(evt);
            }
        });

        jButtonListarFontes.setText("LISTAR FONTES");
        jButtonListarFontes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListarFontesActionPerformed(evt);
            }
        });

        jScrollPane9.setViewportView(jListFontes);

        jButton4.setText("CAPACIDADE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("CONSUMO (Watts)");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("CONSUMO (%)");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jScrollPane12.setViewportView(jListFonteAFonte1);

        jLabel8.setText("SELECIONE A FONTE ORIGEM:");

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton10.setText("CONECTAR");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("DESCONECTAR");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jScrollPane13.setViewportView(jListFonteAFonte2);

        jLabel9.setText("SELECIONE FONTE DESTINO:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("INTERLIGAR FONTES:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("INTERLIGAR UMA FONTE A UMA CASA:");

        jScrollPane14.setViewportView(jListFonteACasa);

        jScrollPane15.setViewportView(jListFonteCasas);

        jLabel12.setText("SELECIONE A FONTE:");

        jLabel13.setText("SELECIONE A CASA:");

        jButton12.setText("CONECTAR");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("DESCONECTAR");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPostosLayout = new javax.swing.GroupLayout(jPanelPostos);
        jPanelPostos.setLayout(jPanelPostosLayout);
        jPanelPostosLayout.setHorizontalGroup(
            jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPostosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator11)
                    .addComponent(jSeparator9)
                    .addGroup(jPanelPostosLayout.createSequentialGroup()
                        .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonOpenFromFileFonte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonCriarFonte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonListarFontes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9)
                            .addGroup(jPanelPostosLayout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6)
                                .addGap(0, 363, Short.MAX_VALUE))))
                    .addGroup(jPanelPostosLayout.createSequentialGroup()
                        .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanelPostosLayout.createSequentialGroup()
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelPostosLayout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton11)))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPostosLayout.createSequentialGroup()
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addGroup(jPanelPostosLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(95, 95, 95)
                                        .addComponent(jLabel13)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanelPostosLayout.createSequentialGroup()
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                    .addGroup(jPanelPostosLayout.createSequentialGroup()
                                        .addComponent(jButton13)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addContainerGap())
        );
        jPanelPostosLayout.setVerticalGroup(
            jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPostosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPostosLayout.createSequentialGroup()
                        .addComponent(jButtonCriarFonte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpenFromFileFonte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonListarFontes))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator12)
                    .addGroup(jPanelPostosLayout.createSequentialGroup()
                        .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPostosLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(28, 28, 28)
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addGap(13, 13, 13)
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelPostosLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(31, 31, 31)
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                    .addComponent(jScrollPane15))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton11)
                                .addComponent(jButton10))
                            .addGroup(jPanelPostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton13)
                                .addComponent(jButton12)))
                        .addGap(0, 105, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane.addTab("FONTES", jPanelPostos);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 859, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );

        jButtonImprimir.setText("IMPRIMIR");
        jButtonImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimirActionPerformed(evt);
            }
        });

        jButton7.setText("CRIAR");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("CRIAR (FICHEIRO)");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelGrafoLayout = new javax.swing.GroupLayout(jPanelGrafo);
        jPanelGrafo.setLayout(jPanelGrafoLayout);
        jPanelGrafoLayout.setHorizontalGroup(
            jPanelGrafoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGrafoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGrafoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelGrafoLayout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonImprimir)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelGrafoLayout.setVerticalGroup(
            jPanelGrafoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGrafoLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanelGrafoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonImprimir)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("GRAFO", jPanelGrafo);

        jMenu1.setText("Ficheiro");

        jMenuItemSaveAll.setText("GUARDAR TUDO");
        jMenuItemSaveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAllActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSaveAll);

        jMenuItemSaveALlBin.setText("GUARDAR TUDO BIN");
        jMenuItemSaveALlBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveALlBinActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSaveALlBin);

        jMenuItemOpenAll.setText("ABRIR TUDO");
        jMenuItemOpenAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenAllActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemOpenAll);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 886, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemSaveAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAllActionPerformed
        // TODO add your handling code here:

        SaveToFileAll(centralST, fonteST, casasST, equipamentosST);
        JOptionPane.showMessageDialog(null, "TODA A INFORMAÇÃO GUARDADA!");
    }//GEN-LAST:event_jMenuItemSaveAllActionPerformed

    private void jMenuItemSaveALlBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveALlBinActionPerformed
        // TODO add your handling code here:

        try {
            SaveToFileBin(equipamentosST, casasST, fonteST, centralST);
            JOptionPane.showMessageDialog(null, "TODA A INFORMAÇÃO GUARDADA!");
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItemSaveALlBinActionPerformed

    private void jMenuItemOpenAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenAllActionPerformed
        // TODO add your handling code here:

        LoadFromFileCentralE(centralST);
        LoadFromFileFonteE(fonteST);
        LoadFromFileCasa(casasST);
        LoadFromFileEquipamentos(equipamentosST, System.getProperty("user.dir") + "//data/onlyEquipamentos.txt");
        LoadFromFileEquipamentos(casasST, equipamentosST);
        dg = LoadGraphFile();

        jButtonListarCasasActionPerformed(evt);
        jButtonListarCentraisActionPerformed(evt);
        jButtonListarFontesActionPerformed(evt);
        jButtonListarEquipamentosActionPerformed(evt);

    }//GEN-LAST:event_jMenuItemOpenAllActionPerformed

    private void jButtonConsumoPercentCentralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsumoPercentCentralActionPerformed
        // TODO add your handling code here:

        if (jListCentraisEletricas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CENTRAL!");
        } else {
            String stringId = jListCentraisEletricas.getSelectedValue().substring(jListCentraisEletricas.getSelectedValue().indexOf("[") + 1, jListCentraisEletricas.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);

            JOptionPane.showMessageDialog(null, "Consumo atual da CENTRAL [" + id + "] - " + centralST.get(id).getZona() + ":\n ->> " + getCentralPercentage(centralST, id) + "%");
        }
    }//GEN-LAST:event_jButtonConsumoPercentCentralActionPerformed

    private void jButtonConsumoWattsCentralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsumoWattsCentralActionPerformed
        // TODO add your handling code here:

        if (jListCentraisEletricas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CENTRAL!");
        } else {
            String stringId = jListCentraisEletricas.getSelectedValue().substring(jListCentraisEletricas.getSelectedValue().indexOf("[") + 1, jListCentraisEletricas.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);

            JOptionPane.showMessageDialog(null, "Consumo atual da CENTRAL [" + id + "] - " + centralST.get(id).getZona() + ":\n ->> " + getCentralPower(centralST, id) + " Watts");
        }
    }//GEN-LAST:event_jButtonConsumoWattsCentralActionPerformed

    private void jButtonCapacidadeCentralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCapacidadeCentralActionPerformed
        // TODO add your handling code here:

        if (jListCentraisEletricas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CENTRAL!");
        } else {
            String stringId = jListCentraisEletricas.getSelectedValue().substring(jListCentraisEletricas.getSelectedValue().indexOf("[") + 1, jListCentraisEletricas.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);

            JOptionPane.showMessageDialog(null, "Capacidade da CENTRAL [" + id + "] - " + centralST.get(id).getZona() + ":\n ->> " + centralST.get(id).getWattsProduzidos() + " Watts");
        }

    }//GEN-LAST:event_jButtonCapacidadeCentralActionPerformed

    private void jButtonListarCentraisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListarCentraisActionPerformed
        // TODO add your handling code here:

        DefaultListModel list = new DefaultListModel();
        DefaultListModel listFontes = new DefaultListModel();

        for (Integer k : centralST.keys()) {
            list.addElement(" [" + centralST.get(k).getId() + "] " + centralST.get(k).getZona() + " - Produz: " + centralST.get(k).getWattsProduzidos() + " Watts");
        }

        for (Integer k : fonteST.keys()) {
            listFontes.addElement(" [" + fonteST.get(k).getId() + "] " + fonteST.get(k).getZona() + " - Capacidade: " + fonteST.get(k).getWattsCapacidade() + " Watts");
        }

        jListCentraisEletricas.setModel(list);
        jListSelectCentral.setModel(list);
        jListSelectFonte.setModel(listFontes);
    }//GEN-LAST:event_jButtonListarCentraisActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        LoadFromFileCentralE(centralST);

        jButtonListarCentraisActionPerformed(evt);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        new jFrameCriarCentral().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonAssociarEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAssociarEquipActionPerformed
        // TODO add your handling code here:

        if (jListAssociarEquip.isSelectionEmpty() || jListEquipamentosCasas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UM EQUIPAMENTO E UMA CASA!");
            return;
        }

        String stringIdCasa = jListEquipamentosCasas.getSelectedValue().substring(jListEquipamentosCasas.getSelectedValue().indexOf("[") + 1, jListEquipamentosCasas.getSelectedValue().indexOf("]"));
        Integer idCasa = Integer.parseInt(stringIdCasa);

        String stringIdEquip = jListAssociarEquip.getSelectedValue().substring(jListAssociarEquip.getSelectedValue().indexOf("[") + 1, jListAssociarEquip.getSelectedValue().indexOf("]"));
        Integer idEquip = Integer.parseInt(stringIdEquip);

        if (AddEquipamentoCasa(casasST, equipamentosST, idCasa, equipamentosST.get(idEquip))) {
            JOptionPane.showMessageDialog(null, equipamentosST.get(idEquip).getNome() + " ADICIONADO À CASA [" + idCasa + "]");
        } else {
            JOptionPane.showMessageDialog(null, "ERRO! TENTE NOVAMENTE!");
        }

    }//GEN-LAST:event_jButtonAssociarEquipActionPerformed

    private void jButtonListarEquipamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListarEquipamentosActionPerformed
        // TODO add your handling code here:

        DefaultListModel list = new DefaultListModel();

        for (Integer k : equipamentosST.keys()) {
            if (equipamentosST.get(k).getTipo() == 0) {
                //consumidor
                list.addElement(" [" + equipamentosST.get(k).getId() + "] " + equipamentosST.get(k).getNome() + " - CONSUMIDOR (" + equipamentosST.get(k).getWattsConsumo() + " W/h)");
            } else {
                list.addElement(" [" + equipamentosST.get(k).getId() + "] " + equipamentosST.get(k).getNome() + " - GERADOR (" + equipamentosST.get(k).getWattsConsumo() + " W/h)");
            }
        }

        jListEquipamentos.setModel(list);

        DefaultListModel listAssociarEquipamentos = new DefaultListModel();

        for (Integer k : equipamentosST.keys()) {
            if (equipamentosST.get(k).getTipo() == 0) {
                //consumidor
                listAssociarEquipamentos.addElement(" [" + equipamentosST.get(k).getId() + "] " + equipamentosST.get(k).getNome() + " - CONSUMIDOR (" + equipamentosST.get(k).getWattsConsumo() + " W/h)");
            } else {
                listAssociarEquipamentos.addElement(" [" + equipamentosST.get(k).getId() + "] " + equipamentosST.get(k).getNome() + " - GERADOR (" + equipamentosST.get(k).getWattsConsumo() + " W/h)");
            }
        }

        jListAssociarEquip.setModel(listAssociarEquipamentos);
    }//GEN-LAST:event_jButtonListarEquipamentosActionPerformed

    private void jButtonOpenEquipsBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenEquipsBinActionPerformed
        // TODO add your handling code here:
        try {
            equipamentosST = LoadToFileEquipamentosBin();
            jButtonListarEquipamentosActionPerformed(evt);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonOpenEquipsBinActionPerformed

    private void jButtonOpenEquipsFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenEquipsFileActionPerformed
        // TODO add your handling code here:

        // Abrir Ficheiro de casas
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "//data"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.canRead()) {
                LoadFromFileEquipamentos(equipamentosST, selectedFile.getAbsolutePath());
                LoadFromFileEquipamentos(casasST, equipamentosST);
            }
        }

        jButtonListarEquipamentosActionPerformed(evt);
    }//GEN-LAST:event_jButtonOpenEquipsFileActionPerformed

    private void jButtonCriarEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCriarEquipActionPerformed
        // TODO add your handling code here:
        new jFrameCriarEquipamento().setVisible(true);
    }//GEN-LAST:event_jButtonCriarEquipActionPerformed

    private void jButtonRemoveEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveEquipActionPerformed
        // TODO add your handling code here:

        String stringIdCasa = jListCasasCasasEquipamentos.getSelectedValue().substring(jListCasasCasasEquipamentos.getSelectedValue().indexOf("[") + 1, jListCasasCasasEquipamentos.getSelectedValue().indexOf("]"));
        Integer idCasa = Integer.parseInt(stringIdCasa);

        String stringIdEquip = jListCasasEquipamentos.getSelectedValue().substring(jListCasasEquipamentos.getSelectedValue().indexOf("[") + 1, jListCasasEquipamentos.getSelectedValue().indexOf("]"));
        Integer idEquip = Integer.parseInt(stringIdEquip);

        DeleteEquipamentoCasa(casasST, equipamentosST, idCasa, idEquip);

        jButtonListarEquipActionPerformed(evt);
    }//GEN-LAST:event_jButtonRemoveEquipActionPerformed

    private void jButtonListarEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListarEquipActionPerformed
        // TODO add your handling code here:

        String stringId = jListCasasCasasEquipamentos.getSelectedValue().substring(jListCasasCasasEquipamentos.getSelectedValue().indexOf("[") + 1, jListCasasCasasEquipamentos.getSelectedValue().indexOf("]"));
        Integer id = Integer.parseInt(stringId);

        if (jListCasasCasasEquipamentos.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CASA!");
            return;
        }
        if (casasST.get(id).getSTEquipamento().isEmpty()) {
            JOptionPane.showMessageDialog(null, "NÃO EXISTEM EQUIPAMENTOS NA CASA [ " + id + " ]");
            return;
        }

        DefaultListModel listequip = new DefaultListModel();

        for (Integer e : casasST.get(id).getSTEquipamento().keys()) {

            if (equipamentosST.get(e).getTipo() == 0) {
                //consumidor
                if (equipamentosST.get(e).getLigado() == 0) {
                    listequip.addElement(" [" + equipamentosST.get(e).getId() + "] " + equipamentosST.get(e).getNome() + " - CONSUMIDOR (" + equipamentosST.get(e).getWattsConsumo() + " W/h) ->> [OFF]");
                } else if (equipamentosST.get(e).getLigado() == 1) {
                    listequip.addElement(" [" + equipamentosST.get(e).getId() + "] " + equipamentosST.get(e).getNome() + " - CONSUMIDOR (" + equipamentosST.get(e).getWattsConsumo() + " W/h) ->> [ON]");
                }
            } else {
                if (equipamentosST.get(e).getLigado() == 0) {
                    listequip.addElement(" [" + equipamentosST.get(e).getId() + "] " + equipamentosST.get(e).getNome() + " - GERADOR (" + equipamentosST.get(e).getWattsConsumo() + " W/h) ->> [OFF]");
                } else {
                    listequip.addElement(" [" + equipamentosST.get(e).getId() + "] " + equipamentosST.get(e).getNome() + " - GERADOR (" + equipamentosST.get(e).getWattsConsumo() + " W/h) ->> [ON]");
                }

            }
        }

        jListCasasEquipamentos.setModel(listequip);
    }//GEN-LAST:event_jButtonListarEquipActionPerformed

    private void jToggleOnOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleOnOffActionPerformed
        // TODO add your handling code here:

        String stringIdCasa = jListCasasCasasEquipamentos.getSelectedValue().substring(jListCasasCasasEquipamentos.getSelectedValue().indexOf("[") + 1, jListCasasCasasEquipamentos.getSelectedValue().indexOf("]"));
        Integer idCasa = Integer.parseInt(stringIdCasa);

        String stringIdEquip = jListCasasEquipamentos.getSelectedValue().substring(jListCasasEquipamentos.getSelectedValue().indexOf("[") + 1, jListCasasEquipamentos.getSelectedValue().indexOf("]"));
        Integer idEquip = Integer.parseInt(stringIdEquip);

        if (casasST.get(idCasa).getSTEquipamento().get(idEquip).getLigado() == 0) {
            try {
                if (!ligarEquipamentoCasa(casasST, idCasa, idEquip)) {
                    JOptionPane.showMessageDialog(null, "IMPOSSÍVEL LIGAR!\nEXCEDE A POTÊNCIA CONTRATADA");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                desligarEquipamentoCasa(casasST, idCasa, idEquip);
            } catch (InterruptedException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        jButtonListarEquipActionPerformed(evt);
    }//GEN-LAST:event_jToggleOnOffActionPerformed

    private void jListCasasEquipamentosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCasasEquipamentosValueChanged
        // TODO add your handling code here:

        if (jListCasasEquipamentos.isSelectionEmpty()) {
            jToggleOnOff.setText("---");
            jToggleOnOff.setSelected(false);
        } else if (equipamentosST.get(jListCasasEquipamentos.getSelectedIndex()).getLigado() == 0) {
            jToggleOnOff.setText("LIGAR");
            jToggleOnOff.setSelected(false);
        } else {
            jToggleOnOff.setText("DESLIGAR");
            jToggleOnOff.setSelected(true);
        }
    }//GEN-LAST:event_jListCasasEquipamentosValueChanged

    private void jButtonBiggestConsumerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBiggestConsumerActionPerformed
        // TODO add your handling code here:
        if (jListCasasPesquisas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CASA!");
        } else {
            String stringId = jListCasasPesquisas.getSelectedValue().substring(jListCasasPesquisas.getSelectedValue().indexOf("[") + 1, jListCasasPesquisas.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);
            JOptionPane.showMessageDialog(null, casasST.get(id).BiggestConsumer());
        }

    }//GEN-LAST:event_jButtonBiggestConsumerActionPerformed

    private void jButtonConsumoEntreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsumoEntreActionPerformed

        if (jListCasasPesquisas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CASA!");
        } else if (jDateInicio.getDate() == null || jDateFim.getDate() == null) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR A DATA DE FIM E DE INICIO!");
        } else {
            try {
                Timestamp inicio;
                Timestamp fim;

                DateFormat dFormat = new SimpleDateFormat("dd/MM/yyyy");

                String dataInicio = dFormat.format(jDateInicio.getDate());
                String dataFim = dFormat.format(jDateFim.getDate());

                Date datestart = dFormat.parse(dataInicio);
                Date dateend = dFormat.parse(dataFim);
                long timeStart = datestart.getTime();
                long timeEnd = dateend.getTime();

                inicio = new Timestamp(timeStart);
                fim = new Timestamp(timeEnd);

                String stringId = jListCasasPesquisas.getSelectedValue().substring(jListCasasPesquisas.getSelectedValue().indexOf("[") + 1, jListCasasPesquisas.getSelectedValue().indexOf("]"));
                Integer id = Integer.parseInt(stringId);

                JOptionPane.showMessageDialog(null, "CONSUMO ENTRE [ " + dataInicio + " ] & [ " + dataFim + " ]:\n ->> " + casasST.get(id).calcularWattsBetween(inicio, fim));

            } catch (ParseException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButtonConsumoEntreActionPerformed

    private void jButtonConsumoAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsumoAtualActionPerformed
        // TODO add your handling code here:

        if (jListCasasPesquisas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CASA!");
        } else {
            String stringId = jListCasasPesquisas.getSelectedValue().substring(jListCasasPesquisas.getSelectedValue().indexOf("[") + 1, jListCasasPesquisas.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);
            JOptionPane.showMessageDialog(null, "CONSUMO ATUAL DA CASA [ " + stringId + " ]:\n ->> " + casasST.get(id).calcularWattsEquipamentosLigados() + " Watts");
        }
    }//GEN-LAST:event_jButtonConsumoAtualActionPerformed

    private void jButtonListarCasasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListarCasasActionPerformed

        //Lista inicial da aba Casas
        this.jTextArea1.setText(printCasaAllInfo(casasST) + "\n");

        DefaultListModel list = new DefaultListModel();

        for (Integer k : casasST.keys()) {
            list.addElement("CASA [" + casasST.get(k).getId() + "] - " + casasST.get(k).getZona() + " - " + casasST.get(k).getPotenciaContratada() + " Watts");
        }

        jListCasasPesquisas.setModel(list);

        //lista da aba Casas - Secção equipamentos
        DefaultListModel listcasasequipamentos = new DefaultListModel();

        for (Integer k : casasST.keys()) {
            listcasasequipamentos.addElement("CASA [" + casasST.get(k).getId() + "] - " + casasST.get(k).getZona() + " - " + casasST.get(k).getPotenciaContratada() + " Watts");
        }

        jListCasasCasasEquipamentos.setModel(listcasasequipamentos);

        //lista da aba equipamentos
        DefaultListModel listEquipCasas = new DefaultListModel();

        for (Integer k : casasST.keys()) {
            listEquipCasas.addElement("CASA [" + casasST.get(k).getId() + "] - " + casasST.get(k).getZona() + " - " + casasST.get(k).getPotenciaContratada() + " Watts");
        }

        jListEquipamentosCasas.setModel(listEquipCasas);
    }//GEN-LAST:event_jButtonListarCasasActionPerformed

    private void jButtonCriarCasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCriarCasaActionPerformed
        // TODO add your handling code here:
        new jFrameCriarCasa().setVisible(true);
    }//GEN-LAST:event_jButtonCriarCasaActionPerformed

    private void jButtonOpenFromBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenFromBinActionPerformed
        try {
            // TODO add your handling code here:
            casasST = LoadToFileCasasBin();
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.jTextArea1.setText(printCasaAllInfo(casasST) + "\n");
        jButtonListarCasasActionPerformed(evt);
    }//GEN-LAST:event_jButtonOpenFromBinActionPerformed

    private void jButtonOpenFromFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenFromFileActionPerformed
        // Abrir Ficheiro de casas
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "//data"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            if (selectedFile.canRead()) {
                LoadFromFileCasa(casasST, selectedFile.getAbsolutePath());
            }
        }
        this.jTextArea1.setText(printCasaAllInfo(casasST) + "\n");

        jButtonListarCasasActionPerformed(evt);
    }//GEN-LAST:event_jButtonOpenFromFileActionPerformed

    //Salva todos os dados!!
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            SaveToFileBin(equipamentosST, casasST, fonteST, centralST);
            SaveToFileAll(centralST, fonteST, casasST, equipamentosST);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonCriarFonteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCriarFonteActionPerformed
        // TODO add your handling code here:

        new jFrameCriarFonte().setVisible(true);
    }//GEN-LAST:event_jButtonCriarFonteActionPerformed

    private void jButtonOpenFromFileFonteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenFromFileFonteActionPerformed
        // TODO add your handling code here:

        LoadFromFileFonteE(fonteST);

        jButtonListarFontesActionPerformed(evt);
    }//GEN-LAST:event_jButtonOpenFromFileFonteActionPerformed

    private void jButtonListarFontesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListarFontesActionPerformed
        // TODO add your handling code here:

        DefaultListModel list = new DefaultListModel();

        for (Integer k : fonteST.keys()) {
            list.addElement(" [" + fonteST.get(k).getId() + "] " + fonteST.get(k).getZona() + " - Capacidade: " + fonteST.get(k).getWattsCapacidade() + " Watts");
        }

        jListFontes.setModel(list);
        jListFonteAFonte1.setModel(list);
        jListFonteAFonte2.setModel(list);
        jListFonteACasa.setModel(list);

        DefaultListModel listCasas = new DefaultListModel();

        for (Integer k : casasST.keys()) {
            listCasas.addElement("CASA [" + casasST.get(k).getId() + "] - " + casasST.get(k).getZona() + " - " + casasST.get(k).getPotenciaContratada() + " Watts");
        }

        jListFonteCasas.setModel(listCasas);
    }//GEN-LAST:event_jButtonListarFontesActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        if (jListFontes.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA FONTE!");
        } else {
            String stringId = jListFontes.getSelectedValue().substring(jListFontes.getSelectedValue().indexOf("[") + 1, jListFontes.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);

            JOptionPane.showMessageDialog(null, "Capacidade do Posto [" + id + "] - " + fonteST.get(id).getZona() + ":\n ->> " + fonteST.get(id).getWattsCapacidade() + " Watts");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        if (jListFontes.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA FONTE!");
        } else {
            String stringId = jListFontes.getSelectedValue().substring(jListFontes.getSelectedValue().indexOf("[") + 1, jListFontes.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);

            JOptionPane.showMessageDialog(null, "Consumo atual da FONTE [" + id + "] - " + fonteST.get(id).getZona() + ":\n ->> " + getFontePower(fonteST, id) + " Watts");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:

        if (jListFontes.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA FONTE!");
        } else {
            String stringId = jListFontes.getSelectedValue().substring(jListFontes.getSelectedValue().indexOf("[") + 1, jListFontes.getSelectedValue().indexOf("]"));
            Integer id = Integer.parseInt(stringId);

            JOptionPane.showMessageDialog(null, "Consumo atual da FONTE [" + id + "] - " + fonteST.get(id).getZona() + ":\n ->> " + getFontePercentage(fonteST, id) + "%");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButtonImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirActionPerformed
        // TODO add your handling code here:

        Random random = new Random();

        this.vertices = new ArrayList<>();
        this.arestas = new ArrayList<>();

        int w = jPanel1.getWidth() - 200;
        int h = jPanel1.getHeight() - 200;

        for (int i = 0; i < dg.V() ; i++) {

            if (i != 0 && i != 1) {
                Vertice n = new Vertice();
                Point p = new Point(random.nextInt(w), random.nextInt(h));
                n.setPonto(p);
                n.setId(i);
                vertices.add(n);
            } else {
                vertices.add(null);
            }

        }

        for (int i = 0; i < dg.V() ; i++) {

            for (FlowEdge e : dg.adj(i)) {

                if (e.to() != 1 && e.to() != 0 && e.from() != 0 && e.from() != 1) {

                    //System.out.println("ARESTA " + e.from() + " - " + e.to());

                    Aresta a = new Aresta();
                    origem = vertices.get(e.from());
                    destino = vertices.get(e.to());
                    a.setOrigem(origem);
                    a.setDestino(destino);
                    a.setPeso((int) e.flow());
                    arestas.add(a);
                    origem = null;
                    destino = null;
                }

            }
        }

        printGraph(this.jPanel1.getGraphics());

    }//GEN-LAST:event_jButtonImprimirActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        dg = LoadGraph(casasST.size() + fonteST.size() + centralST.size() + 2);

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:

        dg = LoadGraphFile();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButtonDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDesconectarActionPerformed
        // TODO add your handling code here:

        if (jListSelectCentral.isSelectionEmpty() || jListSelectFonte.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CENTRAL E UMA FONTE!");
        } else {
            boolean existe = false;

            String stringIdCentral = jListSelectCentral.getSelectedValue().substring(jListSelectCentral.getSelectedValue().indexOf("[") + 1, jListSelectCentral.getSelectedValue().indexOf("]"));
            Integer idCentral = Integer.parseInt(stringIdCentral);

            String stringIdFonte = jListSelectFonte.getSelectedValue().substring(jListSelectFonte.getSelectedValue().indexOf("[") + 1, jListSelectFonte.getSelectedValue().indexOf("]"));
            Integer idFonte = Integer.parseInt(stringIdFonte);

            for (int i = 0; i < dg.V(); i++) {
                for (FlowEdge e : dg.adj(i)) {
                    if (e.from() == idCentral && e.to() == idFonte) {
                        existe = true;
                    }
                }
            }

            if (existe) {
                dg = removeFlowEdge(dg, idCentral, idFonte);
                JOptionPane.showMessageDialog(null, "CENTRAL [" + idCentral + "] DESLIGADA DA FONTE [" + idFonte + "]");
            } else {
                JOptionPane.showMessageDialog(null, "ESTA CONEXÃO NÃO EXISTE!");
            }
        }


    }//GEN-LAST:event_jButtonDesconectarActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:

        if (jListSelectCentral.isSelectionEmpty() || jListSelectFonte.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA CENTRAL E UMA FONTE!");
        } else {
            boolean existe = false;

            String stringIdCentral = jListSelectCentral.getSelectedValue().substring(jListSelectCentral.getSelectedValue().indexOf("[") + 1, jListSelectCentral.getSelectedValue().indexOf("]"));
            Integer idCentral = Integer.parseInt(stringIdCentral);

            String stringIdFonte = jListSelectFonte.getSelectedValue().substring(jListSelectFonte.getSelectedValue().indexOf("[") + 1, jListSelectFonte.getSelectedValue().indexOf("]"));
            Integer idFonte = Integer.parseInt(stringIdFonte);

            for (int i = 0; i < dg.V(); i++) {
                for (FlowEdge e : dg.adj(i)) {
                    if (e.from() == idCentral && e.to() == idFonte) {
                        existe = true;
                    }
                }
            }

            if (existe) {
                JOptionPane.showMessageDialog(null, "ESTA CONEXÃO JÁ EXISTE!");
            } else {
                Integer distancia = Integer.parseInt(JOptionPane.showInputDialog("Introduza a distância entre os pontos:"));
                ConnectVertices(idCentral, idFonte, distancia);
                JOptionPane.showMessageDialog(null, "CENTRAL [" + idCentral + "] CONECTADA À FONTE [" + idFonte + "]");
            }
        }

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:

        if (jListFonteACasa.isSelectionEmpty() || jListFonteCasas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA FONTE E UMA CASA!");
        } else {
            boolean existe = false;

            String stringIdFonte = jListFonteACasa.getSelectedValue().substring(jListFonteACasa.getSelectedValue().indexOf("[") + 1, jListFonteACasa.getSelectedValue().indexOf("]"));
            Integer idFonte = Integer.parseInt(stringIdFonte);

            String stringIdCasa = jListFonteCasas.getSelectedValue().substring(jListFonteCasas.getSelectedValue().indexOf("[") + 1, jListFonteCasas.getSelectedValue().indexOf("]"));
            Integer idCasa = Integer.parseInt(stringIdCasa);

            for (int i = 0; i < dg.V(); i++) {
                for (FlowEdge e : dg.adj(i)) {
                    if (e.from() == idFonte && e.to() == idCasa) {
                        existe = true;
                    }
                }
            }

            if (existe) {
                dg = removeFlowEdge(dg, idFonte, idCasa);
                JOptionPane.showMessageDialog(null, "FONTE [" + idFonte + "] DESLIGADA DA CASA [" + idCasa + "]");
            } else {
                JOptionPane.showMessageDialog(null, "ESTA CONEXÃO NÃO EXISTE!");
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:

        if (jListFonteAFonte1.isSelectionEmpty() || jListFonteAFonte2.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR AS DUAS FONTES!");
        } else {
            boolean existe = false;

            String stringIdFonte1 = jListFonteAFonte1.getSelectedValue().substring(jListFonteAFonte1.getSelectedValue().indexOf("[") + 1, jListFonteAFonte1.getSelectedValue().indexOf("]"));
            Integer idFonte1 = Integer.parseInt(stringIdFonte1);

            String stringIdFonte2 = jListFonteAFonte2.getSelectedValue().substring(jListFonteAFonte2.getSelectedValue().indexOf("[") + 1, jListFonteAFonte2.getSelectedValue().indexOf("]"));
            Integer idFonte2 = Integer.parseInt(stringIdFonte2);

            for (int i = 0; i < dg.V(); i++) {
                for (FlowEdge e : dg.adj(i)) {
                    if (e.from() == idFonte1 && e.to() == idFonte2) {
                        existe = true;
                    }
                }
            }

            if (existe) {
                JOptionPane.showMessageDialog(null, "ESTA CONEXÃO JÁ EXISTE!");
            } else {
                Integer distancia = Integer.parseInt(JOptionPane.showInputDialog("Introduza a distância entre os pontos:"));
                ConnectVertices(idFonte1, idFonte2, distancia);
                JOptionPane.showMessageDialog(null, "FONTE [" + idFonte1 + "] CONECTADA À FONTE [" + idFonte2 + "]");
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:

        if (jListFonteAFonte1.isSelectionEmpty() || jListFonteAFonte2.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR DUAS FONTES!");
        } else {
            boolean existe = false;

            String stringIdFonte1 = jListFonteAFonte1.getSelectedValue().substring(jListFonteAFonte1.getSelectedValue().indexOf("[") + 1, jListFonteAFonte1.getSelectedValue().indexOf("]"));
            Integer idFonte1 = Integer.parseInt(stringIdFonte1);

            String stringIdFonte2 = jListFonteAFonte2.getSelectedValue().substring(jListFonteAFonte2.getSelectedValue().indexOf("[") + 1, jListFonteAFonte2.getSelectedValue().indexOf("]"));
            Integer idFonte2 = Integer.parseInt(stringIdFonte2);

            for (int i = 0; i < dg.V(); i++) {
                for (FlowEdge e : dg.adj(i)) {
                    if (e.from() == idFonte1 && e.to() == idFonte2) {
                        existe = true;
                    }
                }
            }

            if (existe) {
                dg = removeFlowEdge(dg, idFonte1, idFonte2);
                JOptionPane.showMessageDialog(null, "FONTE [" + idFonte1 + "] DESLIGADA DA FONTE [" + idFonte2 + "]");
            } else {
                JOptionPane.showMessageDialog(null, "ESTA CONEXÃO NÃO EXISTE!");
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:

        if (jListFonteACasa.isSelectionEmpty() || jListFonteCasas.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "NECESSÁRIO SELECIONAR UMA FONTE E UMA CASA!");
        } else {
            boolean existe = false;

            String stringIdFonte = jListFonteACasa.getSelectedValue().substring(jListFonteACasa.getSelectedValue().indexOf("[") + 1, jListFonteACasa.getSelectedValue().indexOf("]"));
            Integer idFonte = Integer.parseInt(stringIdFonte);

            String stringIdCasa = jListFonteCasas.getSelectedValue().substring(jListFonteCasas.getSelectedValue().indexOf("[") + 1, jListFonteCasas.getSelectedValue().indexOf("]"));
            Integer idCasa = Integer.parseInt(stringIdCasa);

            for (int i = 0; i < dg.V(); i++) {
                for (FlowEdge e : dg.adj(i)) {
                    if (e.from() == idFonte && e.to() == idCasa) {
                        existe = true;
                    }
                }
            }

            if (existe) {
                JOptionPane.showMessageDialog(null, "ESTA CONEXÃO JÁ EXISTE!");
            } else {
                Integer distancia = Integer.parseInt(JOptionPane.showInputDialog("Introduza a distância entre os pontos:"));
                ConnectVertices(idFonte, idCasa, distancia);
                JOptionPane.showMessageDialog(null, "FONTE [" + idFonte + "] CONECTADA À CASA [" + idCasa + "]");
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

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
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAssociarEquip;
    private javax.swing.JButton jButtonBiggestConsumer;
    private javax.swing.JButton jButtonCapacidadeCentral;
    private javax.swing.JButton jButtonConsumoAtual;
    private javax.swing.JButton jButtonConsumoEntre;
    private javax.swing.JButton jButtonConsumoPercentCentral;
    private javax.swing.JButton jButtonConsumoWattsCentral;
    private javax.swing.JButton jButtonCriarCasa;
    private javax.swing.JButton jButtonCriarEquip;
    private javax.swing.JButton jButtonCriarFonte;
    private javax.swing.JButton jButtonDesconectar;
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JButton jButtonListarCasas;
    private javax.swing.JButton jButtonListarCentrais;
    private javax.swing.JButton jButtonListarEquip;
    private javax.swing.JButton jButtonListarEquipamentos;
    private javax.swing.JButton jButtonListarFontes;
    private javax.swing.JButton jButtonOpenEquipsBin;
    private javax.swing.JButton jButtonOpenEquipsFile;
    private javax.swing.JButton jButtonOpenFromBin;
    private javax.swing.JButton jButtonOpenFromFile;
    private javax.swing.JButton jButtonOpenFromFileFonte;
    private javax.swing.JButton jButtonRemoveEquip;
    private com.toedter.calendar.JDateChooser jDateFim;
    private com.toedter.calendar.JDateChooser jDateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jListAssociarEquip;
    private javax.swing.JList<String> jListCasasCasasEquipamentos;
    private javax.swing.JList<String> jListCasasEquipamentos;
    private javax.swing.JList<String> jListCasasPesquisas;
    private javax.swing.JList<String> jListCentraisEletricas;
    private javax.swing.JList<String> jListEquipamentos;
    private javax.swing.JList<String> jListEquipamentosCasas;
    private javax.swing.JList<String> jListFonteACasa;
    private javax.swing.JList<String> jListFonteAFonte1;
    private javax.swing.JList<String> jListFonteAFonte2;
    private javax.swing.JList<String> jListFonteCasas;
    private javax.swing.JList<String> jListFontes;
    private javax.swing.JList<String> jListSelectCentral;
    private javax.swing.JList<String> jListSelectFonte;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemOpenAll;
    private javax.swing.JMenuItem jMenuItemSaveALlBin;
    private javax.swing.JMenuItem jMenuItemSaveAll;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelCasas;
    private javax.swing.JPanel jPanelCentrais;
    private javax.swing.JPanel jPanelEquipamentos;
    private javax.swing.JPanel jPanelGrafo;
    private javax.swing.JPanel jPanelPostos;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToggleButton jToggleOnOff;
    // End of variables declaration//GEN-END:variables
}
