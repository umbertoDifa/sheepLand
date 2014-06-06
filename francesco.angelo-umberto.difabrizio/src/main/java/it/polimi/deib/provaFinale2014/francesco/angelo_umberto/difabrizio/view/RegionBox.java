package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;

/**
 *
 * @author Francesco
 */
public class RegionBox extends BackgroundAndTextJPanel {

    private int[] numAnimals;
    private int[] xPreview = {10, 0, 10, 6, 10};
    private int[] yPreview = {0, 25, 25, 35, 35};
    private Animal[] previewAnimal;

    public RegionBox() {
        this.numAnimals = new int[5];
        this.setLayout(null);
        repaint();
    }

    //FIXME compattare
    public void add(String animalType) {
        if ("sheep".equals(animalType)) {
            if (numAnimals[0] == 0) {
                MyGui.addComponentsToPane(this, new Animal(animalType), xPreview[0], yPreview[0]);
            }
        } else if ("ram".equals(animalType)) {
            if (numAnimals[1] == 0) {
                MyGui.addComponentsToPane(this, new Animal(animalType), xPreview[1], yPreview[1]);
            }
        } else if ("lamb".equals(animalType)) {
            if (numAnimals[2] == 0) {
                MyGui.addComponentsToPane(this, new Animal(animalType), xPreview[2], yPreview[2]);
            }
        } else if ("blacksheep".equals(animalType)) {
            if (numAnimals[3] == 0) {
                MyGui.addComponentsToPane(this, new Animal(animalType), xPreview[3], yPreview[3]);
            }
        } else if ("wolf".equals(animalType)) {
            if (numAnimals[4] == 0) {
                MyGui.addComponentsToPane(this, new Animal(animalType), xPreview[4], yPreview[4]);
            }
        }
        repaint();
    }
}
