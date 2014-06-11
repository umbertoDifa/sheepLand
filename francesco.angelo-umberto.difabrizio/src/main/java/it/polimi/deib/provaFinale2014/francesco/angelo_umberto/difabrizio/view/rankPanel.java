package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Francesco
 */
public class rankPanel extends JPanel {

    private final JLabel textLabel;
    private JLabel imageLabel;

    public rankPanel(String text) {
        this.textLabel = new JLabel(text);
        if (text.contains("vinto")) {
            imageLabel.setIcon( (Icon) ImagePool.getByName("cup"));
        } else {
            imageLabel.setIcon( (Icon) ImagePool.getByName("lose"));
        }
        this.setPreferredSize(new Dimension(232, 444));
        this.add(textLabel);
        this.add(imageLabel);
        imageLabel.setPreferredSize(new Dimension);
        textLabel.setPreferredSize(new Dimension(140, 230));
    }


}
