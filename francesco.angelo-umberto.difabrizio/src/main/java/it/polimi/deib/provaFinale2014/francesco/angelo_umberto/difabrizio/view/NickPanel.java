package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Francesco
 */
public class NickPanel extends JPanel implements ActionListener, KeyListener {

    private JTextArea area;
    private JLabel label;
    private final Font font;
    private final int width;
    private final int height;
    //   private final Image imageBackground;
    private final JButton button;

    public NickPanel(GuiView gui) {
        this.button = new JButton("Login");

        this.width = 140;
        this.height = 100;
        this.font = FontFactory.getFont();
        button.setFont(font);
        //   this.imageBackground = ImagePool.getByName("infoPanel");
        this.setBackground(Color.BLUE);
        this.area = new JTextArea(""+ (int) (Math.random() * 10));
        area.setPreferredSize(new Dimension(140, 60));
        this.area.addKeyListener(this);
        this.setLayout(new BorderLayout());
        this.add(area, BorderLayout.CENTER);
        this.add(button, BorderLayout.SOUTH);
        this.button.addActionListener(this);
        this.button.addActionListener(gui);
        this.setVisible(false);
        revalidate();
        repaint();
    }

    public String getMyNickName() {
        return area.getText();
    }

    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void keyTyped(KeyEvent e) {
        this.revalidate();
        this.repaint();
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

}
