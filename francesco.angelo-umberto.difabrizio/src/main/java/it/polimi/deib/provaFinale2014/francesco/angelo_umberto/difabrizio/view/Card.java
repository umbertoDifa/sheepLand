package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Francesco
 */
public class Card extends JPanel {

//    private JLabel imageLabel;
    private Image image;
    private JLabel textLabel;
    private Font font;

    public Card(Font font, String text) {
        setBounds(0, 0, 105, 104);
        textLabel = new JLabel("0");
        this.font = font;
    }

    public Card(Font font) {
//        imageLabel = new JLabel();
        textLabel = new JLabel("0");
        this.font = font;
    }

    public Card() {
//        imageLabel = new JLabel();
        textLabel = new JLabel("0");
    }

    /**
     * imposta background trasparente, aggiunge nel panel un label per lo
     * sfondo. setta lo sfondo con l'image e dentro lo sfondo un label di testo.
     * il testo lo formatto e lo posiziono secondo le distanze x, y dall angolo
     * in alto a sx
     *
     * @param image
     * @param xText
     * @param yText
     */
    public void setUpCard(String imgPath, int xText, int yText) {
        //setto struttura
        this.setBackground(new Color(0, 0, 0, 0));
        try {
            image = ImageIO.read(new File(imgPath));
            System.out.println("immagine carta azione impostata");
        } catch (IOException ex) {
            image = null;
            System.out.println("immagine carta azione non impostata");
        }
        repaint();
        this.setLayout(null);
        this.add(textLabel);
        Insets insets = getInsets();
        Dimension size = textLabel.getPreferredSize();
        textLabel.setBounds(xText + insets.left, yText + insets.top,
                28, 28);
        textLabel.setVisible(true);

        //imposto sfondo
        repaint();

        //imposto font
        textLabel.setFont(font);
        textLabel.setForeground(Color.WHITE);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        //return super.getPreferredSize(); //To change body of generated methods, choose Tools | Templates.
        //return new Dimension(this.getWidth(), this.getHeight());
        return new Dimension(105, 104);
    }

    @Override
    public Dimension getSize() {
        return super.getSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getMinimumSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getMaximumSize() {
        return super.getMaximumSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Insets getInsets() {
        return super.getInsets(); //To change body of generated methods, choose Tools | Templates.
    }
}
