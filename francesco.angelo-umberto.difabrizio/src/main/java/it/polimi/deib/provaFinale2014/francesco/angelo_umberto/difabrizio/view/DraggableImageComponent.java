package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

/**
 * This is an extension of Draggable Component with a custom Image for
 * backgraound setting it by setImage method.
 */
public class DraggableImageComponent extends DraggableComponent implements ImageObserver {

    protected Image imageToShow;

    public DraggableImageComponent() {
        super();
        setLayout(null);
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (imageToShow != null) {
            g.drawImage(imageToShow, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    /**
     * Get the value of image
     *
     * @return the value of image
     */
    public Image getImage() {
        return imageToShow;
    }

    /**
     * Set the value of image by String name. Use ToolKit to create image from
     * file.
     *
     * @param image fileName of image
     */
    public void setImage(String image) {
        setImage(Toolkit.getDefaultToolkit().getImage(image));
    }

    /**
     * Set the value of image
     *
     * @param image new value of image
     */
    public void setImage(Image image) {
        this.imageToShow = image;
        repaint();
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if (infoflags == ALLBITS) {
            repaint();
            return false;
        }
        return true;
    }
}
