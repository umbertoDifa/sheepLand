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

    private JLabel textLabel;
    Font font;
    Image image;
    private int delta;
    private int width;
    private int height;

    public BackgroundAndTextJPanel(Font font, String text) {
        textLabel = new JLabel(text);
        this.font = font;
    }

    public BackgroundAndTextJPanel() {
    }

    /**
     * imposta l img corrispondente al path come sfondo del panel e centra il
     * testo in base al centro delle coordinate xText yText
     *
     * @param imgPath
     * @param xText
     * @param yText
     */
    public void setUp(String imgPath, int xText, int yText, int width, int height) {
        this.height = height;
        this.width = width;
        //setto struttura
        this.setBackground(new Color(0, 0, 0, 0));

        this.setImage(imgPath);

        //se ha testo
        if (textLabel != null) {
            //imposto la posizione del testo
            this.setLayout(null);
            this.add(textLabel);
            Insets insets = getInsets();
            Dimension size = textLabel.getPreferredSize();
            int verticalAlign = (int) getAlignmentX();
            int horizontalAlign = (int) getAlignmentY();
            //centrandolo rispetto le coord xText e yText
            textLabel.setBounds((xText + insets.left + horizontalAlign - width / 2),
                    (yText + insets.top + verticalAlign - (font.getSize() / 2)),
                    width, font.getSize());

            //imposto font
            textLabel.setFont(font);
            textLabel.setForeground(Color.WHITE);
        }

        //applico modifiche
        repaint();

    }

    public void setUp(String imgPath, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setUp(imgPath, 0, 0, width, height);
    }

    public void setUp(Image image, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setUp(image);
    }

    public void setUp(Image image) {
        this.image = image;
        repaint();
    }

    private void setImage(String imgPath) {
        try {
            image = ImageIO.read(new File(imgPath));
            System.out.println("immagine impostata");
        } catch (IOException ex) {
            image = null;
            System.out.println("immagine non impostata");
        }
    }

    protected void setMySize(int w, int h) {
        this.width = w;
        this.height = h;
    }

    protected void addPanel(JPanel sonPanel, int x, int y) {
        //  this.setLayout(null);
        this.add(sonPanel);
        Insets insets = getInsets();
        Dimension size = sonPanel.getPreferredSize();
        sonPanel.setBounds((x + (int) sonPanel.getAlignmentY()),
                (y + (int) sonPanel.getAlignmentX()), (int) size.getWidth(), (int) size.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
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
