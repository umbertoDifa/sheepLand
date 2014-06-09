package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 *
 * @author Francesco
 */
public class NickPanel extends JPanel implements ActionListener, KeyListener {

    private JTextArea area;
    private JLabel label;
    private Font font;
    private final int width;
    private final int height;
 //   private final Image imageBackground;
    private JButton button;

    public NickPanel(MyGui gui) {
        this.button = new JButton("Submit");

        this.width = 140;
        this.height = 100;
        this.font = MyFont.getFont();
        button.setFont(font);
     //   this.imageBackground = ImagePool.getByName("infoPanel");
        this.setBackground(Color.BLUE);
        this.area = new JTextArea("inserire qui nickname");
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
        this.area.setEditable(false);
    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        setOpaque(false);
//        if (imageBackground != null) {
//            g.drawImage(imageBackground, 0, 0, new Color(0, 0, 0, 0), this);
//        }
//        super.paintComponent(g);
//    }

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
