package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class Animal can be used in 2 way, preview
 * and default. The class has a JLabel for indicate
 * the number of the animal, except for animaltype
 * "blacksheep" and "wolf"
 * @author Francesco
 */
public class Animal extends JPanel {

    private final Image imagePreview;
    private final Image image;
    private final int widthPreview;
    private final int heightPreview;
    private final int width;
    private final int height;
    private boolean preview;
    private final String animalType;
    private JLabel numLabel;

    /**
     * creates the Animal of the indicated type.
     * sets 2 images base on the type: preview and default image.
     * it sets the dimension for both. sets preview to true.
     * if the animal is not blacksheep and wolf set the jlabel
     * initializing it to "1"
     * @param animalType 
     */
    public Animal(String animalType) {
        this.animalType = animalType;
        imagePreview = ImagePool.getByName(animalType + "P");
        image = ImagePool.getByName(animalType);
        widthPreview = imagePreview.getWidth(this);
        heightPreview = imagePreview.getHeight(this);
        width = image.getWidth(this);
        height = image.getHeight(this);
        preview = true;
        if (!animalType.equals("blacksheep") && !animalType.equals("wolf")) {
            numLabel = new JLabel("1");
            numLabel.setPreferredSize(new Dimension(28, 28));
            numLabel.setBackground(new Color(0, 0, 0, 0));
            numLabel.setForeground(Color.white);
            this.setLayout(null);
            this.add(numLabel);
            refreshLabelBounds();
        }
    }

    /**
     * returns the animal type
     * @return 
     */
    public String getAnimalType() {
        return animalType;
    }

    /**
     * Refresh the bounds of the animal according to
     * the modality of view
     */
    protected void refreshLabelBounds() {
        numLabel.setSize(28, 28);
        if (preview) {
            //numLabel.setBounds(widthPreview / 2 - 8, heightPreview / 2 - 16, 28, 28);
            numLabel.setLocation(widthPreview / 2 - 8, heightPreview / 2 - 16);
        } else {
            //  numLabel.setBounds(width / 2+30, height / 2+30, 28, 28);
            numLabel.setLocation(width / 2 - 10, height / 2 - 16);

        }
    }

    /**
     * set, if exists, the background image
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        //  super.paintComponent(g);
        if (imagePreview != null) {
            if (preview) {
                g.drawImage(imagePreview, 0, 0, this);
            } else {
                g.drawImage(image, 0, 0, this);
            }
        }
        super.paintComponent(g);
    }

    /**
     * return the dimensions according to the modality of view
     * @return 
     */
    @Override
    public Dimension getPreferredSize() {
        if (preview) {
            return new Dimension(widthPreview, heightPreview);
        } else {
            return new Dimension(width, height);
        }
    }

    /**
     *  return the dimensions according to the modality of view
     * @return 
     */
    @Override
    public Dimension getSize() {
        if (preview) {
            return new Dimension(widthPreview, heightPreview);
        } else {
            return new Dimension(width, height);
        }
    }

    /**
     * set the modality of view. iff ok is true
     * the modality will be "preview"
     * @param ok 
     */
    public void setPreview(boolean ok) {
        preview = ok;
        if (this.numLabel != null) {
            refreshLabelBounds();
        }
        repaint();
    }

    /**
     * return the number of the label, if exists
     * @return 
     */
    public int getNum() {
        if (numLabel != null) {
            return Integer.parseInt(numLabel.getText());
        }
        return 0;
    }

    /**
     * set, if exists, the number og the label
     * @param num 
     */
    public void setNum(int num) {
        if (numLabel != null) {
            this.numLabel.setText(String.valueOf(num));
        }
    }

    /**
     * create a clone of the animal, with same preview modality
     * and label, if exists
     * @return
     * @throws CloneNotSupportedException 
     */
    @Override
    protected Animal clone() throws CloneNotSupportedException {
        Animal newAnimal = new Animal(this.animalType);
        if (newAnimal.numLabel != null) {
            newAnimal.setNum(this.getNum());
        }
        return newAnimal;
    }

}
