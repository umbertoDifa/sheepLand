package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.BorderLayout;
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
 * The class is the gui, it starts the thread to create and display the JFrame,
 * implements the interface to be commanded by the Client. It creates and
 * orchestrates all the other components of the JFrame It manages the events
 * that listens
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

    /**
     * Constructor of the gui. Create JFrame and starts the setUp of ImagePool
     * and the JFrame itself.
     */
    public GuiView() {
        setUpImagePool();
        setUpFrame();
    }

    /**
     * instanciates all the components of the JFrame create the hierarchy of the
     * structure. add to the components images, colors, dimensions and the
     * listener "this"
     */
    private void setUpFrame() {

        instantiateFrameComponents();

        historyScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

//        //imposto autoscroll
//        historyScrollPane.getVerticalScrollBar().addAdjustmentListener(
//                new AdjustmentListener() {
//                    public void adjustmentValueChanged(AdjustmentEvent e) {
//                        e.getAdjustable().setValue(
//                                e.getAdjustable().getMaximum());
//                    }
//                });

        setUpImageFrameComponents();

        setUpFrameStructure();
        setUpBackgroundColorFrameComponents();
        setUpDimensionFrameComponents();

        //nascondo infoPanel e mappa
        mainJPanel.setVisible(false);

        addFrameComponentsListener();

        frame.pack();
        frame.setVisible(true);

    }

    /**
     * It instantiates all the Players, setting up with images, dimension. It
     * adds them to the hierarchy of the gui and fill the hashmap
     * "nickShepherdToStreet"
     */
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

    /**
     * The method instantiates all the components of the JFrame givins to the
     * constructor all the parameters needed
     */
    private void instantiateFrameComponents() {
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
        actionsContainerJPanel = new JPanel();
        cardsConteinerJPanel = new JPanel();
        mapJPanel = new MapBoard();
        actions = new Action[GameConstants.NUM_TOT_ACTIONS.getValue()];
        cardsJPanels = new Card[RegionType.values().length];
        actions[0] = new Action("Muovi ovino");
        actions[1] = new Action("Muovi pastore");
        actions[2] = new Action("Compra carta");
        actions[3] = new Action("Accoppia pecore");
        actions[4] = new Action("Accoppia montone e percora");
        actions[5] = new Action("Uccidi ovino");

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

    }

    /**
     * It gives to all the components a background image with dimension and
     * eventually the coordinates of the text in the image
     */
    private void setUpImageFrameComponents() {
        //aggiungo immagini
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
    }

    /**
     * It creates the structure of all the elements of the JFrame
     */
    private void setUpFrameStructure() {
        //setto la struttura
        frame.setLayout(null);
        layeredPane.add(mainJPanel, new Integer(0));
        layeredPane.add(infoPanel, new Integer(3));
        layeredPane.add(nickPanel, new Integer(2));
        layeredPane.revalidate();

        mainJPanel.setLayout(new FlowLayout());
        mainJPanel.add(cardsConteinerJPanel);
        mainJPanel.add(mapJPanel);
        mainJPanel.add(dxBar);
        dxBar.setLayout(new FlowLayout());
        dxBar.add(playersContainerJPanel);
        dxBar.add(actionsContainerJPanel);
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
    }

    /**
     * It sets up the backgroundcolor of the components of the jframe
     */
    private void setUpBackgroundColorFrameComponents() {
        //imposto colore sfondi
        layeredHolder.setBackground(backgroundColor);
        mainJPanel.setBackground(backgroundColor);
        actionsContainerJPanel.setBackground(noneColor);
        cardsConteinerJPanel.setBackground(noneColor);
        playersContainerJPanel.setBackground(noneColor);
        dxBar.setBackground(noneColor);
        layeredHolder.setOpaque(true);
    }

    /**
     * It sets up the dimensions of the components of the jframe
     */
    private void setUpDimensionFrameComponents() {
        //setto dimensioni
        layeredPane.setPreferredSize(new Dimension(900, 800));
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
        infoPanel.setBounds((int) (mainJPanel.getPreferredSize().width / 2.5 - (444 / 2)),
                mainJPanel.getPreferredSize().height / 2 - (400), 232, 444);
        nickPanel.setBounds((int) (mainJPanel.getPreferredSize().width / 2 - (444 / 2)),
                mainJPanel.getPreferredSize().height / 2 - (400), 140, 100);
        historyScrollPane.setPreferredSize(new Dimension((68 + 10) * 3, 80));

    }

    /**
     * It adds the listener "this" to all the components of the jframe
     */
    private void addFrameComponentsListener() {
        //aggiungo listener delle strade
        //imposto che non generano eventi
        for (Street street : streets) {
            street.addMouseListener(this);
            street.setEnabled(false);
        }

        //aggiungo listener delle regioni
        //imposto che non generano eventi
        for (RegionBox region : regionBoxes) {
            region.addMouseListener(this);
            region.setEnabled(false);
        }

        //aggiungo listener delle azioni
        //imposto che non generano eventi
        for (Action action : actions) {
            action.addMouseListener(this);
            action.setEnabled(false);
        }

        //aggiungo listener delle carte
        //imposto che non generano eventi
        for (Card card : cardsJPanels) {
            card.addMouseListener(this);
            card.setEnabled(false);
        }
    }

    private static final List<String> holder = new LinkedList<String>();

    /**
     * When the button to submit the nickname inserted is clicked it sets the
     * parameter MyNickName and adds it to holder
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        myNickName = nickPanel.getMyNickName();
        synchronized (holder) {
            holder.add(myNickName);
            holder.notify();
        }
    }

    /**
     * when a JPanel between Action, Street, CardBoard, RegionBox and Animal is
     * clicked, the method call the right method
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        synchronized (holder) {
            if (e.getSource() instanceof Action) {
                actionClicked(e);
            } else if (e.getSource() instanceof Street) {
                streetClicked(e);
//                }
            } else if (e.getSource() instanceof CardBoard) {
                cardBoardClicked(e);
            } else if (e.getSource() instanceof RegionBox) {

                regionClicked(e);

            } else if (e.getSource() instanceof Animal) {
                animalClicked(e);
            }
            DebugLogger.println(e.getX() + " , " + e.getY());
        }
    }

    /**
     * When an Action is clicked, the method adds to holder the corrisponding id
     * and set disabled all the Actions
     *
     * @param e
     */
    private void actionClicked(MouseEvent e) {
        for (int i = 0; i < actions.length; i++) {
            if (e.getSource().equals(actions[i])) {
                DebugLogger.println(" azione " + i + "selezionata");

                holder.add(String.valueOf(i + 1));
                holder.notify();
                DebugLogger.println("aggiunto a holder azione " + i);
                switch (i) {
                    case 0:
                        historyPanel.showToHistoryPanel("Scelta azione: Muovi ovino.");
                        break;
                    case 1:
                        historyPanel.showToHistoryPanel(
                                "Scelta azione: Muovi pastore.");
                        break;
                    case 2:
                        historyPanel.showToHistoryPanel(
                                "Scelta azione: Compra carta.");
                        break;
                    case 3:
                        historyPanel.showToHistoryPanel(
                                "Scelta azione: Accoppia pecore.");
                        break;
                    case 4:
                        historyPanel.showToHistoryPanel(
                                "Scelta azione: Accoppia montone e pecora.");
                        break;
                    case 5:
                        historyPanel.showToHistoryPanel(
                                "Scelta azione: Uccidi ovino.");
                        break;
                }
            }
            actions[i].setEnabled(false);
            actions[i].setOpaqueView(true);
        }
        mainJPanel.revalidate();
        mainJPanel.repaint();
    }

    /**
     * When a Street is clicked, the method adds to holder the corrisponding id
     * and set disabled all the Streets
     *
     * @param e
     */
    private void streetClicked(MouseEvent e) {
        for (int i = 0; i < streets.length; i++) {
            if (e.getSource().equals(streets[i])) {
                DebugLogger.println("strada " + i + "selezionata");
                holder.add(String.valueOf(i));
                holder.notify();
                DebugLogger.println("aggiunto a holder strada " + i);
            }
            streets[i].setEnabled(false);
        }
    }

    /**
     * When an CardBoard is clicked, the method adds to holder the corrisponding
     * id and set disabled all the CardBoard
     *
     * @param e
     */
    private void cardBoardClicked(MouseEvent e) {
        for (int i = 0; i < cardsJPanels.length; i++) {
            if (e.getSource().equals(cardsJPanels[i])) {
                DebugLogger.println("carta terreno " + i + "selezionata");
                holder.add(String.valueOf(i));
                holder.notify();
                DebugLogger.println(
                        "aggiunto a holder carta terreno " + i);
            }
            cardsJPanels[i].setEnabled(false);
            cardsJPanels[i].revalidate();
        }
        mainJPanel.revalidate();
        mainJPanel.repaint();
    }

    /**
     * When a RegionBox is clicked, the method adds to holder the corrisponding
     * id and set disabled all the RegionBox
     *
     * @param e
     */
    private void regionClicked(MouseEvent e) {
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
        disableAllRegionListener();
    }

    /**
     * When an Animal is clicked, the method adds to holder the corrispondindg
     * AnimalType. Then it removes all the Animal from the layer 1 and switches
     * on the Animal preview of all the regions
     *
     * @param e
     */
    private void animalClicked(MouseEvent e) {

        //casto l'oggetto che ha generato l evento a Animal
        Animal chosenAnimal = (Animal) e.getSource();

        //per ogni oggetto nel layeredPane
        for (Component component : layeredPane.getComponents()) {
            //se è un Animal
            if (component instanceof Animal) {
                //lo casto ad Animal
                Animal animal = (Animal) component;
                //se ha AnimalType uguale all AnimalType di chi ha generato l evento
                if (animal.getAnimalType().equals(chosenAnimal.getAnimalType())) {
                    //aggiungo ad holder il corrispondente tipo
                    holder.add(animal.getAnimalType());
                    holder.notify();
                }
            }
        }

        //rimuovo tutti gli Animal dal layer 1
        Component[] toRemove = layeredPane.getComponentsInLayer(1);
        for (Component componentToRemove : toRemove) {
            componentToRemove.setVisible(false);
            layeredPane.remove(componentToRemove);
        }
        layeredPane.repaint();

        //per ogni regione
        for (RegionBox region : regionBoxes) {
            //rimetto visibili gli animali
            region.setAnimalsVisibles(true);
            //in modalità preview
            region.setAnimalPreview(true);
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
     * Place the son panel in the father panel, placing the left corner of the
     * son in x,y rispect the father
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
     * It adds to the ImagePool all the images that are used dinamically
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
        ImagePool.add(".\\images\\nickPanel.png", "nickPanel");
    }

    /**
     * Load all the images for the InfoPanel and instantiates the InfoPanel with
     * font, list of images, and dimensions
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

    /**
     * show infoPanel with the result of the dice
     *
     * @param result the dice result to show
     */
    private void showResultDice(int result) {
        infoPanel.setText("è uscito: ");
        infoPanel.setDice(result);
        infoPanel.setVisible(true);
    }

    /**
     * Hide the InfoPanel setting its visibility to false
     */
    private void hideInfoPanel() {
        infoPanel.setVisible(false);
    }

    /**
     * Set disabled all the regions so they don't generate events when clicked
     */
    private void disableAllRegionListener() {
        for (RegionBox region : regionBoxes) {
            region.setEnabled(false);
        }
        DebugLogger.println("rimosso listener delle regioni");

    }

    /**
     * Set enabled all the regions do they can generate events when clicked
     */
    private void addAllRegionListener() {
        for (RegionBox region : regionBoxes) {
            region.setEnabled(true);
        }
        DebugLogger.println("aggiunto listener a tutte le regioni");

    }

    /**
     * Return the Index of the Player that corrisponds to the nickname of the
     * shepherd
     *
     * @param nickShepherd
     * @return
     */
    private int getIndexPlayerByNickName(String nickShepherd) {
        for (int indexPlayer = 0; indexPlayer < nickNames.length; indexPlayer++) {
            if (nickNames[indexPlayer].equals(nickShepherd)) {
                return indexPlayer;
            }
        }
        return -1;
    }

    /**
     * Wait for answer by the holder. Return the first element of the holder
     * when added
     *
     * @return
     */
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

    /**
     * Set enabled only the empty streets so that they can generate events
     */
    private void setFreeStreetsClickable() {
        for (Street street : streets) {
            if (street.isEmpty()) {
                street.setEnabled(true);
            }
        }
    }

    /**
     * It clones all the Animals of the i-th RegionBox. Dispose them in circle
     * setting the bounds. Set them clickable, adds them to the layer 1
     *
     * @param i
     */
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

            if (!"blacksheep".equals(animalToHighlight.getAnimalType())
                    || !"wolf".equals(animalToHighlight.getAnimalType())) {
                animalToHighlight.addMouseListener(this);
            }
            layeredPane.add(animalToHighlight, new Integer(1));
            j++;
        }
        layeredPane.repaint();
    }

    /**
     * set enalbed the street/s where my shepherds are. so the street/s can
     * generate events.
     */
    private void setMyStreetClickable() {
        for (Street street : streets) {
            if (street.getImage() != null && street.getImage().equals(
                    ImagePool.getByName("shepherd" + getIndexPlayerByNickName(
                                    myNickName)))) {
                street.setEnabled(true);
            }
        }
    }

    /**
     * return the Shepherd id that is on the specific street. 0 if it don't find
     * it
     *
     * @param streetOfMyShepherd
     * @return
     */
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
        historyPanel.showToHistoryPanel(info);
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
        refreshStreet(Integer.parseInt(streetIndex), false, myNickName, shepherdIndex);
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

        layeredPane.add(new RankPannel(result), new Integer(6));
        mainJPanel.revalidate();
        mainJPanel.repaint();
    }

    public void showClassification(String classification) {

        String classificationToShow = "Classifica: \n";
        String[] token = classification.split(",");
        int i = 0;
        while (i < token.length - 1) {
            classificationToShow += "Giocatore " + token[i] + " punteggio: " + token[i + 1]+"\n";
            i += 2;
        }
        historyPanel.showToHistoryPanel(classificationToShow);
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
        showInfo("Scegli dove posizionare la pedina");
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
            String nickShepherd, int shepherdIndex) {
        hideInfoPanel();

        if (fence) {
            streets[streetIndex].setFence();
        } else if (nickShepherd != null && !"null".equals(nickShepherd)) {
            int indexPlayer = getIndexPlayerByNickName(nickShepherd);
            streets[streetIndex].setImage("shepherd" + String.valueOf(
                    indexPlayer));
            nickShepherdToStreet.putIfAbsent(nickShepherd + "-" + shepherdIndex, streetIndex);
        }

    }

    public void refreshMoveShepherd(String nickNameMover, int shepherdIndex,
            String streetIndex, int price) {
        hideInfoPanel();

        DebugLogger.println(
                "in refresh move shepherd con " + shepherdIndex + " e " + streetIndex + " e " + nickNameMover);

        //Se vi è una strada di partenza vuol dire che l'azione è una MoveOvine
        //non una setShepherd e quindi faccio una
        //refresh della strada di partenza
        //cerco la strada dov'era il pastore e metto un recinto, lo faccio pagare
        if (nickShepherdToStreet.get(nickNameMover + "-" + shepherdIndex) != null) {
            DebugLogger.println("metto fence");
            streets[nickShepherdToStreet.get(nickNameMover + "-" + shepherdIndex)].setFence();
            nickShepherdToStreet.replace((nickNameMover + "-" + shepherdIndex),
                    Integer.parseInt(streetIndex));

            //decremento recinti
            fenceJPanel.decrease(1);
            playersJPanels[getIndexPlayerByNickName(nickNameMover)].pay(price);
        }

        //refresh della strada di arrivo
        refreshStreet(Integer.parseInt(streetIndex), false, nickNameMover, shepherdIndex);
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
        hideInfoPanel();
        if (outcome.equals("ok")) {
            regionBoxes[Integer.parseInt(region)].removeOvine(type);
        }
        String resultToShow = "Il giocatore " + killer + " ha ";
        if (outcome.equals("ok")) {
            resultToShow += "ucciso ";
        } else {
            resultToShow += "provato ad uccidere ";
        }
        showInfo(resultToShow + "un " + type);

    }

    public void refreshMoney(String money) {
        DebugLogger.println("inizio refresh money " + money);
        playersJPanels[getIndexPlayerByNickName(myNickName)].setAmount(
                Integer.parseInt(money));
        DebugLogger.println("fine refresh money ");
    }

    public void refereshCurrentPlayer(String currenPlayer) {
        hideInfoPanel();
        for (int i = 0; i < playersJPanels.length; i++) {
            if (i == getIndexPlayerByNickName(currenPlayer)) {
                playersJPanels[i].isYourShift();
            } else {
                playersJPanels[i].isNotYourShift();
            }
        }

        Component[] components = playersJPanels[getIndexPlayerByNickName(currenPlayer)].getComponents();
        for (Component comp : components) {
            if (comp instanceof DisconnectImage) {
                playersJPanels[getIndexPlayerByNickName(currenPlayer)].remove(comp);
            }
        }

        showInfo("E' il turno di " + currenPlayer);
    }

    public void refereshCard(String type, int value) {
        hideInfoPanel();
        cardsJPanels[RegionType.valueOf(type).getIndex()].setText("" + value);
    }

    public void refreshBlackSheep(String result) {
        hideInfoPanel();

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
            historyPanel.showToHistoryPanel("La pecora nera si è spostata dalla regione " + startRegion
                    + " alla regione " + endRegion);
        } else if ("nok".equalsIgnoreCase(outcome)) {
            historyPanel.showToHistoryPanel("La pecora nera non può muoversi");
        }
    }

    public void refreshWolf(String result) {
        hideInfoPanel();

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
                historyPanel.showToHistoryPanel(
                        "Il lupo si è mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando sulla strada di valore " + diceValue + " e saltando la recinzione!");
            } else {
                historyPanel.showToHistoryPanel(
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
            historyPanel.showToHistoryPanel(
                    "Il lupo non è riuscito a passare attraverso la strada di valore "
                    + diceValue + " nella regione " + startRegion);
        }
    }

    public void refreshPlayerDisconnected(String player) {
        hideInfoPanel();

        playersJPanels[getIndexPlayerByNickName(player)].setEnabled(false);
        playersJPanels[getIndexPlayerByNickName(player)].setOpacity(true);
        playersJPanels[getIndexPlayerByNickName(player)].setLayout(new BorderLayout());
        playersJPanels[getIndexPlayerByNickName(player)].add(new DisconnectImage(), BorderLayout.CENTER);
        showInfo("Il giocatore " + player + " si è disconnesso");
        dxBar.revalidate();
        dxBar.repaint();
    }

    public void specialAnimalInitialCondition(String region) {
        hideInfoPanel();

        String[] token = region.split(",");
        if ("Wolf".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].addAnimal("wolf");
            historyPanel.showToHistoryPanel("Il lupo si trova nella regione " + token[1]);
        } else if ("BlackSheep".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].addAnimal("blacksheep");
            historyPanel.showToHistoryPanel("La pecora nera si trova nella regione " + token[1]);
        }
    }

    public String chooseAction(int[] availableActions,
            String[] availableStringedActions) {

        hideInfoPanel();

        //imposto visibilità players
        playersJPanels[getIndexPlayerByNickName(myNickName)].isYourShift();
        for (int i = 0; i < playersJPanels.length; i++) {
            if (getIndexPlayerByNickName(myNickName) != i) {
                playersJPanels[i].isNotYourShift();
            }
        }

        //imposto cliccabili solo le actions
        for (int i = 0; i < availableActions.length; i++) {
            actions[availableActions[i] - 1].setEnabled(true);
            actions[availableActions[i] - 1].setOpaqueView(false);
        }

        mainJPanel.revalidate();
        mainJPanel.repaint();

        historyPanel.showToHistoryPanel("Scegli un azione");

        return getAnswerByHolder();
    }

    public void refreshMateSheepWith(String nickName, String region,
            String otherType, String newType,
            String outcome) {

        hideInfoPanel();

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

        hideInfoPanel();

        regionBoxes[Integer.parseInt(startRegion)].removeOvine(type);
        regionBoxes[Integer.parseInt(endRegion)].addAnimal(type);
        mapJPanel.revalidate();
        mapJPanel.repaint();
    }

    public String askBuyLand() {
        for (int i = 0; i < cardsJPanels.length; i++) {
            cardsJPanels[i].setEnabled(true);
        }
        historyPanel.showToHistoryPanel("Seleziona il territorio da comprare");
        String idResult = getAnswerByHolder();
        return RegionType.getRegionByIndex(Integer.parseInt(idResult));
    }

    public String askMoveOvine() {
        addAllRegionListener();
        zoomOn = true;
        historyPanel.showToHistoryPanel("Seleziona la  regione di partenza:");
        String startRegion = getAnswerByHolder();

        historyPanel.showToHistoryPanel("Seleziona quale ovino muovere:");
        String ovineType = getAnswerByHolder();

        addAllRegionListener();

        zoomOn = false;
        historyPanel.showToHistoryPanel("Seleziona la  regione di arrivo:");
        String endRegion = getAnswerByHolder();

        return startRegion + "," + endRegion + "," + ovineType;

    }

    public String askMateSheepWith() {
        setMyStreetClickable();

        historyPanel.showToHistoryPanel("Seleziona il tuo pastore");
        String idShepherd = getShepherdByStreet(getAnswerByHolder());

        addAllRegionListener();
        historyPanel.showToHistoryPanel("Seleziona la  regione dell'accoppiamento");
        String region = getAnswerByHolder();
        return idShepherd + "," + region;
    }

    public String askKillOvine() {
        setMyStreetClickable();

        historyPanel.showToHistoryPanel("Seleziona il tuo pastore");
        String streetOfMyShepherd = getAnswerByHolder();

        String killerShepherd = getShepherdByStreet(streetOfMyShepherd);
        addAllRegionListener();
        zoomOn = true;

        historyPanel.showToHistoryPanel("Seleziona la  regione in cui uccidere");
        String startRegion = getAnswerByHolder();

        historyPanel.showToHistoryPanel("Seleziona quale ovino uccidere");
        String ovineType = getAnswerByHolder();
        zoomOn = false;

        return killerShepherd + "," + startRegion + "," + ovineType;

    }

    public String askMoveShepherd() {

        setMyStreetClickable();

        historyPanel.showToHistoryPanel("Seleziona il pastore da muovere");
        String streetOfMyShepherd = getAnswerByHolder();

        String shepherdToMove = getShepherdByStreet(streetOfMyShepherd);

        DebugLogger.println("trovato pastore " + shepherdToMove);

        setFreeStreetsClickable();
        historyPanel.showToHistoryPanel("Seleziona la  strada su cui spostare il pastore");
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

    public void refreshOtherPlayerMoney(String otherPlayer, int money) {
        playersJPanels[getIndexPlayerByNickName(otherPlayer)].setAmount(money);
    }

    public static void main(String args[]) {
        //faccio gestire il thread da EDT, per il controllo ciclico della coda
        //degli eventi generati dai vari componenti

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GuiView gui = new GuiView();
            }
        });
    }

}
