package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Francesco
 */
public class MyGui implements MouseListener, TypeOfViewController {

    private JFrame frame;
    private JPanel mainJPanel;
    private Action[] actions;
    private Card[] cardsJPanels;
    private JPanel actionsJPanel;
    private JPanel cardsConteinerJPanel;
    private MapBoard mapJPanel;
    private Card fenceJPanel;
    private Player[] playersJPanels;
    private JPanel dxBar;
    private JPanel playersContainerJPanel;
    private JComponent layeredHolder;
    private JLayeredPane layeredPane;
    private InfoPanel infoPanel;
    private Street[] streets;
    private RegionBox[] regionBoxes;

    private String[] nickNames;
    private String myNickName;
    private int numOfPlayers;
    private int shepherds4player;
    private Map<String, Integer> nickShepherdToStreet;
    int xStreetPoints[] = {126, 252, 342, 152, 200, 248, 289, 322, 353, 406, 81, 238, 307, 389, 437, 153, 219, 256, 292, 382, 186, 329, 151, 222, 298, 382, 118, 158, 228, 263, 298, 364, 421, 188, 225, 296, 326, 371, 124, 259, 188, 296};
    int yStreetPoints[] = {176, 114, 119, 223, 202, 179, 166, 195, 217, 171, 251, 232, 241, 237, 251, 281, 292, 266, 290, 286, 321, 321, 348, 343, 343, 340, 381, 413, 413, 367, 401, 406, 385, 461, 481, 474, 449, 494, 521, 503, 578, 552};
    int xRegionBoxes[] = {62, 88, 168, 168, 281, 170, 257, 343, 408, 322, 399, 381, 313, 309, 227, 156, 244, 81, 236};
    int yRegionBoxes[] = {131, 269, 349, 111, 73, 226, 194, 134, 185, 243, 285, 412, 349, 490, 549, 488, 410, 420, 292};
    private final Color backgroundColor = new Color(35, 161, 246);
    private final Color noneColor = new Color(0, 0, 0, 0);
    private final MyFont font = new MyFont();

    private final List<String> holder = new LinkedList<String>();

    public MyGui() {
        setUpImagePool();
        setUpFrame();
    }

    /**
     * inizializzo tutti i componenti della Gui
     */
    private void setUpFrame() {

        //istanzio tutti i componenti
        frame = new JFrame();
        layeredHolder = new JPanel();
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        mainJPanel = new JPanel();
        setUpInfoPanel();
        actionsJPanel = new JPanel();
        cardsConteinerJPanel = new JPanel();
        mapJPanel = new MapBoard();
        actions = new Action[GameConstants.NUM_TOT_ACTIONS.getValue()];
        cardsJPanels = new Card[RegionType.values().length];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new Action();
        }
        for (int i = 0; i < cardsJPanels.length; i++) {
            cardsJPanels[i] = new Card(font.getFont(), "0");
        }
        dxBar = new JPanel();
        playersContainerJPanel = new JPanel();
        fenceJPanel = new Card(font.getFont(), String.valueOf(GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue()));

        streets = new Street[xStreetPoints.length];
        List<Image> imgShepherds = new ArrayList<Image>();
        //ogni strada ha il sempre 4 pedine 
        for (int i = 0; i < 4; i++) {
            imgShepherds.add(ImagePool.getByName("shepherd" + String.valueOf(i)));
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

        fenceJPanel.setUp(".\\images\\numFences.png", 67, 77, 78, 94);

        for (RegionBox region : regionBoxes) {
            region.setUp((String) null, 52, 78);
        }

        //setto la struttura
        frame.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(900, 800));
        layeredPane.add(mainJPanel, new Integer(0));
        layeredPane.add(infoPanel, new Integer(1));

        mainJPanel.setLayout(new FlowLayout());
        mainJPanel.add(cardsConteinerJPanel);
        mainJPanel.add(mapJPanel);
        mainJPanel.add(dxBar);
        dxBar.setLayout(new FlowLayout());
        dxBar.add(playersContainerJPanel);
        dxBar.add(actionsJPanel);
        addComponentsToPane(mapJPanel, fenceJPanel, 55, 0);
        mapJPanel.setLayout(null);
        for (int i = 0; i < streets.length; i++) {
            mapJPanel.addPanel(streets[i], xStreetPoints[i] - 10, yStreetPoints[i] - 10);
        }
        for (int i = 0; i < regionBoxes.length; i++) {
            mapJPanel.addPanel(regionBoxes[i], xRegionBoxes[i] - 10, yRegionBoxes[i] - 10);
        }

        cardsConteinerJPanel.setLayout(new FlowLayout());
        actionsJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        playersContainerJPanel.setLayout(new FlowLayout());
        for (Action action : actions) {
            actionsJPanel.add(action);
        }
        for (Card card : cardsJPanels) {
            cardsConteinerJPanel.add(card);
        }
        layeredHolder.add(layeredPane);
        frame.setContentPane(layeredHolder);

        //imposto colore sfondi
        layeredHolder.setBackground(backgroundColor);
        mainJPanel.setBackground(backgroundColor);
        actionsJPanel.setBackground(noneColor);
        mapJPanel.setBackground(noneColor);
        cardsConteinerJPanel.setBackground(noneColor);
        for (Action action : actions) {
            action.setBackground(noneColor);
            action.repaint();
        }
        for (Street street : streets) {
            street.setBackground(noneColor);
        }
        fenceJPanel.setBackground(noneColor);
        playersContainerJPanel.setBackground(noneColor);
        dxBar.setBackground(noneColor);
        layeredHolder.setOpaque(true);
        infoPanel.setBackground(noneColor);
        for (RegionBox region : regionBoxes) {
            region.setBackground(Color.MAGENTA);
        }

        //setto dimensioni
        actionsJPanel.setPreferredSize(new Dimension((68 + 10) * 3, (72 + 10) * actions.length));
        //il contenitore dei player ha le dim per contenere sempre 4 player
        playersContainerJPanel.setPreferredSize(new Dimension(220, (99 + 10) * 4));
        cardsConteinerJPanel.setPreferredSize(new Dimension(105, (116 + 10) * cardsJPanels.length));
        mainJPanel.setPreferredSize(mainJPanel.getPreferredSize());
        mainJPanel.setBounds(0, 0, 900, 800);
        //la barra di destra ha le dim per contenere sempre 4 player
        dxBar.setPreferredSize(new Dimension((68 + 10) * 3,
                (((72 + 10) * actions.length) + (99 + 10) * 4) - 90));
        infoPanel.setPreferredSize(new Dimension(232, 444));
        infoPanel.setBounds(mainJPanel.getPreferredSize().width / 2 - (444 / 2), mainJPanel.getPreferredSize().height / 2 - (400), 232, 444);

//        //aggiungo this come listener per le azioni
//        for (Action action : actions) {
//            action.addMouseListener(this);
//        }
//        //aggiungo this come listener per la map
//        mapJPanel.addMouseListener(this);
//
//        //aggiungo l strada come listener di se stessa
//        for (Street street : streets) {
//            street.addMouseListener(street);
//            street.addMouseListener(this);
//        }
//
//        for (Card card : cardsJPanels) {
//            card.addMouseListener(this);
//        }
//
//        for (Player player : playersJPanels) {
//            player.addMouseListener(player);
//        }
//        infoPanel.addMouseListener(infoPanel);
//
        for (RegionBox regionBox : regionBoxes) {
            regionBox.addMouseListener(this);
        }
        hideInfoPanel();

        frame.pack();
        frame.setVisible(true);
    }

    private void setUpPlayers() {
        //istanzio
        playersJPanels = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            playersJPanels[i] = new Player(nickNames[i], font.getFont());
            playersJPanels[i].setUp(".\\images\\giocatore" + i + ".png", ".\\images\\money.png", 145, 81, 20, 40, 200, 99);
        }

        for (Player player : playersJPanels) {
            playersContainerJPanel.add(player);
        }
        for (Player player : playersJPanels) {
            player.setBackground(noneColor);
        }

        nickShepherdToStreet = new HashMap();

        for (int i = 0; i < numOfPlayers; i++) {
            for (int j = 0; j < shepherds4player; j++)
            {
                nickShepherdToStreet.put(nickNames[i]+"-"+j, null);
            }
        }

        frame.revalidate();
    }

    public static void main(String args[]) {
        //faccio gestire il thread da EDT, per il controllo ciclico della coda
        //degli eventi generati dai vari componenti
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MyGui gui = new MyGui();
                //test regions
                gui.refreshGameParameters(new String[]{"a", "b"}, 4);
                gui.refreshRegion(0, 1, 0, 2);
                gui.refreshRegion(4, 2, 1, 0);
                gui.refreshRegion(18, 3, 2, 84);
                gui.refreshRegion(7, 0, 3, 0);
                //test streets
                gui.refreshStreet(15, true, "a");
                gui.refreshStreet(35, false, "b");
                gui.refreshStreet(15, true, "c");
                gui.refreshStreet(35, true, "");
                gui.refreshStreet(15, false, "a");
                gui.refreshStreet(0, false, "b");
                //test moveShepherd
                gui.refreshMoveShepherd("b", 0, String.valueOf(2));

                //test
                gui.showMoveOvine("0", "7", "sheep");

                //test
                gui.specialAnimalInitialCondition("Blacksheep,3");
                gui.refreshBlackSheep("ok,3,0,5");
                
                gui.showKillOvine("7", "sheep", "84");
                gui.showKillOvine("18", "lamb", "0");

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
                        holder.add(String.valueOf(i));
                        holder.notify();
                        System.out.println("aggiunto a holder azione " + i);
                    }
                }
            } else if (e.getSource() instanceof Street) {
                for (int i = 0; i < streets.length; i++) {
                    if (e.getSource().equals(streets[i])) {
                        System.out.println("strada " + i + "selezionata");
                        streets[i].removeMouseListener(this);
                        holder.add(String.valueOf(i));
                        holder.notify();
                        System.out.println("aggiunto a holder strada " + i);
                    }
                }
            } else if (e.getSource() instanceof Card) {
                for (int i = 0; i < cardsJPanels.length; i++) {
                    if (e.getSource().equals(cardsJPanels[i])) {
                        System.out.println("carta terreno " + i + "selezionata");
                        cardsJPanels[i].removeMouseListener(this);
                        holder.add(String.valueOf(i));
                        holder.notify();
                        System.out.println("aggiunto a holder carta terreno " + i);
                    }
                }
                //TODO spostare in RegionBox (e aggiungere getter layeredPane)
            } else if (e.getSource() instanceof RegionBox) {
                for (int i = 0; i < regionBoxes.length; i++) {
                    if (e.getSource().equals(regionBoxes[i])) {
                        System.out.println("regione " + i + "selezionata");
                        List<Animal> animalsToHighlight = regionBoxes[i].cloneAndHideAnimals();
                        int j = 0;
                        for (Animal animalToHighlight : animalsToHighlight) {
                            int animalWidth = animalToHighlight.getSize().width;
                            int animalHeight = animalToHighlight.getSize().height;
                            Point p = regionBoxes[i].getLocation();
                            int first = 1;
                            if (j == 0) {
                                first = 0;
                            }
                            animalToHighlight.setBounds(
                                    (int) (p.x + first * (animalWidth * Math.sqrt(2) * Math.cos((Math.PI / 4) + ((Math.PI * j) / 2))) / 1.5 + 140),
                                    (int) (p.y - first * (animalWidth * Math.sqrt(2) * Math.sin((Math.PI / 4) + ((Math.PI * j) / 2))) / 1.5),
                                    animalWidth, animalHeight);

                            animalToHighlight.addMouseListener(this);
                            layeredPane.add(animalToHighlight, new Integer(2));
                            j++;
                        }
                        layeredPane.repaint();
                    }
                }
                //FIXME da rimettere
                //   removeRegionListener();
                //se il click è su un animale aggiungo il tipo a holder e rimuovo tutti gli animali
                //dal layer 2, rimetto visibili in preview gli animali nelle regioni
            } else if (e.getSource() instanceof Animal) {
                for (Component component : layeredPane.getComponents()) {
                    if (component instanceof Animal) {
                        Animal animal = (Animal) component;
                        holder.add(animal.getAnimalType());
                        holder.notify();
                    }
                }
                Component[] toRemove = layeredPane.getComponentsInLayer(2);
                for (Component componentToRemove : toRemove) {
                    componentToRemove.setVisible(false);
                    layeredPane.remove(componentToRemove);
                }
                System.out.println("num comp in layer 1:" + layeredPane.getComponentCountInLayer(2));
                layeredPane.repaint();
                System.out.println(layeredPane.getComponentCountInLayer(2));
                for (RegionBox region : regionBoxes) {
                    region.setAnimalsVisibles(true);
                    region.setAnimalPreview(true);
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

    public String askBuyLand() {
        for (int i = 0; i < cardsJPanels.length; i++) {
            cardsJPanels[i].addMouseListener(this);
        }
        String result = getAnswerByHolder();
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
        ImagePool.add(".\\images\\shepherd1.png", "shepherd1");
        ImagePool.add(".\\images\\shepherd2.png", "shepherd2");
        ImagePool.add(".\\images\\shepherd3.png", "shepherd3");
        ImagePool.add(".\\images\\shepherd0.png", "shepherd0");
        ImagePool.add(".\\images\\fence2.png", "fence");
        ImagePool.add(".\\images\\sheep2P.png", "sheepP");
        ImagePool.add(".\\images\\wolf2P.png", "wolfP");
        ImagePool.add(".\\images\\blacksheepP.png", "blacksheepP");
        ImagePool.add(".\\images\\ramP.png", "ramP");
        ImagePool.add(".\\images\\lambP.png", "lambP");
        ImagePool.add(".\\images\\sheep.png", "sheep");
        ImagePool.add(".\\images\\wolf.png", "wolf");
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

    private String askStreet() throws InterruptedException {
        for (Street street : streets) {
            if (street.isEmpty()) {
                street.addMouseListener(this);
            }
        }
        String result = getAnswerByHolder();
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

    private void removeRegionListener() {
        for (RegionBox region : regionBoxes) {
            region.removeMouseListener(this);
        }
    }

    private int getIndexShepherdByNickName(String nickShepherd) {
        for (int indexShepherd = 0; indexShepherd < nickNames.length; indexShepherd++) {
            if (nickNames[indexShepherd] == nickShepherd) {
                return indexShepherd;
            }
        }
        return -1;
    }

    private String getAnswerByHolder() {
        String result;
        synchronized (holder) {
            // wait for answer
            while (holder.isEmpty()) {
                try {
                    holder.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //la risposta che accetto è la prima
            result = holder.remove(0);
            System.out.println("prelevato da holder " + result);
        }
        return result;
    }

    private void setFreeStreetsClickable() {
        for (Street street : streets) {
            if (street.isEmpty()) {
                street.addMouseListener(this);
            }
            //FIXME solo per test aggiungo strada come listener
            street.addMouseListener(street);
        }
    }

    //metodi dell'intefaccia
    //metodi dell'intefaccia
    //metodi dell'intefaccia
    //metodi dell'intefaccia
    //metodi dell'intefaccia
    //metodi dell'intefaccia
    public void showWelcome() {
        showInfo("Benvenuto. Il gioco sta per iniziare!");
    }

    public void showEndGame() {
        showInfo("Il gioco è terminato. Arrivederci.");
    }

    public void showInfo(String info) {
        infoPanel.addMouseListener(infoPanel);
        infoPanel.hideDice();
        infoPanel.setText(info);
        infoPanel.setVisible(true);
        infoPanel.repaint();
    }

    public void showBoughtLand(String boughLand, String price) {
        showInfo("Hai acquistato la carta " + boughLand + " per " + price + " danari.");
        //TODO aggiornare carte
    }

    public void showSetShepherd(int shepherdIndex, String streetIndex) {
        refreshStreet(Integer.parseInt(streetIndex), false, myNickName);
        nickShepherdToStreet.replace(myNickName+"-"+shepherdIndex,
                Integer.valueOf(streetIndex));
    }

    public void refreshGameParameters(String[] nickNames, int shepherds4Player) {
        this.nickNames = nickNames;
        this.numOfPlayers = nickNames.length;
        this.shepherds4player = shepherds4Player;
        setUpPlayers();
    }

    //problema: dovrei tener traccia dell'ultima risposta del metodo askMoveShepherd.
    //ma anche volendo il risultato se va bene i chiama qst metodo. se va male invece
    //l ho mette come messaggio nell showInfo quindi nello show info dovrei far dei controlli
    //se cancellare o meno il risultato della askMoveShepherd di cui avevo tenuto traccia..
    public void showMoveShepherd(String priceToMove) {
        playersJPanels[getIndexShepherdByNickName(myNickName)].pay(Integer.parseInt(priceToMove));
//        nickShepherdToStreet.get(myNickName+)
        //aggiorno soldi
        //rimuovo shepherd dalla startStreet
        //aggiungo hepherd nella endStreet
    }

    public void showMoveOvine(String startRegion, String endRegion, String type) {
        //TODO animazione?
        regionBoxes[Integer.parseInt(startRegion)].removeOvine(type);
        regionBoxes[Integer.parseInt(endRegion)].add(type);
    }

    public void showMateSheepWith(String region, String otherType, String newType) {
        //TODO animazione accoppiamento?
        regionBoxes[Integer.parseInt(region)].add(newType);
    }

    public void showMyRank(Boolean winner, String rank) {
        String result = "Hai ";
        if (winner) {
            result += "vinto ";
        } else {
            result += "perso ";
        }
        result += "con " + rank + " punti.";
        showInfo(result);
    }

    public void showClassification(String classification) {
        String classificationToShow = "Classifica: ";
        String[] token = classification.split(",");
        int i = 0;
        while (i < token.length - 1) {
            classification += "Giocatore :" + token[i] + " punteggio: " + token[i + 1];
            i += 2;
        }
        showInfo(classificationToShow);
    }

    public void showUnexpectedEndOfGame() {
        showInfo("Il gioco è terminato per mancanza di giocatori, si scusiamo.");
    }

    public void showKillOvine(String region, String type, String shepherdPayed) {
        showInfo("Hai ucciso un " + type + " nella regione " + region
                + " pagando " + shepherdPayed + " pastori per il silenzio");
        regionBoxes[Integer.parseInt(region)].removeOvine(type);
    }

    public String setUpShepherd(int idShepherd) {
        setFreeStreetsClickable();
        String result = getAnswerByHolder();
        return result;
    }

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam, int numbOfLamb) {
        if (numbOfSheep > 0) {
            regionBoxes[regionIndex].add("sheep", numbOfSheep);
        }
        if (numbOfRam > 0) {
            regionBoxes[regionIndex].add("ram", numbOfRam);
        }
        if (numbOfLamb > 0) {
            regionBoxes[regionIndex].add("lamb", numbOfLamb);
        }
    }

    public void refreshStreet(int streetIndex, boolean fence, String nickShepherd) {
        if (fence) {
            streets[streetIndex].setFence();
        } else if (nickShepherd != null) {
            int indexShepherd = getIndexShepherdByNickName(nickShepherd);
            streets[streetIndex].setImage("shepherd" + String.valueOf(indexShepherd));
        }

    }

    public void refreshMoveShepherd(String nickNameMover, int shepherdIndex, String streetIndex) {
        //refresh della strada di partenza
        //cerco la strada dov'era il pastore, lo rimuovo e metto un recinto
        //FIXME funziona solo con un pastore... mi serve la startStreet
        for (Street street : streets) {
            if (street.getStringedImage().equals("shepherd" + getIndexShepherdByNickName(nickNameMover))) {
                street.removeImg();
                street.setFence();
            }
        }

        //refresh della strada di arrivo
        //metto il pastore nella nuova strada
        refreshStreet(Integer.parseInt(streetIndex), false, nickNameMover);
    }

    public void refreshBuyLand(String buyer, String land, int price) {
        showInfo("Il giocatore " + buyer + " ha acquistato un territorio "
                + land + " per " + price + " danari");
        //TODO aggiungere +1 al territorio fra le mie carte
    }

    public void refreshKillOvine(String killer, String region, String type, String outcome) {
        if (outcome.equals("ok")) {
            regionBoxes[Integer.parseInt(region)].removeOvine(type);
        }
        String resultToShow = "Il giocatore " + killer + " ha ";
        if (outcome.equals("ok")) {
            resultToShow += "ucciso ";
        } else {
            resultToShow += "provato ad uccidere ";
        }
        showInfo(resultToShow + "un " + type + " nella regione " + region);

    }

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer, int shepherd4player) {
        //FIXME da rimuovere dall interfaccia poi da qua
    }

    public void refreshMoney(String money) {
        playersJPanels[getIndexShepherdByNickName(myNickName)].setAmount(Integer.parseInt(money));
    }

    public void refereshCurrentPlayer(String currenPlayer) {
        showInfo("E' il turno di " + currenPlayer);
    }

    public void refereshCard(String type, int value) {
        //TODO aggiungere alle mie carte
    }

    public void refreshBlackSheep(String result) {
        //splitta il risultato e raccogli l'outcome
        String[] token = result.split(",");
        String outcome = token[0];

        String diceValue = token[1];
        String startRegion = token[2];
        if ("ok".equalsIgnoreCase(outcome)) {
            String endRegion = token[3];
            regionBoxes[Integer.parseInt(startRegion)].removeOvine("blacksheep");
            regionBoxes[Integer.parseInt(endRegion)].add("blacksheep");
            showInfo("La pecora nera si è spostata da la regione " + startRegion
                    + " alla regione " + endRegion + " passando per la strada di valore " + diceValue);
        } else if ("nok".equalsIgnoreCase(outcome)) {
            showInfo(
                    "La pecora nera non può muoversi, la strada di valore "
                    + diceValue + " è bloccata o non esiste nella regione " + startRegion);
        }
    }

    public void refreshWolf(String result) {
        //splitta il risultato e raccogli l'outcome
        String[] token = result.split(",");
        String outcome = token[0];

        if ("ok".equalsIgnoreCase(outcome)) {
            String fence = token[1];
            String ovine = token[2];
            String diceValue = token[3];
            String startRegion = token[4];
            String endRegion = token[5];

            regionBoxes[Integer.parseInt(startRegion)].removeOvine("wolf");
            regionBoxes[Integer.parseInt(endRegion)].add("wolf");
            if ("ok".equalsIgnoreCase(fence)) {
                showInfo(
                        "Il lupo si è mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando sulla strada di valore " + diceValue + " e saltando la recinzione!");
            } else {
                showInfo(
                        "Il lupo si è mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando per la strada di valore " + diceValue);
            }
            if (!"nok".equalsIgnoreCase(ovine)) {
                regionBoxes[Integer.parseInt(endRegion)].removeOvine(ovine);
                showInfo("Il lupo ha mangiato una " + ovine);
            }
        } else {
            String diceValue = token[1];
            String startRegion = token[2];
            showInfo(
                    "Il lupo non è riuscito a passare attraverso la strada di valore "
                    + diceValue + " nella regione " + startRegion);
        }
    }

    public void refreshPlayerDisconnected(String player) {
        playersJPanels[getIndexShepherdByNickName(player)].setEnabled(false);
        showInfo("Il giocatore " + player + " si è disconnesso");
    }

    public void specialAnimalInitialCondition(String region) {
        String[] token = region.split(",");
        if ("Wolf".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].add("wolf");
            showInfo("Il lupo si trova nella regione " + token[1]);
        } else if ("BlackSheep".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].add("blacksheep");
            showInfo("La pecora nera si trova nella regione " + token[1]);
        }
    }

    public String chooseAction(int[] availableActions, String[] availableStringedActions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMateSheepWith(String nickName, String region, String otherType, String newType, String outcome) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMoveOvine(String nickName, String type, String startRegion, String endRegion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askMoveOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askMateSheepWith() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askKillOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askMoveShepherd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
