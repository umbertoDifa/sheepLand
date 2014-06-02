package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.DimensionUIResource;

/**
 *
 * @author Francesco
 */
public class MyGui implements ActionListener, MouseListener {

    private JFrame frame;
    private JPanel mainJPanel;
    private JButton[] actionsJButtons;
    private Card[] cardsJPanels;
    private JPanel actionsButtonsJPanel;
    private JPanel cardsJPanel;
    private Map mapJPanel;
    private Card fenceJPanel;
    private Player[] playersJPanels;
    private int NUM_OF_REGIONS;
    private final int NUM_OF_CARDS = 6;
    private final int NUM_OF_ACTIONS = 5;
    private final int NUM_OF_FENCES = 20;
    private final int NUM_OF_PLAYERS = 4;
    private final int ray = 12;
    int xStreetPoints[] = {126, 252, 342, 152, 200, 248, 289, 322, 353, 406, 81, 253, 307, 389, 437, 153, 219, 256, 292, 382, 186, 329, 151, 222, 298, 382, 118, 158, 228, 263, 298, 364, 427, 188, 225, 296, 326, 371, 124, 259, 188, 296};
    int yStreetPoints[] = {176, 114, 119, 223, 202, 179, 166, 195, 217, 171, 251, 232, 241, 237, 251, 281, 292, 266, 290, 286, 321, 321, 348, 343, 343, 340, 381, 413, 413, 367, 401, 406, 380, 461, 481, 474, 449, 494, 521, 503, 578, 552};
    int xRegionPoints[] = {0, 100, 0, 100};
    int yRegionPoints[] = {0, 50, 50, 0};
    Ellipse2D[] streetShape = new Ellipse2D[xStreetPoints.length];
    int buttonSelected = -1;
    private final Color backgroundColor = new Color(35, 161, 246);
    private final Color noneColor = new Color(0, 0, 0, 0);
    private final MyFont font = new MyFont();
    private String[] nickNames;

    public MyGui() {
        setUpNickNames();
        setUpMap();
        setUpFrame();
    }

    
    public void setUpNickNames() {
        nickNames = new String[NUM_OF_PLAYERS];
        nickNames[0] = "Francesco";
        nickNames[1] = "Umberto";
        nickNames[2] = "Ross";
        nickNames[3] = "Umbertone";
    }
    
    /**
     * imposto le
     */
    public void setUpMap() {
        //idRegionPointMap.put(1, new MyPoint());
        setUpStreet();
    }

    /**
     * imposto l'array di ellissi delle strada
     */
    private void setUpStreet() {
        for (int i = 0; i < xStreetPoints.length; i++) {
            streetShape[i] = new Ellipse2D.Double(xStreetPoints[i] - ray, yStreetPoints[i] - ray, 2 * ray, 2 * ray);
            System.out.println("ellisse +" + i + ": centro:" + xStreetPoints[i] + "," + yStreetPoints[i]);
        }
    }

    /**
     * inizializzo tutti i componenti della Gui
     */
    private void setUpFrame() {

        //istanzio tutti i componenti
        frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        mainJPanel = new JPanel();
        actionsButtonsJPanel = new JPanel();
        cardsJPanel = new JPanel();
        mapJPanel = new Map(".\\images\\Game_Board_big.jpg");
        actionsJButtons = new JButton[NUM_OF_ACTIONS];
        cardsJPanels = new Card[NUM_OF_CARDS];
        for (int i = 0; i < actionsJButtons.length; i++) {
            actionsJButtons[i] = new JButton();
        }
        for (int i = 0; i < NUM_OF_CARDS; i++) {
            cardsJPanels[i] = new Card(font.getFont());
        }
        //  mapJLabel = new JLabel();
        fenceJPanel = new Card(font.getFont(), String.valueOf(NUM_OF_FENCES));
        playersJPanels = new Player[NUM_OF_PLAYERS];
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            playersJPanels[i] = new Player(nickNames[i], font.getFont());
        }

        //setto la struttura
        frame.setLayout(new BorderLayout());
        frame.setContentPane(mainJPanel);
        mainJPanel.setLayout(new FlowLayout());
        mainJPanel.add(cardsJPanel);
        mainJPanel.add(mapJPanel);
        mainJPanel.add(actionsButtonsJPanel);

        addComponentsToPane(mapJPanel, fenceJPanel, 55, 0);

        cardsJPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionsButtonsJPanel.setLayout(new FlowLayout());
        for (Player player : playersJPanels) {
            actionsButtonsJPanel.add(player);
        }
        for (JButton jButton : actionsJButtons) {
            actionsButtonsJPanel.add(jButton);
        }
        for (Card card : cardsJPanels) {
            cardsJPanel.add(card);
        }

        //aggiungo immagini
        actionsJButtons[0].setIcon(new ImageIcon(".\\images\\moveSheep.png"));
        actionsJButtons[1].setIcon(new ImageIcon(".\\images\\moveShepherd.png"));
        actionsJButtons[2].setIcon(new ImageIcon(".\\images\\buyLand.png"));
        actionsJButtons[3].setIcon(new ImageIcon(".\\images\\mateSheep.png"));
        actionsJButtons[4].setIcon(new ImageIcon(".\\images\\killOvine.png"));

        cardsJPanels[0].setUpCard(".\\images\\hill2.png", 87, 76);
        cardsJPanels[1].setUpCard(".\\images\\countryside2.png", 87, 76);
        cardsJPanels[2].setUpCard(".\\images\\mountain2.png", 87, 76);
        cardsJPanels[3].setUpCard(".\\images\\desert2.png", 87, 76);
        cardsJPanels[4].setUpCard(".\\images\\lake2.png", 87, 76);
        cardsJPanels[5].setUpCard(".\\images\\plain2.png", 87, 76);

        playersJPanels[0].setUpCard(".\\images\\giocatore4.png", 0, 0);
        playersJPanels[1].setUpCard(".\\images\\giocatore5.png", 0, 0);
        playersJPanels[2].setUpCard(".\\images\\giocatore6.png", 0, 0);
        playersJPanels[3].setUpCard(".\\images\\giocatore4.png", 0, 0);

        fenceJPanel.setUpCard(".\\images\\numFences.png", 34, 62);

        //imposto colore sfondi
        mainJPanel.setBackground(backgroundColor);
        actionsButtonsJPanel.setBackground(noneColor);
        mapJPanel.setBackground(noneColor);
        cardsJPanel.setBackground(noneColor);
        for (JButton action : actionsJButtons) {
            action.setBackground(noneColor);
            action.setBorderPainted(false);
            action.setRolloverEnabled(false);
            action.setContentAreaFilled(false);
        }
        fenceJPanel.setBackground(noneColor);
        for (Player player : playersJPanels) {
            player.setBackground(noneColor);
        }

        //setto dimensioni
        for (JButton jButton : actionsJButtons) {
            jButton.setPreferredSize(new java.awt.Dimension(68, 72));
        }
        for (Card card : cardsJPanels) {
            card.setPreferredSize(new Dimension(105, 116));
        }
        for (Player player : playersJPanels) {
            player.setPreferredSize(new Dimension(200, 99));
        }
        mapJPanel.setPreferredSize(new java.awt.Dimension(487, 700));
        fenceJPanel.setPreferredSize(new Dimension(78, 94));
        actionsButtonsJPanel.setPreferredSize(new Dimension(200, (150) * actionsJButtons.length));
        cardsJPanel.setPreferredSize(new Dimension(105, (116 + 10) * cardsJPanels.length));
        mainJPanel.setMaximumSize(mainJPanel.getPreferredSize());

        //aggiungo this come listener per i bottom
        for (JButton jButton : actionsJButtons) {
            jButton.addActionListener(this);
        }

        //aggiungo this come listener per la map
        mapJPanel.addMouseListener(this);

        frame.pack();
//        Insets insets = frame.getInsets();
//        frame.setSize(800 + insets.left + insets.right,
//                900 + insets.top + insets.bottom);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        //faccio gestire il thread da EDT, per il controllo ciclico della coda
        //degli eventi generati dai vari componenti
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyGui();
            }
        });
    }

    /**
     * quando la MyGui ascolta un evento ActionEvent controlla chi l ha generato
     * e aggiorna la variabile bottomSelected
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        //aggiorno bottomSelected in base a chi ha generato 
        for (int i = 0; i < actionsJButtons.length; i++) {
            if (e.getSource().equals(actionsJButtons[i])) {
                System.out.println("bottone " + i + " clickato");
                buttonSelected = i;
            }
        }
    }

    /**
     * quando MyGui ascolta un evento MouseEvent (che per ora vengono generati
     * solo da mapJLabel) controlla in quale strada è avvenuto il click
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        System.out.println("click: " + e.getX() + " " + e.getY());
        //per ogni strada
        for (int i = 0; i < streetShape.length; i++) {
            //la strada contiene la posizione del click
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

    /**
     * abilita i bottoni corrispondenti alle availableActions e controlla
     * ciclicamente la variabile bottomSelected, appena cambia ritorno quel
     * valore
     *
     * @param availableActionsIndex
     * @return
     */
    public int chooseAction(int[] availableActionsIndex) {
        //per ogni azione possibile
        for (int availableActionIndex : availableActionsIndex) {
            //abilito il corrispondente jButton FIXME
            actionsJButtons[availableActionIndex - 1].setEnabled(true);
        }

        //finchè la pressione di uno di questi bottoni
        //non cambia la var bottomSelected faccio ;
        while (buttonSelected < 0) {
            ;
        }
        int choice = buttonSelected;
        //risetto
        buttonSelected = -1;
        return choice;
    }

    public static void addComponentsToPane(Container panelFather, JPanel panelSon, int x, int y) {

        panelFather.setLayout(null);
        panelFather.add(panelSon);

        Insets insets = panelFather.getInsets();
        Dimension size = panelSon.getPreferredSize();
        panelSon.setBounds(x + insets.left, y + insets.top,
                size.width, size.height);
        panelFather.repaint();
    }
}
