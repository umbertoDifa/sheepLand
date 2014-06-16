package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 *
 * @author Francesco
 */
public class DinamicGui extends GuiView {

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof Animal) {
            Animal animal = (Animal) e.getSource();
            synchronized (HOLDER) {
                HOLDER.add(animal.getAnimalType());
                HOLDER.notify();
                DebugLogger.println("aggiunto a holder animale " + animal.getAnimalType());

            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {
        if (e.getSource() instanceof Animal) {
            DebugLogger.println("mouse released su un animal");
            Animal animal = (Animal) e.getSource();
            for (int i = 0; i < regionBoxes.length; i++) {
                Point r = regionBoxes[i].getLocationOnScreen();
                Point m = e.getLocationOnScreen();
                if (m.x > r.x && m.x < r.x + Dim.REGION_BOX.getW() && m.y > r.y && m.y < r.y + Dim.REGION_BOX.getH()) {
                    synchronized (HOLDER) {
                        HOLDER.add(String.valueOf(i));
                        HOLDER.notify();
                        DebugLogger.println("aggiunto a holder animale " + i);
                    }
                }
            }

            DebugLogger.println("rimuovo animali zoomati");
            //rimuovo tutti gli Animal dal layer 1
            Component[] toRemove = layeredPane.getComponentsInLayer(1);
            for (Component componentToRemove : toRemove) {
                componentToRemove.setVisible(false);
                layeredPane.remove(componentToRemove);
            }
            layeredPane.repaint();

            //per ogni regione
            for (RegionBox region : regionBoxes) {
                //rimetto visibili gli animali
                region.setAnimalsVisibles(true);
                //in modalità preview
                region.setAnimalPreview(true);
            }
            animal.removeMouseListener(this);
        }

    }

    /**
     * It clones all the Animals of the i-th RegionBox. Dispose them in circle
     * setting the bounds. Set them draggable, adds them to the layer 1
     *
     * @param i
     */
    @Override
    protected void zoomAnimals(int i
    ) {
        List<Animal> animalsToHighlight = regionBoxes[i].cloneAndHideAnimals();
        int j = 0;
        for (Animal animalToHighlight : animalsToHighlight) {

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
                animalToHighlight.addMouseListener(this);
                animalToHighlight.setDraggable(true);
            }
            layeredPane.add(animalToHighlight, new Integer(1));
            j++;
        }
        layeredPane.repaint();
    }

}
