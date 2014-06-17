package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 * The class extends JPanel adding the possibility to set the jpanel
 * draggable and to drag the JPanel
 * @author Francesco
 */
public class DraggableComponent extends JPanel {


    private boolean draggable = true;
    
    /**
     * 2D Point representing the coordinate where mouse is, relative parent container
     */
    protected Point anchorPoint;
    
    /**
     * Default mouse cursor for dragging action
     */
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    /**
     * create a draggable component setting listener to with drag functions
     * opacity and background
     */
    public DraggableComponent() {
        addDragListeners();
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));
    }

    /**
     * Add Mouse Motion Listener with drag function
     */
    private void addDragListeners() {
       
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                anchorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                DebugLogger.println("inside mouseMove");
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension bestsize = tk.getBestCursorSize(24, 24);
                Cursor c;
                if (bestsize.width != 0) {
                    c = tk.createCustomCursor(tk.getImage(".\\images\\closedHandCursor.gif"), new Point(0,0), "closedHand");
                } else {
                    c = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
                }

                setCursor(c);
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
                setLocation(position);
                DebugLogger.println("location:"+position.getX()+" "+position.getY());
            }
        });
    }

    /**
     * Remove all Mouse Motion Listener. Freeze component.
     */
    private void removeDragListeners() {
        for (MouseMotionListener listener : this.getMouseMotionListeners()) {
            removeMouseMotionListener(listener);
        }
        setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Get the value of draggable
     *
     * @return the value of draggable
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Set the value of draggable
     *
     * @param draggable new value of draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
        if (draggable) {
            addDragListeners();
        } else {
            removeDragListeners();
        }

    }

    /**
     * Get the value of draggingCursor
     *
     * @return the value of draggingCursor
     */
    public Cursor getDraggingCursor() {
        return draggingCursor;
    }

    /**
     * Set the value of draggingCursor
     *
     * @param draggingCursor new value of draggingCursor
     */
    public void setDraggingCursor(Cursor draggingCursor) {
        this.draggingCursor = draggingCursor;
    }

}
