package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Francesco
 */
public class NickPanel extends JPanel implements ActionListener{

    private JTextArea area;
    private JLabel label;
    private Font font;
    private final int width;
    private final int height;
    private final Image imageBackground;
    private JButton button;

    public NickPanel(MyGui gui) {
        this.button = new JButton("Submit");
        this.width = 232;
        this.height = 444;
        this.font = MyFont.getFont();
        this.imageBackground = ImagePool.getByName("infoPanel");
        this.area = new JTextArea("inserire qui nickname");
        area.setPreferredSize(new Dimension(140, 30));
        this.setLayout(new BorderLayout());
        this.add(area, BorderLayout.CENTER);
        this.add(button, BorderLayout.SOUTH);
        this.button.addActionListener(this);
        this.button.addActionListener(gui);
        this.setVisible(false);
        repaint();
    }
    
    public String getMyNickName(){
        return area.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        if (imageBackground != null) {
            g.drawImage(imageBackground, 0, 0, new Color(0, 0, 0, 0), this);
        }
        super.paintComponent(g);
    }

    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
        this.area.setEditable(false);
    }

}
