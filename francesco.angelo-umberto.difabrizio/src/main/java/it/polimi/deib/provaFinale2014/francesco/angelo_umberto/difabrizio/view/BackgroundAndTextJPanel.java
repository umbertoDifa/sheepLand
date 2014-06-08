package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Francesco
 */
public abstract class BackgroundAndTextJPanel extends JPanel {

    private JLabel textLabel = new JLabel();
    protected Font font;
    private Image image;
    private int delta;
    private int width;
    private int height;
    private int xImage = 0;
    private int yImage = 0;

    protected BackgroundAndTextJPanel(Font font, String text) {
        textLabel.setText(text);
        this.font = font;
    }

    protected BackgroundAndTextJPanel() {
    }

    protected void setDimension(int width, int height){
        this.width = width;
        this.height = height;
    }
    /**
     * set the panel's background image corrisponding to the path center the
     * Label with xText, yText
     *
     * @param imgPath
     * @param xText
     * @param yText
     */
    protected void setUp(String imgPath, int xText, int yText, int width, int height) {
        //salvo le dimensioni
        setDimension(width, height);

        //setto immagine di sfondo e colore di sfondo
        this.setImage(imgPath);
        this.setBackground(new Color(0, 0, 0, 0));

        //se ha testo
        if (textLabel.getText() != "") {
            //aggiungo il label e setto la posizione del testo rispetto al panel
            this.setLayout(null);
            this.add(textLabel);
            Insets insets = getInsets();
            Dimension size = textLabel.getPreferredSize();
            int verticalAlign = (int) getAlignmentX();
            int horizontalAlign = (int) getAlignmentY();
            //centrandolo rispetto le coord xText e yText
            textLabel.setBounds((xText + insets.left + horizontalAlign - width / 2),
                    (yText + insets.top + verticalAlign - (28 / 2)),
                    width, 28);

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
        setDimension(image.getWidth(this), image.getHeight(this));
        this.image = image;
        repaint();
    }
    
    protected void removeImg(){
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
                System.out.println("immagine impostata");
            } else {
                image = null;
            }
        } catch (IOException ex) {
            image = null;
            System.out.println("immagine non impostata" + imgPath);
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

    protected Image getImage(){
        return this.image;
    }
    
    /**
     * add a Panel, positioning it with x,y referred to this
     *
     * @param sonPanel
     * @param x
     * @param y
     */
    protected void addPanel(JPanel sonPanel, int x, int y) {
        //  this.setLayout(null);
        this.add(sonPanel);
        Insets insets = getInsets();
        Dimension size = sonPanel.getPreferredSize();
        sonPanel.setBounds((x + (int) sonPanel.getAlignmentY() + insets.left),
                (y + (int) sonPanel.getAlignmentX() + insets.top), (int) size.getWidth(), (int) size.getHeight());
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
        //  super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, xImage, yImage, this);
        }
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(width, height); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(width, height); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getSize() {
        return new Dimension(width, height); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Insets getInsets() {
        return super.getInsets(); //To change body of generated methods, choose Tools | Templates.
    }
}
