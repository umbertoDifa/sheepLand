package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
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
public class GuiView implements MouseListener, TypeOfViewController,
        ActionListener {

    private JFrame frame;
    private JPanel mainJPanel;
    private Action[] actions;
    private Card[] cardsJPanels;
    private JPanel actionsContainerJPanel;
    private JPanel cardsConteinerJPanel;
    private MapBoard mapJPanel;
    private CardBoard fenceJPanel;
    private Player[] playersJPanels;
    private JPanel dxBar;
    private JPanel playersContainerJPanel;
    private JComponent layeredHolder;
    private JLayeredPane layeredPane;
    private InfoPanel infoPanel;
    private NickPanel nickPanel;
    private Street[] streets;
    private RegionBox[] regionBoxes;
    private HistoryPanel historyPanel;
    private JScrollPane historyScrollPane;

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
    private int lastStreet;
    private boolean zoomOn;

    public GuiView() {
        setUpImagePool();
        setUpFrame();
    }

    /**
     * inizializzo tutti i componenti della Gui
     */
    private void setUpFrame() {
        //istanzio font
        FontFactory.createFont();

        //istanzio tutti i componenti
        frame = new JFrame();
        layeredHolder = new JPanel();
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        mainJPanel = new JPanel();
        nickPanel = new NickPanel(this);
        setUpInfoPanel();
        historyPanel = new HistoryPanel();
        //aggiungo il textarea della history ad uno scrolla pane
        historyScrollPane = new JScrollPane(historyPanel);
        historyScrollPane.setPreferredSize(new Dimension((68 + 10) * 3, 80));
        historyScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        //imposto autoscroll
        historyScrollPane.getVerticalScrollBar().addAdjustmentListener(
                new AdjustmentListener() {
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        e.getAdjustable().setValue(
                                e.getAdjustable().getMaximum());
                    }
                });

        actionsContainerJPanel = new JPanel();
        cardsConteinerJPanel = new JPanel();
        mapJPanel = new MapBoard();
        actions = new Action[GameConstants.NUM_TOT_ACTIONS.getValue()];
        cardsJPanels = new Card[RegionType.values().length];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new Action();
        }
        for (int i = 0; i < cardsJPanels.length; i++) {
            cardsJPanels[i] = new Card(FontFactory.getFont(), "9", "0");
        }
        dxBar = new JPanel();
        playersContainerJPanel = new JPanel();
        fenceJPanel = new CardBoard(FontFactory.getFont(), String.valueOf(
                GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue()));

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
        actions[4].setUp(".\\images\\mateRam.png", 68, 72);
        actions[5].setUp(".\\images\\killOvine.png", 68, 72);

        cardsJPanels[1].setUp(".\\images\\hill2.png", 139, 91, 105, 104);
        cardsJPanels[2].setUp(".\\images\\countryside2.png", 139, 91, 105, 104);
        cardsJPanels[0].setUp(".\\images\\mountain2.png", 139, 91, 105, 104);
        cardsJPanels[5].setUp(".\\images\\desert2.png", 139, 91, 105, 104);
        cardsJPanels[4].setUp(".\\images\\lake2.png", 139, 91, 105, 104);
        cardsJPanels[3].setUp(".\\images\\plain2.png", 139, 91, 105, 104);

        fenceJPanel.setUp(".\\images\\numFences.png", 67, 77, 78, 94);

        for (RegionBox region : regionBoxes) {
            region.setUp((String) null, 52, 78);
        }

        //setto la struttura
        frame.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(900, 800));
        layeredPane.add(mainJPanel, new Integer(0));
        layeredPane.add(infoPanel, new Integer(1));
        layeredPane.add(nickPanel, new Integer(3));
        layeredPane.revalidate();

        mainJPanel.setLayout(new FlowLayout());
        mainJPanel.add(cardsConteinerJPanel);
        mainJPanel.add(mapJPanel);
        mainJPanel.add(dxBar);
        dxBar.setLayout(new FlowLayout());
        dxBar.add(playersContainerJPanel);
        dxBar.add(actionsContainerJPanel);

        // dxBar.add(historyPanel);
        dxBar.add(historyScrollPane);
        addComponentsToPane(mapJPanel, fenceJPanel, 55, 0);
        mapJPanel.setLayout(null);
        for (int i = 0; i < streets.length; i++) {
            mapJPanel.addPanel(streets[i], xStreetPoints[i] - 10,
                    yStreetPoints[i] - 10);
        }
        for (int i = 0; i < regionBoxes.length; i++) {
            mapJPanel.addPanel(regionBoxes[i], xRegionBoxes[i] - 10,
                    yRegionBoxes[i] - 10);
        }

        cardsConteinerJPanel.setLayout(new FlowLayout());
        actionsContainerJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        playersContainerJPanel.setLayout(new FlowLayout());
        for (Action action : actions) {
            actionsContainerJPanel.add(action);
        }
        for (CardBoard card : cardsJPanels) {
            cardsConteinerJPanel.add(card);
        }
        layeredHolder.add(layeredPane);
        frame.setContentPane(layeredHolder);

        //imposto colore sfondi
        layeredHolder.setBackground(backgroundColor);
        mainJPanel.setBackground(backgroundColor);
        actionsContainerJPanel.setBackground(noneColor);
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
        historyPanel.setBackground(Color.white);
        for (RegionBox region : regionBoxes) {
            region.setBackground(Color.MAGENTA);
        }

        //setto dimensioni
        actionsContainerJPanel.setPreferredSize(
                new Dimension((68 + 10) * 3, (72 + 5) * 2));
        //il contenitore dei player ha le dim per contenere sempre 4 player
        playersContainerJPanel.setPreferredSize(new Dimension(220, (99 + 4) * 4));
        cardsConteinerJPanel.setPreferredSize(new Dimension(105,
                (116 + 10) * cardsJPanels.length));
        mainJPanel.setPreferredSize(mainJPanel.getPreferredSize());
        mainJPanel.setBounds(0, 0, 900, 800);
        //la barra di destra ha le dim per contenere sempre 4 player
        dxBar.setPreferredSize(new Dimension((68 + 10) * 3, 800));
        infoPanel.setPreferredSize(new Dimension(232, 444));
        infoPanel.setBounds((int) (mainJPanel.getPreferredSize().width / 2.5 - (444 / 2)),
                mainJPanel.getPreferredSize().height / 2 - (400), 232, 444);
        nickPanel.setBounds(mainJPanel.getPreferredSize().width / 2 - (444 / 2),
                mainJPanel.getPreferredSize().height / 2 - (400), 140, 100);

        //nascondo infoPanel e mappa
        hideInfoPanel();
        mainJPanel.setVisible(false);

        //aggiungo listener delle strade
        for (Street street : streets) {
            street.addMouseListener(this);
            street.addMouseListener(street);
            street.setEnabled(false);
        }

        frame.pack();
        frame.setVisible(true);

    }

    private void setUpPlayers() {
        //rendo visibile il panel
        mainJPanel.setVisible(true);
        mainJPanel.revalidate();
        mainJPanel.repaint();

        //istanzio
        playersJPanels = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            playersJPanels[i] = new Player(nickNames[i], FontFactory.getFont());
            playersJPanels[i].setUp(".\\images\\giocatore" + i + ".png",
                    ".\\images\\money.png", 145, 81, 20, 40, 200, 99);
        }

        for (Player player : playersJPanels) {
            playersContainerJPanel.add(player);
        }
        for (Player player : playersJPanels) {
            player.setBackground(noneColor);
        }

        nickShepherdToStreet = new HashMap();

        for (int i = 0; i < numOfPlayers; i++) {
            for (int j = 0; j < shepherds4player; j++) {
                nickShepherdToStreet.put(nickNames[i] + "-" + j, null);
            }
        }

        frame.revalidate();
    }
    private static final List<String> holder = new LinkedList<String>();

    public static void main(String args[]) {
        //faccio gestire il thread da EDT, per il controllo ciclico della coda
        //degli eventi generati dai vari componenti

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GuiView gui = new GuiView();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        myNickName = nickPanel.getMyNickName();
        synchronized (holder) {
            holder.add(myNickName);
            holder.notify();
        }
    }

    public void mouseClicked(MouseEvent e) {
        synchronized (holder) {
            if (e.getSource() instanceof Action) {
                for (int i = 0; i < actions.length; i++) {
                    if (e.getSource().equals(actions[i])) {
                        DebugLogger.println(" azione " + i + "selezionata");

                        holder.add(String.valueOf(i + 1));
                        holder.notify();
                        DebugLogger.println("aggiunto a holder azione " + i);
                        switch (i) {
                            case 0:
                                showToHistoryPanel("Scelta azione: Muovi ovino.");
                                break;
                            case 1:
                                showToHistoryPanel(
                                        "Scelta azione: Muovi pastore.");
                                break;
                            case 2:
                                showToHistoryPanel(
                                        "Scelta azione: Compra carta.");
                                break;
                            case 3:
                                showToHistoryPanel(
                                        "Scelta azione: Accoppia pecore.");
                                break;
                            case 4:
                                showToHistoryPanel(
                                        "Scelta azione: Accoppia montone e pecora.");
                                break;
                            case 5:
                                showToHistoryPanel(
                                        "Scelta azione: Uccidi ovino.");
                                break;
                        }
                    }
                    actions[i].removeMouseListener(this);
                    actions[i].setOpaqueView(true);
                }
                mainJPanel.revalidate();
                mainJPanel.repaint();
            } else if (e.getSource() instanceof Street) {
                for (int i = 0; i < streets.length; i++) {
                    if (e.getSource().equals(streets[i])) {
                        DebugLogger.println("strada " + i + "selezionata");
                        holder.add(String.valueOf(i));
                        holder.notify();
                        DebugLogger.println("aggiunto a holder strada " + i);
                    }
//                    streets[i].removeMouseListener(this);
//                    streets[i].removeMouseListener(streets[i]);
                    streets[i].setEnabled(false);
                }
            } else if (e.getSource() instanceof CardBoard) {
                for (int i = 0; i < cardsJPanels.length; i++) {
                    if (e.getSource().equals(cardsJPanels[i])) {
                        DebugLogger.println("carta terreno " + i + "selezionata");
                        holder.add(String.valueOf(i));
                        holder.notify();
                        DebugLogger.println(
                                "aggiunto a holder carta terreno " + i);
                    }
                    cardsJPanels[i].removeMouseListener(this);
                }
            } else if (e.getSource() instanceof RegionBox) {
                for (int i = 0; i < regionBoxes.length; i++) {
                    if (e.getSource().equals(regionBoxes[i])) {
                        DebugLogger.println("regione " + i + "selezionata");
                        holder.add(String.valueOf(i));
                        holder.notify();
                        if (zoomOn) {
                            zoomAnimals(i);
                        }
                    }
                }
                removeAllRegionListener();

            } else if (e.getSource() instanceof Animal) {
                //se il click è su un animale aggiungo il tipo a holder e rimuovo tutti gli animali
                //dal layer 2, rimetto visibili in preview gli animali nelle regioni

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
                layeredPane.repaint();
                for (RegionBox region : regionBoxes) {
                    region.setAnimalsVisibles(true);
                    region.setAnimalPreview(true);
                }
            }
            DebugLogger.println(e.getX() + " , " + e.getY());
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
        showToHistoryPanel("Seleziona il territorio da comprare");
        String idResult = getAnswerByHolder();
        return RegionType.getRegionByIndex(Integer.parseInt(idResult));
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
    public static void addComponentsToPane(Container panelFather,
            JComponent panelSon, int x, int y) {

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
        ImagePool.add(".\\images\\info.png", "infoPanel");
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
            DebugLogger.println("immagine infoPanel caricata");
        } catch (IOException ex) {
            imageBg = null;
            DebugLogger.println("immagine infoPanel non caricata");
        }
        infoPanel = new InfoPanel(FontFactory.getFont(), listIcon, imageBg, 232, 444);
    }

    private void showResultDice(int result) {
        infoPanel.setText("è uscito: ");
        infoPanel.setDice(result);
        infoPanel.setVisible(true);
    }

    private void hideInfoPanel() {
        infoPanel.setVisible(false);
    }

    private void showDice(int result) {
        infoPanel.setVisible(true);
        infoPanel.setDice(result);
    }

    private void removeAllRegionListener() {
        for (RegionBox region : regionBoxes) {
            region.removeMouseListener(this);
        }
        DebugLogger.println("rimosso listener delle regioni");

    }

    private void addAllRegionListener() {
        for (RegionBox region : regionBoxes) {
            region.addMouseListener(this);
        }
        DebugLogger.println("aggiunto listener a tutte le regioni");

    }

    private int getIndexPlayerByNickName(String nickShepherd) {
        for (int indexPlayer = 0; indexPlayer < nickNames.length; indexPlayer++) {
            if (nickNames[indexPlayer].equals(nickShepherd)) {
                return indexPlayer;
            }
        }
        return -1;
    }

    private String getAnswerByHolder() {
        String result;
        DebugLogger.println("nella getanswerbyholder");

        synchronized (holder) {
            // wait for answer
            while (holder.isEmpty()) {
                DebugLogger.println("nel while");

                try {
                    holder.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GuiView.class.getName()).log(Level.SEVERE,
                            null, ex);
                }
            }
            //la risposta che accetto è la prima
            result = holder.remove(0);
            holder.clear();
            DebugLogger.println("prelevato da holder " + result);
        }
        DebugLogger.println("fine get answer by holder");

        return result;
    }

    private void setFreeStreetsClickable() {
        for (Street street : streets) {
            if (street.isEmpty()) {
//                street.addMouseListener(this);
//                street.addMouseListener(street);
                street.setEnabled(true);
            }
        }
    }

    private void zoomAnimals(int i) {
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
                    (int) (p.x + first * (animalWidth * Math.sqrt(2) * Math.cos(
                            (Math.PI / 4) + ((Math.PI * j) / 2))) / 1.5 + 140),
                    (int) (p.y - first * (animalWidth * Math.sqrt(2) * Math.sin(
                            (Math.PI / 4) + ((Math.PI * j) / 2))) / 1.5),
                    animalWidth, animalHeight);

            animalToHighlight.addMouseListener(this);
            layeredPane.add(animalToHighlight, new Integer(2));
            j++;
        }
        layeredPane.repaint();
    }

    /**
     * set clickable the streets where my shepherds are
     */
    private void setMyStreetClickable() {
        for (Street street : streets) {
            if (street.getImage() != null && street.getImage().equals(
                    ImagePool.getByName("shepherd" + getIndexPlayerByNickName(
                                    myNickName)))) {
//                street.addMouseListener(this);
//                street.addMouseListener(street);
                street.setEnabled(true);
            }
        }
    }

    private String getShepherdByStreet(String streetOfMyShepherd) {
        for (Map.Entry pairs : nickShepherdToStreet.entrySet()) {
            String key = (String) pairs.getKey();
            if (Integer.toString(nickShepherdToStreet.get(key)).equals(
                    streetOfMyShepherd)) {
                return key.split("-", -1)[1];
            }
        }
        return "0";
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
        showToHistoryPanel(info);
        DebugLogger.println("msg in info panel " + info);

        infoPanel.addMouseListener(infoPanel);
        infoPanel.hideDice();
        infoPanel.setText(info);
        infoPanel.setVisible(true);
        this.layeredHolder.revalidate();
        this.layeredHolder.repaint();
    }

    public void showBoughtLand(String boughLand, String price) {

        cardsJPanels[RegionType.valueOf(boughLand.toUpperCase()).getIndex()].increase(1);
        playersJPanels[getIndexPlayerByNickName(myNickName)].pay(Integer.parseInt(price));
        showInfo(
                "Hai acquistato la carta " + boughLand + " per " + price + " danari.");
    }

    public void showSetShepherd(int shepherdIndex, String streetIndex) {
        hideInfoPanel();
        refreshStreet(Integer.parseInt(streetIndex), false, myNickName);
        nickShepherdToStreet.putIfAbsent(myNickName + "-" + shepherdIndex,
                Integer.valueOf(streetIndex));
    }

    public void refreshGameParameters(String[] nickNames, int[] wallets,
            int shepherd4player) {
        this.nickNames = nickNames;
        this.numOfPlayers = nickNames.length;
        this.shepherds4player = shepherd4player;
        setUpPlayers();

        for (int i = 0; i < nickNames.length; i++) {
            playersJPanels[getIndexPlayerByNickName(nickNames[i])].setAmount(
                    wallets[i]);
        }
    }

    public void showMoveShepherd(String idShepherd, String priceToMove) {
        hideInfoPanel();

        DebugLogger.println(
                "in show move shepherd con " + idShepherd + " e " + priceToMove);
        //faccio pagare il player
        playersJPanels[getIndexPlayerByNickName(myNickName)].pay(
                Integer.parseInt(priceToMove));
        //metto il recinto nella strada dove si trovava
        streets[nickShepherdToStreet.get(myNickName + "-" + idShepherd)].setFence();
        //aggiorno l img della strada d arrivo
        streets[lastStreet].setImage("shepherd" + getIndexPlayerByNickName(
                myNickName));
        //aggiorno la hashmap
        nickShepherdToStreet.replace(myNickName + "-" + idShepherd, lastStreet);

        //decremento recinti
        fenceJPanel.decrease(1);
        //OLD
//        fenceJPanel.setText("" + (Integer.parseInt(fenceJPanel.getText()) - 1));

        mapJPanel.revalidate();
        mapJPanel.repaint();
    }

    public void showMoveOvine(String startRegion, String endRegion, String type) {
        hideInfoPanel();

        //TODO animazione?
        regionBoxes[Integer.parseInt(startRegion)].removeOvine(type);
        regionBoxes[Integer.parseInt(endRegion)].addAnimal(type);

        mapJPanel.revalidate();
        mapJPanel.repaint();
    }

    public void showMateSheepWith(String region, String otherType,
            String newType) {
        //TODO animazione accoppiamento?
        hideInfoPanel();

        regionBoxes[Integer.parseInt(region)].addAnimal(newType);
    }

    public void showMyRank(Boolean winner, String rank) {
        hideInfoPanel();

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
        hideInfoPanel();

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
        hideInfoPanel();

        showInfo("Il gioco è terminato per mancanza di giocatori, si scusiamo.");
    }

    public void showKillOvine(String region, String type, String shepherdPayed) {
        hideInfoPanel();

        regionBoxes[Integer.parseInt(region)].removeOvine(type);
        playersJPanels[getIndexPlayerByNickName(myNickName)].pay(
                GameConstants.PRICE_OF_SILENCE.getValue() * Integer.parseInt(
                        shepherdPayed));
        showInfo("Hai ucciso un " + type + " nella regione " + region
                + " pagando " + shepherdPayed + " pastori per il silenzio");
    }

    public String setUpShepherd(int idShepherd) {
        //svuoto la history appena il gioco inizia
        historyPanel.setText("");

        //imposto visibilità players
        playersJPanels[getIndexPlayerByNickName(myNickName)].isYourShift();
        for (int i = 0; i < playersJPanels.length; i++) {
            if (getIndexPlayerByNickName(myNickName) != i) {
                playersJPanels[i].isNotYourShift();
            }
        }

        setFreeStreetsClickable();
        showInfo("Scegli dove posizionare il pastore");
        String result = getAnswerByHolder();
        return result;
    }

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
            int numbOfLamb) {
        hideInfoPanel();

        regionBoxes[regionIndex].removeAllAnimals();

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

    public void refreshStreet(int streetIndex, boolean fence,
            String nickShepherd) {
        hideInfoPanel();

        if (fence) {
            streets[streetIndex].setFence();
        } else if (nickShepherd != null && !"null".equals(nickShepherd)) {
            int indexShepherd = getIndexPlayerByNickName(nickShepherd);
            streets[streetIndex].setImage("shepherd" + String.valueOf(
                    indexShepherd));
        }

    }

    public void refreshMoveShepherd(String nickNameMover, int shepherdIndex,
            String streetIndex) {
        hideInfoPanel();

        DebugLogger.println(
                "in refresh move shepherd con " + shepherdIndex + " e " + streetIndex + " e " + nickNameMover);

        //refresh della strada di partenza
        //cerco la strada dov'era il pastore e metto un recinto
        if (nickShepherdToStreet.get(nickNameMover + "-" + shepherdIndex) != null) {
            DebugLogger.println("metto fence");
            streets[nickShepherdToStreet.get(nickNameMover + "-" + shepherdIndex)].setFence();
            nickShepherdToStreet.replace((nickNameMover + "-" + shepherdIndex),
                    Integer.parseInt(streetIndex));

            //decremento recinti
            fenceJPanel.decrease(1);
            //OLD
//            fenceJPanel.setText(
//                    "" + (Integer.parseInt(fenceJPanel.getText()) - 1));
        }

        //refresh della strada di arrivo
        refreshStreet(Integer.parseInt(streetIndex), false, nickNameMover);
        DebugLogger.println("posiziono " + nickNameMover + " in " + streetIndex
        );
        if (null != nickShepherdToStreet.putIfAbsent(
                (nickNameMover + "-" + shepherdIndex), Integer.parseInt(
                        streetIndex))) {
            DebugLogger.println(" risultato putifabsent ok");
        }

    }

    public void refreshBuyLand(String buyer, String land, int price) {
        hideInfoPanel();

        playersJPanels[getIndexPlayerByNickName(buyer)].pay(price);
        showInfo("Il giocatore " + buyer + " ha acquistato un territorio "
                + land + " per " + price + " danari");
    }

    public void refreshKillOvine(String killer, String region, String type,
            String outcome) {
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

    public void refreshMoney(String money) {
        DebugLogger.println("inizio refresh money " + money);
        playersJPanels[getIndexPlayerByNickName(myNickName)].setAmount(
                Integer.parseInt(money));
        DebugLogger.println("fine refresh money " + money);
    }

    public void refereshCurrentPlayer(String currenPlayer) {

        for (int i = 0; i < playersJPanels.length; i++) {
            if (i == getIndexPlayerByNickName(currenPlayer)) {
                playersJPanels[i].isYourShift();
            } else {
                playersJPanels[i].isNotYourShift();
            }
        }
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
            regionBoxes[Integer.parseInt(startRegion)].removeSpecialAnimal(
                    "blacksheep");
            regionBoxes[Integer.parseInt(endRegion)].addAnimal("blacksheep");
            showInfo("La pecora nera si è spostata dalla regione " + startRegion
                    + " alla regione " + endRegion);
        } else if ("nok".equalsIgnoreCase(outcome)) {
            showInfo("La pecora nera non può muoversi");
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

            regionBoxes[Integer.parseInt(startRegion)].removeSpecialAnimal(
                    "wolf");
            regionBoxes[Integer.parseInt(endRegion)].addAnimal("wolf");
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
        playersJPanels[getIndexPlayerByNickName(player)].setEnabled(false);
        showInfo("Il giocatore " + player + " si è disconnesso");
        //TODO: effetto grafico
    }

    public void specialAnimalInitialCondition(String region) {
        String[] token = region.split(",");
        if ("Wolf".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].addAnimal("wolf");
            showInfo("Il lupo si trova nella regione " + token[1]);
        } else if ("BlackSheep".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].addAnimal("blacksheep");
            showInfo("La pecora nera si trova nella regione " + token[1]);
        }
    }

    public String chooseAction(int[] availableActions,
            String[] availableStringedActions) {
        //imposto visibilità players
        playersJPanels[getIndexPlayerByNickName(myNickName)].isYourShift();
        for (int i = 0; i < playersJPanels.length; i++) {
            if (getIndexPlayerByNickName(myNickName) != i) {
                playersJPanels[i].isNotYourShift();
            }
        }

        //imposto cliccabili solo le actions
        for (int i = 0; i < availableActions.length; i++) {
            actions[availableActions[i] - 1].addMouseListener(this);
            actions[availableActions[i] - 1].setOpaqueView(false);
        }

        mainJPanel.revalidate();
        mainJPanel.repaint();

        showToHistoryPanel("Scegli un azione");

        return getAnswerByHolder();
    }

    public void refreshMateSheepWith(String nickName, String region,
            String otherType, String newType,
            String outcome) {
        if ("ok".equals(outcome)) {
            regionBoxes[Integer.parseInt(region)].addAnimal(newType);
            showInfo(
                    "Il giocatore " + nickName + " ha accoppiato una pecora con un "
                    + otherType + " nella regione " + region + " ed è nato un " + newType + "!");

        } else {
            showInfo(
                    "Il giocatore " + nickName + " ha tentato di accoppiare una pecora con un " + otherType + " ma ha fallito!");
        }
    }

    public void refreshMoveOvine(String nickName, String type,
            String startRegion, String endRegion) {
        regionBoxes[Integer.parseInt(startRegion)].removeOvine(type);
        regionBoxes[Integer.parseInt(endRegion)].addAnimal(type);
        mapJPanel.revalidate();
        mapJPanel.repaint();
    }

    public String askMoveOvine() {
        addAllRegionListener();
        zoomOn = true;
        showToHistoryPanel("Seleziona la  regione di partenza:");
        String startRegion = getAnswerByHolder();

        showToHistoryPanel("Seleziona quale ovino muovere:");
        String ovineType = getAnswerByHolder();

        addAllRegionListener();

        zoomOn = false;
        showToHistoryPanel("Seleziona la  regione di arrivo:");
        String endRegion = getAnswerByHolder();

        return startRegion + "," + endRegion + "," + ovineType;

    }

    public String askMateSheepWith() {
        setMyStreetClickable();

        showToHistoryPanel("Seleziona il tuo pastore");
        String idShepherd = getShepherdByStreet(getAnswerByHolder());

        addAllRegionListener();
        showToHistoryPanel("Seleziona la  regione dell'accoppiamento");
        String region = getAnswerByHolder();
        return idShepherd + "," + region;
    }

    public String askKillOvine() {
        setMyStreetClickable();

        showToHistoryPanel("Seleziona il tuo pastore");
        String streetOfMyShepherd = getAnswerByHolder();

        String killerShepherd = getShepherdByStreet(streetOfMyShepherd);
        addAllRegionListener();
        zoomOn = true;

        showToHistoryPanel("Seleziona la  regione in cui uccidere");
        String startRegion = getAnswerByHolder();

        showToHistoryPanel("Seleziona quale ovino uccidere");
        String ovineType = getAnswerByHolder();
        zoomOn = false;

        return killerShepherd + "," + startRegion + "," + ovineType;

    }

    public String askMoveShepherd() {

        setMyStreetClickable();

        showToHistoryPanel("Seleziona il pastore da muovere");
        String streetOfMyShepherd = getAnswerByHolder();

        String shepherdToMove = getShepherdByStreet(streetOfMyShepherd);

        DebugLogger.println("trovato pastore " + shepherdToMove);

        setFreeStreetsClickable();
        showToHistoryPanel("Seleziona la  strada su cui spostare il pastore");
        String endStreet = getAnswerByHolder();

        lastStreet = Integer.parseInt(endStreet);

        return shepherdToMove + "," + endStreet;
    }

    public String askNickName() {
        DebugLogger.println("nella nickname");
        nickPanel.setVisible(true);
        String r = getAnswerByHolder();
        DebugLogger.println(r);
        return r;
    }

    public void refreshFences(int fences) {
        fenceJPanel.setText(String.valueOf(fences));
    }

    private void showToHistoryPanel(String message) {
        historyPanel.append("-");
        historyPanel.append(message);
        historyPanel.append("\n");
    }
}
