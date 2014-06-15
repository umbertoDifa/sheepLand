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
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * The class extends JPanel adding text area to insert text, and a button
 * to hide and disable the possibility to insert text, and a method to return
 * the text
 * @author Francesco
 */
public class NickPanel extends JPanel implements ActionListener, KeyListener {

    private final JTextArea area;
    private final Font font;
    private final int width;
    private final int height;

    private final JButton button;

    /**
     * Create a NickPanel with textarea and button
     * set dimension, font and listeners. the gui listen
     * the event of the button
     * @param gui 
     */
    public NickPanel(GuiView gui) {
        this.button = new JButton("Login");
        this.width = 140;
        this.height = 100;
        this.font = FontFactory.getFont();
        button.setFont(font);

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

    /**
     * return the text of the textarea
     * @return 
     */
    public String getMyNickName() {
        return area.getText();
    }

    /**
     * when the button is clicked hide the jpanel
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);        
    }

    /**
     * return the jpanel dimensions
     * @return 
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    /**
     *  return the jpanel dimensions
     * @return 
     */
    @Override
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * repaint when the user type a character
     * @param e 
     */
    public void keyTyped(KeyEvent e) {
        this.revalidate();
        this.repaint();
    }

    public void keyPressed(KeyEvent e) {
        //not used
    }

    public void keyReleased(KeyEvent e) {
        //not used
    }

    protected JButton getButton(){
        return button;
    }
}