package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

/**
 *
 * @author Francesco
 */
public class MyGui implements ActionListener, MouseListener {

    private JFrame frame;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JPanel bottomJPanel;
    private JPanel mapJPanel;
    private JLabel mapJLabel;
    private int NUM_OF_REGIONS;
    private int ray = 12;
    int xStreetPoints[] = {126, 252, 342, 152, 200, 248, 289, 322, 353, 406, 81, 253, 307, 389, 437, 153, 219, 256, 292, 382, 186, 329, 151, 222, 298, 382, 118, 158, 228, 263, 298, 364, 427, 188, 225, 296, 326, 371, 124, 259, 188, 296};
    int yStreetPoints[] = {176, 114, 119, 223, 202, 179, 166, 195, 217, 171, 251, 232, 241, 237, 251, 281, 292, 266, 290, 286, 321, 321, 348, 343, 343, 340, 381, 413, 413, 367, 401, 406, 380, 461, 481, 474, 449, 494, 521, 503, 578, 552};
    int xRegionPoints[] = {0, 100, 0, 100};
    int yRegionPoints[] = {0, 50, 50, 0};
    Ellipse2D[] streetShape = new Ellipse2D[xStreetPoints.length];
    int bottomSelected = -1;

    public MyGui() {
        System.out.println("costruttore avviato");
        setUpMap();
        setUpFrame();
    }

    public void setUpMap() {
        //idRegionPointMap.put(1, new MyPoint());
        setUpStreet();
    }

    private void setUpStreet() {
        for (int i = 0; i < xStreetPoints.length; i++) {
            streetShape[i] = new Ellipse2D.Double(xStreetPoints[i] - ray, yStreetPoints[i] - ray, 2 * ray, 2 * ray);
            System.out.println("ellisse +"+i+": centro:"+xStreetPoints[i]+","+yStreetPoints[i]);
        }
    }

    private void setUpFrame() {
        System.out.println("setUp avviato");
        frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jButton4 = new JButton();
        jButton5 = new JButton();
        bottomJPanel = new JPanel();
        mapJPanel = new JPanel();
        mapJLabel = new JLabel();

        frame.setLayout(new BorderLayout());
        bottomJPanel.setLayout(new FlowLayout());
        bottomJPanel.add(jButton1);
        bottomJPanel.add(jButton2);
        bottomJPanel.add(jButton3);
        bottomJPanel.add(jButton4);
        bottomJPanel.add(jButton5);
        frame.add(bottomJPanel, BorderLayout.EAST);
        frame.add(mapJPanel, BorderLayout.CENTER);
        mapJPanel.add(mapJLabel);

        //aggiungo immagini
        jButton1.setIcon(new ImageIcon("C:\\Users\\Francesco\\NetBeansProjects\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\images\\moveSheep.jpg")); // NOI18N
        jButton2.setIcon(new ImageIcon("C:\\Users\\Francesco\\NetBeansProjects\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\images\\moveShepherd.jpg")); // NOI18N
        jButton3.setIcon(new ImageIcon("C:\\Users\\Francesco\\NetBeansProjects\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\images\\buyLand.jpg")); // NOI18N
        jButton4.setIcon(new ImageIcon("C:\\Users\\Francesco\\NetBeansProjects\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\images\\mateSheep.jpg")); // NOI18N
        jButton5.setIcon(new ImageIcon("C:\\Users\\Francesco\\NetBeansProjects\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\images\\killOvine.jpg")); // NOI18N
        mapJLabel.setIcon(new ImageIcon("C:\\Users\\Francesco\\NetBeansProjects\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\francesco.angelo-umberto.difabrizio\\images\\Game_Board_big.jpg")); // NOI18N
        //frame.getContentPane().setBackground(Color.getHSBColor(205, 85, 250));
        
        //setto dimensioni
        jButton1.setPreferredSize(new java.awt.Dimension(68, 72));
        jButton2.setPreferredSize(new java.awt.Dimension(68, 72));
        jButton3.setPreferredSize(new java.awt.Dimension(68, 72));
        jButton4.setPreferredSize(new java.awt.Dimension(68, 72));
        jButton5.setPreferredSize(new java.awt.Dimension(68, 72));
        mapJLabel.setPreferredSize(new java.awt.Dimension(487, 700));
        mapJPanel.setPreferredSize(new java.awt.Dimension(487, 700));
        bottomJPanel.setPreferredSize(new Dimension(68, bottomJPanel.getHeight()));

        //aggiungo this come listener per i bottom
        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
        jButton3.addActionListener(this);
        jButton4.addActionListener(this);
        jButton5.addActionListener(this);

        //aggiungo this come listener per la map
        mapJLabel.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
        System.out.println("setUp finito");
    }

    public static void main(String args[]) {
        System.out.println("main avviato");
        System.out.println("main avviato");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyGui();
            }
        });
        System.out.println("main finito");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jButton1)) {
            System.out.println("buttone 1 clickato");
            bottomSelected = 1;
        } else if (e.getSource().equals(jButton2)) {
            System.out.println("buttone 2 clickato");
            bottomSelected = 2;
        } else if (e.getSource().equals(jButton3)) {
            System.out.println("buttone 3 clickato");
            bottomSelected = 3;
        } else if (e.getSource().equals(jButton4)) {
            System.out.println("buttone 4 clickato");
            bottomSelected = 4;
        } else if (e.getSource().equals(jButton5)) {
            System.out.println("buttone 5 clickato");
            bottomSelected = 5;
        }
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("click: " + e.getX() + " " + e.getY());
        for (int i = 0; i < streetShape.length; i++) {
            if (streetShape[i].contains(e.getX(), e.getY())) {
                System.out.println("strada " + i + " cliccata!");
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public int chooseAction(int[] availableActions) {
        for (int actionAvailable : availableActions) {
            if (actionAvailable == 1) {
                jButton1.setEnabled(false);
            }
            if (actionAvailable == 2) {
                jButton2.setEnabled(false);
            }
            if (actionAvailable == 3) {
                jButton3.setEnabled(false);
            }
            if (actionAvailable == 4) {
                jButton4.setEnabled(false);
            }
            if (actionAvailable == 5) {
                jButton5.setEnabled(false);
            }
        }
        
        while(bottomSelected<0){
            ;
        }
        int choice = bottomSelected;
        bottomSelected = -1;
        return choice;
    }
}
