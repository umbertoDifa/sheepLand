package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Francesco
 */
public class MyGui implements MouseListener {

    private JFrame frame;
    private JPanel mainJPanel;
    private Action[] actions;
    private Card[] cardsJPanels;
    private JPanel actionsJPanel;
    private JPanel cardsJPanel;
    private Map mapJPanel;
    private Card fenceJPanel;
    private Player[] playersJPanels;
    private JPanel sxBar;
    private JPanel playersJPanel;
    private int NUM_OF_REGIONS;
    private final int NUM_OF_CARDS = 6;
    private final int NUM_OF_ACTIONS = 5;
    private final int NUM_OF_FENCES = 20;
    private final int NUM_OF_PLAYERS = 4;
    private final int SHEPHERD4PLAYERS = 1;
    private final int ray = 12;
    int xStreetPoints[] = {126, 252, 342, 152, 200, 248, 289, 322, 353, 406, 81, 238, 307, 389, 437, 153, 219, 256, 292, 382, 186, 329, 151, 222, 298, 382, 118, 158, 228, 263, 298, 364, 421, 188, 225, 296, 326, 371, 124, 259, 188, 296};
    int yStreetPoints[] = {176, 114, 119, 223, 202, 179, 166, 195, 217, 171, 251, 232, 241, 237, 251, 281, 292, 266, 290, 286, 321, 321, 348, 343, 343, 340, 381, 413, 413, 367, 401, 406, 385, 461, 481, 474, 449, 494, 521, 503, 578, 552};
    int xRegionPoints[] = {0, 100, 0, 100};
    int yRegionPoints[] = {0, 50, 50, 0};
    Ellipse2D[] streetShape = new Ellipse2D[xStreetPoints.length];
    private final Color backgroundColor = new Color(35, 161, 246);
    private final Color noneColor = new Color(0, 0, 0, 0);
    private final MyFont font = new MyFont();
    private String[] nickNames;
    private Street[] streets;
    private InfoPanel infoPanel;

    public MyGui() {
        setUpNickNames();
        setUpMap();
        setUpImagePool();
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
        infoPanel = new InfoPanel();
        actionsJPanel = new JPanel();
        cardsJPanel = new JPanel();
        mapJPanel = new Map();
        actions = new Action[NUM_OF_ACTIONS];
        cardsJPanels = new Card[NUM_OF_CARDS];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new Action();
        }
        for (int i = 0; i < cardsJPanels.length; i++) {
            cardsJPanels[i] = new Card(font.getFont(), "0");
        }
        sxBar = new JPanel();
        playersJPanel = new JPanel();
        fenceJPanel = new Card(font.getFont(), String.valueOf(NUM_OF_FENCES));
        playersJPanels = new Player[NUM_OF_PLAYERS];
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            playersJPanels[i] = new Player(nickNames[i], font.getFont());
        }
        streets = new Street[xStreetPoints.length];
        List<Image> imgShepherds = new ArrayList<Image>();
        for(int i=0; i<NUM_OF_PLAYERS; i++){
            imgShepherds.add(ImagePool.getByName("shepherd"+String.valueOf(i+1)));
        }
        for (int i = 0; i < xStreetPoints.length; i++) {
            streets[i] = new Street(ImagePool.getByName("fence"), imgShepherds);
        }

        //aggiungo immagini
        mapJPanel.setUp(".\\images\\Game_Board_big.jpg", 487, 700);
        actions[0].setUp(".\\images\\moveSheep.png", 68, 72);
        actions[1].setUp(".\\images\\moveShepherd.png", 68, 72);
        actions[2].setUp(".\\images\\buyLand.png", 68, 72);
        actions[3].setUp(".\\images\\mateSheep.png", 68, 72);
        actions[4].setUp(".\\images\\killOvine.png", 68, 72);

        cardsJPanels[0].setUp(".\\images\\hill2.png", 139, 91, 105, 104);
        cardsJPanels[1].setUp(".\\images\\countryside2.png", 139, 91, 105, 104);
        cardsJPanels[2].setUp(".\\images\\mountain2.png", 139, 91, 105, 104);
        cardsJPanels[3].setUp(".\\images\\desert2.png", 139, 91, 105, 104);
        cardsJPanels[4].setUp(".\\images\\lake2.png", 139, 91, 105, 104);
        cardsJPanels[5].setUp(".\\images\\plain2.png", 139, 91, 105, 104);

        playersJPanels[0].setUp(".\\images\\giocatore1.png", 145, 81, 200, 99);
        playersJPanels[1].setUp(".\\images\\giocatore2.png", 145, 81, 200, 99);
        playersJPanels[2].setUp(".\\images\\giocatore3.png", 145, 81, 200, 99);
        playersJPanels[3].setUp(".\\images\\giocatore4.png", 145, 81, 200, 99);

        fenceJPanel.setUp(".\\images\\numFences.png", 67, 77, 78, 94);

        for(Street street: streets){
            street.setUp(".\\images\\shepherd1.png", 2 * ray, 2 * ray);
        }
        
        //setto la struttura
        frame.setLayout(new BorderLayout());
       // frame.setContentPane(mainJPanel);
        frame.getContentPane().add(mainJPanel, 0 );
    //    frame.getLayeredPane().add(infoPanel, 2 );
        mainJPanel.setLayout(new FlowLayout());
        mainJPanel.add(cardsJPanel);
        mainJPanel.add(mapJPanel);
        mainJPanel.add(sxBar);
        sxBar.setLayout(new FlowLayout());
        sxBar.add(playersJPanel);
        sxBar.add(actionsJPanel);
        addComponentsToPane(mapJPanel, fenceJPanel, 55, 0);
        mapJPanel.setLayout(null);
        for(int i=0; i<streets.length; i++){
             mapJPanel.addPanel(streets[i], xStreetPoints[i] - 10, yStreetPoints[i] -10);
        }

        FlowLayout f = new FlowLayout(FlowLayout.LEFT, 0, 0);
        f.setVgap(12);
        cardsJPanel.setLayout(f);
        actionsJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        for (Player player : playersJPanels) {
            playersJPanel.add(player);
        }
        for (Action action : actions) {
            actionsJPanel.add(action);
        }
        for (Card card : cardsJPanels) {
            cardsJPanel.add(card);
        }

        //imposto colore sfondi
        mainJPanel.setBackground(backgroundColor);
        actionsJPanel.setBackground(noneColor);
        mapJPanel.setBackground(noneColor);
        cardsJPanel.setBackground(noneColor);
        for (Action action : actions) {
            action.setBackground(noneColor);
            action.repaint();
        }
        for (Street street: streets){
            street.setBackground(noneColor);
         }
        fenceJPanel.setBackground(noneColor);
        for (Player player : playersJPanels) {
            player.setBackground(noneColor);
        }
        playersJPanel.setBackground(noneColor);
        sxBar.setBackground(noneColor);

        //setto dimensioni
        actionsJPanel.setPreferredSize(new Dimension((68 + 10) * 3, (72 + 10) * actions.length));
        playersJPanel.setPreferredSize(new Dimension(200, (99 + 10) * playersJPanels.length));
        cardsJPanel.setPreferredSize(new Dimension(105, (116 + 10) * cardsJPanels.length));
        mainJPanel.setMaximumSize(mainJPanel.getPreferredSize());
        sxBar.setPreferredSize(new Dimension((68 + 10) * 3,
                (((72 + 10) * actions.length) + (99 + 10) * playersJPanels.length) - 90));
        infoPanel.setPreferredSize(new Dimension(200, 200));
        
        //aggiungo this come listener per le azioni
        for (Action action : actions) {
            action.addMouseListener(this);
        }

        //aggiungo this come listener per la map
        mapJPanel.addMouseListener(this);

        //aggiungo l
        for(Street street: streets){
            street.addMouseListener(street);
        }

        frame.pack();
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
     * quando MyGui ascolta un evento MouseEvent (che per ora vengono generati
     * solo da mapJLabel) controlla in quale strada è avvenuto il click
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        System.out.println("click: " + e.getX() + " " + e.getY());

        if (e.getSource() instanceof Action) {
            for (int i = 0; i < actions.length; i++) {
                if (e.getSource().equals(actions[i])) {
                    System.out.println("azione " + i + "selezionata");
                }
            }
        } else {
            //per ogni strada
            for (int i = 0; i < streetShape.length; i++) {
                //la strada contiene la posizione del click
                if (streetShape[i].contains(e.getX(), e.getY())) {
                    System.out.println("strada " + i + " cliccata!");
                }
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
//    public int chooseAction(int[] availableActionsIndex) {
//        //per ogni azione possibile
//        for (int availableActionIndex : availableActionsIndex) {
//            //abilito il corrispondente jButton FIXME
//            actionsJButtons[availableActionIndex - 1].setEnabled(true);
//        }
//
//        //finchè la pressione di uno di questi bottoni
//        //non cambia la var bottomSelected faccio ;
//        while (buttonSelected < 0) {
//            ;
//        }
//        int choice = buttonSelected;
//        //risetto
//        buttonSelected = -1;
//        return choice;
//    }
    public static void addComponentsToPane(Container panelFather, JPanel panelSon, int x, int y) {

        panelFather.setLayout(null);
        panelFather.add(panelSon);

        Insets insets = panelFather.getInsets();
        Dimension size = panelSon.getPreferredSize();
        panelSon.setBounds(x + insets.left, y + insets.top,
                size.width, size.height);
        panelFather.repaint();
    }

    private void setUpImagePool() {
        //TODO creare ultime 2 pedine dei giocatori e modificare qui path
        ImagePool.add(".\\images\\shepherd1.png", "shepherd1");
        ImagePool.add(".\\images\\shepherd2.png", "shepherd2");
        ImagePool.add(".\\images\\shepherd3.png", "shepherd3");
        ImagePool.add(".\\images\\shepherd4.png", "shepherd4");
        ImagePool.add(".\\images\\fence2.png", "fence");
    }
    
    /**
     * abilita le azioni corripondenti ai numeri nell array intActions
     * @param intActions 
     */
    public void setActionEnabled(int[] intActions){
        for(int i=0; i<intActions.length; i++)
            actions[i].setEnabled(true);
    }

}
