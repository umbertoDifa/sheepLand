
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import sun.java2d.pipe.DrawImage;

/**
 *
 * @author Francesco
 */
public class Map extends JPanel{
    private Image image;
    public Map(String imgPath) {
        try {
            image = ImageIO.read(new File(imgPath));
            System.out.println("immagine mappa impostata");
        } catch (IOException ex) {
            image = null;
            System.out.println("immagine mappa non impostata");
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null)
            g.drawImage(image, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(487, 700);
    }

    @Override
    public Dimension getMaximumSize() {
        return super.getMaximumSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getMinimumSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getSize() {
        return super.getSize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Insets getInsets() {
        return super.getInsets(); //To change body of generated methods, choose Tools | Templates.
    }        
}
