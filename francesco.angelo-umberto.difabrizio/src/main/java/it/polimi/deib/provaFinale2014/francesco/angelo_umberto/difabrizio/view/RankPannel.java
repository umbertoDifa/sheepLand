package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Francesco
 */
public class RankPannel extends JPanel {

    private final JLabel textLabel;
    private final JLabel imageLabel;

    public RankPannel(String text) {
        this.textLabel = new JLabel(text);
        this.imageLabel = new JLabel();
        this.setBackground(new Color(0, 0, 0, 0));
        if (text.contains("vinto")) {
            imageLabel.setIcon( new ImageIcon(".\\images\\cup.png"));
        } else {
            imageLabel.setIcon( new ImageIcon(".\\images\\lose.png"));
        }
        this.setPreferredSize(new Dimension(326, 444));
        this.add(textLabel);
        this.add(imageLabel);
        Font biggerFont = FontFactory.getFont().deriveFont(new Float(40.0));
        textLabel.setFont(biggerFont);
        textLabel.setPreferredSize(new Dimension(326, 111));
        textLabel.setBackground(Color.white);
        imageLabel.setPreferredSize(new Dimension(326,333));
        this.setBounds(235, 160, 326, 444);
    }


}
