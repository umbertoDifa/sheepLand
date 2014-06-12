
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;


import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * The class load and return when asked the images
 * @author Francesco
 */
public class ImagePool {
    private static final HashMap<String, Image> nameImageMap = new HashMap<String, Image>();
    
    /**
     * create an ImagePool
     */
    private ImagePool(){
    }
    
    /**
     * add to the hashmap of the images the image corrisponding
     * to the relative path, with the indicated name
     * @param imgPath
     * @param name 
     */
    public static void add(String imgPath, String name) {
        try {
            Image image = ImageIO.read(new File(imgPath));
            nameImageMap.put(name, image);
            DebugLogger.println("immagine "+name+" aggiunta al pool");
        } catch (IOException ex) {
            DebugLogger.println("immagine "+name+" non aggiunta al pool");
        }
    }
    
    /**
     * return the image corrisponding to the indicated
     * name
     * @param name
     * @return 
     */
    public static Image getByName(String name){
        return nameImageMap.get(name);
    }
}
