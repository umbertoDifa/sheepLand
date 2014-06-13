package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;

/**
 * The class extends BackgroundAndTextJPanel 
 * @author Francesco
 */
public class MapBoard extends BackgroundAndTextJPanel {

    /**
     * create a MapBoard with image, dimensions and color background
     */
    public MapBoard() {
        super();
        this.setUp(".\\images\\Game_Board_big.jpg", Dim.MAP_BOARD.getW(), Dim.MAP_BOARD.getH());
        this.setBackground(new Color(0,0,0,0));
    }
}
