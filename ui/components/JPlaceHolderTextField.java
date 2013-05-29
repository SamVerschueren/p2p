package ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.text.Document;

/**
 * This is a simple normal textfield with the possibility of adding a placeholder to it.
 * The placeholder will dissapear when the user types in something on the keyboard.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 11/10/2012
 */
public class JPlaceHolderTextField extends JTextField implements KeyListener {

    private String originalPlaceHolder;
    private String placeHolder;
    
    /**
     * Constructs a new TextField.
     */
    public JPlaceHolderTextField() {
        this(null, null, 0);
    }
    
    /**
     * Constructs a new empty TextField with the specified number of columns.
     * 
     * @param columns       the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally results from the component implementation
     */
    public JPlaceHolderTextField(int columns) {
        this(null, null, columns);
    }
    
    /**
     * Constructs a new empty TextField with the specified number of columns. A default model is created and the initial string is set to null.
     * 
     * @param text          the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally results from the component implementation
     */
    public JPlaceHolderTextField(String text) {
        this(null, text, 0);
    }
    
    /**
     * Constructs a new TextField initialized with the specified text and columns. A default model is created.
     * 
     * @param text          the text to be displayed, or null
     * @param columns       the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally results from the component implementation
     */
    public JPlaceHolderTextField(String text, int columns) {
        this(null, text, columns);
    }
    
    /**
     * Constructs a new JTextField that uses the given text storage model and the given number of columns.
     * 
     * @param doc           the text storage to use; if this is null, a default will be provided by calling the createDefaultModel method
     * @param text          the initial string to display, or null
     * @param columns       the number of columns to use to calculate the preferred width >= 0; if columns is set to zero, the preferred width will be whatever naturally results from the component implementation
     */
    public JPlaceHolderTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        
        this.addKeyListener(this);
    }
    
    /**
     * Sets the placeholder on the textfield.
     * 
     * @param placeHolder       The placeholder text.
     */
    public void setPlaceHolder(String placeHolder) {
        this.originalPlaceHolder = placeHolder;
        this.placeHolder = this.originalPlaceHolder;
        
        this.repaint();
    }
    
    /**
     * Clears the placeholder text.
     */
    private void clear() {
        if(this.placeHolder != null) {
            this.placeHolder = null;
            
            this.repaint();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        if(this.placeHolder != null) {
            Font font = new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize());
            
            g2d.setColor(Color.gray);
            g2d.setFont(font);
            g2d.drawString(this.placeHolder, 3, this.getBaseline(this.getWidth(), this.getHeight()));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(this.getText().length() > 0) {
            clear();
        }
        else {
            this.setPlaceHolder(originalPlaceHolder);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(this.getText().length() > 0) {
            clear();
        }
        else {
            this.setPlaceHolder(originalPlaceHolder);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(this.getText().length() > 0) {
            clear();
        }
        else {
            this.setPlaceHolder(originalPlaceHolder);
        }
    }
}
