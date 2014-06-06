
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Francesco
 */
public class Animal extends JPanel {
    private Image image;
    private int width;
    private int height;
            
    public Animal(String animalType){
        image = ImagePool.getByName(animalType);
        width = image.getWidth(null);
        height = image.getHeight(null);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        //  super.paintComponent(g);
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
    public Dimension getSize() {
        return new Dimension(width, height);
    }
    
    
}
