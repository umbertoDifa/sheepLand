
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
    private Image fence;
    private List<Image> shepherds;
    
    public Street(Image image, List<Image> shepherds){
        this.fence = image;
        this.shepherds = shepherds;
    }
    
    
    public void mouseClicked(MouseEvent e) {
        System.out.println("strada clickata, dentro la catch dell evento");
    //    this.setMySize(this.getPreferredSize().width+10,this.getPreferredSize().height+10);
        this.setUp(shepherds.get(2), 24, 24);
        repaint();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
//        super.setMySize(getPreferredSize().width+10, getPreferredSize().height+10);
//        this.repaint();
    }

    public void mouseExited(MouseEvent e) {
//        super.setMySize(getPreferredSize().width-10, getPreferredSize().height-10);
//        this.repaint();
    }

    
}
