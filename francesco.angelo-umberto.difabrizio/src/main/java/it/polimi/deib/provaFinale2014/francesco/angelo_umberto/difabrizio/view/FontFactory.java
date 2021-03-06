package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class loads the font and return it when asked
 *
 * @author Francesco
 */
public class FontFactory {

    private static Font myFont;

    /**
     * load and save the font
     */
    private FontFactory() {
        try {
            myFont = Font.createFont(Font.TRUETYPE_FONT, new File(
                    ".\\font\\DenneFreakshow.ttf"));
            DebugLogger.println("impostato font DenneFeakshow");
            myFont = myFont.deriveFont(Font.PLAIN, 28);
        } catch (IOException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);

            myFont = new Font("Verdana", Font.PLAIN, 12);
            DebugLogger.println("nel catch IOEx");
        } catch (FontFormatException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);

            myFont = new Font("Verdana", Font.PLAIN, 12);
            DebugLogger.println("nel catch FontFormatEx");
        }
    }

    /**
     * create and object of the class and call the method to load the font
     */
    public static void createFont() {
        new FontFactory();
    }

    /**
     * return the font loaded
     *
     * @return
     */
    public static Font getFont() {
        return myFont;
    }
}
