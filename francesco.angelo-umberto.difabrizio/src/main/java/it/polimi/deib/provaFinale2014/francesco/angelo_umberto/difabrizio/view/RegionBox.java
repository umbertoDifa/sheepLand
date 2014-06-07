package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francesco
 */
public class RegionBox extends BackgroundAndTextJPanel implements MouseListener {

    private int[] numAnimals;
    private int[] xPreview = {10, 0, 10, 6, 10};
    private int[] yPreview = {0, 25, 25, 35, 35};
    private Animal[] animals = new Animal[5];

    public RegionBox() {
        this.numAnimals = new int[5];
        this.setLayout(null);
        repaint();
    }

    public void add(String animalType) {
        int i;
        //controllo che in animals non ci sia già un animale col tipo da aggiungere
        //mi fermo al primo libero o se arrivo a fine array
        for (i = 0; i < animals.length; i++) {
            if (animals[i] == null) {
                break;
            } else if (animals[i].getAnimalType() == animalType) {
                return;
            }
        }
        Animal newAnimal = new Animal(animalType);
        //se sono riuscito a creare l animale, la posizione nell array è vuota
        //e non sono arrivato a fine array
        if (animals[i] == null && i != animals.length) {
            //aggiungo l animale all'array
            animals[i] = newAnimal;
            //lo posiziono nel RegionBox
            if ("sheep".equals(animalType)) {
                MyGui.addComponentsToPane(this, newAnimal, xPreview[0], yPreview[0]);
            } else if ("ram".equals(animalType)) {
                MyGui.addComponentsToPane(this, newAnimal, xPreview[1], yPreview[1]);
            } else if ("lamb".equals(animalType)) {
                MyGui.addComponentsToPane(this, newAnimal, xPreview[2], yPreview[2]);
            } else if ("blacksheep".equals(animalType)) {
                MyGui.addComponentsToPane(this, newAnimal, xPreview[3], yPreview[3]);
            } else if ("wolf".equals(animalType)) {
                MyGui.addComponentsToPane(this, newAnimal, xPreview[4], yPreview[4]);
            }
        }
        repaint();
    }

    /**
     * return the clones of each animals in the RegionBox, change the preview of each
     * clone in "false". hide the animals in the region.
     * @return 
     */
    protected Animal[] cloneAndHideAnimals() {
        if (animals == null) {
            return null;
        }
        
        Animal[] result = new Animal[animals.length];
        for (int i=0; i<animals.length; i++) {
            try {
                result[i] = animals[i].clone();
                animals[i].setVisible(true);
                result[i].setPreview(false);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RegionBox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.setAnimalsVisibles(false);

        System.out.println("equals: " + animals[0].equals(result[0]) + ". stesso rif: " + (animals[0] == result[0]));

        this.repaint();
        return result;
    }
    
    public void setAnimalsVisibles(boolean b){
        for(int i=0; i<animals.length; i++){
            animals[i].setVisible(b);
        }
    }
    
    public void setAnimalPreview(boolean b){
        for(int i=0; i<animals.length; i++){
            animals[i].setPreview(b);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
