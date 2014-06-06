
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 *
 * @author Francesco
 */
public class Street extends BackgroundAndTextJPanel implements MouseListener{
    private final Image fence;
    private final List<Image> shepherds;
    
    public Street(Image image, List<Image> shepherds){
        this.fence = image;
        this.shepherds = shepherds;
    }
    
    /**
     * remove the background image
     */
    public void clear(){
        this.setUp(null);
        repaint();
    }
    
    
    public void mouseClicked(MouseEvent e) {
        System.out.println("strada clickata, dentro la catch dell evento");
     //   FIX ME
        clear();
        repaint();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * set the background image corrisponding to
     * the name of the shepherd
     * @param string 
     */
    public void setImage(String string){
        if("fence".equals(string)){
            this.setUp(fence);
        }else if("shepherd1".equals(string)){
            this.setUp(shepherds.get(0));
        }else if("shepherd2".equals(string)){
            this.setUp(shepherds.get(1));
        }else if("shepherd3".equals(string)){
            this.setUp(shepherds.get(2));
        }else if("shepherd4".equals(string)){
            this.setUp(shepherds.get(3));
        }
    }
    
    /**
     * set the background image
     * @param newImage image to set
     */
    public void setImage(Image newImage){
        this.setUp(newImage);
    }
    
    /**
     * return the current background image
     * @return 
     */
    public Image getImage(){
        return this.getImage();
    }
    
    /**
     * return true iff the street is empty
     * @return 
     */
    public boolean isEmpty(){
        return (this.getImage() == null);
    }
 
    protected void setFence(){
        setImage("fence");
    }
}
