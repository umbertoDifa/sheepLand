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
class Player extends JPanel {

    private JLabel nickName;
    Image image;
    Font font;

    public Player(String nick, Font font) {
        this.nickName = new JLabel(nick);
        this.font = font;
    }

    public void setUpCard(String imgPath, int xText, int yText) {
        //setto struttura
        this.setBackground(new Color(0, 0, 0, 0));
        try {
            image = ImageIO.read(new File(imgPath));
            System.out.println("immagine giocatore azione impostata");
        } catch (IOException ex) {
            image = null;
            System.out.println("immagine giocatore azione non impostata");
        }
        this.setLayout(null);
        this.add(nickName);
        Insets insets = getInsets();
        Dimension size = nickName.getPreferredSize();
        nickName.setBounds(xText + insets.left, yText + insets.top,
                28, 200);
        nickName.setVisible(true);

        //imposto sfondo
        repaint();

        //imposto font
        nickName.setFont(font);
        nickName.setForeground(Color.WHITE);

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
        // return super.getPreferredSize(); //To change body of generated methods, choose Tools | Templates.
        return new Dimension(200, 99);
    }

    @Override
    public Dimension getSize() {
        return super.getSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getMaximumSize() {
        return super.getMaximumSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getMinimumSize(); //To change body of generated methods, choose Tools | Templates.
    }
}
