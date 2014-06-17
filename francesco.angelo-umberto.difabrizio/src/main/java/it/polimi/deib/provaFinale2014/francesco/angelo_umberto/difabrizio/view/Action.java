package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The class Action extends BackgroundAndTextJPanel. It adds the possibility
 * to set the opacity and a tooltipText.
 * @author Francesco
 */
public class Action extends BackgroundAndTextJPanel implements MouseListener {    
    /**
     * Create a JPanel Action, with the toolTipText indicated.
     * It removes the background color, add itself to the mouselistener
     * for cursor effects.  It switch on the opacity 
     * @param toolTipText 
     */
    public Action(String toolTipText) {
        setOpacity(true);
        this.setBackground(new Color(0, 0, 0, 0));
        this.addMouseListener(this);
        this.setToolTipText(toolTipText);
    }

    /**
     * iff ok true, the image is opaque
     * @param ok
     */
    protected void setOpaqueView(boolean ok) {
        super.setOpacity(ok);
    }

    /**
     * Debug method
     * @param e 
     */
    public void mouseClicked(MouseEvent e) {
        DebugLogger.println("azione clickata, dentro la catch dell evento");
        revalidate();
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        //not used
    }

    public void mouseReleased(MouseEvent e) {
        //not used
    }

    /**
     * When the mouse enters, the cursor becames Hand
     * @param e 
     */
    public void mouseEntered(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * when the mouse exits the cursor return default cursor
     * @param e 
     */
    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

}
