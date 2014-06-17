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

    /**
     * the layeredPane contains all the layers of the frame
     */
    protected JLayeredPane layeredPane;
    private InfoPanel infoPanel;
    private NickPanel nickPanel;
    private Street[] streets;

    /**
     * Array of RegionBox containing all the regions in the map
     */
    protected RegionBox[] regionBoxes;
    private HistoryPanel historyPanel;
    private JScrollPane historyScrollPane;
    private Market market;

    private String[] nickNames;
    private String myNickName;
    private int numOfPlayers;
    private int shepherds4player;
    private Map<String, Integer> NICK_SHEPHERD_TO_STREET;
    /**
     * indicate the kill state
     */
    protected boolean kill = false;

    /**
     * array of x-coordinates of all the top-left point of each streets
     */
    protected int[] xStreetPoints = {126, 252, 342, 152, 200, 248, 289, 322, 353, 406, 81, 238, 307, 389, 437, 153, 219, 256, 292, 382, 186, 329, 151, 222, 298, 382, 118, 158, 228, 263, 298, 364, 421, 188, 225, 296, 326, 371, 124, 259, 188, 296};
    /**
     * array of y-coordinates of all the top-left point of each streets
     */
    protected int[] yStreetPoints = {176, 114, 119, 223, 202, 179, 166, 195, 217, 171, 251, 232, 241, 237, 251, 281, 292, 266, 290, 286, 321, 321, 348, 343, 343, 340, 381, 413, 413, 367, 401, 406, 385, 461, 481, 474, 449, 494, 521, 503, 578, 552};
    /**
     * array of x-coordinates of all the top-left point of each regions
     */
    protected int[] xRegionBoxes = {62, 88, 168, 168, 281, 170, 257, 343, 408, 322, 399, 381, 313, 309, 227, 156, 244, 81, 236};
    /**
     * array of y-coordinates of all the top-left point of each regions
     */
    protected int[] yRegionBoxes = {131, 269, 349, 111, 73, 226, 194, 134, 185, 243, 285, 412, 349, 490, 549, 488, 410, 420, 292};
    private final Color backgroundColor = new Color(35, 161, 246);
    private final Color noneColor = new Color(0, 0, 0, 0);
    private int lastStreet;

    /**
     * boolean to indicate to zoom or not the animals in regions when clicked
     */
    protected boolean zoomOn;

    /**
     * object used to syncronize the comunication
     */
    protected static final List<String> HOLDER = new LinkedList<String>();

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
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

        //istanzio
        playersJPanels = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            playersJPanels[i] = new Player(nickNames[i], FontFactory.getFont());
            playersJPanels[i].setUp(".\\images\\giocatore" + i + ".png",
                    ".\\images\\money.png", 145, 81, 20, 40, Dim.PLAYER.getW(),
                    Dim.PLAYER.getH());
        }

        for (Player player : playersJPanels) {
            playersContainerJPanel.add(player);
        }
        for (Player player : playersJPanels) {
            player.setBackground(noneColor);
        }

        NICK_SHEPHERD_TO_STREET = new HashMap<String, Integer>();

        for (int i = 0; i < numOfPlayers; i++) {
            for (int j = 0; j < shepherds4player; j++) {
                NICK_SHEPHERD_TO_STREET.put(nickNames[i] + "-" + j, null);
            }
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.revalidate();
                frame.repaint();
            }
        });
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
        market = new Market();

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
            String shepherdIndex = String.valueOf(i);
            imgShepherds.add(ImagePool.getByName("shepherd" + shepherdIndex));
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
        actions[0].setUp(".\\images\\moveSheep.png", Dim.ACTION.getW(),
                Dim.ACTION.getH());
        actions[1].setUp(".\\images\\moveShepherd.png", Dim.ACTION.getW(),
                Dim.ACTION.getH());
        actions[2].setUp(".\\images\\buyLand.png", Dim.ACTION.getW(),
                Dim.ACTION.getH());
        actions[3].setUp(".\\images\\mateSheep.png", Dim.ACTION.getW(),
                Dim.ACTION.getH());
        actions[4].setUp(".\\images\\mateRam.png", Dim.ACTION.getW(),
                Dim.ACTION.getH());
        actions[5].setUp(".\\images\\killOvine.png", Dim.ACTION.getW(),
                Dim.ACTION.getH());

        cardsJPanels[1].setUp(".\\images\\hill2.png", Dim.TEXT_CARD.getW(),
                Dim.TEXT_CARD.getH(), Dim.CARD.getW(), Dim.CARD.getH());
        cardsJPanels[2].setUp(".\\images\\countryside2.png",
                Dim.TEXT_CARD.getW(), Dim.TEXT_CARD.getH(), Dim.CARD.getW(),
                Dim.CARD.getH());
        cardsJPanels[0].setUp(".\\images\\mountain2.png", Dim.TEXT_CARD.getW(),
                Dim.TEXT_CARD.getH(), Dim.CARD.getW(), Dim.CARD.getH());
        cardsJPanels[5].setUp(".\\images\\desert2.png", Dim.TEXT_CARD.getW(),
                Dim.TEXT_CARD.getH(), Dim.CARD.getW(), Dim.CARD.getH());
        cardsJPanels[4].setUp(".\\images\\lake2.png", Dim.TEXT_CARD.getW(),
                Dim.TEXT_CARD.getH(), Dim.CARD.getW(), Dim.CARD.getH());
        cardsJPanels[3].setUp(".\\images\\plain2.png", Dim.TEXT_CARD.getW(),
                Dim.TEXT_CARD.getH(), Dim.CARD.getW(), Dim.CARD.getH());

        fenceJPanel.setUp(".\\images\\numFences.png", Dim.TEXT_FENCE.getW(),
                Dim.TEXT_FENCE.getH(), Dim.FENCE.getW(), Dim.FENCE.getH());
    }

    /**
     * It creates the structure of all the elements of the JFrame
     */
    private void setUpFrameStructure() {
        //setto la struttura
        frame.setLayout(null);
        layeredPane.add(mainJPanel, Integer.valueOf(0));
        layeredPane.add(infoPanel, Integer.valueOf(7));
        layeredPane.add(nickPanel, Integer.valueOf(2));
        layeredPane.add(market, Integer.valueOf(4));
        layeredPane.revalidate();

        mainJPanel.setLayout(new FlowLayout());
        mainJPanel.add(cardsConteinerJPanel);
        mainJPanel.add(mapJPanel);
        mainJPanel.add(dxBar);
        dxBar.setLayout(new FlowLayout());
        dxBar.add(playersContainerJPanel);
        dxBar.add(actionsContainerJPanel);
        dxBar.add(historyScrollPane);
        addComponentsToPane(mapJPanel, fenceJPanel,
                Dim.FENCE_POSITION.getW(), Dim.FENCE_POSITION.getH());
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
        layeredPane.setPreferredSize(new Dimension(Dim.MAIN.getW(),
                Dim.MAIN.getH()));
        actionsContainerJPanel.setPreferredSize(new Dimension(
                Dim.ACTIONS_CONTAINER.getW(), Dim.ACTIONS_CONTAINER.getH()));
        //il contenitore dei player ha le dim per contenere sempre 4 player
        playersContainerJPanel.setPreferredSize(new Dimension(
                Dim.PLAYERS_CONTAINER.getW(), Dim.PLAYERS_CONTAINER.getH()));
        cardsConteinerJPanel.setPreferredSize(new Dimension(
                Dim.CARD_CONTAINER.getW(), Dim.CARD_CONTAINER.getH()));
        mainJPanel.setPreferredSize(mainJPanel.getPreferredSize());
        mainJPanel.setBounds(0, 0, Dim.MAIN.getW(), Dim.MAIN.getH());
        //la barra di destra ha le dim per contenere sempre 4 player
        dxBar.setPreferredSize(new Dimension(Dim.DX_BAR.getW(),
                Dim.DX_BAR.getH()));
        infoPanel.setBounds(Dim.INFO_PANEL_POSITION.getW(),
                Dim.INFO_PANEL_POSITION.getH(),
                Dim.INFO_PANEL.getW(), Dim.INFO_PANEL.getH());
        nickPanel.setBounds(Dim.NICK_PANEL_POSITION.getW(),
                Dim.NICK_PANEL_POSITION.getH(), Dim.NICK_PANEL.getW(),
                Dim.NICK_PANEL.getH());
        historyScrollPane.setPreferredSize(new Dimension(Dim.HISTORY.getW(),
                Dim.HISTORY.getH()));
        market.setBounds(Dim.MARKET_POSITION.getW(), Dim.MARKET_POSITION.getH(),
                Dim.MARKET.getW(), Dim.MARKET.getH());

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

        market.getButtonOk().addActionListener(this);
        market.getButtonKo().addActionListener(this);
    }

    /**
     * When the button to submit the nickname inserted is clicked it sets the
     * parameter MyNickName and adds it to holder
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nickPanel.getButton()) {
            myNickName = nickPanel.getMyNickName();
            synchronized (HOLDER) {
                HOLDER.add(myNickName);
                HOLDER.notify();
            }

        } else if (e.getSource() == market.getButtonOk()) {
            synchronized (HOLDER) {
                DebugLogger.println("bottone ok aggiunge a holde true");
                HOLDER.add("true");
                HOLDER.notify();
            }
            market.getButtonOk().setEnabled(false);
            market.getButtonKo().setEnabled(false);

        } else if (e.getSource() == market.getButtonKo()) {
            synchronized (HOLDER) {
                DebugLogger.println("bottone ko aggiunge a holde false");
                HOLDER.add("false");
                HOLDER.notify();
            }
            market.getButtonKo().setEnabled(false);
            market.getButtonOk().setEnabled(false);

        } else if (e.getSource() instanceof JButton && market.getPriceButtons().contains(
                (JButton) e.getSource())) {
            for (int i = 0; i < market.getPriceButtons().size(); i++) {
                if (e.getSource() == market.getPriceButtons().get(i)) {
                    synchronized (HOLDER) {
                        String buttone = String.valueOf(i + 1);

                        DebugLogger.println(
                                "bottone price aggiunge a holder " + buttone);
                        HOLDER.add(String.valueOf(i + 1));
                        HOLDER.notify();
                        break;
                    }
                }
                market.getPriceButtons().get(i).setEnabled(false);
            }
        }
    }

    /**
     * when a JPanel between Action, Street, CardBoard, RegionBox and Animal is
     * clicked, the method call the right method
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        synchronized (HOLDER) {
            DebugLogger.println("dentro mouse clicked");
            if (e.getSource() instanceof Action && ((Action) e.getSource()).isEnabled()) {
                actionClicked(e);
                DebugLogger.println("dentro mouse clicked di una action");

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        mainJPanel.revalidate();
                        mainJPanel.repaint();
                    }
                });

            } else if (e.getSource() instanceof Street && ((Street) e.getSource()).isEnabled()) {
                DebugLogger.println("dentro mouse clicked di una strada");
                streetClicked(e);

            } else if (e.getSource() instanceof CardBoard && ((CardBoard) e.getSource()).isEnabled()) {
                DebugLogger.println("dentro mouse clicked di una Cardboard");
                cardBoardClicked(e);

            } else if (e.getSource() instanceof RegionBox && ((RegionBox) e.getSource()).isEnabled()) {
                DebugLogger.println("dentro mouse clicked di una regione");
                regionClicked(e);

            } else if (e.getSource() instanceof Animal && ((Animal) e.getSource()).isEnabled()) {
                DebugLogger.println("dentro mouse clicked di una animal");
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
                DebugLogger.println(" azione " + i + " selezionata");
                synchronized (HOLDER) {
                    HOLDER.add(String.valueOf(i + 1));
                    HOLDER.notify();
                }
                DebugLogger.println("aggiunto a holder azione " + i);
                switch (i) {
                    case 0:
                        historyPanel.show("Scelta azione: Muovi ovino.");
                        break;
                    case 1:
                        historyPanel.show(
                                "Scelta azione: Muovi pastore.");
                        break;
                    case 2:
                        historyPanel.show(
                                "Scelta azione: Compra carta.");
                        break;
                    case 3:
                        historyPanel.show(
                                "Scelta azione: Accoppia pecore.");
                        break;
                    case 4:
                        historyPanel.show(
                                "Scelta azione: Accoppia montone e pecora.");
                        break;
                    case 5:
                        historyPanel.show(
                                "Scelta azione: Uccidi ovino.");
                        break;
                    default:
                        historyPanel.show("L'azione scelta non e' eseguibile.");
                }
            }
            actions[i].setEnabled(false);
            actions[i].setOpaqueView(true);
        }
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });
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
                DebugLogger.println("strada " + i + "selezionata ");
                synchronized (HOLDER) {
                    HOLDER.add(String.valueOf(i));
                    HOLDER.notify();
                }
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
                DebugLogger.println("carta terreno " + i + " selezionata ");
                synchronized (HOLDER) {
                    HOLDER.add(String.valueOf(i));
                    HOLDER.notify();
                }
                DebugLogger.println(
                        "aggiunto a holder carta terreno " + i);
            }
            cardsJPanels[i].setEnabled(false);

            cardsJPanels[i].revalidate();
        }

        for (int i = 0; i < market.getCards().size(); i++) {
            if (e.getSource().equals(market.getCards().get(i))) {
                DebugLogger.println("carta  terreno " + i + "selezionta");
                synchronized (HOLDER) {
                    HOLDER.add(market.getCards().get(i).getType());
                    HOLDER.notify();
                }
                DebugLogger.println(
                        "aggiunto a holder carta terreno " + market.getCards().get(
                                i).getType());
            }
            market.getCards().get(i).setEnabled(false);
        }
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });
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
                DebugLogger.println("regione " + i + "slezionata");
                synchronized (HOLDER) {
                    HOLDER.add(String.valueOf(i));
                    HOLDER.notify();
                }
                disableAllRegionListener();
                if (zoomOn) {
                    zoomAnimals(i);
                }
            }
        }
    }

    /**
     * When an Animal is clicked, the method adds to holder the corrispondindg
     * AnimalType. Then it removes all the Animal from the layer 1 and switches
     * on the Animal preview of all the regions
     *
     * @param e
     */
    protected void animalClicked(MouseEvent e) {

        //casto l'oggetto che ha generato l evento a Animal
        Animal chosenAnimal = (Animal) e.getSource();

        //per ogni oggetto nel layeredPane
        for (Component component : layeredPane.getComponents()) {
            //se e' un Animal
            if (component instanceof Animal) {
                //lo casto ad Animal
                Animal animal = (Animal) component;
                //se ha AnimalType uguale all AnimalType di chi ha generato l evento
                if (animal.getAnimalType().equals(chosenAnimal.getAnimalType())) {
                    DebugLogger.println("dentro la catch di animalclicked");
                    //aggiungo ad holder il corrispondente tipo
                    synchronized (HOLDER) {
                        HOLDER.add(animal.getAnimalType());
                        HOLDER.notify();
                    }
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
            //in modalitÃ  preview
            region.setAnimalPreview(true);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        //not used
    }

    /**
     * {@inheritDoc }
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
        DebugLogger.println(
                "mouse released in : " + e.getLocationOnScreen().x + " " + e.getLocationOnScreen().y);
    }

    /**
     * {@inheritDoc }
     *
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
        //not used
    }

    /**
     * {@inheritDoc }
     *
     * @param e
     */
    public void mouseExited(MouseEvent e) {
        //not used
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
        panelFather.revalidate();
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
        ImagePool.add(".\\images\\cup.png", "cup");
        ImagePool.add(".\\images\\lose.png", "lose");
        ImagePool.add(".\\images\\market.png", "market");

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
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            imageBg = null;
            DebugLogger.println("immagine infoPanel non caricata");
        }
        infoPanel = new InfoPanel(FontFactory.getFont(), listIcon, imageBg, 232,
                444);
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
    protected void addAllRegionListener() {
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
     *
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

        synchronized (HOLDER) {
            // wait for answer
            HOLDER.clear();
            while (HOLDER.isEmpty()) {
                DebugLogger.println("nel while");

                try {
                    HOLDER.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
                }
            }
            //la risposta che accetto e' la prima
            result = HOLDER.remove(0);
            HOLDER.clear();
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
    protected void zoomAnimals(int i) {
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
            layeredPane.add(animalToHighlight, Integer.valueOf(1));
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
     *
     * @return
     */
    private String getShepherdByStreet(String streetOfMyShepherd) {
        for (Map.Entry pairs : NICK_SHEPHERD_TO_STREET.entrySet()) {
            String key = (String) pairs.getKey();
            if (NICK_SHEPHERD_TO_STREET.get(key) != null && Integer.toString(
                    NICK_SHEPHERD_TO_STREET.get(key)).equals(
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
    /**
     * {@inheritDoc }
     */
    public void showWelcome() {
        historyPanel.show("Benvenuto. Il gioco sta per iniziare!");

    }

    /**
     * {@inheritDoc }
     */
    public void showEndGame() {
        historyPanel.show("Il gioco e' terminato. Arrivederci.");
    }

    /**
     * {@inheritDoc }
     *
     * @param info
     */
    public void showInfo(String info) {
        historyPanel.show(info);
        DebugLogger.println("msg in info panel " + info);

        infoPanel.addMouseListener(infoPanel);
        infoPanel.hideDice();
        infoPanel.setText(info);
        infoPanel.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });
    }

    /**
     * {@inheritDoc }
     *
     * @param boughLand
     * @param price
     */
    public void showBoughtLand(String boughLand, String price) {

        cardsJPanels[RegionType.valueOf(boughLand.toUpperCase()).getIndex()].increase(
                1);
        String bankNum = cardsJPanels[RegionType.valueOf(boughLand.toUpperCase()).getIndex()].bankNum.getText();
        cardsJPanels[RegionType.valueOf(boughLand.toUpperCase()).getIndex()].bankNum.setText(
                String.valueOf(Integer.parseInt(bankNum) - 1));
        playersJPanels[getIndexPlayerByNickName(myNickName)].pay(
                Integer.parseInt(price));
        historyPanel.show(
                "Hai acquistato la carta " + boughLand + " per " + price + " danari.");
    }

    /**
     * {@inheritDoc }
     *
     * @param shepherdIndex
     * @param streetIndex
     */
    public void showSetShepherd(int shepherdIndex, String streetIndex) {
        hideInfoPanel();
        refreshStreet(Integer.parseInt(streetIndex), false, myNickName,
                shepherdIndex);
        myPutIfAbsent(NICK_SHEPHERD_TO_STREET, myNickName + "-" + shepherdIndex,
                Integer.valueOf(streetIndex));

    }

    /**
     * {@inheritDoc }
     *
     * @param nickNames
     * @param wallets
     * @param shepherd4player
     */
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

    /**
     * {@inheritDoc }
     *
     * @param idShepherd
     * @param priceToMove
     */
    public void showMoveShepherd(String idShepherd, String priceToMove) {
        hideInfoPanel();

        DebugLogger.println(
                "in show move shepherd con " + idShepherd + " e " + priceToMove);
        //faccio pagare il player
        playersJPanels[getIndexPlayerByNickName(myNickName)].pay(
                Integer.parseInt(priceToMove));
        //metto il recinto nella strada dove si trovava
        streets[NICK_SHEPHERD_TO_STREET.get(myNickName + "-" + idShepherd)].setFence();
        //aggiorno l img della strada d arrivo
        streets[lastStreet].setImage("shepherd" + getIndexPlayerByNickName(
                myNickName));
        //aggiorno la hashmap

        myReplace(NICK_SHEPHERD_TO_STREET, myNickName + "-" + idShepherd,
                lastStreet);

        //decremento recinti
        fenceJPanel.decrease(1);

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });
    }

    /**
     * {@inheritDoc }
     *
     * @param startRegion
     * @param endRegion
     * @param type
     */
    public void showMoveOvine(String startRegion, String endRegion, String type) {
        hideInfoPanel();

        regionBoxes[Integer.parseInt(startRegion)].removeOvine(type);
        regionBoxes[Integer.parseInt(endRegion)].addAnimal(type);

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mapJPanel.revalidate();
                mapJPanel.repaint();
            }
        });
    }

    /**
     * {@inheritDoc }
     *
     * @param region
     * @param otherType
     * @param newType
     */
    public void showMateSheepWith(String region, String otherType,
            String newType) {
        hideInfoPanel();

        regionBoxes[Integer.parseInt(region)].addAnimal(newType);
    }

    /**
     * {@inheritDoc }
     *
     * @param winner
     * @param rank
     */
    public void showMyRank(Boolean winner, String rank) {
        hideInfoPanel();

        String result = "Hai ";
        if (winner) {
            result += "vinto ";
        } else {
            result += "perso ";
        }
        result += "con " + rank + " punti";

        layeredPane.add(new RankPanel(result), Integer.valueOf(6));
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

    }

    /**
     * {@inheritDoc }
     *
     * @param classification
     */
    public void showClassification(String classification) {

        String classificationToShow = "Classifica: \n";
        String[] token = classification.split(",");
        int i = 0;
        while (i < token.length - 1) {
            classificationToShow += "Giocatore " + token[i] + " punteggio: " + token[i + 1] + "\n";
            i += 2;
        }
        historyPanel.show(classificationToShow);
    }

    /**
     * {@inheritDoc }
     */
    public void showUnexpectedEndOfGame() {
        hideInfoPanel();

        showInfo("Il gioco e' terminato per mancanza di giocatori, si scusiamo.");
    }

    /**
     * {@inheritDoc }
     *
     * @param region
     * @param type
     * @param shepherdPayed
     */
    public void showKillOvine(String region, String type, String shepherdPayed) {
        hideInfoPanel();

        regionBoxes[Integer.parseInt(region)].removeOvine(type);
        playersJPanels[getIndexPlayerByNickName(myNickName)].pay(
                GameConstants.PRICE_OF_SILENCE.getValue() * Integer.parseInt(
                        shepherdPayed));
        historyPanel.show("Hai ucciso un " + type + " nella  regione " + region
                + " pagando " + shepherdPayed + " pastori per il silenzio");
    }

    /**
     * {@inheritDoc }
     *
     * @param idShepherd
     *
     * @return
     */
    public String setUpShepherd(int idShepherd) {

        //imposto visibilità players
        setMyShiftView();

        setFreeStreetsClickable();
        showInfo("Scegli dove posizionare la pedina");
        return getAnswerByHolder();
    }

    /**
     * {@inheritDoc }
     *
     * @param regionIndex
     * @param numbOfSheep
     * @param numbOfRam
     * @param numbOfLamb
     */
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

    /**
     * {@inheritDoc }
     *
     * @param streetIndex
     * @param fence
     * @param nickShepherd
     * @param shepherdIndex
     */
    public void refreshStreet(int streetIndex, boolean fence,
            String nickShepherd, int shepherdIndex) {
        hideInfoPanel();

        if (fence) {
            streets[streetIndex].setFence();
        } else if (nickShepherd != null && !"null".equals(nickShepherd)) {
            int indexPlayer = getIndexPlayerByNickName(nickShepherd);
            String playerIndexStringed = String.valueOf(
                    indexPlayer);
            streets[streetIndex].setImage("shepherd" + playerIndexStringed);

            myPutIfAbsent(NICK_SHEPHERD_TO_STREET,
                    nickShepherd + "-" + shepherdIndex,
                    streetIndex);

        }

    }

    /**
     * {@inheritDoc }
     *
     * @param nickNameMover
     * @param shepherdIndex
     * @param streetIndex
     * @param price
     */
    public void refreshMoveShepherd(String nickNameMover, int shepherdIndex,
            String streetIndex, int price) {
        hideInfoPanel();

        DebugLogger.println(
                "in refresh move shepherd con " + shepherdIndex + " e " + streetIndex + " e " + nickNameMover);

        //Se vi e' una strada di partenza vuol dire che l'azione e' una MoveOvine
        //non una setShepherd e quindi faccio una
        //refresh della strada di partenza
        //cerco la strada dov'era il pastore e metto un recinto, lo faccio pagare
        if (NICK_SHEPHERD_TO_STREET.get(nickNameMover + "-" + shepherdIndex) != null) {
            DebugLogger.println("metto fence");
            streets[NICK_SHEPHERD_TO_STREET.get(
                    nickNameMover + "-" + shepherdIndex)].setFence();

            myReplace(NICK_SHEPHERD_TO_STREET,
                    nickNameMover + "-" + shepherdIndex, Integer.parseInt(
                            streetIndex));

            //decremento recinti
            fenceJPanel.decrease(1);
            playersJPanels[getIndexPlayerByNickName(nickNameMover)].pay(price);
        }

        //refresh della strada di arrivo
        refreshStreet(Integer.parseInt(streetIndex), false, nickNameMover,
                shepherdIndex);
        DebugLogger.println("posiziono " + nickNameMover + " in " + streetIndex
        );

        myPutIfAbsent(NICK_SHEPHERD_TO_STREET,
                nickNameMover + "-" + shepherdIndex, Integer.parseInt(
                        streetIndex));

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });
    }

    /**
     * {@inheritDoc }
     *
     * @param buyer
     * @param land
     * @param price
     */
    public void refreshBuyLand(String buyer, String land, int price) {
        hideInfoPanel();

        playersJPanels[getIndexPlayerByNickName(buyer)].pay(price);
        String bankNum = cardsJPanels[RegionType.valueOf(land.toUpperCase()).getIndex()].bankNum.getText();
        cardsJPanels[RegionType.valueOf(land.toUpperCase()).getIndex()].bankNum.setText(
                String.valueOf(Integer.parseInt(bankNum) - 1));
        showInfo("Il  giocatore " + buyer + " ha acquistato un territorio "
                + land + " per " + price + " danari");
    }

    /**
     * {@inheritDoc }
     *
     * @param killer
     * @param region
     * @param type
     * @param outcome
     */
    public void refreshKillOvine(String killer, String region, String type,
            String outcome) {
        hideInfoPanel();
        if ("ok".equals(outcome)) {
            regionBoxes[Integer.parseInt(region)].removeOvine(type);
        }
        String resultToShow = " Il giocatore " + killer + " ha ";
        if ("ok".equals(outcome)) {
            resultToShow += "ucciso ";
        } else {
            resultToShow += "provato ad uccidere ";
        }
        showInfo(resultToShow + "un " + type);

    }

    /**
     * {@inheritDoc }
     *
     * @param money
     */
    public void refreshMoney(String money) {
        DebugLogger.println("inizio refresh money " + money);
        playersJPanels[getIndexPlayerByNickName(myNickName)].setAmount(
                Integer.parseInt(money));
        DebugLogger.println("fine refresh money ");
    }

    /**
     * {@inheritDoc }
     *
     * @param currenPlayer
     */
    public void refereshCurrentPlayer(String currenPlayer) {
        hideInfoPanel();
        for (int i = 0; i < playersJPanels.length; i++) {
            if (i == getIndexPlayerByNickName(currenPlayer)) {
                playersJPanels[i].isYourShift();
            } else {
                playersJPanels[i].isNotYourShift();
            }
        }

        Component[] components = playersJPanels[getIndexPlayerByNickName(
                currenPlayer)].getComponents();
        for (Component comp : components) {
            if (comp instanceof DisconnectImage) {
                playersJPanels[getIndexPlayerByNickName(currenPlayer)].remove(
                        comp);
            }
        }

        historyPanel.show("E' il turno di " + currenPlayer);
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

    }

    /**
     * {@inheritDoc }
     *
     * @param type
     * @param value
     */
    public void refereshCard(String type, int value) {
        hideInfoPanel();
        if (value != -2) {
            cardsJPanels[RegionType.valueOf(type).getIndex()].increase(1);
        } else {
            removeAllCards();
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param result
     */
    public void refreshBlackSheep(String result) {
        hideInfoPanel();

        //splitta il risultato e raccogli l'outcome
        String[] token = result.split(",");
        String outcome = token[0];

        String startRegion = token[2];
        if ("ok".equalsIgnoreCase(outcome)) {
            String endRegion = token[3];
            regionBoxes[Integer.parseInt(startRegion)].removeSpecialAnimal(
                    "blacksheep");
            regionBoxes[Integer.parseInt(endRegion)].addAnimal("blacksheep");
            historyPanel.show(
                    "La pecora nera si e' spostata dalla regione " + startRegion
                    + " alla regione " + endRegion);
        } else if ("nok".equalsIgnoreCase(outcome)) {
            historyPanel.show("La pecora nera non puo' muoversi");
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param result
     */
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
                historyPanel.show(
                        "Il lupo si e'  mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando sulla strada di valore " + diceValue + " e saltando la recinzione!");
            } else {
                historyPanel.show(
                        "Il lupo  si e' mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando per la strada di valore " + diceValue);
            }
            if (!"nok".equalsIgnoreCase(ovine)) {
                regionBoxes[Integer.parseInt(endRegion)].removeOvine(ovine);
                historyPanel.show("Il lupo ha mangiato una " + ovine);
            }
        } else {
            String diceValue = token[1];
            String startRegion = token[2];
            historyPanel.show(
                    "Il lupo non e' riuscito a passare attraverso la strada di valore "
                    + diceValue + " nella regione " + startRegion);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param player
     */
    public void refreshPlayerDisconnected(String player) {
        hideInfoPanel();

        playersJPanels[getIndexPlayerByNickName(player)].setEnabled(false);
        playersJPanels[getIndexPlayerByNickName(player)].setOpacity(true);

        boolean hasImage = false;
        Component[] comps = playersJPanels[getIndexPlayerByNickName(player)].getComponents();
        for (Component comp : comps) {
            if (comp instanceof DisconnectImage) {
                hasImage = true;
            }
        }

        if (!hasImage) {
            playersJPanels[getIndexPlayerByNickName(player)].setLayout(
                    new BorderLayout());
            playersJPanels[getIndexPlayerByNickName(player)].add(
                    new DisconnectImage(), BorderLayout.CENTER);
        }

        historyPanel.show(" Il  giocatore " + player + " si è disconnesso");

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

    }

    /**
     * {@inheritDoc }
     *
     * @param region
     */
    public void specialAnimalInitialCondition(String region) {
        hideInfoPanel();

        String[] token = region.split(",");
        if ("Wolf".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].addAnimal("wolf");
            historyPanel.show("Il lupo si trova nella regione " + token[1]);
        } else if ("BlackSheep".equals(token[0])) {
            regionBoxes[Integer.parseInt(token[1])].addAnimal("blacksheep");
            historyPanel.show(
                    "La pecora nera si trova nella regione " + token[1]);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param availableActions
     * @param availableStringedActions
     *
     * @return
     */
    public String chooseAction(int[] availableActions,
            String[] availableStringedActions) {

        hideInfoPanel();

        setMyShiftView();

        //imposto cliccabili solo le actions
        for (int i = 0; i < availableActions.length; i++) {
            actions[availableActions[i] - 1].setEnabled(true);
            actions[availableActions[i] - 1].setOpaqueView(false);
        }

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

        historyPanel.show("Scegli un azione");
        return getAnswerByHolder();
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param region
     * @param otherType
     * @param newType
     * @param outcome
     */
    public void refreshMateSheepWith(String nickName, String region,
            String otherType, String newType,
            String outcome) {

        hideInfoPanel();

        if ("ok".equals(outcome)) {
            regionBoxes[Integer.parseInt(region)].addAnimal(newType);
            showInfo(
                    "Il giocatre " + nickName + " ha accoppiato una pecora con un "
                    + otherType + " nella regione " + region + " ed e' nato un " + newType + "!");

        } else {
            showInfo(
                    "Il gioctore " + nickName + " ha tentato di accoppiare una pecora con un " + otherType + " ma ha fallito!");
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param type
     * @param startRegion
     * @param endRegion
     */
    public void refreshMoveOvine(String nickName, String type,
            String startRegion, String endRegion) {

        hideInfoPanel();

        regionBoxes[Integer.parseInt(startRegion)].removeOvine(type);
        regionBoxes[Integer.parseInt(endRegion)].addAnimal(type);
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askBuyLand() {
        for (Card cardsJPanel : cardsJPanels) {
            cardsJPanel.setEnabled(true);
        }
        historyPanel.show(
                "Seleziona il territorio da comprare dalla barra laterale sinistra");
        String idResult = getAnswerByHolder();
        return RegionType.getRegionByIndex(Integer.parseInt(idResult));
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askMoveOvine() {
        addAllRegionListener();
        zoomOn = true;
        historyPanel.show("Seleziona la  regione di partenza:");
        String startRegion = getAnswerByHolder();

        historyPanel.show("Seleziona quale ovino muovere:");
        String ovineType = getAnswerByHolder();

        addAllRegionListener();

        zoomOn = false;
        historyPanel.show("Seleziona la  regione di arrivo:");
        String endRegion = getAnswerByHolder();

        return startRegion + "," + endRegion + "," + ovineType;

    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askMateSheepWith() {
        setMyStreetClickable();

        historyPanel.show("Seleziona il tuo  pastore");
        String idShepherd = getShepherdByStreet(getAnswerByHolder());

        addAllRegionListener();
        historyPanel.show("Seleziona la  regione dell'accoppiamento");
        String region = getAnswerByHolder();
        return idShepherd + "," + region;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askKillOvine() {
        setMyStreetClickable();
        kill = true;

        historyPanel.show("Seleziona il tuo pastore");
        String streetOfMyShepherd = getAnswerByHolder();

        String killerShepherd = getShepherdByStreet(streetOfMyShepherd);
        addAllRegionListener();
        zoomOn = true;

        historyPanel.show("Seleziona la  regione in cui uccidere");
        String startRegion = getAnswerByHolder();

        historyPanel.show("Seleziona quale ovino uccidere");
        String ovineType = getAnswerByHolder();
        zoomOn = false;

        return killerShepherd + "," + startRegion + "," + ovineType;

    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askMoveShepherd() {

        setMyStreetClickable();

        historyPanel.show("Seleziona il pastore da muovere");
        String streetOfMyShepherd = getAnswerByHolder();

        String shepherdToMove = getShepherdByStreet(streetOfMyShepherd);

        DebugLogger.println("trovato pastore " + shepherdToMove);

        setFreeStreetsClickable();
        historyPanel.show("Seleziona la  strada su cui spostare il pastore");
        String endStreet = getAnswerByHolder();

        lastStreet = Integer.parseInt(endStreet);

        return shepherdToMove + "," + endStreet;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askNickName() {
        DebugLogger.println("nella nickname");
        nickPanel.setVisible(true);
        String r = getAnswerByHolder();
        DebugLogger.println(r);
        return r;
    }

    /**
     * {@inheritDoc }
     *
     * @param fences
     */
    public void refreshFences(int fences) {
        fenceJPanel.setText(String.valueOf(fences));
        if (fences < 0) {
            fenceJPanel.setUp(".\\images\\numFencesRed.png",
                    Dim.TEXT_FENCE.getW(),
                    Dim.TEXT_FENCE.getH(), Dim.FENCE.getW(), Dim.FENCE.getH());
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param otherPlayer
     * @param money
     */
    public void refreshOtherPlayerMoney(String otherPlayer, int money) {
        playersJPanels[getIndexPlayerByNickName(otherPlayer)].setAmount(money);
    }

    /**
     * {@inheritDoc }
     *
     * @param regionType
     * @param availableCards
     */
    public void refreshBankCard(String regionType, int availableCards) {
        cardsJPanels[RegionType.valueOf(regionType.toUpperCase()).getIndex()].bankNum.setText(
                String.valueOf(availableCards));
    }

    /**
     * {@inheritDoc }
     *
     * @param action
     * @return
     */
    public boolean askWillingTo(String action) {
        setMyShiftView();

        DebugLogger.println("ASK WILLING TO " + action);
        market.askWillingToView(action);

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

        String answer = getAnswerByHolder();
        Boolean boolAnswer = true;
        if ("true".equals(answer)) {
            boolAnswer = true;
        } else if ("false".equals(answer)) {
            boolAnswer = false;
        } else {
            DebugLogger.println(
                    "askWilling catcha risposta sbagliata!!!!:" + answer);
        }
        if (!boolAnswer) {
            market.setVisible(false);
        }
        return boolAnswer;
    }

    /**
     * {@inheritDoc }
     *
     * @param availableCards
     * @return
     */
    public String askSellCard(String[] availableCards) {
        market.askWhatSellView(availableCards);

        List<Card> cards = market.getCards();
        for (Card card : cards) {
            card.addMouseListener(this);
        }

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

        String cardToSell = getAnswerByHolder();
        market.setVisible(false);
        return cardToSell;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public int askPriceCard() {
        market.askPriceView();
        List<JButton> buttons = market.getPriceButtons();
        for (JButton button : buttons) {
            button.addActionListener(this);
        }

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

        int price = Integer.parseInt(getAnswerByHolder());
        market.setVisible(false);
        return price;

    }

    /**
     * {@inheritDoc }
     *
     * @param availableCards
     * @param prices
     * @return
     */
    public String askBuyMarketCard(String[] availableCards, int[] prices) {

        market.askBuyView(availableCards, prices);

        List<Card> cards = market.getCards();
        for (Card card : cards) {
            card.addMouseListener(this);
        }

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });

        String answer = getAnswerByHolder();
        market.setVisible(false);

        return answer;
    }

    private void removeAllCards() {
        for (Card card : cardsJPanels) {
            card.setText("0");
        }
    }

    private void setMyShiftView() {
        //imposto visibilitÃ  players
        playersJPanels[getIndexPlayerByNickName(myNickName)].isYourShift();
        for (int i = 0; i < playersJPanels.length; i++) {
            if (getIndexPlayerByNickName(myNickName) != i) {
                playersJPanels[i].isNotYourShift();
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * run
             */
            public void run() {
                mainJPanel.revalidate();
                mainJPanel.repaint();
            }
        });
    }

    private void myPutIfAbsent(Map<String, Integer> mappa, String key,
            Integer value) {
        if (mappa.get(key) == null) {
            // è null perchè non esiste o è stato volontariamente mappato a null
            mappa.put(key, value);
        }
    }

    private void myReplace(Map<String, Integer> mappa, String key,
            Integer value) {
        mappa.remove(key);
        mappa.put(key, value);

    }

}
