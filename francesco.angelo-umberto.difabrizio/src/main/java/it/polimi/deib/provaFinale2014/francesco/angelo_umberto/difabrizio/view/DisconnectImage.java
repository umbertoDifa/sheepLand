package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The clas extends JPanel adding a background image
 * of the disconnection
 * @author Francesco
 */
public class DisconnectImage extends JPanel {

    private Image image;

    /**
     * Create a disconnected image setting the background image
     */
    public DisconnectImage() {
        try {
            image = ImageIO.read(new File(".\\images\\disconnect.png"));
            DebugLogger.println("immagine disconnessione aggiunta al pool");
        } catch (IOException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);

            DebugLogger.println("immagine disconnessione non aggiunta al pool");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        if (image != null) {
            g.drawImage(image, 0, 0, new Color(0, 0, 0, 0), this);
        }
        super.paintComponent(g);
    }
}
