package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * The class extends BackgroundAndTextJPanel adding method to set background
 * image from a list of image, return method of the background image
 *
 * @author Francesco
 */
public class Street extends BackgroundAndTextJPanel implements MouseListener {

    private final Image fence;
    private final List<Image> shepherds;

    /**
     * create a Street, set dimension and the possible background images. add
     * itself as listener for cursor effect.
     *
     * @param image
     * @param shepherds
     */
    public Street(Image image, List<Image> shepherds) {
        this.fence = image;
        this.shepherds = shepherds;
        super.setUp(null, fence.getWidth(this), fence.getHeight(this));
        this.setBackground(new Color(0, 0, 0, 0));
        this.addMouseListener(this);

    }

    /**
     * remove the background image
     */
    public void clear() {
        this.setUp(null);
        repaint();
    }

    /**
     * debug method
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        DebugLogger.println("strada clickata, dentro la catch dell evento");
        repaint();
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
     * When the mouse enters, the cursor becames Hand
     *
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * when the mouse exits the cursor return default cursor
     *
     * @param e
     */
    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * set the background image corrisponding to the name of the shepherd
     *
     * @param string
     */
    public void setImage(String string) {
        if ("fence".equals(string)) {
            this.setUp(fence);
        } else if ("shepherd0".equals(string)) {
            this.setUp(shepherds.get(0));
        } else if ("shepherd1".equals(string)) {
            this.setUp(shepherds.get(1));
        } else if ("shepherd2".equals(string)) {
            this.setUp(shepherds.get(2));
        } else if ("shepherd3".equals(string)) {
            this.setUp(shepherds.get(3));
        }
    }

    /**
     * set the background image
     *
     * @param newImage image to set
     */
    public void setImage(Image newImage) {
        this.setUp(newImage);
        this.repaint();
        this.revalidate();
    }

    /**
     * return the stringed current background image if exists, otherwise empty
     * stringed
     *
     * @return
     */
    public String getStringedImage() {
        Image img = this.getImage();
        if (img != null) {
            if (img.equals(fence)) {
                return "fence";
            } else {
                for (int i = 0; i < 4; i++) {
                    if (shepherds.get(i).equals(img)) {
                        return "shepherd" + i;
                    }
                }
            }
        }
        return "";

    }

    /**
     * return true iff the street is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return this.getImage() == null;
    }

    /**
     * set the fance image as background image of the street
     */
    protected void setFence() {
        setImage("fence");
        this.repaint();
    }
}
