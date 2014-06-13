package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Francesco
 */
public class DinamicGui extends GuiView {

    private static final List<String> holder = new LinkedList<String>();

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() instanceof Animal) {
            for(RegionBox region: regionBoxes){
//                if(e.getLocationOnScreen() ){
//                    
//                }
            }
            //TODO aggiungere a holder regione corrispondente
        }
    }

    /**
     * It clones all the Animals of the i-th RegionBox. Dispose them in circle
     * setting the bounds. Set them draggable, adds them to the layer 1
     *
     * @param i
     */
    @Override
    protected void zoomAnimals(int i) {
        List<Animal> animalsToHighlight = regionBoxes[i].cloneAndHideAnimals();
        int j = 0;
        for (Animal animalToHighlight : animalsToHighlight) {
            animalToHighlight.setDraggable(true);
            int animalWidth = animalToHighlight.getSize().width;
            int animalHeight = animalToHighlight.getSize().height;
            Point p = regionBoxes[i].getLocation();
            int first = 1;
            if (j == 0) {
                first = 0;
            }
            animalToHighlight.setBounds(
                    (int) (p.x + first * (animalWidth * Math.sqrt(2) * Math.cos(
                            (Math.PI / 4) + ((Math.PI * j) / 2))) / 1.5 + 140),
                    (int) (p.y - first * (animalWidth * Math.sqrt(2) * Math.sin(
                            (Math.PI / 4) + ((Math.PI * j) / 2))) / 1.5),
                    animalWidth, animalHeight);

            if (!"blacksheep".equals(animalToHighlight.getAnimalType())
                    || !"wolf".equals(animalToHighlight.getAnimalType())) {
                //animalToHighlight.addMouseListener(this);
            }
            layeredPane.add(animalToHighlight, new Integer(1));
            j++;
        }
        layeredPane.repaint();
    }


}
