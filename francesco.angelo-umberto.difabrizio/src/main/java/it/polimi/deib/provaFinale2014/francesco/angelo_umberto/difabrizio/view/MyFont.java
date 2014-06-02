package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Francesco
 */
public class MyFont {

    private Font myFont;

    public MyFont() {
        try {
            myFont = Font.createFont(Font.TRUETYPE_FONT, new File(".\\font\\DenneFreakshow.ttf"));
            System.out.println("impostato font DenneFeakshow");
            myFont = myFont.deriveFont(Font.PLAIN, 28);
        } catch (IOException e) {
            myFont = new Font("Verdana", Font.PLAIN, 12);
            System.out.println("nel catch IOEx");
        } catch (FontFormatException e) {
            myFont = new Font("Verdana", Font.PLAIN, 12);
            System.out.println("nel catch FontFormatEx");
        }
    }
    
    public Font getFont(){
        return myFont;
    }
}
