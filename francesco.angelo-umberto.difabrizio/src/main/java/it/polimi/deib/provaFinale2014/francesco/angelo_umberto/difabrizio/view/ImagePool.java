
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;


import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author Francesco
 */
public class ImagePool {
    private static final HashMap<String, Image> nameImageMap = new HashMap<String, Image>();
    
    private ImagePool(){
    }
    
    public static void add(String imgPath, String name) {
        try {
            Image image = ImageIO.read(new File(imgPath));
            nameImageMap.put(name, image);
            DebugLogger.println("immagine "+name+" aggiunta al pool");
        } catch (IOException ex) {
            DebugLogger.println("immagine "+name+" non aggiunta al pool");
        }
    }
    
    public static Image getByName(String name){
        return nameImageMap.get(name);
    }
}
