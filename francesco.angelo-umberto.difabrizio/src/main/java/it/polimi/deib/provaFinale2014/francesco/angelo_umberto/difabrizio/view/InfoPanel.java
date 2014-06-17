package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * The class extends JPanel adding the possibility to draw a background image
 * from a list of images and a personalized hide method
 *
 * @author Francesco
 */
public class InfoPanel extends JPanel implements MouseListener {

    private final JTextArea textArea;
    private List<Icon> imagesDice = new ArrayList<Icon>();
    private final JLabel dice;
    private final int width;
    private final int height;
    private final Image imageBackground;

    /**
     * create the InfoPanel, setting font, width, height, set imageBackground,
     * save the list of possible background images.
     *
     * @param font
     * @param imagesDice
     * @param imageBackground
     * @param width
     * @param height
     */
    public InfoPanel(Font font, List<Icon> imagesDice, Image imageBackground, int width, int height) {
        this.setPreferredSize(new Dimension(Dim.INFO_PANEL.getW(), Dim.INFO_PANEL.getH()));
        this.setBackground(new Color(0, 0, 0, 0));
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 0, 0, 0));
        Font f = font.deriveFont(new Float(22.0));
        textArea.setFont(f);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setPreferredSize(new Dimension(140, 230));
        textArea.addMouseListener(this);
        this.imagesDice = imagesDice;
        dice = new JLabel();
        dice.setVisible(false);
        this.setLayout(null);
        this.width = width;
        this.height = height;
        this.imageBackground = imageBackground;
        this.setToolTipText("click me to close me!");
        GuiView.addComponentsToPane(this, textArea, 50, 140);
        GuiView.addComponentsToPane(this, dice, 77, 300);
        this.setVisible(false);
        this.textArea.setToolTipText("click me to close me!");
        this.setToolTipText("click me to close me!");
        repaint();
    }

    /**
     * set the Label's text
     *
     * @param text
     */
    public void setText(String text) {
        textArea.setText(text);
    }

    /**
     * set the dice's image corrisponding to value "result"
     *
     * @param result
     */
    public void setDice(int result) {
        dice.setVisible(true);
        dice.setIcon(imagesDice.get(result - 1));
    }

    /**
     * hide the dice image
     */
    public void hideDice() {
        dice.setVisible(false);
    }

    /**
     * override the paintComponent to set the background image of the panel
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        if (imageBackground != null) {
            g.drawImage(imageBackground, 0, 0, new Color(0, 0, 0, 0), this);
        }
        super.paintComponent(g);
    }

    /**
     * return the jpanel dimensione
     *
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    /**
     * return the jpanel dimensione
     *
     * @return
     */
    @Override
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * hide the jpanel if clicked
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        this.setVisible(false);
        DebugLogger.println("dentro mouseClicked di infoPanel");
        this.removeMouseListener(this);
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
        //not used
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

}
