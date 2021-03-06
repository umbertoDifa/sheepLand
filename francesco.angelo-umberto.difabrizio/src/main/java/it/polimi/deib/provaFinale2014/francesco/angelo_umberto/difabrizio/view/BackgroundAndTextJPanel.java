package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class extends JPanel, adding the possibility to set a background image,
 * set a JLabel and to position the JLabel inside the panel.
 * @author Francesco
 */
public abstract class BackgroundAndTextJPanel extends JPanel {

    private JLabel textLabel = new JLabel();
    protected Font font;
    private Image image;
    private int width;
    private int height;
    private boolean opacity = false;

    /**
     * create the panel, set the jlabel's text with the indicated
     * text and font
     * @param font
     * @param text 
     */
    protected BackgroundAndTextJPanel(Font font, String text) {
        textLabel.setText(text);
        this.font = font;
    }

    /**
     * create the jpanel
     */
    protected BackgroundAndTextJPanel() {
    }

    /**
     * set the dimension of the jpanel with the indicated dimension
     * @param width
     * @param height 
     */
    protected void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * set the panel's background image corrisponding to the path, centering the
     * Label with xText, yText
     *
     * @param imgPath
     * @param xText
     * @param yText
     * @param width
     * @param height
     */
    protected void setUp(String imgPath, int xText, int yText, int width,
                         int height) {
        //salvo le dimensioni
        setDimension(width, height);

        //setto immagine di sfondo e colore di sfondo
        this.setImage(imgPath);
        this.setBackground(new Color(0, 0, 0, 0));

        //se ha testo
        if (!"".equals(textLabel.getText())) {
            //aggiungo il label e setto la posizione del testo rispetto al panel
            this.setLayout(null);
            this.add(textLabel);
            Insets insets = getInsets();
            int verticalAlign = (int) getAlignmentX();
            int horizontalAlign = (int) getAlignmentY();
            //centrandolo rispetto le coord xText e yText
            textLabel.setBounds(
                    xText + insets.left + horizontalAlign - width / 2,
                    yText + insets.top + verticalAlign - (28 / 2),
                    width / 2, 28);

            //imposto font e colore font
            textLabel.setFont(font);
            textLabel.setForeground(Color.WHITE);
        }

        //applico modifiche
        repaint();

    }

    /**
     * set the background image corrisponding to the path, and set the dimension
     * of the panel
     *
     * @param imgPath
     * @param width
     * @param height
     */
    protected void setUp(String imgPath, int width, int height) {
        setDimension(width, height);
        setPreferredSize(new Dimension(width, height));
        setUp(imgPath, 0, 0, width, height);

    }

    /**
     * set the only background image
     *
     * @param image
     */
    protected void setUp(Image image) {
        if (image != null) {
            setDimension(image.getWidth(this), image.getHeight(this));
        }
        this.image = image;
        repaint();
    }

    /**
     * remove the background imahe
     */
    protected void removeImg() {
        this.image = null;
    }

    /**
     * set only the background image corrispondig to the path
     *
     * @param imgPath
     */
    private void setImage(String imgPath) {
        try {
            if (imgPath != null) {
                image = ImageIO.read(new File(imgPath));
                DebugLogger.println("immagine impostata");
            } else {
                image = null;
            }
        } catch (IOException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            image = null;
            DebugLogger.println("immagine non impostata" + imgPath);
        }
    }

    /**
     * set the Label's text
     *
     * @param text
     */
    protected void setText(String text) {
        textLabel.setText(text);
    }

    /**
     * get the jlabel's text
     * @return 
     */
    protected String getText() {
        return textLabel.getText();
    }

    /**
     * get the background image
     * @return 
     */
    protected Image getImage() {
        return this.image;
    }

    /**
     * add a Panel, positioning it with x,y referred to "this"
     *
     * @param sonPanel
     * @param x
     * @param y
     */
    protected void addPanel(JPanel sonPanel, int x, int y) {
        this.add(sonPanel);
        Insets insets = getInsets();
        Dimension size = sonPanel.getPreferredSize();
        sonPanel.setBounds(x + (int) sonPanel.getAlignmentY() + insets.left,
                y + (int) sonPanel.getAlignmentX() + insets.top,
                (int) size.getWidth(), (int) size.getHeight());
    }

    /**
     * set the jpanel opaque iff opacity parameter is true
     * @param opacity
     */
    protected void setOpacity(boolean opacity) {
        this.opacity = opacity;
    }

    /**
     * override paintComponent to set the background image if it exists,
     * otherwise clear the background image
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        Graphics2D g2D = (Graphics2D) g;
        if (image != null) {
            if (opacity) {
                AlphaComposite ac = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) 0.6);
                g2D.setComposite(ac);
            }
            g2D.drawImage(image, 0, 0, this);
        }
        super.paintComponent(g2D);
    }

    /**
     * get the jpanel's dimension
     * @return 
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    /**
     *  get the jpanel's dimension
     * @return 
     */
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(width, height);
    }

    /**
     *  get the jpanel's dimension
     * @return 
     */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(width, height);
    }

    /**
     *  get the jpanel's dimension
     * @return 
     */
    @Override
    public Dimension getSize() {
        return new Dimension(width, height); 
    }    
}
