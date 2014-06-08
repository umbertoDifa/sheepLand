package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francesco
 */
public class RegionBox extends BackgroundAndTextJPanel implements MouseListener {

    private final int[] xPreview = {10, 0, 10, 6, 10};
    private final int[] yPreview = {0, 25, 25, 35, 35};
    private List<Animal> animals = new ArrayList<Animal>();

    public RegionBox() {
        this.setLayout(null);
        repaint();
    }

    public void add(String animalType) {
        for (Animal animal : animals) {
            if (animal.getAnimalType().equals(animalType)) {
                animal.setNum(animal.getNum() + 1);
                return;
            }
        }

        Animal newAnimal = new Animal(animalType);
        //se sono riuscito a creare l animale, la posizione nell array è vuota
        //e non sono arrivato a fine array

        //aggiungo l animale all'array
        animals.add(newAnimal);
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

        repaint();
    }

    public boolean removeOvine(String ovineType) {
        //per ogni animale nella regione
        for (Animal animal : animals) {
            //se il tipo è uguale a quello da rimuovere
            if (animal.getAnimalType().equals(ovineType)) {
                //se di quel tipo di animale vi è almeno 1 occorrenza
                if (animal.getNum() > 0) {
                    //se non è ne pecora nera ne lupo
                    if (!animal.getAnimalType().equals("wolf") && !animal.getAnimalType().equals("blacksheep")) {
                        //decrementa le occorrenze
                        animal.setNum(animal.getNum() - 1);
                        //nel caso rimuovilo
                        if (animal.getNum() == 0) {
                            this.remove(animal);
                            animals.remove(animal);
                        }
                    //per lupo o pecora nera rimuovili
                    } else {
                        this.remove(animal);
                        animals.remove(animal);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * return the clones of each animals in the RegionBox, change the preview of
     * each clone in "false". hide the animals in the region.
     *
     * @return
     */
    protected List<Animal> cloneAndHideAnimals() {
        if (animals == null) {
            return null;
        }

        List<Animal> result = new ArrayList<Animal>();
        for (int i = 0; i < animals.size(); i++) {

            try {
                result.add(animals.get(i).clone());
                animals.get(i).setVisible(false);
                result.get(i).setPreview(false);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RegionBox.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        this.repaint();
        return result;
    }

    public void add(String animalType, int quantity) {
        for (int i = 0; i < quantity; i++) {
            add(animalType);
        }
    }

    public void setAnimalsVisibles(boolean b) {
        for (Animal animal : animals) {
            if (animal != null) {
                animal.setVisible(b);
            }
        }
    }

    public void setAnimalPreview(boolean b) {
        for (Animal animal : animals) {
            if (animal != null) {
                animal.setPreview(b);
            }
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
