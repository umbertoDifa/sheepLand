package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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
    private JPanel dxBar;
    private JPanel playersJPanel;
    private final int NUM_OF_PLAYERS = 4;
    private final int SHEPHERD4PLAYERS = 1;
    private final int ray = 12;
    int xStreetPoints[] = {126, 252, 342, 152, 200, 248, 289, 322, 353, 406, 81, 238, 307, 389, 437, 153, 219, 256, 292, 382, 186, 329, 151, 222, 298, 382, 118, 158, 228, 263, 298, 364, 421, 188, 225, 296, 326, 371, 124, 259, 188, 296};
    int yStreetPoints[] = {176, 114, 119, 223, 202, 179, 166, 195, 217, 171, 251, 232, 241, 237, 251, 281, 292, 266, 290, 286, 321, 321, 348, 343, 343, 340, 381, 413, 413, 367, 401, 406, 385, 461, 481, 474, 449, 494, 521, 503, 578, 552};
    int xRegionBoxes[] = {62, 88, 168, 168, 281, 170, 257, 343, 408, 322, 399, 381, 313, 309, 227, 156, 244, 81, 236};
    int yRegionBoxes[] = {131, 269, 349, 111, 73, 226, 194, 134, 185, 243, 285, 412, 349, 490, 549, 488, 410, 420, 292};
    Ellipse2D[] streetShape = new Ellipse2D[xStreetPoints.length];
    private final Color backgroundColor = new Color(35, 161, 246);
    private final Color noneColor = new Color(0, 0, 0, 0);
    private final MyFont font = new MyFont();
    private String[] nickNames;
    private Street[] streets;
    private InfoPanel infoPanel;
    private int buttonSelected;
    private JComponent mainPanel2;
    private JLayeredPane layeredPane;
    private List<Integer> holder = new LinkedList<Integer>();
    private RegionBox[] regionBoxes;

    public MyGui() {
        setUpNickNames();
        setUpMap();
        setUpImagePool();
        setUpFrame();
        for (int i = 0; i < xRegionBoxes.length; i++) {
            regionBoxes[i].add("sheep");
            regionBoxes[i].add("blacksheep");
            regionBoxes[i].add("ram");
            regionBoxes[i].add("lamb");
            regionBoxes[i].add("wolf");
        }
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
        mainPanel2 = new JPanel();
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        mainJPanel = new JPanel();
        setUpInfoPanel();
        actionsJPanel = new JPanel();
        cardsJPanel = new JPanel();
        mapJPanel = new Map();
        actions = new Action[GameConstants.NUM_TOT_ACTIONS.getValue()];
        cardsJPanels = new Card[RegionType.values().length];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new Action();
        }
        for (int i = 0; i < cardsJPanels.length; i++) {
            cardsJPanels[i] = new Card(font.getFont(), "0");
        }
        dxBar = new JPanel();
        playersJPanel = new JPanel();
        fenceJPanel = new Card(font.getFont(), String.valueOf(GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue()));
        playersJPanels = new Player[NUM_OF_PLAYERS];
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            playersJPanels[i] = new Player(nickNames[i], font.getFont());
        }
        streets = new Street[xStreetPoints.length];
        List<Image> imgShepherds = new ArrayList<Image>();
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            imgShepherds.add(ImagePool.getByName("shepherd" + String.valueOf(i + 1)));
        }
        for (int i = 0; i < xStreetPoints.length; i++) {
            streets[i] = new Street(ImagePool.getByName("fence"), imgShepherds);
        }
        regionBoxes = new RegionBox[xRegionBoxes.length];
        for (int i = 0; i < xRegionBoxes.length; i++) {
            regionBoxes[i] = new RegionBox();
        }

        //aggiungo immagini
        mapJPanel.setUp(".\\images\\Game_Board_big.jpg", 487, 900);
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

        playersJPanels[0].setUp(".\\images\\giocatore1.png", ".\\images\\money.png", 145, 81, 20, 40, 200, 99);
        playersJPanels[1].setUp(".\\images\\giocatore2.png", ".\\images\\money.png", 145, 81, 20, 40, 200, 99);
        playersJPanels[2].setUp(".\\images\\giocatore3.png", ".\\images\\money.png", 145, 81, 20, 40, 200, 99);
        playersJPanels[3].setUp(".\\images\\giocatore4.png", ".\\images\\money.png", 145, 81, 20, 40, 200, 99);

        fenceJPanel.setUp(".\\images\\numFences.png", 67, 77, 78, 94);

        for (Street street : streets) {
            street.setUp(".\\images\\shepherd3.png", 2 * ray, 2 * ray);
        }

        for (RegionBox region : regionBoxes) {
            region.setUp((String) null, 52, 78);
        }

        //setto la struttura
        frame.setLayout(new BorderLayout());
        layeredPane.setPreferredSize(new Dimension(900, 800));
        layeredPane.add(mainJPanel, 1);
        layeredPane.add(infoPanel, 0);

        mainJPanel.setLayout(new FlowLayout());
        mainJPanel.add(cardsJPanel);
        mainJPanel.add(mapJPanel);
        mainJPanel.add(dxBar);
        dxBar.setLayout(new FlowLayout());
        dxBar.add(playersJPanel);
        dxBar.add(actionsJPanel);
        addComponentsToPane(mapJPanel, fenceJPanel, 55, 0);
        mapJPanel.setLayout(null);
        for (int i = 0; i < streets.length; i++) {
            mapJPanel.addPanel(streets[i], xStreetPoints[i] - 10, yStreetPoints[i] - 10);
        }
        for (int i = 0; i < regionBoxes.length; i++) {
            mapJPanel.addPanel(regionBoxes[i], xRegionBoxes[i] - 10, yRegionBoxes[i] - 10);
        }

        cardsJPanel.setLayout(new FlowLayout());
        actionsJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        playersJPanel.setLayout(new FlowLayout());
        for (Player player : playersJPanels) {
            playersJPanel.add(player);
        }
        for (Action action : actions) {
            actionsJPanel.add(action);
        }
        for (Card card : cardsJPanels) {
            cardsJPanel.add(card);
        }
        mainPanel2.add(layeredPane);
        frame.setContentPane(mainPanel2);

        //imposto colore sfondi
        mainPanel2.setBackground(backgroundColor);
        mainJPanel.setBackground(backgroundColor);
        actionsJPanel.setBackground(noneColor);
        mapJPanel.setBackground(noneColor);
        cardsJPanel.setBackground(noneColor);
        for (Action action : actions) {
            action.setBackground(noneColor);
            action.repaint();
        }
        for (Street street : streets) {
            street.setBackground(noneColor);
        }
        fenceJPanel.setBackground(noneColor);
        for (Player player : playersJPanels) {
            player.setBackground(noneColor);
        }
        playersJPanel.setBackground(noneColor);
        dxBar.setBackground(noneColor);
        mainPanel2.setOpaque(true);
        infoPanel.setBackground(noneColor);
        for (RegionBox region : regionBoxes) {
            region.setBackground(Color.MAGENTA);
        }

        //setto dimensioni
        actionsJPanel.setPreferredSize(new Dimension((68 + 10) * 3, (72 + 10) * actions.length));
        playersJPanel.setPreferredSize(new Dimension(220, (99 + 10) * playersJPanels.length));
        cardsJPanel.setPreferredSize(new Dimension(105, (116 + 10) * cardsJPanels.length));
        mainJPanel.setPreferredSize(mainJPanel.getPreferredSize());
        mainJPanel.setBounds(0, 0, 900, 800);
        dxBar.setPreferredSize(new Dimension((68 + 10) * 3,
                (((72 + 10) * actions.length) + (99 + 10) * playersJPanels.length) - 90));
        //      layeredPane.setPreferredSize(new Dimension(800, 800));
        //    mainPanel2.setPreferredSize(new Dimension(800, 800));
        infoPanel.setPreferredSize(new Dimension(232, 444));
        infoPanel.setBounds(mainJPanel.getPreferredSize().width / 2 - (444 / 2), mainJPanel.getPreferredSize().height / 2 - (400), 232, 444);
//        for (RegionBox region : regionBoxes) {
//            region.setPreferredSize(new Dimension(51, 76));
//        }

        //aggiungo this come listener per le azioni
        for (Action action : actions) {
            action.addMouseListener(this);
        }

        //aggiungo this come listener per la map
        mapJPanel.addMouseListener(this);

        //aggiungo l strada come listener di se stessa
        for (Street street : streets) {
            street.addMouseListener(street);
            street.addMouseListener(this);
        }

        for (Card card : cardsJPanels) {
            card.addMouseListener(this);
        }

        for (Player player : playersJPanels) {
            player.addMouseListener(player);
        }
        infoPanel.addMouseListener(infoPanel);

        //nascondo dei componenti
        //  hideInfoPanel();
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

    public void mouseClicked(MouseEvent e) {
        synchronized (holder) {
            if (e.getSource() instanceof Action) {
                for (int i = 0; i < actions.length; i++) {
                    if (e.getSource().equals(actions[i])) {
                        System.out.println("azione " + i + "selezionata");
                        actions[i].removeMouseListener(this);
                        holder.add(i);
                        holder.notify();
                        System.out.println("aggiunto a holder azione " + i);
                    }
                }
            } else if (e.getSource() instanceof Street) {
                for (int i = 0; i < streets.length; i++) {
                    if (e.getSource().equals(streets[i])) {
                        System.out.println("strada " + i + "selezionata");
                        streets[i].removeMouseListener(this);
                        holder.add(i);
                        holder.notify();
                        System.out.println("aggiunto a holder strada " + i);
                    }
                }
            } else if (e.getSource() instanceof Card) {
                for (int i = 0; i < cardsJPanels.length; i++) {
                    if (e.getSource().equals(cardsJPanels[i])) {
                        System.out.println("carta terreno " + i + "selezionata");
                        cardsJPanels[i].removeMouseListener(this);
                        holder.add(i);
                        holder.notify();
                        System.out.println("aggiunto a holder carta terreno " + i);
                    }
                }
            }

            System.out.println(e.getX() + " , " + e.getY());
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
            //actions[availableActionIndex - 1].setEnabled(true);
            actions[availableActionIndex - 1].addMouseListener(this);
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

    public int askBuyLand(int[] availableLand) throws InterruptedException {
        for (int i = 0; i < availableLand.length; i++) {
            cardsJPanels[i].addMouseListener(this);
        }
        int result;
        synchronized (holder) {
            // wait for answer
            while (holder.isEmpty()) {
                holder.wait();
            }

            //la risposta che accetto è la prima
            result = holder.remove(0);
            System.out.println("prelevato da holder carta terreno" + result);
        }
        return result;
    }

    /**
     * posiziona il panel figlio nel panel padre, posizionando l'angolo sx del
     * figlio a x,y rispetto al padre
     *
     * @param panelFather
     * @param panelSon
     * @param x
     * @param y
     */
    public static void addComponentsToPane(Container panelFather, JComponent panelSon, int x, int y) {

        panelFather.setLayout(null);
        panelFather.add(panelSon);

        Insets insets = panelFather.getInsets();
        Dimension size = panelSon.getPreferredSize();
        panelSon.setBounds(x + insets.left, y + insets.top,
                size.width, size.height);
        panelFather.repaint();
    }

    /**
     * carica le immagini per le strade nell'ImagePool
     */
    private void setUpImagePool() {
        //TODO creare ultime 2 pedine dei giocatori e modificare qui path
        ImagePool.add(".\\images\\shepherd1.png", "shepherd1");
        ImagePool.add(".\\images\\shepherd2.png", "shepherd2");
        ImagePool.add(".\\images\\shepherd3.png", "shepherd3");
        ImagePool.add(".\\images\\shepherd4.png", "shepherd4");
        ImagePool.add(".\\images\\fence2.png", "fence");
        ImagePool.add(".\\images\\sheep2.png", "sheep");
        ImagePool.add(".\\images\\wolf2.png", "wolf");
        ImagePool.add(".\\images\\blacksheep.png", "blacksheep");
        ImagePool.add(".\\images\\ram.png", "ram");
        ImagePool.add(".\\images\\lamb.png", "lamb");
    }

    /**
     * abilita le azioni corripondenti ai numeri nell array intActions
     *
     * @param intActions
     */
    public void setActionEnabled(int[] intActions) {
        for (int i = 0; i < intActions.length; i++) {
            actions[i].setEnabled(true);
        }
    }

    /**
     * carica le immagini che servono al infoPanel, istanzia l'infoPanel
     * passandogli font, img per il dado, e img sfondo, dimensioni
     */
    private void setUpInfoPanel() {
        List<Icon> listIcon = new ArrayList<Icon>();
        listIcon.add(new ImageIcon(".\\images\\dice1.png"));
        listIcon.add(new ImageIcon(".\\images\\dice2.png"));
        listIcon.add(new ImageIcon(".\\images\\dice3.png"));
        listIcon.add(new ImageIcon(".\\images\\dice4.png"));
        listIcon.add(new ImageIcon(".\\images\\dice5.png"));
        listIcon.add(new ImageIcon(".\\images\\dice6.png"));
        Image imageBg;
        try {
            imageBg = ImageIO.read(new File(".\\images\\info.png"));
            System.out.println("immagine infoPanel caricata");
        } catch (IOException ex) {
            imageBg = null;
            System.out.println("immagine infoPanel non caricata");
        }
        infoPanel = new InfoPanel(font.getFont(), listIcon, imageBg, 232, 444);
    }

    private void showResultDice(int result) {
        infoPanel.setText("è uscito: ");
        infoPanel.setDice(result);
        infoPanel.setVisible(true);
    }

    private void hideInfoPanel() {
        infoPanel.setVisible(false);
    }

    protected void moveShepherd(int idStartStreet, int idEndStreet) {
        Image shepherdImage = streets[idStartStreet].getImage();
        streets[idStartStreet].setFence();
        streets[idEndStreet].setImage(shepherdImage);
    }

    protected String askMoveShepherd() {
        String result = "";
        boolean ok = false;
        do {
            try {
                result += askStreet();
                result += ",";
                result += askStreet();
                ok = true;
            } catch (InterruptedException ex) {
                ok = false;
            }

        } while (!ok);
        return result;
    }

    private String askStreet() throws InterruptedException {
        for (Street street : streets) {
            if (street.isEmpty()) {
                street.addMouseListener(this);
            }
        }
        int result;
        synchronized (holder) {
            // wait for answer
            while (holder.isEmpty()) {
                holder.wait();
            }

            //la risposta che accetto è la prima
            result = holder.remove(0);
            System.out.println("prelevato da holder strada " + result);
        }
        return String.valueOf(result);
    }

    private void setMoney(int idShepherd, int amount) {
        playersJPanels[idShepherd].setAmount(amount);
    }

    private void setNumberTypeCard(int idTypeCard, int tot) {
        cardsJPanels[idTypeCard].setText(String.valueOf(tot));
    }

    private void showDice(int result) {
        infoPanel.setVisible(true);
        infoPanel.setDice(result);
    }

}
