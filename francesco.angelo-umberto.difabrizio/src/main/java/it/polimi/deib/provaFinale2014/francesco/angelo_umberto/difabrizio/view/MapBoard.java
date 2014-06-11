package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;

/**
 *
 * @author Francesco
 */
public class MapBoard extends BackgroundAndTextJPanel {

    public MapBoard() {
        super();
        this.setUp(".\\images\\Game_Board_big.jpg", 487, 900);
        this.setBackground(new Color(0,0,0,0));
    }
}
