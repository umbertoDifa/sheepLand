
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

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
     * rimuove l'img di sfondo
     */
    public void clear(){
        this.setUp(null);
        repaint();
    }
    
    
    public void mouseClicked(MouseEvent e) {
        System.out.println("strada clickata, dentro la catch dell evento");
    //    this.setMySize(this.getPreferredSize().width+10,this.getPreferredSize().height+10);
     //   this.setUp(shepherds.get(2), 24, 24);
        clear();
        repaint();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        this.setUp(".\\images\\shepherd3b.png", 100, 100);
        revalidate();
        repaint();
    }

    public void mouseExited(MouseEvent e) {
        clear();
        this.setUp(shepherds.get(0), 24, 24);
        revalidate();
        repaint();
    }
    
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

    
}
