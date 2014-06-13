

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Main {

    public static JFrame frame;
    public static JPanel album;

    public static void main(String[] args) {

        
        frame = new JFrame("Photo Album");
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        album = new JPanel();
        album.setBackground(Color.darkGray);
        album.setLayout(null);//!! IMPORTAN !! you have to set a free layout at least ( FREE, ABSOLUTE, NULL is best)
        frame.add(album, BorderLayout.CENTER);

        /*Is good create a Thread to manipulate Forms and Files. In this particular
         * case an <b>invokeLater</b> is needed becaouse all Forms graphics operations
         * needs to be elaborated after pending events are processed
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                loadPhotos();
            }
        });
    }

    /**
     * Generate names of files
     */
    public static void loadPhotos() {
        album.removeAll();
        for (int i = 1; i <= 8; i++) {
            String fileName = String.valueOf(i) + ".jpg";
            addNewPhoto(fileName);
        }
        album.repaint();
    }

    public static void addNewPhoto(String fileName) {
        //Get resources from Directory or Jar file
        Image img = Toolkit.getDefaultToolkit().createImage("images/"+fileName);
        
        //Creates a draggableImageComponent and adds loaded image
        DraggableImageComponent photo = new DraggableImageComponent();
        album.add(photo);//Adds this component to main container
        photo.setImage(img);//Sets image
        photo.setAutoSize(true);//The component get ratio w/h of source image
        photo.setOverbearing(true);//On click ,this panel gains lowest z-buffer
        photo.setBorder(new LineBorder(Color.black, 1));

        //A small randomization of object size/position
        int delta = album.getWidth() / 4;
        int randomGrow = getRandom(getRandom(delta * 2));
        int cx = album.getWidth() / 2;
        int cy = album.getHeight() / 2;
        photo.setSize(delta + randomGrow, delta + randomGrow);
        photo.setLocation(cx + getRandom(delta / 2) - photo.getWidth() / 2, cy + getRandom(delta / 2) - photo.getHeight() / 2);
        album.repaint();
    }

    public static int getRandom(int range) {
        int r = (int) (Math.random() * range) - range;
        return r;
    }
}
